package com.gionee.autotest.runner;

import com.gionee.autotest.util.Constant;

import java.io.File;

/**
 * 保存参数信息类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-2 10:23:38
 */
public class Configuration {

    private final File[] files ;

    private final int type ;

    private final File out_directory ;

    private final File work_dir ;

    private final String serverUrl ;

    private final String project ;

    private final String buildId ;

    private final String jobName ;

    private final String mobileTag ;

    private final String rom ;

    private final String subject ;

    private final String tag ;

    private final String testType ;

    private final String reportUrl ;

    private final int errorTimes ;

    public Configuration(File[] files, int type, File out_directory, File work_dir ,
                         String serverUrl , String project , String buildId , String jobName ,
                         String mobileTag, String rom ,
                         String subject ,String tag ,String testType , String reportUrl , int errorTimes) {
        this.files = files;
        this.type = type;
        this.out_directory = out_directory;
        this.work_dir = work_dir ;
        this.serverUrl = serverUrl ;
        this.project = project ;
        this.buildId = buildId ;
        this.mobileTag = mobileTag ;
        this.rom = rom ;
        this.jobName = jobName ;
        this.subject = subject ;
        this.tag = tag;
        this.testType = testType ;
        this.reportUrl = reportUrl ;
        this.errorTimes = errorTimes ;
    }

    public String getRom() {
        return rom;
    }

    public String getSubject() {
        if (subject == null){
            return "Unknown Subject" ;
        }
        return subject;
    }

    public String getJobName() {
        if (jobName == null){
            return "Unknown Job" ;
        }
        return jobName;
    }

    public String getBuildId() {
        if (buildId == null){
            return "8888" ;
        }
        return buildId;
    }

    public String getProject() {
        if (project == null){
            return "Unknown Project" ;
        }
        return project;
    }

    public String getServerUrl() {
        //TODO 适配之前的版本,全部修改后可能删除
        if (serverUrl != null){
            String[] args = serverUrl.split("/") ;
            String ota = args[0] ;
            String mobileT = args[1] ;
            return Constant.MAIN_SERVER_URL + ota +"/" + mobileT + "/";
        }
        return Constant.MAIN_SERVER_URL + getTestType() +"/" + mobileTag + "/";
    }

    public String getMobileTag() {
        return mobileTag;
    }

    public File[] getLogFiles() {
        return files;
    }

    public File getOutDir() {
        return out_directory;
    }

    public String getTag() {
        if (tag == null || tag.equals("")){
            return Constant.CASE_TAG ;
        }
        return tag;
    }

    public String getTestType(){
        if(testType == null)
            return "ota" ;
        return testType ;
    }

    public File getWorkDir() {
        return work_dir;
    }

    public int getType() {
        return type;
    }

    public String getReportUrl(){
        /*if (reportUrl == null){
            return "Unknown Report Url" ;
        }*/
        return reportUrl ;
    }

    public int getErrorTimes(){
        return errorTimes ;
    }
}
