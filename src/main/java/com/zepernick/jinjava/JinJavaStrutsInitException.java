package com.zepernick.jinjava;

/**
 * Created by PaulZepernick on 7/27/2016.
 */
public class JinJavaStrutsInitException extends RuntimeException{

    public JinJavaStrutsInitException(String msg) {
        super(msg);
    }

    public JinJavaStrutsInitException(String msg, Throwable e) {
        super(msg, e);
    }

}
