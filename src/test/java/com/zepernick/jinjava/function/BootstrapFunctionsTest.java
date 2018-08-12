package com.zepernick.jinjava.function;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.zepernick.jinjava.JinJavaTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paul zepernick on 7/21/2017.
 */
public class BootstrapFunctionsTest {

    JinjavaInterpreter interpreter;


    @Before
    public void setup() {
        interpreter = JinJavaTemplate.getJinJava().newInterpreter();
    }

    @Test
    public void testTextArea(){

        Map<String, Object> context = new HashMap<>();
        context.put("control", new Control());

        String template = "{{ boot:textarea({\n" +
                "                'name' : 'control.value',\n" +
                "                'rows' : '3',\n" +
                "                'value' : control.value,\n" +
                "                'label' : 'Description'\n" +
                "            })\n" +
                "        }}";

        String result = JinJavaTemplate.getJinJava().render(template, context);

       /* try {
            FileUtils.write(new File("c:/tmp/debug.txt"), result);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Document dom = Jsoup.parseBodyFragment(result);
        verifyFormGroup(dom);

        Element textAreaEle = dom.select("div textarea").first();
        String textAreaName = textAreaEle.attr("name");
        Assert.assertEquals("name should be control.value", "control.value", textAreaName);

        String cssClass = textAreaEle.attr("class").trim();
        Assert.assertEquals("form-control", cssClass);

        String rowsAttr = textAreaEle.attr("rows").trim();
        Assert.assertEquals("3", rowsAttr);
    }

    @Test
    public void testSelectNoHeaderOrLabel() {
        Map<String, Object> context = new HashMap<>();
        Department department =  new Department(2, "dept 2");
        context.put("department", department);
        context.put("departments", department.listDepartments());


        String template = "{{ boot:select({\n" +
                "                    'name' : 'department.id',\n" +
                "                    'value' : department.id,\n" +
                "                    'options' : departments,\n" +
                "                    'option_value' : 'id',\n" +
                "                    'option_text' : 'name'\n" +
                "                })\n" +
                "            }}";

        String result = JinJavaTemplate.getJinJava().render(template, context);
      /*  try {
            FileUtils.write(new File("c:/tmp/debug.txt"), result);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
     // System.out.println(result);
        Document dom = Jsoup.parseBodyFragment(result);
        int totalOpts = dom.select("div select option").size();
        Assert.assertEquals("total options should be 3", 3, totalOpts);

        Boolean noLabel = dom.select("div label").isEmpty();
        Assert.assertTrue("label should not be present", noLabel);
    }

    @Test
    public void testSelect() {
        Map<String, Object> context = new HashMap<>();
        Department department =  new Department(2, "dept 2");
        context.put("department", department);
        context.put("departments", department.listDepartments());


        String template = "{{ boot:select({\n" +
                "                    'name' : 'department.id',\n" +
                "                    'value' : department.id,\n" +
                "                    'options' : departments,\n" +
                "                    'option_value' : 'id',\n" +
                "                    'option_text' : 'name',\n" +
                "                    'option_prepend' : '|--select one--',\n" +
                "                    'label' : 'Default Department'\n" +
                "                })\n" +
                "            }}";

        String result = JinJavaTemplate.getJinJava().render(template, context);
      /*  try {
            FileUtils.write(new File("c:/tmp/debug.txt"), result);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Document dom = Jsoup.parseBodyFragment(result);
        verifyFormGroup(dom);

        boolean hasLabel = !dom.select("div label").isEmpty();
        Assert.assertTrue("select should have label", hasLabel);

        Element selectEle = dom.select("div select").first();
        // verify the name of the select control per the config
        String selectName = selectEle.attr("name").trim();
        Assert.assertEquals("department.id", selectName);

        //verify the css class of the control
        String selectCss = selectEle.attr("class").trim();
        Assert.assertEquals("form-control", selectCss);

        // verify 4 options with the header option
        Elements optionElements = dom.select("div select option");
        Assert.assertEquals("should be 4 options", 4, optionElements.size());

        Elements selectedOptions = dom.select("div select option[selected]");
        if(selectedOptions.isEmpty()) {
            Assert.fail("could not find selected option in the select list");
        }

        // verify that we selected the correct option per the template
        Element selectedOpt = selectedOptions.first();
        String optValue = selectedOpt.attr("value").trim();
        Assert.assertEquals("value should be 2", "2", optValue);
    }


    @Test
    public void testInput() {

        Map<String, Object> context = new HashMap<>();
        context.put("control", new Control());
        String template = " {{ boot:input({\n" +
                "                    'name': 'control.value',\n" +
                "                    'label' : 'My Control',\n" +
                "                    'value' : control.value\n" +
                "                })\n" +
                "            }}";

        String result = JinJavaTemplate.getJinJava().render(template, context);
     /*   try {
            FileUtils.write(new File("c:/tmp/debug.txt"), result);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Document dom = Jsoup.parseBodyFragment(result);
        verifyFormGroup(dom);

        boolean labelIsPresent = !dom.select("div label").isEmpty();
        Assert.assertTrue("should have label", labelIsPresent);

        Element input = dom.select("div input").first();
        String inputType = input.attr("type").trim();
        Assert.assertEquals("should be input type text", "text", inputType);

        String inputName = input.attr("name").trim();
        Assert.assertEquals("should be control.value", "control.value", inputName);

        String inputValue = input.attr("value").trim();
        Assert.assertEquals("should be 'a value'", "a value", inputValue);

        String inputClass = input.attr("class").trim();
        Assert.assertEquals("should be form-control", "form-control", inputClass);

    }

    private void verifyFormGroup(Document dom) {
        String formGroupDivClass = dom.select("div").attr("class").trim();
        Assert.assertEquals("element should have form-group class", "form-group", formGroupDivClass);
    }


    private class Control {
        private String value = "a value";

        public String getValue() {
            return value;
        }
    }

    private class Department {
        private Integer id;

        private String name;

        public Department(Integer id, String name) {
            this.id = id;
            this.name  = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        private List<Department> listDepartments() {
            List<Department> retList = new ArrayList<>();
            retList.add(new Department(1, "dept 1"));
            retList.add(new Department(2, "dept 2"));
            retList.add(new Department(3, "dept 3"));

            return retList;
        }
    }
}
