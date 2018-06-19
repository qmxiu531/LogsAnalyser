package com.gionee.autotest.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @Author Viking Den
 * @Version 1.0
 * @Email dengwj@gionee.com
 * @Time 14:27
 */
public class FailPhotoFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return (name.endsWith(".png") || name.endsWith(".jpg"))
//                && name.startsWith("Case_")
                && !name.contains("NA") ;
    }
}
