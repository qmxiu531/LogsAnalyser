package com.gionee.autotest.util;

import com.gionee.autotest.model.AppModel;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

/**
 * 项目的辅助类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-29 11:54:56
 */
public class Util {

    private static final String TAG = Util.class.getSimpleName() ;

    private static final String ALL_APP_INFO_FILE = "AppInfo.txt" ;

    public static void writeProperties(String key, String value){
        Log.i(TAG ,"写入死机重启的次数到文件中");
        Configuration configuration = ConfigurationInjector.configuration() ;
        File workSpace = configuration.getWorkDir() ;
        if (workSpace != null && workSpace.exists() && workSpace.isDirectory()){
            try{
                Log.i(TAG ,"工作区存在，初始化写数据");
                Properties prop = new Properties();// 属性集合对象
                File pFile = new File(workSpace, "auto.properties") ;
                if (!pFile.exists()) {
                    pFile.createNewFile() ;
                }
                FileInputStream fis = new FileInputStream(pFile);// 属性文件输入流
                prop.load(fis);// 将属性文件流装载到Properties对象中

                // 文件输出流
                FileOutputStream fos = new FileOutputStream(pFile);
                // 将Properties集合保存到流中
                prop.setProperty(key,value);
                prop.store(fos, "Copyright (c) Gionee Inc");
                fos.flush();
                fos.close();// 关闭流
                fis.close();// 关闭流
                Log.i(TAG , "写入特性成功");
            }catch (Exception e){
                Log.i(TAG , "写入特性失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加Log文件
     * @param fileOrDirectory
     * @return
     */
    public static List<File> parseFiles(File fileOrDirectory){
        List<File> files = new ArrayList<File>() ;
        if(fileOrDirectory.isFile()){
            files.add(fileOrDirectory) ;
        }else if (fileOrDirectory.isDirectory()){
            File[] files_ = listFiles(fileOrDirectory , new LogFileFilter()) ;
            for(File file : files_){
                files.add(file) ;
            }
        }
        return files ;
    }

    public static File getAllAppInfo(File dir){
        File[] files = listFiles(dir, new FilenameFilter() {
            public boolean accept(File dir, String name) {
//                return name.equals(ALL_APP_INFO_FILE);
                return new File(dir , name).isDirectory()
                        || (new File(dir , name).isFile() && name.equals(ALL_APP_INFO_FILE)) ;
            }
        }) ;
        if (files != null && files.length > 0){
            for (File file : files ){
                return file ;
            }
        }
        return null ;
    }


    private static void innerListFiles(Collection<File> files, File directory , FilenameFilter filenameFilter){
//        FileUtils.listFiles()
        File[] found = directory.listFiles(filenameFilter);
        if (found != null) {
//            System.out.println("found is not null");
            for (File file : found) {
//                File file = new File(file_name) ;
//                System.out.println("found file : " + file.getPath());
                if (file.isDirectory()){
                    if ( !file.getName().contains("db_dir")
                            && !file.getName().contains("log_analysis")) {
                        innerListFiles(files, file, filenameFilter);
                    }
                } else {
                    files.add(file);
                }
            }
        }
    }

    /**
      *添加处理特定目录的下所有的文件
      *@param
      *@author Viking Den
      *@version 1.0
      *@return
      *@date 2016-03-07 14:22:49
      */
    public static File[] listFiles(File dir , FilenameFilter filenameFilter){
        if (!dir.isDirectory()){
            return null ;
        }
        Collection<File> files = new java.util.LinkedList<File>();
        innerListFiles(files , dir , filenameFilter);
        if (files.isEmpty()){
            return null ;
        }
        File[] finFiles = new File[files.size()] ;
        int count = 0 ;
        for (File file : files){
            Log.i(TAG , "found file :" +file.getPath());
            finFiles[count] = file ;
            count++ ;
        }
        return finFiles ;
    }


    /**
     * 根据Dump出来的所有应用信息，解析
     * @param file
     * @return
     */
    public static List<AppModel> parseAllApps(File file){
        List<AppModel> apps = new ArrayList<AppModel>() ;
        try {
            File allAppInfo = getAllAppInfo(file) ;
            if(allAppInfo == null) return null ;
            Log.i("All apps info file path : " + allAppInfo.getPath());
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(allAppInfo), "utf-8"));
            while(in.ready()){
                String line = in.readLine() ;
                if(line != null && line.contains("baseInfo")){
                    break ;
                }
                //"com.android.contacts"|5.3.2.ae(联系人)&2
                if(line != null && line.contains("|")){
                    try{
                        String pkg = line.split("\"")[1] ;
                        String version = line.split("\"")[2].split("\\(")[0].replace("|" ,"") ;
                        String appName = line.split("\\(")[1].split("\\)")[0] ;
                        AppModel model = new AppModel(appName , pkg , version) ;
                        if (!apps.contains(model)){
                            apps.add(model) ;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        //catch this ;
                        Log.i("Exception Split :" + e.getMessage());
                    }
                }
            }
            if(in != null){
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    /*    if (apps != null && apps.size() > 0){
            for (AppModel appModel : apps){

                Log.i(TAG , "pkg : " + appModel.getPkg() + " , name : " + appModel.getAppName() + " , version : " + appModel.getVersion());
            }
        }*/

        return apps ;
    }
}
