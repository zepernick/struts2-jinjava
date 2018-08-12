package com.zepernick.jinjava.tag;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Created by Paul Zepernick on 11/9/2016.
 * Updated by Justin Musick on 11/10/2016.
 */

public class ButtonTag extends HsTagAbs{

    @Override
    public String getEndTagName() {
        return "endbutton";
    }

    @Override
    public String getName() {
        return "button";
    }

    @Override
    protected Set<String> parametersUsedByTag() {
        return ImmutableSet.of("type", "class");
    }

    @Override
    protected String templateHtmlFilePath() {
        return "com/hs/jinjava/tag/button.html";
    }
}
