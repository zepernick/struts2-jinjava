package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;

import java.util.Collection;
import java.util.Map;

/**
 * Created by PaulZepernick on 3/8/2016.
 */
public class IsEmpty implements Filter {
    @Override
    public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
        if(var == null) {
            return true;
        }

        if(var instanceof Collection) {
            Collection c = (Collection)var;
            return c.isEmpty();
        } else if(var instanceof Map) {
            Map m = (Map)var;
            return m.isEmpty();
        } else if (var.getClass().isArray()) {
            Object[] o = (Object[])var;
            return o.length == 0;
        } else if(var instanceof String) {
            String s = (String)var;
            return s.trim().isEmpty();
        }

        throw new InterpretException("Could not evaluate class to empty expression " + var.getClass());
    }

    @Override
    public String getName() {
        return "isempty";
    }
}
