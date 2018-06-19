package com.gionee.autotest.util;

/**
 * 异常类的集合
 * TODO 转换为配置文件
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-2 15:16:13
 */
public enum Exceptions {

    EXCEPTION_01("java.lang.NullPointerException" , "空指针异常") ,
    EXCEPTION_02("java.lang.ClassNotFoundException" , "类不存在异常") ,
    EXCEPTION_03("java.lang.ArithmeticException" , "数学运算异常") ,
    EXCEPTION_04("java.lang.ArrayIndexOutOfBoundsException" , "数组下标越界异常") ,
    EXCEPTION_05("java.lang.IllegalArgumentException" , "非法参数异常") ,
    EXCEPTION_06("java.lang.IllegalStateException" , "非法状态异常") ,
    EXCEPTION_07("java.lang.NegativeArrayException" , "数组负下标异常") ,
    EXCEPTION_08("java.lang.SecturityException" , "违背安全原则异常") ,
    EXCEPTION_09("java.lang.EOFException" , "文件已结束异常") ,
    EXCEPTION_10("java.lang.FileNotFoundException" , "文件未找到异常") ,
    EXCEPTION_11("java.lang.NumberFormatException" , "数字转换异常") ,
    EXCEPTION_12("java.lang.SQLException" , "操作数据库异常") ,
    EXCEPTION_13("java.lang.IOException" , "输入输出异常") ,
    EXCEPTION_14("java.lang.NoSuchMethodException" , "方法未找到异常") ,
    EXCEPTION_15("java.lang.OutOfMemoryError" , "内存不足错误") ,
    EXCEPTION_16("java.lang.StackOverflowError" , "堆栈溢出错误") ,
    EXCEPTION_17("java.lang.NoSuchMethodError" , "找不到方法错误") ,
    EXCEPTION_18("java.lang.RuntimeException" , "运行时异常") ,
    EXCEPTION_19("java.lang.IndexOutOfBoundsException" , "下标越界异常") ,
    EXCEPTION_20("java.lang.IllegalAccessException" , "非法访问异常") ,
    EXCEPTION_21("java.util.concurrent.TimeoutException" , "超时异常") ,
    EXCEPTION_22("java.util.ConcurrentModificationException" , "并发修改异常") ,
    EXCEPTION_23("java.util.NoSuchElementException" , "元素错误异常") ,
    EXCEPTION_24("java.lang.ClassCastException" , "类转换异常") ,
    EXCEPTION_25("java.lang.IncompatibleClassChangeError" , "不兼容类错误") ,
    EXCEPTION_26("java.lang.SecurityException" , "安全异常") ;

    String name ;

    String description ;

    Exceptions(String name , String description){
        this.name = name ;
        this.description = description ;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

}
