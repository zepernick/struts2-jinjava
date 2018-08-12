package com.zepernick.jinjava.tag;

import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.tag.Tag;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.zepernick.jinjava.JinJavaTemplate;

import java.util.Map;
import java.util.Set;

/**
 * Created by PaulZepernick on 11/9/2016.
 */
public abstract class HsTagAbs implements Tag{

    @Override
    public String interpret(TagNode tagNode, JinjavaInterpreter jinjavaInterpreter) {
        StringBuilder buff = new StringBuilder();
        Map<String, String> paramMap = JinJavaTemplate.tagParameterHelperMap(tagNode.getHelpers());


        // run the body of the form tag through the interpreter
        for (Node node : tagNode.getChildren()) {
            buff.append(node.render(jinjavaInterpreter));
        }

        String parsedTemplate = null;
        try (JinjavaInterpreter.InterpreterScopeClosable itr = jinjavaInterpreter.enterScope()) {
            // send the result of the interpreted tag body our as a
            // jinjava variable
            jinjavaInterpreter.getContext().put("tag_body", buff.toString());

            // add all parameters passed in as jinjava variables
            for (Map.Entry<String, String> p : paramMap.entrySet()) {
                // run each value through the interpreter in case they contain jinjava
                String value = jinjavaInterpreter.render(p.getValue());
                jinjavaInterpreter.getContext().put("param_" + p.getKey(), value);
            }

            Set<String> knownParameters = parametersUsedByTag();
            if(knownParameters != null) {
                paramMap.keySet().removeAll(knownParameters);
            }

            // allow the implemnting class to push vars into the context
            exportVars(jinjavaInterpreter.getContext());

            jinjavaInterpreter.getContext().put("tag_attributes", paramMap);

            parsedTemplate = JinJavaTemplate.renderResource(templateHtmlFilePath(), jinjavaInterpreter);
        }

        return parsedTemplate;

    }

    /**
     * Allows the opportunity to export additional vars to the jinjava context before the
     * context is made available to the html template
     *
     * @param context
     *      Current context in scope
     */
    protected void exportVars(Context context) {

    }

    /**
     *
     * @return
     *      Set of the parameters being used by this tag and would not be
     *      included in the attributes map
     */
    protected abstract Set<String> parametersUsedByTag();

    /**
     *
     * @return
     *      Path to the html template file that is being used to produce this template
     */
    protected abstract String templateHtmlFilePath();
}
