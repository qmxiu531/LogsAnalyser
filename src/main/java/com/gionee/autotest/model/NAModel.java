package com.gionee.autotest.model;

import java.util.List;

/**
 * @Author Viking Den
 * @Version 1.0
 * @Email dengwj@gionee.com
 * @Time 10:48
 */
public class NAModel {

    int type ;

    String reason ;

    List<String> caseName ;

/*    int count ;*/

    public NAModel(int type, String reason /*, int count*/) {
        this.type = type;
        this.reason = reason;
//        this.count = count ;
    }

    public List<String> getCaseName() {
        return caseName;
    }

    public void setCaseName(List<String> caseName) {
        this.caseName = caseName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
/*
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NAModel naModel = (NAModel) o;

        if (type != naModel.type) return false;
        return reason != null ? reason.equals(naModel.reason) : naModel.reason == null;

    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
