package com.gionee.autotest.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 死机重启Log的解析文件过滤
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-29 12:00:53
 */
public class DieAndOffResultFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
//        return name.contains(Constant.EXCEPTION_EXCEL);
/*        System.out.println("dir : " + dir.getPath());
        System.out.println("name : " + name);
        boolean isFilter = new File(dir , name).isDirectory()  ;
        boolean isEx = (new File(dir , name).isFile() && name.equals(Constant.EXCEPTION_EXCEL)) ;
        System.out.println("text : " + isFilter + "-----------: " + isEx);
        System.out.println("*****************************************************8");*/
        return new File(dir , name).isDirectory()
                || (new File(dir , name).isFile() && name.equals(Constant.EXCEPTION_EXCEL)) ;
    }
}