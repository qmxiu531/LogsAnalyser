package com.gionee.autotest.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Log文件的过滤器，当前只判定以.txt , .log结尾的文件
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-29 12:00:53
 */
public class LogFileFilter implements FilenameFilter{

    public boolean accept(File dir, String name) {
        return name.endsWith(".txt") || name.endsWith(".log");
    }
}
