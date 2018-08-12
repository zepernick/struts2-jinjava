package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by PaulZepernick on 10/23/2017.
 * Last Modified by PaulZepernick on 10/23/2017.
 */
public class HtmlLineBreak implements Filter {

    @Override
    public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
        if(var == null) {
            return "";
        }

        if(!(var instanceof String)) {
            return var;
        }

        String s = (String)var;
        return StringUtils.replace(s, "\n", "<br>");

    }

    @Override
    public String getName() {
        return "htmllinebreak";
    }
}
