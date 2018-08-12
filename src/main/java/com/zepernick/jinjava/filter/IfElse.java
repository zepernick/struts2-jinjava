package com.zepernick.jinjava.filter;

import com.google.common.collect.ImmutableSet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;

import java.util.Set;

/**
 * Created by PaulZepernick on 3/8/2016.
 */
public class IfElse implements Filter{

    private static final Set<String> BOOLEANS_TRUE = ImmutableSet.of("true", "1", "y");


    @Override
    public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {

        if (args.length != 2) {
            throw new InterpretException("filter getEntry expects 2 arg >>> " + args.length);
        }

        if(var != null) {
            String booleanExp = String.valueOf(var).toLowerCase().trim();
            if(BOOLEANS_TRUE.contains(booleanExp)) {
                return args[0];
            }

        }

        return args[1];
    }

    @Override
    public String getName() {
        return "ifelse";
    }
}
