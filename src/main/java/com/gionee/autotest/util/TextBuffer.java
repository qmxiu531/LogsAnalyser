package com.gionee.autotest.util;

import java.util.LinkedList;

/**
 * 定义一字符串的缓冲器
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-26 16:40:42
 */
public class TextBuffer {

    /**
     * 初始化的缓冲容量为100，如100行
     */
    int capacity = 100 ;

    private LinkedList<String> box ;

    /**
     * 构造默认的TextBuffer，缓冲容量为默认的100
     */
    public TextBuffer(){
        box = new LinkedList<String>() ;
    }

    /**
     *构造TextBuffer，指定缓冲容量
     */
    public TextBuffer(int capacity){
        this.capacity = capacity ;
        box = new LinkedList<String>() ;
    }

    /**
     * 添加数据到缓冲器中
     * @param text 添加到缓冲器中的数据
     */
    public void add(String text){
        if(text == null || "".equals(text)){
            return ;
        }
        //判断当前的容器是否饱和，如果饱和，移除最后一个；如果不饱和，直接添加
        if(box.size() >= capacity){
            box.removeFirst() ;
        }
        box.addLast(text);
    }

    /**
     * 清除容器中的所有数据
     */
    public void clear(){
        box.clear();
    }

    /**
     * 以字符串的方式得到容器中所有的数据
     * @return
     */
    public String getAllText(){
        if(box.isEmpty())
            return "" ;
        StringBuilder sb = new StringBuilder() ;
        for(String text : box){
            sb.append(text + "\n") ;
        }
        return sb.toString() ;
    }

}
