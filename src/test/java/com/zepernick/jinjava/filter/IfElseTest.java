package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by paul zepernick on 7/20/2017.
 */
public class IfElseTest {

    JinjavaInterpreter interpreter;

    @Before
    public void setup() {
        interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }

    @Test
    public void testIfTrue() {
        String result = interpreter.render("{{ true|ifelse('i am true', 'i am false') }}");
        Assert.assertEquals("true is expected result", "i am true", result);

    }

    @Test
    public void testIfFalse() {
        String result = interpreter.render("{{ false|ifelse('i am true', 'i am false') }}");
        Assert.assertEquals("false is expected result", "i am false", result);

    }


}
