package com.gionee.autotest.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 特定文件的过滤器
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-2 11:10:00
 */
public class ResultLogFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.contains(Constant.RESULT_HTML_FILE)
                || name.contains(Constant.FAIL_CASE_FILE)
                || name.contains(Constant.RESULT_LOG_FILE);
    }
}
