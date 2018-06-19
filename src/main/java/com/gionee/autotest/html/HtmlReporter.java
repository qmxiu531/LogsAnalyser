package com.gionee.autotest.html;

import com.gionee.autotest.model.*;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.Constant;
import com.gionee.autotest.util.Log;
import com.gionee.autotest.util.ZipTools;

import java.io.*;
import java.util.List;

/**
 * 用于生成HTML报告
 */
public class HtmlReporter {

    private static final String TAG = HtmlReporter.class.getSimpleName() ;

//    public static final String MAIL_TITLE = "%s " ;

    public static final String FAIL_CASE_ZIP_FAILE = "CaseFailDetails_%s.zip" ;

    public static final String FAIL_CASE_HTML_TITLE = "%s CASE FAIL清单" ;

    private static File outDir ;

    public static void createHtml(ResultModel result){
        Configuration configuration = ConfigurationInjector.configuration() ;
        String name = configuration.getProject() + "_" + configuration.getJobName() + "_" +configuration.getBuildId() ;
        outDir  = configuration.getOutDir() ;
        File resultHtml = new File(outDir + File.separator + (name + "_" +Constant.RESULT_HTML_FILE)) ;
//        File failCaseHtml = new File(outDir + File.separator + "result" + File.separator + (name + "_" + Constant.FAIL_CASE_FILE)) ;
        File failCaseHtml = new File(outDir + File.separator + "result" + File.separator + configuration.getProject()
                +"_" + configuration.getMobileTag() + "_" +configuration.getBuildId() + "_CaseFailDetails.html") ;
        File logResultTxt = new File(outDir + File.separator + (name + "_" + Constant.RESULT_LOG_FILE)) ;
        //创建总的html文件
        createSummaryHtml(resultHtml , configuration.getSubject() , configuration.getProject() , configuration.getJobName() , configuration.getBuildId(), result);

        if (result != null  && result.getCaseInfo() != null
                && result.getCaseInfo().getFailedCases() != null
                && result.getCaseInfo().getFailedCases().size() > 0){
            //创建失败Fail的清单html文件
            createFailCaseHtml(failCaseHtml , configuration.getProject() , result) ;
            //打包文件
            try {
                ZipTools zipTool = ZipTools.getInstance() ;
                File distFile = new File(outDir + File.separator + String.format(FAIL_CASE_ZIP_FAILE ,
                        configuration.getProject() + "_" + configuration.getMobileTag() +"_" + configuration.getBuildId())) ;
                //如果存在，先删除
                if (distFile.exists()){
                    distFile.delete() ;
                }
                zipTool.zip(new File(outDir + File.separator + "result") , distFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //创建失败的Fail结果文件
        createFailLogResultTxt(logResultTxt , result);
    }

    private static void createFailLogResultTxt(File logResultTxt, ResultModel result) {
        try{
            File file = logResultTxt ;
            if(file.exists()){
                Log.i(TAG , "文件存在，先删除 " );
                file.delete() ;
            }
            if(result != null){
                FileWriter fw = new FileWriter(file);
                List<AppCrashModel> appCrashs = result.getAppCrashModel() ;
                List<AppANRModel> appANRS = result.getAppANRS();
                List<DeviceDieOffModel> deviceDieOffs = result.getDeviceDieOffModel() ;
                CaseInfo caseInfos = result.getCaseInfo() ;

                //先输出应用报错的信息
                if(appCrashs != null && appCrashs.size() > 0){
                    Log.i(TAG , "输出应用报错的信息到文件");
                    for(BaseModel model :  appCrashs){
                    	AppCrashModel crash=(AppCrashModel)model;
                        fw.append("Name : " + crash.getName() + Constant.NEW_LINE) ;
                        fw.append("Error : " + crash.getError()+ Constant.NEW_LINE) ;
                        fw.append("time : " + crash.getTime()+ Constant.NEW_LINE) ;//lsq
                        fw.append("Version : " + crash.getVersion()+ Constant.NEW_LINE) ;
                        fw.append("Description : " + crash.getDescription()+ Constant.NEW_LINE) ;
                        fw.append("Count : " + crash.getCount()+ Constant.NEW_LINE) ;
                        fw.append("Error Message : " + crash.getShortMsg() + ":"+crash.getLongMsg()+ Constant.NEW_LINE) ;
                        fw.append("Log : \n" + crash.getLog()+ Constant.NEW_LINE) ;
                        fw.append(Constant.NEW_LINE);
                    }
                }
                //输出应用程序无响应异常
                if(appANRS != null && appANRS.size() > 0){
                    Log.i(TAG , "输出应用无响应的信息到文件");
                    for(BaseModel model :  appANRS){
                    	AppANRModel crash=(AppANRModel)model;
                        fw.append("Name : " + crash.getName() + Constant.NEW_LINE) ;
                        fw.append("Error : " + crash.getError()+ Constant.NEW_LINE) ;
                        fw.append("time : " + crash.getTime()+ Constant.NEW_LINE) ;//lsq
                        fw.append("Version : " + crash.getVersion()+ Constant.NEW_LINE) ;
                        fw.append("Description : " + crash.getDescription()+ Constant.NEW_LINE) ;
                        fw.append("Count : " + crash.getCount()+ Constant.NEW_LINE) ;
                        fw.append("Error Message : " + crash.getShortMsg() + ":"+crash.getLongMsg()+ Constant.NEW_LINE) ;
                        fw.append("Log : \n" + crash.getLog()+ Constant.NEW_LINE) ;
                        fw.append(Constant.NEW_LINE);
                    }
                }

                //输出死机重启的出错信息
                if(deviceDieOffs != null && deviceDieOffs.size() > 0){
                    Log.i(TAG , "输出死机重启的出错信息到文件");
                    for(DeviceDieOffModel deviceDie : deviceDieOffs){
                        fw.append("Name : " + deviceDie.getName() + Constant.NEW_LINE) ;
                        fw.append("Error : " + deviceDie.getError() + Constant.NEW_LINE) ;
                        fw.append("time : " + deviceDie.getDbtime() + Constant.NEW_LINE) ;//lsq
                        fw.append("Description : " + deviceDie.getDescription() + Constant.NEW_LINE) ;
                        fw.append("Count : " + deviceDie.getCount() + Constant.NEW_LINE) ;
                        fw.append("Log : \n" + deviceDie.getLog() + Constant.NEW_LINE) ;
                        fw.append(Constant.NEW_LINE);
                    }
                }

                //输出Case Fail的错误信息
                if(caseInfos != null ){
                    List<CaseModel> caseModels = caseInfos.getFailedCases() ;
                    if(caseModels != null && caseModels.size() > 0){
                        Log.i(TAG , "输出Case Fail的log到文件");
                        for(CaseModel caseModel : caseModels){
                            fw.append("Name : " + caseModel.getName() + Constant.NEW_LINE) ;
                            fw.append("STM ID : " + caseModel.getStmCaseId()+ Constant.NEW_LINE) ;
                            fw.append("Count : " + caseModel.getCount() + Constant.NEW_LINE) ;
                            fw.append("Error Message : " + caseModel.getShortMsg() + Constant.NEW_LINE) ;
                            fw.append("Procedure : " + caseModel.getProcedure()+ Constant.NEW_LINE) ;
                            fw.append("Case Error Log : \n" ) ;
                            parseLogFilesToFile(fw , caseModel.getName());
                            fw.append("\nCase Log : \n" + caseModel.getLog()+ Constant.NEW_LINE) ;
                            fw.append(Constant.NEW_LINE);
                        }
                    }
                }

                fw.flush();
                fw.close();
            }
        }catch (Exception e){
            Log.i(TAG , "结果写入到文件中失败 . " +e.getMessage());
            e.printStackTrace();
        }
    }

    private static void parseLogFilesToFile(FileWriter fw , String filename){
        for(File file : ConfigurationInjector.configuration().getOutDir().listFiles()){
            if(file.getName().contains(filename)){
                Log.i(TAG, "Contain file  : " + file.getPath());
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file), "utf-8"), Constant.BUFFER_SIZE);
                    while (reader.ready()) {
                        String line = reader.readLine();
                        fw.append( line +"\n") ;
                    }
                    if(reader != null){
                        reader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createFailCaseHtml(File failCaseHtml , String ota_name , ResultModel result) {
        StringBuilder content = new StringBuilder() ;
        CreateHtmlFactory factory = new CreateHtmlFactory(result) ;
        try {
            //如果文件存在 先删除
            if(failCaseHtml.exists())
                failCaseHtml.delete() ;
            failCaseHtml.createNewFile() ;

            //添加Html的样式文本
            content.append(HtmlCommon.getReportStyle(String.format(FAIL_CASE_HTML_TITLE, ota_name))) ;

            content.append(factory.getFailCaseContentHtml()) ;

            //添加HTML结束的标签文本
            content.append(HtmlCommon.getEndHtmlTag()) ;

            FileOutputStream writerStream = new FileOutputStream(failCaseHtml);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            writer.write(content.toString());
            writer.close();
            writerStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("生成Fail Case 结果HTML失败！！！！");
        }
    }

    private static void createSummaryHtml(File resultHtml , String subject , String ota_name , String job , String buildId ,ResultModel result) {
        StringBuilder content = new StringBuilder() ;
        CreateHtmlFactory factory = new CreateHtmlFactory(result) ;
        try {
            //如果文件存在 先删除
            if(resultHtml.exists())
                resultHtml.delete() ;
            resultHtml.createNewFile() ;

            //添加Html的样式文本
            content.append(HtmlCommon.getReportStyle(subject)) ;

            //添加测试结论
            content.append(factory.getLogAnalysisResult(ota_name+"_" + job + "_" + buildId + "_logReport.html")) ;

            //添加测试详情
            content.append(factory.getLogAnalysisSummary()) ;

            //添加测试结果信息
            content.append(factory.getAnalysisResult()) ;

            //添加备份Log下载地址
            content.append(factory.getBackUpLogAddress()) ;

            //添加测试结果的路径
//            content.append(factory.getReportUrlText()) ;

            //添加HTML结束的标签文本
            content.append(HtmlCommon.getEndHtmlTag()) ;
            FileOutputStream writerStream = new FileOutputStream(resultHtml);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            writer.write(content.toString());
            writer.close();
            writerStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("生成结果HTML失败！！！！");
        }
    }

    public static void generateExcludeNAHtml(List<NAModel> fNaModels) {
        try{
            Configuration config = ConfigurationInjector.configuration() ;
            File work_dir = config.getWorkDir() ;
            File na_ExcludeFile = new File(work_dir , "na_exception.html") ;
            if (na_ExcludeFile.exists() && na_ExcludeFile.isFile()){
                Log.i("删除na_exception.html");
                na_ExcludeFile.delete() ;
            }
            if (fNaModels != null && fNaModels.size() > 0){
                Log.i("有NA异常的信息，生成na_exception.html文件");
                na_ExcludeFile.createNewFile() ;

                StringBuilder content = new StringBuilder() ;
                //添加Html的样式文本
                content.append(HtmlCommon.getReportStyle(config.getSubject())) ;

                //添加测试结论
                content.append("<h2>测试结论:</h2>");
                content.append("<table width='95%'><tr><td><H3><FONT color=red>&nbsp;&nbsp;&nbsp;&nbsp;"
                        + HtmlCommon.RESULT_FAIL
                        + "</FONT></H3></td></tr></table>");

                //添加测试详情
                content.append("<h2>测试详情:</h2>");
                content.append("<table width='95%'><tr><td><H3>&nbsp;&nbsp;&nbsp;&nbsp;"
                        + "添加测试环境资源过滤后，仍有以下Case出现NA"
                        + "</H3></td></tr></table>");

                content.append("<TABLE class=details cellSpacing=2 cellPadding=5 width=\"95%\" border=0>\n");
                content.append("<TBODY>\n");
                content.append("<TR vAlign=top>\n");
                content.append("<TD class=\"cell P20\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>序号</STRONG></TD>\n");
                content.append("<TD class=\"cell P35\"' style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>原因类型</STRONG></TD>\n");
                content.append("<TD class=\"cell P45\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>Case列表</STRONG></TD>\n");
                content.append("</TR>\n");
                int count = 1 ;
                for(NAModel na : fNaModels){
                    content.append("<TR valign=\"top\" style=\"BACKGROUND: #bfbfbf; FONT-WEIGHT: bold; COLOR: green\">\n");
                    content.append("<TD >"+count+"</TD>\n");
                    content.append("<TD >"+na.getReason()+"</TD>\n");
                    content.append("<TD >"+na.getCaseName().toString()+"</TD>\n");
                    content.append("</TR>\n");
                    count ++ ;
                }
                content.append("</TBODY></TABLE>\n");

                //添加HTML结束的标签文本
                content.append(HtmlCommon.getEndHtmlTag()) ;
                FileOutputStream writerStream = new FileOutputStream(na_ExcludeFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
                writer.write(content.toString());
                writer.close();
                writerStream.close();
            }
        }catch (Exception e){
            Log.i("生成剔除的NA html文件失败");
            e.printStackTrace();
        }
    }

    /**
     * 获取邮件的标题
     * @param ota_name
     * @return
     */
/*    private static String getMailTitle(String ota_name){
        return String.format(MAIL_TITLE , ota_name ) ;
    }*/

}
