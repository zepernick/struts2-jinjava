package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by paul zepernick on 7/20/2017.
 */
public class IsEmptyTest {

    JinjavaInterpreter interpreter;


    @Before
    public void setup() {
        interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }

    @Test
    public void testEmptyMap() {
        String result = interpreter.render("{{ {}|isempty }}");
        Boolean resultBool = Boolean.valueOf(result);
        Assert.assertTrue("expected empty map", resultBool);
    }

    @Test
    public void testNotEmptyMap() {
        String result = interpreter.render("{{ {'key1' : 'val1'}|isempty }}");
        Boolean resultBool = Boolean.valueOf(result);
        Assert.assertFalse("expected non empty map", resultBool);
    }
}
