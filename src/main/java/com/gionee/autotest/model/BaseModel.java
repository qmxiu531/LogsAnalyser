package com.gionee.autotest.model;

/**
 * 几种不同类型异常的Base类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-27 09:27:11
 */
public class BaseModel {

    String name ;

    String shortMsg ;

    String log ;
    
    int count ;

	public BaseModel(){}

    public BaseModel(String name, String shortMsg , String log,int count) {
        this.name = name;
        this.shortMsg = shortMsg ;
        this.log = log;
        this.count=count;
    }

    public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }
}
