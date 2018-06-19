package com.gionee.autotest.util;

/**
 * @Author Viking Den
 * @Version 1.0
 * @Email dengwj@gionee.com
 * @Time 14:52
 */
public enum DieOff {

    JE("JE"),
    SWT("SWT");

    String name ;

    DieOff(String name){
        this.name = name ;
    }

    public String getName() {
        return name;
    }
}
