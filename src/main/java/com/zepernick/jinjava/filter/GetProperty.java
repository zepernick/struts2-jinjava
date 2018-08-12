package com.zepernick.jinjava.filter;

import com.google.common.base.Splitter;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;

import java.util.List;
import java.util.Map;

/**
 * Retrieves a property from a Bean or Map
 *
 * @author PaulZepernick
 */
public class GetProperty implements Filter {
    @Override
    public Object filter(Object o, JinjavaInterpreter jinjavaInterpreter, String... strings) {

        if (strings.length != 1) {
            throw new InterpretException("filter getprop expects 1 arg >>> " + strings.length);
        }

        if(o instanceof Map) {
            Map<String, ?> m = (Map)o;
            return m.get(strings[0]);
        }

        List<String> path = Splitter.on('.').splitToList(strings[0]);
        return jinjavaInterpreter.resolveProperty(o, path);
    }

    @Override
    public String getName() {
        return "getprop";
    }
}
