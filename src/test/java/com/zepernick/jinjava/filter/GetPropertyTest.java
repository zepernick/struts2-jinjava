package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by paul zepernick on 7/20/2017.
 */
public class GetPropertyTest {

    JinjavaInterpreter interpreter;

    @Before
    public void setup() {
        interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }


    @Test
    public void getPropertyFromMap() {
        String result = interpreter.render("{{ {'key1' : 'val1', 'key2' : 'val2'}|getprop('key2') }}");
        Assert.assertEquals("key2 should equal val2", "val2", result);
    }

    @Test
    public void getPropertyFromBean() {
        interpreter.getContext().put("testBean", new TestBean());
        String result = interpreter.render("{{ testBean|getprop('prop1') }}");
        Assert.assertEquals("prop1 should equal val1", "val1", result);
    }

    private class TestBean {
        String prop1 = "val1";
        String prop2 = "val2";

        public String getProp1() {
            return prop1;
        }

        public String getProp2() {
            return prop2;
        }
    }


}
