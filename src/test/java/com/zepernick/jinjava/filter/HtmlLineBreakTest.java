package com.zepernick.jinjava.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by PaulZepernick on 10/23/2017.
 * Last Modified by PaulZepernick on 10/23/2017.
 */
public class HtmlLineBreakTest {

    JinjavaInterpreter interpreter;

    @Before
    public void setup() {
        interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }

    @Test
    public void replaceLineBreaks() {
        String result = interpreter.render("{{ 'a string with a \nline \nbreak'|htmllinebreak  }}");
        int matches = StringUtils.countMatches(result, "<br>");
        Assert.assertEquals("expected to replace 2 line breaks in the string", 2, matches);
    }

}
