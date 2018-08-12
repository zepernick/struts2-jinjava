package com.zepernick.struts.result;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.filter.Filter;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.hubspot.jinjava.lib.tag.Tag;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.zepernick.jinjava.JinJavaTemplate;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.result.StrutsResultSupport;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by KevinBonnett on 2/25/2016.
 */
public class JinJavaResult extends StrutsResultSupport {

    private static final Map<String, String> TEMPLATE_CACHE = new HashMap<>();

    private static final Map<String, Long> TEMPLATE_CACHE_DATES = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(JinJavaResult.class);

    private static Boolean registeredJinJavaCusomizations = Boolean.FALSE;

    private static String templateBasePath;

    private String tagPackageToScan;

    private String functionPackageToScan;

    private String filterPackageToScan;

    /**
     *
     * @return
     *      The base path of the JinJava templates as defined by the "struts.jinjava.basepath" constant
     */
    public static String getTemplateBasePath() {
        return templateBasePath;
    }

    @Inject(value = "struts.jinjava.basepath", required = false)
    public void setTemplatePath(String path) {
        templateBasePath = path;
    }

    @Inject(value = "struts.jinjava.scan.tagPackage", required = false)
    public void setTagPackageToScan(String tagPackageToScan) {
        this.tagPackageToScan = tagPackageToScan;
    }

    @Inject(value = "struts.jinjava.scan.functionPackage", required = false)
    public void setFunctionPackageToScan(String functionPackageToScan) {
        this.functionPackageToScan = functionPackageToScan;
    }

    @Inject(value = "struts.jinjava.scan.filterPackage", required = false)
    public void setFilterPackageToScan(String filterPackageToScan) {
        this.filterPackageToScan = filterPackageToScan;
    }

    @Override
    protected void doExecute(String resultLocation, ActionInvocation actionInvocation) throws Exception {
        Jinjava jj = JinJavaTemplate.getJinJava();

        if(!registeredJinJavaCusomizations) {
            // we have never registered custom items before.  Let's do it!
            registeredJinJavaCusomizations = Boolean.TRUE;

            if(this.tagPackageToScan != null && !this.tagPackageToScan.isEmpty()) {
                if(log.isDebugEnabled()) log.debug("searching for custom tags in {}", this.tagPackageToScan);

                Reflections reflections = new Reflections(this.tagPackageToScan);
                Set<Class<? extends Tag>> subTypes = reflections.getSubTypesOf(Tag.class);
                for(Class t : subTypes) {
                    if(!Modifier.isAbstract(t.getModifiers())) {
                        JinJavaTemplate.registerTag((Tag) t.newInstance());
                    }
                }



            }

            if(this.filterPackageToScan != null && !this.filterPackageToScan.isEmpty()) {
                if(log.isDebugEnabled()) log.debug("searching for custom filters in {}", this.filterPackageToScan);

                Reflections reflections = new Reflections(this.tagPackageToScan);
                Set<Class<? extends Filter>> subTypes = reflections.getSubTypesOf(Filter.class);
                for(Class t : subTypes) {
                    if(!Modifier.isAbstract(t.getModifiers())) {
                        JinJavaTemplate.registerFilter((Filter) t.newInstance());
                    }
                }

            }

            if(this.functionPackageToScan != null && !this.functionPackageToScan.isEmpty()) {
                if(log.isDebugEnabled()) log.debug("searching for custom functions in {}", this.functionPackageToScan);

                Reflections reflections = new Reflections(this.functionPackageToScan);
                Set<Class<? extends StrutsJinJavaFunctionRegistration>> subTypes = reflections.getSubTypesOf(StrutsJinJavaFunctionRegistration.class);
                for(Class t : subTypes) {
                    if(!Modifier.isAbstract(t.getModifiers())) {
                        StrutsJinJavaFunctionRegistration reg = (StrutsJinJavaFunctionRegistration) t.newInstance();
                        for(ELFunctionDefinition func : reg.register()) {
                            JinJavaTemplate.registerFunction(func);
                        }

                    }
                }


            }

        }


        Object topOfStack = actionInvocation.getStack().getRoot().get(0);
        Map context = new HashMap<>(new BeanMap(topOfStack));
        HttpServletRequest request = ServletActionContext.getRequest();
        boolean isAjax = StringUtils.trimToEmpty(request.getHeader("X-Requested-With")).equalsIgnoreCase("XMLHttpRequest");
        String contextPath = request.getContextPath();
        context.put("appContext", contextPath);
        context.put("request", request);
        context.put("session", request.getSession());
        context.put("is_ajax_request", isAjax);



        String template = TEMPLATE_CACHE.get(resultLocation);
        Long lastTemplateUpdate = TEMPLATE_CACHE_DATES.get(resultLocation);
        File f = new File(ServletActionContext.getServletContext().getRealPath(resultLocation));
        if(template == null || f.lastModified() != lastTemplateUpdate) {
            template = FileUtils.readFileToString(f);
            TEMPLATE_CACHE.put(resultLocation, template);
            TEMPLATE_CACHE_DATES.put(resultLocation, f.lastModified());
        }

        String renderedTemplate = jj.render(template, context);
        String encoding = "UTF-8";

        HttpServletResponse response = ServletActionContext.getResponse();
        try(OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), encoding)) {
            response.setContentType("text/html;charset=" + encoding);
            writer.write(renderedTemplate);
            writer.flush();
        }


    }

}
