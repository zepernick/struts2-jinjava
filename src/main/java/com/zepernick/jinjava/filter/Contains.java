package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;

import java.util.List;
import java.util.Map;

/**
 * Created by PaulZepernick on 3/17/2016.
 */
public class Contains implements Filter {
    @Override
    public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {

        if (args.length == 0) {
            throw new InterpretException("filter contains expects 1 arg >>> " + args.length);
        }

        if(var instanceof String) {
            String s = (String)var;

            // all arguments must match
            for(String a : args) {
                if(!s.contains(a)) {
                    return false;
                }
            }
            return true;

        } else if(var instanceof List) {
            List<String> list = (List<String>)var;

            // all arguments must match
            for(String a : args) {
                if(!list.contains(a)) {
                    return false;
                }
            }
            return true;

        } else if(var instanceof Map) {
            Map<String, Object> map = (Map<String, Object>)var;
            for(String a : args) {
                if(!map.containsKey(a)) {
                    return false;
                }
            }
            return true;
        }


        // TODO log warning to the error log that the class passed in is not supported for the filter
        return false;
    }

    @Override
    public String getName() {
        return "contains";
    }
}
