package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;

import java.util.Map;

/**
 * Created by PaulZepernick on 3/4/2016.
 */
public class RemoveMapEntry implements Filter {
    @Override
    public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
        if (args.length == 0) {
            throw new InterpretException("filter remove_entry expects at least 1 arg >>> " + args.length);
        }

        if(var instanceof Map) {
            Map<String, ?>  map = (Map<String, ?>) var;
            for(String arg : args) {
                map.remove(arg);
            }
        }

        return var;
    }

    @Override
    public String getName() {
        return "remove_entry";
    }
}
