package com.gionee.autotest.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;

public final class ZipTools {
  
    private int bufferLen = 1024 * 1024;  
  
    /** 
     * 私有化构造方法，prevents calls from subclass 
     */  
    private ZipTools() {  
        throw new UnsupportedOperationException();  
    }  
  
    private ZipTools(int bufferLen) {  
        this.bufferLen = bufferLen;  
    }  
  
    public static ZipTools getInstance() {  
        return new ZipTools(1024 * 1024);  
    }  
  
    public static ZipTools getInstance(int bufferLen) {  
        return new ZipTools(bufferLen);  
    }  

    /**
     * 用于单文件压缩
     */
    protected void doCompress(File srcFile, File destFile) throws IOException {
        ZipArchiveOutputStream out = null;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(srcFile), bufferLen);
            out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), bufferLen));
            ZipArchiveEntry entry = new ZipArchiveEntry(srcFile.getName());
            entry.setSize(srcFile.length());
            out.putArchiveEntry(entry);
            IOUtils.copy(is, out);
            out.closeArchiveEntry();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 把一个目录打包到一个指定的zip文件中
     * @param out           目录绝对地址
     * @param pathName       zip文件内相对结构
     * @throws IOException
     */
    private void packFiles(ZipArchiveOutputStream out, File dir, String pathName) throws IOException {
        InputStream is = null;
        //返回此绝对路径下的文件
        File[] files = dir.listFiles();
        if (files == null || files.length < 1) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File zFile = files[i];
            out.putArchiveEntry(new ZipArchiveEntry(zFile, pathName + zFile.getName()));
            //判断此文件是否是一个文件夹
            if (zFile.isDirectory()) {
                packFiles(out, zFile, pathName + zFile.getName() + "/");
            } else {
                try {
                    //out.putArchiveEntry(new ZipArchiveEntry(pathName + files[i].getName()));
                    is = new BufferedInputStream(new FileInputStream(zFile), bufferLen);
                    IOUtils.copy(is, out);
                } finally {
                    is.close();
                }
            }
        }
    }
  
    /** 
     * 压缩文件或者文件夹 
     * @param srcFileStr 待压缩文件或文件夹路径 
     * @param destFileStr 目标文件路径 
     * @throws IOException 
     */  
    public void zip(String srcFileStr, String destFileStr) throws IOException {  
        File destFile = new File(destFileStr);  
        File srcFile = new File(srcFileStr);  
        zip(srcFile, destFile);  
    }  
  
    /** 
     * 压缩文件 
     * @param srcFile 待压缩文件或文件夹 
     * @param destFile 目标压缩文件
     * @throws IOException 
     */  
    public void zip(File srcFile, File destFile) throws IOException {  
        ZipArchiveOutputStream out = null;  
  
        // 如果压缩文件存放目录不存在,则创建之.  
        File pfile = destFile.getParentFile();  
        if (!pfile.exists()) {  
            pfile.mkdirs();  
        }  
        //如果是文件  
        if (srcFile.isFile()) {  
            doCompress(srcFile, destFile);  
            return;  
        } else {  
            try {  
                out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), bufferLen));  
                packFiles(out, srcFile, "");  
                out.closeArchiveEntry();  
            } finally {  
                IOUtils.closeQuietly(out);  
            }  
        }  
    }  

} 