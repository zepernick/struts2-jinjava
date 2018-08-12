package com.zepernick.jinjava.tag;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.tag.Tag;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.zepernick.jinjava.JinJavaTemplate;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by KevinBonnett on 3/29/2016.
 */
public class PanelTag implements Tag {
    @Override
    public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {

        StringBuilder buff = new StringBuilder();
        Map<String, String> helpers = JinJavaTemplate.tagParameterHelperMap(tagNode.getHelpers());


        // run the body of the form tag through the interpreter
        for (Node node : tagNode.getChildren()) {
            buff.append(node.render(interpreter));
        }

        String parsedTag = null;
        try (JinjavaInterpreter.InterpreterScopeClosable itr = interpreter.enterScope()) {
            interpreter.getContext().put("panel_body", buff.toString());

            // add defaults
            HttpServletRequest request = ServletActionContext.getRequest();
            helpers.put("context", request.getContextPath());

            // add context to the form action
            String panelAction = request.getContextPath() + helpers.get("param_action");

            for (Map.Entry<String, String> p : helpers.entrySet()) {
                interpreter.getContext().put("param_" + p.getKey(), p.getValue());
            }

            parsedTag = JinJavaTemplate.renderResource("com/hs/jinjava/tag/panel.html", interpreter);
        }

            return parsedTag;
    }

    @Override
    public String getEndTagName() {
        return "endpanel";
    }

    @Override
    public String getName() {
        return "panel";
    }
}
