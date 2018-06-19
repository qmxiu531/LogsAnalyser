package com.gionee.autotest;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.gionee.autotest.runner.AnalysisLog;
import com.gionee.autotest.util.Log;

import java.io.File;

/**
 * Log处理的主函数入口，定义对外的开放接口
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-27 15:54:17
 */
public class LogsAnalyser {

    private static final String TAG = LogsAnalyser.class.getSimpleName() ;

    public static void main(String[] args){
        CommandLineArgs parsedArgs = new CommandLineArgs();
        JCommander jc = new JCommander(parsedArgs);
        try {
            jc.parse(args);
        } catch (ParameterException e) {
            Log.i("解析命令参数错误!");
            StringBuilder out = new StringBuilder(e.getLocalizedMessage()).append("\n\n");
            jc.usage(out);
            System.err.println(out.toString());
            Log.i("错误 " + out.toString());
            System.exit(1);
            return;
        }

        if (parsedArgs.help) {
            Log.i("输出命令参数帮助信息");
            jc.usage();
            return ;
        }

//        Log.i(TAG , "版本信息：添加写入死机重启的次数到工作区的数据文件auto.properties中");
//        Log.i(TAG , "版本信息：2016年5月28日09:42:37 更新当case失败数为0时，未执行数不为0不显示的bug ");
//        Log.i(TAG , "版本信息：2016年5月31日14:59:18 更新邮件中部分词语 ");
//        Log.i(TAG , "版本信息：2016年6月1日11:03:04 添加预置条件中排除的NA Case添加到NA结果中 ");
//        Log.i(TAG , "版本信息：2016年6月7日11:39:57 添加已过滤环境Case，仍然NA的邮件发送 ");
//        Log.i(TAG , "版本信息：2016年6月16日15:08:06 报告中更新添加查看处理按钮 ");
//        Log.i(TAG , "版本信息：2016年6月27日10:15:06 添加有死机重启数据时，生成dieOffAndReboot.txt文件 ");
//        Log.i(TAG , "版本信息：2016年6月27日17:35:22 添加Case失败详情链接的处理以及增加新的rom参数 ");
//        Log.i(TAG , "版本信息：2016年6月28日09:18:26 应用持续报错处也添加dieOffAndReboot.txt文件的生成 ");
//        Log.i(TAG , "版本信息：2016年7月1日10:34:04 报告中添加MTK log下载地址 ");
//        Log.i(TAG , "版本信息：2016年7月5日10:20:26 修改Case Fail html文件的中文为英文，解决在FTP链接中不能显示的问题 ");
//        Log.i(TAG , "版本信息：2016年7月5日14:25:25 更改CaseFailDetail的下载路径为html形式 ");
//        Log.i(TAG , "版本信息：2016年7月5日14:25:25 添加写入Crash的次数到auto.properties文件中 ");
//        Log.i(TAG , "版本信息：2016年7月13日09:43:03 删除测试任务的链接地址 ");
//        Log.i(TAG , "版本信息：2016年8月5日14:11:32 添加shouldInnerMail.txt文件控制Case总数为0时内发 ");
//        Log.i(TAG , "版本信息：2016年8月17日15:13:01 添加死机重启的时间和路径到dieOffAndReboot.txt文件中 ");
//        Log.i(TAG , "版本信息：2016年9月12日15:35:43 修改Case Fails的FTP地址 ");
//        Log.i(TAG , "版本信息：2016年9月12日15:35:43 修改Case Fails的FTP地址Bug");
//        Log.i(TAG , "版本信息：2016年10月8日16:58:04 修改失败的报告只保存最后一张的截图");
//        Log.i(TAG , "版本信息：2017年1月6日16:22:46 修改默认的Log tag");
//        Log.i(TAG , "版本信息：2017-1-10 11:44:31 修改只跑一次错误的适配");
//        Log.i(TAG , "版本信息：2017-3-30 17:11:32 修改FTP地址的适配");
        Log.i(TAG , "版本信息：2017-4-11 17:19:33 修改错误Case详情中无报错信息");

        AnalysisLog analysisLog = new AnalysisLog.Builder()
                .withLogFile(parsedArgs.log_file)
                .withLogDirectory(parsedArgs.log_directory)
                .withType(parsedArgs.type)
                .withOutDirectory(parsedArgs.out_directory)
                .withWorkDir(parsedArgs.work_dir)
                .withServerUrl(parsedArgs.server_url)
                .withProject(parsedArgs.project)
                .withBuildID(parsedArgs.buildid)
                .withJobName(parsedArgs.jobName)
                .withMobileTag(parsedArgs.mobile_tag)
                .withRom(parsedArgs.rom)
                .withSubject(parsedArgs.subject)
                .withTag(parsedArgs.tag)
                .withTestType(parsedArgs.testType)
                .withReportUrl(parsedArgs.reportUrl)
                .withErrorTimes(parsedArgs.errorTimes)
                .build() ;
        try {
            analysisLog.analysis();
        }catch (Exception e){
            Log.i("出现异常 ： " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("解析异常~~~~~~~~~~~~~" + e.getMessage()) ;
        }

    }

    /**
      * 命令行的定义
      *@param
      *@author Viking Den
      *@version 1.0
      *@exception
      *@return null
      *@date 2016-2-26 14:54:49
      */
    public static class CommandLineArgs {

        @Parameter(names = {"-f","--file"} ,
                description = "指定要解析的Log文件路径,如果要指定多个文件，文件路径之间用逗号分隔")
        public File log_file ;

        @Parameter(names = {"-d","--dir"} ,
                description = "指定要解析的Log文件夹路径，则会自动扫描该路径下以.log和.txt结尾的文件"
        )
        public File log_directory ;

        @Parameter(names = {"--type"} ,
                description = "指定解析Log的类型，0：解析所有，1：解析应用报错，" +
                        "2：解析死机、重启，3：解析Case失败Log"
        )
        public int type = 0 ;

        @Parameter(names = {"-o","--out_dir"} ,
                description = "指定Log解析的输出目录，主要是把结果logReport.html和logFailCases.html文件生成到此目录"
        )
        public File out_directory ;

        @Parameter(names = {"-w","--work_dir"} ,
                description = "指定工作区目录，主要会自动遍历目录下是否有AndroidException.xls和allpackagecycle.txt文件"
        )
        public File work_dir ;

        @Parameter(names = {"-u","--server_url"} ,
                description = "指定服务器地址"
        )
        public String server_url ;

        @Parameter(names = {"-p","--project"} ,
                description = "当前项目的名称和版本组合，如BBL7332A02_A_T4355"
        )
        public String project ;

        @Parameter(names = {"-b","--buildid"} ,
                description = "构建的ID值，如2564"
        )
        public String buildid ;

        @Parameter(names = {"-j","--job"} ,
                description = "构建的任务名，如BVT"
        )
        public String jobName ;

        @Parameter(names = {"--tag"} ,
                description = "指定解析Case Log的Tag，如gionee.os.autotest,主要用于解析Case Log时TAG的变化"
        )
        public String tag ;

        @Parameter(names = {"-m"} ,
                description = "指定当前测试的手机型号，如GN3002"
        )
        public String mobile_tag ;

        @Parameter(names = {"--rom"} ,
                description = "指定当前测试的手机Rom"
        )
        public String rom ;


        @Parameter(names = {"--subject"} ,
                description = "用于指定邮件发送的主题"
        )
        public String subject ;

        @Parameter(names = {"--errorTimes"} ,
                description = "用于指定匹配错误的次数"
        )
        public int errorTimes ;

        @Parameter(names = {"--testType"} ,
                description = "指定当前测试类型，如ota，注意：如果不指定此值，默认为ota"
        )
        public String testType;

        @Parameter(names = {"--reportUrl"} ,
                description = "指定测试的任务路径，如http://18.8.10.110:8383/job/DebugBasicTest_3167/1069/testReport/?"
        )
        public String reportUrl ;

        @Parameter(names = { "-h", "--help" },
                description = "输出命令帮助信息",
                help = true,
                hidden = true)
        public boolean help;
    }
}
