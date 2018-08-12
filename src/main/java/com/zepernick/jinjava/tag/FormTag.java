package com.zepernick.jinjava.tag;

import com.google.common.collect.ImmutableSet;
import com.hubspot.jinjava.interpret.Context;

import java.util.Set;

/**
 * Created by PaulZepernick on 3/16/2016.
 */
public class FormTag extends HsTagAbs {

    @Override
    protected Set<String> parametersUsedByTag() {
        return ImmutableSet.of("action", "method", "record_id");
    }

    @Override
    protected String templateHtmlFilePath() {
        return "com/hs/jinjava/tag/form.html";
    }

    @Override
    protected void exportVars(Context context) {
        context.putIfAbsent("param_action", "");
        context.putIfAbsent("param_method", "GET");

       /* String formAction = (String)context.get("param_action");
        String formMethod = (String)context.get("param_method");
        String recordId = (String)context.get("param_record-id");

        // struts rest URL's should have the record ID at the end of the URL for PUT request
        if("put".equalsIgnoreCase(formMethod) && StringUtils.isNotBlank(recordId)) {
            if(!formAction.endsWith("/")) {
                formAction += "/";
            }
            formAction += recordId;
            context.put("param_action", formAction);
        } */

    }


    @Override
    public String getEndTagName() {
        return "endform";
    }

    @Override
    public String getName() {
        return "form";
    }

}
