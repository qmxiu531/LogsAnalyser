package com.gionee.autotest.model;

/**
 * 异常的Model
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-1 10:46:06
 */
public class ExceptionModel {

    private String name ;

    private String description ;

    public ExceptionModel(){

    }

    public ExceptionModel(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
