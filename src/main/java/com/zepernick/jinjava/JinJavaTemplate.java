package com.zepernick.jinjava;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.hubspot.jinjava.lib.tag.Tag;
import com.hubspot.jinjava.loader.FileLocator;
import com.hubspot.jinjava.util.HelperStringTokenizer;
import com.zepernick.jinjava.filter.*;
import com.zepernick.jinjava.function.BootstrapFunctions;
import com.zepernick.jinjava.tag.ButtonTag;
import com.zepernick.jinjava.tag.FormTag;
import com.zepernick.jinjava.tag.PanelTag;
import com.zepernick.struts.result.JinJavaResult;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PaulZepernick on 2/29/2016.
 *
 * Provides access to the JinJava api and provides some
 * utility methods that are not available in JinJava
 *
 */
public class JinJavaTemplate {

    private static final Map<String, String> TEMPLATE_CACHE = new HashMap<>();

    private static Jinjava jinjava;


    private JinJavaTemplate(){}



    /**
     *
     * @param resource
     *      path to the resource on the classpath
     * @param jjint
     *      if null, the current interpreter will be used
     * @return
     *      result of template file parsed by JinJava
     */
    public static String renderResource(String resource, JinjavaInterpreter jjint) {
        try {

            if(jjint == null) {
                jjint = JinjavaInterpreter.getCurrent();
            }

            String template = TEMPLATE_CACHE.get(resource);
            if(template == null) {
                template = Resources.toString(Resources.getResource(resource), Charsets.UTF_8);
                TEMPLATE_CACHE.put(resource, template);
            }

            return jjint.render(template);
        } catch(IOException e) {
            throw new JinJavaStrutsResourceException(
                    String.format("problem finding [%s] template", resource),
                        e
            );
        }
    }

    /**
     *
     * @param resource
     *      path to the resource on the classpath
     * @return
     *      result of template file parsed by JinJava
     */
    public static String renderResource(String resource) {
        return renderResource(resource, JinjavaInterpreter.getCurrent());
    }

    /**
     *
     * @param helpers
     *      String from TagNode.getHelpers()
     * @return
     *      Map of parameter name.  Key is the helper label
     */
    public static Map<String, String> tagParameterHelperMap(String helpers) {
        Map<String, String> paramMap = new HashMap<>();

        List<String> helperList = new HelperStringTokenizer(helpers).allTokens();
        for(String s : helperList) {
            if(!s.contains("=")) {
                throw new RuntimeException("Label not present on tag parameter.  Check syntax. {% my_tag aparam='value' %}");
            }

            String[] split = s.split("\\=");
            String label = split[0].trim();
            String value = split[1].trim();
            if(value.startsWith("'") && value.endsWith("'")) {
                value = value.substring(1, value.length() - 1);
            }

            paramMap.put(label, value);
        }

        return paramMap;
    }


    /**
     *
     * @return
     *      An instance of the JinJava api
     */
    public static Jinjava getJinJava() {
        try {
            if (jinjava == null) {
                jinjava = new Jinjava();

                if(ServletActionContext.getContext() != null) {
                    // will be null when running Junit tests
                    String realPath =  ServletActionContext.getServletContext().getRealPath(
                            JinJavaResult.getTemplateBasePath()
                    );
                    if(realPath == null) {
                        throw new JinJavaStrutsInitException(String.format("Struts Constant struts.jinjava.basepath contains an invalid path[%s]", JinJavaResult.getTemplateBasePath()));
                    }
                    File baseTemplateDir = new File(realPath);
                    FileLocator jjFileLocator = new FileLocator(baseTemplateDir);
                    jinjava.setResourceLocator(jjFileLocator);
                }

                /*String realPath =  ServletActionContext.getServletContext().getRealPath(
                        JinJavaResult.getTemplateBasePath()
                );

                if(realPath == null) {
                    throw new JinJavaStrutsInitException(String.format("Struts Constant struts.jinjava.basepath contains an invalid path[%s]", JinJavaResult.getTemplateBasePath()));
                }
                File baseTemplateDir = new File(realPath);
                FileLocator jjFileLocator = new FileLocator(baseTemplateDir);
                jinjava.setResourceLocator(jjFileLocator);*/

                Context jjContext = jinjava.getGlobalContext();



                // register custom functions
                jjContext.registerFunction(new ELFunctionDefinition("boot", "input", BootstrapFunctions.class, "input", Map.class));
                jjContext.registerFunction(new ELFunctionDefinition("boot", "checkbox", BootstrapFunctions.class, "checkbox", Map.class));
                jjContext.registerFunction(new ELFunctionDefinition("boot", "checkboxes", BootstrapFunctions.class, "checkboxes", List.class));
                jjContext.registerFunction(new ELFunctionDefinition("boot", "radio", BootstrapFunctions.class, "radio", Map.class));
                jjContext.registerFunction(new ELFunctionDefinition("boot", "radios", BootstrapFunctions.class, "radios", List.class));
                jjContext.registerFunction(new ELFunctionDefinition("boot", "textarea", BootstrapFunctions.class, "textarea", Map.class));
                jjContext.registerFunction(new ELFunctionDefinition("boot", "select", BootstrapFunctions.class, "select", Map.class));

                // register custom filters
                jjContext.registerFilter(new RemoveMapEntry());
                jjContext.registerFilter(new GetMapEntry());
                jjContext.registerFilter(new IfElse());
                jjContext.registerFilter(new IsEmpty());
                jjContext.registerFilter(new GetProperty());
                jjContext.registerFilter(new Contains());
                jjContext.registerFilter(new SplitFilter());
                jjContext.registerFilter(new HtmlLineBreak());


                // register custom tags
                jjContext.registerTag(new FormTag());
                jjContext.registerTag(new PanelTag());
                jjContext.registerTag(new ButtonTag());
            }
        } catch(Exception e) {
            throw new JinJavaStrutsInitException("failed to setup jinjava", e);
        }


        //global vars
        jinjava.getGlobalContext().put("timestamp", System.currentTimeMillis());

        return jinjava;
    }


    public static void registerFilter(Filter filter) {
        getJinJava().getGlobalContext().registerFilter(filter);
    }

    public static void registerTag(Tag tag) {
        getJinJava().getGlobalContext().registerTag(tag);
    }

    public static void registerFunction(ELFunctionDefinition function) {
        getJinJava().getGlobalContext().registerFunction(function);
    }

}
