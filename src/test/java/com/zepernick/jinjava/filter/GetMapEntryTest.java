package com.zepernick.jinjava.filter;

import com.google.common.collect.ImmutableMap;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * Created by paul zepernick on 7/19/2017.
 */
public class GetMapEntryTest {

    JinjavaInterpreter interpreter;


    @Before
    public void setup() {
        interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }

    @Test
    public void getEntryFromMap() {
        String expression = "{{ {'key1': 'value1', 'key2': 'value2', 'key3' : 'value3'}|entry('key2') }}";
        String result = interpreter.render(expression);
        Assert.assertEquals("should return value2", "value2", result);
    }

    @Test
    public void getEntryFromEntrySet() {
        Map<String, Object> m = ImmutableMap.of("key1", "value1", "key2", "value2");
        Set<Map.Entry<String, Object>> entrySet = m.entrySet();
        String expression = "{{ entrySet|entry('key1') }}";
        interpreter.getContext().put("entrySet", entrySet);
        String result = interpreter.render(expression);
        Assert.assertEquals("shoul return value1", "value1", result);
    }

}
