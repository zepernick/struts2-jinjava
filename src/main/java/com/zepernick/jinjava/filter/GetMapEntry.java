package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;

import java.util.Map;
import java.util.Set;

/**
 * Created by PaulZepernick on 3/4/2016.
 */
public class GetMapEntry implements Filter {

    @Override
    public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {

        if (args.length != 1) {
            throw new InterpretException("filter getEntry expects 1 arg >>> " + args.length);
        }

        if(var instanceof Map) {
            Map<String, ?> map = (Map<String, ?>)var;
            return map.get(args[0]);
        } else if(var instanceof Set) {
            // received an EntrySet
            Set<Map.Entry> entries = (Set<Map.Entry>)var;
            for(Map.Entry<String, ?> m : entries) {
                if(m.getKey().equals(args[0])) {
                    return m.getValue();
                }
            }
        }

        return "";
    }

    @Override
    public String getName() {
        return "entry";
    }
}
