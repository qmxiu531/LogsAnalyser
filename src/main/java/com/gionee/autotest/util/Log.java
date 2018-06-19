package com.gionee.autotest.util;

/**
 * Log输出类
 *
 * @param
 * @author Viking Den
 * @version 1.0
 * @date 2016年1月7日14:31:48
 */
public class Log {

    private static final String TAG = "LogsAnalyser" ;

    public static void i(String msg){
        i(TAG , msg);
    }

    public static void i(String tag , String msg){
        System.out.println(tag  + " : "+ msg);
    }
}
