package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by paul zepernick on 7/19/2017.
 */
public class ContainsTest {

    JinjavaInterpreter interpreter;


    @Before
    public void setup() {
       interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }

    @Test
    public void testContainsString() {
        String expression = "{{ 'what do you want?!?!'|contains('do you') }}";
        String result = interpreter.render(expression);
        Boolean resultBool = Boolean.valueOf(result);
        Assert.assertTrue("contains should have returned TRUE", resultBool);

        expression = "{{ 'what do you want?!?!'|contains('not found') }}";
        result = interpreter.render(expression);
        resultBool = Boolean.valueOf(result);
        Assert.assertFalse("contains should have returned FALSE", resultBool);

    }

    @Test
    public void testArrayContains() {
        String expression = "{{ ['val1', 'val2', 'val3']|contains('val2') }}";
        String result = interpreter.render(expression);
        Boolean resultBool = Boolean.valueOf(result);
        Assert.assertTrue("contains should have returned TRUE", resultBool);


        expression = "{{ ['val1', 'val2', 'val3']|contains('val4') }}";
        result = interpreter.render(expression);
        resultBool = Boolean.valueOf(result);
        Assert.assertFalse("contains should have returned FALSE", resultBool);


    }

    @Test
    public void testMapContains() {
        String expression = "{{ {'key1': 'value1', 'key2': 'value2', 'key3' : 'value3'}|contains('key2') }}";
        String result = interpreter.render(expression);
        Boolean resultBool = Boolean.valueOf(result);
        Assert.assertTrue("contains should have returned TRUE", resultBool);

        expression = "{{ {'key1': 'value1', 'key2': 'value2', 'key3' : 'value3'}|contains('you shall not be found') }}";
        result = interpreter.render(expression);
        resultBool = Boolean.valueOf(result);
        Assert.assertFalse("contains should have returned FALSE", resultBool);
    }


}
