package com.gionee.autotest.util;

import javax.print.attribute.standard.MediaSize;

/**
 * @Author Viking Den
 * @Version 1.0
 * @Email dengwj@gionee.com
 * @Time 9:38
 */
public enum NAType {

    SIM("sim", 2 , "未插sim卡或sim卡不可用，请插入sim卡测试") ;

    String name ;

    int type ;

    String reason ;

    NAType(String name, int type, String reason) {
        this.name = name;
        this.type = type;
        this.reason = reason;
    }

    public static NAType getType(String name){
        for (NAType t : NAType.values()){
            if (t.getName().equals(name)){
                return t ;
            }
        }
        return null ;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }
}


