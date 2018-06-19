package com.gionee.autotest.process;

import com.gionee.autotest.model.AppANRModel;
import com.gionee.autotest.model.AppCrashModel;
import com.gionee.autotest.model.AppModel;
import com.gionee.autotest.model.BaseModel;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.Constant;
import com.gionee.autotest.util.Exceptions;
import com.gionee.autotest.util.Log;
import com.gionee.autotest.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

/**
 * 用于处理AppCrash的Log
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-29 16:35:34
 */
public class AppCrashLogProcess implements Process{

    private static final String DZ_SOFT_PKG = "com.dzsoft.smartrobot.uiauto.daemon.service" ;

    private static final String TAG = AppCrashLogProcess.class.getSimpleName() ;

    /**
     * 全局的失败Case
     */
    List<BaseModel> crashExceptions ;

    /**
     * 所有应用的信息
     */
    List<AppModel> allAppsInfo;

    public Object analysis(Configuration configuration) {
        Log.i(TAG , "进入解析处理逻辑");
        try {
            crashExceptions = new ArrayList<BaseModel>();
            allAppsInfo = Util.parseAllApps(configuration.getWorkDir()) ;
            File[] files = configuration.getLogFiles();
            BufferedReader in = null;
            BufferedInputStream fis = null ;
            for (File file : files) {
                Log.i(TAG, "当前解析的文件名:" + file.getName());
                fis = new BufferedInputStream(new FileInputStream(file));
                in = new BufferedReader(new InputStreamReader(fis,"utf-8"), Constant.BUFFER_SIZE);
                //处理Crash的变量集合
                boolean isCutting = false ;
                boolean isProcessHandle = false ;
                StringBuilder sb = null ;
                String pkg = null ;
                String shortMsg = null ;
                String longMsg = null ;
                String time=null;//增加首次出现时间 lsq
                //处理ANR的变量集合
//                boolean isACutting = false ;
//                boolean isHandleOver = false ;
//                StringBuilder aSb = null ;
//                String aPkg = null ;
//                String aTime=null;
//                String aShortMsg = "N/A" ;
//                String aLongMsg = "N/A" ;
                String line  ;
                while((line = in.readLine()) != null){
                    //先处理Crash，判断当前行是否包括: FATAL EXCEPTION: ，如果包括则是Crash开始点，初始化
                    //变量
                    if(line.contains(Constant.CRASH_ANDROID_RUNTIME)
                        && line.contains(Constant.CRASH_FATAL_EXCEPTION)){
                        isCutting = true ;
                        isProcessHandle = false ;
                        pkg = null ;
                        shortMsg = "N/A" ;
                        longMsg = "N/A" ;
                        sb = new StringBuilder() ;
                    }
//                    //再判断是否是ANR，如果包含: Application is not responding:，则是ANR开始点，初始化变量
//                    else if (line.contains(Constant.ANR_MANAGER_TAG) && line.contains(Constant.ANR_PACKAGE_TAG)){
//                        Log.i(TAG , "ANR Info :"+ line);
//                        isACutting = true ;
//                        aSb = new StringBuilder() ;
//                        isHandleOver = false ;
//                        aPkg = null ;
//                        aShortMsg = null ;
//                        aLongMsg = null ;
//                        aCount = 0 ;
//                    }

                    //先处理Crash，如果当前是截取状态
                    if(isCutting && line.contains(Constant.CRASH_ANDROID_RUNTIME)){
                        sb.append(splitText(line)+ Constant.NEW_LINE) ;
                        //获取包名信息
                        if(line.contains(Constant.CRASH_PROCESS)){
                        	String[] times = line.split(" ");
                            String[] args = line.split(Constant.CRASH_PROCESS) ;
                            if(args != null && args.length > 1){
                                pkg = args[1].split(",")[0].trim() ;
                                time=times[0]+" "+times[1];
                            }
                            isProcessHandle = true ;
                        }
                        //获取出错的类型信息
                        else if(isProcessHandle){
                            String[] args = line.split(Constant.CRASH_ANDROID_RUNTIME)[1].split(":") ;
                            if(args != null && args.length > 2){
                                shortMsg = args[1].trim() ;
                                longMsg = args[2].trim() ;
                            }
                            isProcessHandle = false ;
                        }
                    }
                    //截取完毕 添加到集合中，有去重功能
                    else if (isCutting && !line.contains(Constant.CRASH_ANDROID_RUNTIME)){
                        isCutting = false ;
                        //添加到集合中
                        AppCrashModel appCrashModel = new AppCrashModel() ;
                        if (!pkg.equals(DZ_SOFT_PKG) || !pkg.contains("com.gionee.demo")){
                            String[] pkgAndVersion = getFormatPkgAndVersion(pkg , allAppsInfo) ;
                            appCrashModel.setName(pkgAndVersion[0]);
                            appCrashModel.setVersion(pkgAndVersion[1]);
                            appCrashModel.setTime(time);
                            String[] formatErrorInfo = getFormatErrorInfo(shortMsg ) ;
                            appCrashModel.setError(formatErrorInfo[0]);
                            appCrashModel.setDescription(formatErrorInfo[1]);
                            appCrashModel.setCount(1);
                            appCrashModel.setLog(sb.toString());
                            appCrashModel.setLongMsg(longMsg);
                            addExceptionOrSetCount(appCrashModel);
                        }
                    }
//                    //判断是否是ANR截取
//                    if(isACutting ){
////                        aCount ++ ;
//                        if (line.contains(Constant.ANR_MANAGER_TAG)){
////                            Log.i(TAG , "Split text : " + splitText(line));
//                            aSb.append(splitText(line) + Constant.NEW_LINE) ;
//                        }
////                        03-01 16:19:28.818 E/ANRManager(  874): ANR in com.viking.test (com.viking.test/.MainActivity), time=504603143
////                        03-01 16:19:28.818 E/ANRManager(  874): Reason: Input dispatching timed out (Waiting to send non-key event because the touched window has not finished processing certain input events that were delivered to it over 500.0ms ago.  Wait queue length: 42.  Wait queue head age: 17028.4ms.)
////                        03-01 16:19:28.818 E/ANRManager(  874): Load: 16.6 / 15.89 / 15.79
////                        03-01 16:19:28.818 E/ANRManager(  874): Android time :[2016-03-01 16:19:28.75] [504628.781]
////                        03-01 16:19:28.818 E/ANRManager(  874): CPU usage from 5483ms to 0ms ago:
//                        //ANR处理包名信息
//                        if(line.contains(Constant.ANR_MANAGER_TAG) && line.contains(Constant.ANR_PACKAGE_TAG)){
//
//                            if (line.contains("),")){
//                                //03-01 16:19:28.818 E/ANRManager(  874): ANR in com.viking.test (com.viking.test/.MainActivity), time=504603143
//                                String[] args = line.split(Constant.ANR_PACKAGE_TAG)[1].split(",")[0].split("\\(") ;
//                                String[] times = line.split(" ");
//                                if(args != null && args.length > 1){
//                                    aPkg = args[0].trim() ;
//                                    aTime=times[0]+" "+times[1];
//                                }
//                            }else{
//                                //03-01 16:19:28.818 E/ANRManager(  874): ANR in com.viking.test, time=504603143
//                                String[] args = line.split(Constant.ANR_PACKAGE_TAG)[1].split(",") ;
//                                if(args != null && args.length > 1){
//                                    aPkg = args[0].trim() ;
//                                }
//                            }
//
//                        }
//                        //ANR出现的原因信息
//                        else if (line.contains(Constant.ANR_MANAGER_TAG) && line.contains(Constant.ANR_REASON_TAG)){
//                            isHandleOver = true ;
//                            String[] args = line.split(Constant.ANR_REASON_TAG)[1].split("\\(") ;
//                            if(args != null && args.length > 1){
//                                aShortMsg = args[0] ;
//                                aLongMsg = args[1] ;
//                            }
//                        }
//                        //停止截取，以及添加到集合列表中
//                        else if(!line.equals("") && isHandleOver && !line.contains(Constant.ANR_MANAGER_TAG)){
//                            isACutting = false ;
//                            //保存
//                            if (!aPkg.contains("com.gionee.demo")){
//                                AppANRModel appANRModel = new AppANRModel() ;
//                                String[] pkgAndVersion = getFormatPkgAndVersion(aPkg , allAppsInfo) ;
//                                appANRModel.setName(pkgAndVersion[0]);
//                                appANRModel.setVersion(pkgAndVersion[1]);
//                                appANRModel.setTime(aTime);
//                                appANRModel.setError(aShortMsg);
//                                appANRModel.setDescription(Constant.APPLICATION_NOT_RESPOND);
//                                appANRModel.setCount(1);
//                                appANRModel.setLog(aSb.toString());
//                                appANRModel.setLongMsg(aLongMsg);
//                                addExceptionOrSetCount(appANRModel);
//                            }
//                        }
//                    }
                }
            }
            if(in != null){
                in.close();
            }
            if (fis != null){
                fis.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return  crashExceptions ;
    }

    private String splitText(String line) {
        if(line.contains("):")){
            return line.split("\\):")[1].trim() ;
        }
        return line ;
    }

    private void addExceptionOrSetCount(BaseModel appCrashModel){
        if(crashExceptions.contains(appCrashModel)){
            for(BaseModel model : crashExceptions){
                if(model.equals(appCrashModel)){
                    //计数加一
                    model.setCount(model.getCount() + 1);
                }
            }
        }else{
            crashExceptions.add(appCrashModel) ;
        }
    }

    public String[] getFormatErrorInfo(String shortMsg){
        String error = null ;
        String description = null ;
        for(Exceptions exception : Exceptions.values()){
            if(exception.getName().equals(shortMsg)){
                error = exception.getName() ;
                description = exception.getDescription() ;
                break ;
            }
        }
        if(error == null){
            error = shortMsg ;
            description = "*" ;
        }
        return new String[]{error , description} ;
    }

    public String[] getFormatPkgAndVersion(String pkg , List<AppModel> allAppsInfo){
        String format_pkg = null ;
        String format_version = null ;
        if (allAppsInfo != null && allAppsInfo.size() > 0){
            for(AppModel appModel : allAppsInfo){
                if(appModel.getPkg().equals(pkg)){
                    format_pkg = pkg + "["+appModel.getAppName()+"]" ;
                    format_version = appModel.getVersion() ;
                    break ;
                }
            }
        }
        if(format_pkg == null){
            format_pkg = pkg + "[*]" ;
            format_version = "[*]" ;
        }
        return new String[]{format_pkg ,format_version} ;
    }
}
