package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paul zepernick on 7/20/2017.
 */
public class RemoveMapEntryTest {

    JinjavaInterpreter interpreter;

    @Before
    public void setup() {
        interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }

    @Test
    public void removeEntryTest() {
        Map<String, Object> hm = new HashMap<>();
        hm.put("key1", "val1");
        hm.put("key2", "val2");
        interpreter.getContext().put("map", hm);
        interpreter.render("{{ map|remove_entry('key2') }}");
        Assert.assertFalse("map should not contain key2", hm.containsKey("key2"));

    }

}
