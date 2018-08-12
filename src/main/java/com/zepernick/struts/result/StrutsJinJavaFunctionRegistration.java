package com.zepernick.struts.result;

import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;

import java.util.List;

/**
 * Can be implemented by classes in the "struts.jinjava.scan.functionPackage" struts.xml parameter to
 * expose custom functions outside of the JinJava Struts 2 plugin
 */
public interface StrutsJinJavaFunctionRegistration {

    List<ELFunctionDefinition> register();

}
