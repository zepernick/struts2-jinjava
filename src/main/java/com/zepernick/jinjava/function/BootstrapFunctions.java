package com.zepernick.jinjava.function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;

import java.util.*;

/**
 * Created by PaulZepernick on 3/1/2016.
 */
public class BootstrapFunctions {

    private static Set<String> COMMON_USED_PARAMS = ImmutableSet.of(
            "label", "help", "class", "icon", "wrapper_css", "help_icon",
            "html_help_icon", "required"
    );



    private BootstrapFunctions(){}

    public static String input(Map<String, Object> params) {
         return evalJinjava(
                params,
                ImmutableSet.of("password"),
                "com/hs/jinjava/function/boot_input.html"
        );

    }

    public static String textarea(Map<String, Object> params) {

        return evalJinjava(
                params,
                ImmutableSet.of("value"),
                "com/hs/jinjava/function/boot_textarea.html"
        );
    }


    public static String checkbox(Map<String, Object> params) {
        // default value for the checkbox if not specified
        params.putIfAbsent("value", "true");
        return evalJinjava(
                params,
                null,
                "com/hs/jinjava/function/boot_checkbox.html"
        );
    }


    public static String checkboxes(List<Map<String, Object>> params) {
        StringBuilder sb = new StringBuilder();
        for(Map<String, Object> p : params) {
           sb.append(checkbox(p));
        }

        return sb.toString();
    }

    public static String select(Map<String, Object> params) {

        // force the value into a collection so that we can support multiple value selects
        Object controlValue = params.get("value");
        if(controlValue == null) {
            params.put("value", Collections.EMPTY_LIST);
        } else if(!(controlValue instanceof Collection)) {
            params.put("value", ImmutableList.of(String.valueOf(controlValue)));
        }

        //System.out.println(params);

        return evalJinjava(
                params,
                ImmutableSet.of("options", "option_value", "option_text", "value", "option_prepend"),
                "com/hs/jinjava/function/boot_select.html"
        );

    }

    public static String radio(Map<String, Object> params) {

        return evalJinjava(
                params,
                null,
                "com/hs/jinjava/function/boot_radio.html"
        );
    }


    public static String radios(List<Map<String, Object>> params) {
        StringBuilder sb = new StringBuilder();
        for(Map<String, Object> p : params) {
            sb.append(radio(p));
        }

        return sb.toString();
    }

    private static String evalJinjava(Map<String, Object> params, Set<String> usedParams, String resource) {

        String name = (String)params.getOrDefault("name", "");
        if (name.equalsIgnoreCase("percentage")) {
            System.out.println("percentage");
        }

        // this scope is important.  It gets rid of all vars put into the context at the end of the try block
        // otherwise these vars remain in the global context and can get picked up by other
        try (JinjavaInterpreter.InterpreterScopeClosable itr = JinjavaInterpreter.getCurrent().enterScope()) {
            Context context = JinjavaInterpreter.getCurrent().getContext();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                context.put("param_" + entry.getKey(), entry.getValue());
            }

            if (usedParams != null) {
                params.keySet().removeAll(usedParams);
            }

            params.keySet().removeAll(COMMON_USED_PARAMS);

            context.put("param_map", params);

            return JinJavaTemplate.renderResource(resource, JinjavaInterpreter.getCurrent());
        }
    }
}
