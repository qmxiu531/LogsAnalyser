package com.gionee.autotest.util;

/**
 * 常量类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-26 16:18:07
 */
public class Constant {

    /**********************************
     * COMMON PROPERTIES
     **********************************/
    /**
     * 指定读取文件时的缓冲大小为10M
     */
    public static final int BUFFER_SIZE = 10 * 1024 * 1024 ;

    /**
     * 换行的标志位
     */
    public static final String NEW_LINE = "\n" ;

    /**
     * 定义Log解析输出的html文件
     */
    public static final String RESULT_HTML_FILE = "logReport.html" ;

    /**
     * 定义Log解析Fail Case输出文件
     */
    public static final String FAIL_CASE_FILE = "logFailCases.html" ;

    /**
     * 定义解析死机重启的异常Excel文件
     */
    public static final String EXCEPTION_EXCEL = "AndroidException.xls" ;

    /**
     * 用于保存输出所有的Fail的信息
     */
    public static final String RESULT_LOG_FILE = "log_analysis_result.txt" ;

    /**
     * 用于指定Case Fail现象图片的地址前缀
     */
    public static final String LOG_PICTURE_URL_PREFIX = "http://18.8.10.110:8686/DataAnalysis/report/log/" ;

    /**
     * 主服务器地址前缀
     */
//    public static final String MAIN_SERVER_URL = "http://18.8.10.110:8686/DataAnalysis/report/" ;
    public static final String MAIN_SERVER_URL = "ftp://autotest_ftp:888888@ftp.autotest.gionee.com/autoTestReport/report/" ;


    public static final String FAIL_CASE_RESULT_DIR = "result" ;

    public static final String FAIL_PIC_ORIGIN_DIR = "result/images_origin" ;

    public static final String FAIL_PIC_COMPRESS_DIR = "result/images" ;

    public static final String D_LOG_MASHMALLOW = "D/gionee.os.autotest" ;

    public static final String D_LOG_MASHMALLOW_BELOW = "D gionee.os.autotest" ;

    /**********************************
     * CASE PROPERTIES
     **********************************/
    /**
     * Case失败的标志位
     */
    public static final String CASE_FAIL_TAG = ": Result:Fail" ;
    /**
     * Case NA的标志位
     */
    public static final String CASE_NA_TAG = ": Result:N/A" ;
    /**
     * Case开始的标志位
     */
    public static final String CASE_START_TAG = ": Start:" ;
    /**
     * Case结束的标志位
     */
    public static final String CASE_END_TAG = ": End:" ;

    /**
     * Case错误的标志位
     */
    public static final String CASE_ERROR_MSG_TAG = ": ErrorMsg:" ;

    /**
     * Case的STM case id
     */
    public static final String CASE_STM_CASE_ID =": STMID:" ;

    /**
     * Case的打印TAG
     */
    public static final String CASE_TAG = "gionee.os.autotest" ;

    /**
     * 用于定义Case步骤开始的Tag
     */
    public static final String CASE_PROCEDURE_START_TAG = ": IsPublish:" ;

    /**
     * 用于定义Case步骤结束的Tag
     */
    public static final String CASE_PROCEDURE_END_TAG = ": Result:" ;

    /**
     * 用于定义Case Error Stack Trace结束的Tag
     */
    public static final String CASE_ERROR_STACK_TRACE_END_TAG = ": End Time:" ;

    public static final String N_A_RESULT_TAG = "N_A_REASON" ;


    /**********************************************************
     * App Crash Constant Properties
     *********************************************************/
    /**
     * 用于判断应用层是否Crash的标志位
     */
    public static final String CRASH_FATAL_EXCEPTION = ": FATAL EXCEPTION: ";

    /**
     * 用于判断应用层Crash的应用名
     */
    public static final String CRASH_PROCESS = ": Process:" ;

    /**
     * 用于判断应用是否Crash
     */
    public static final String CRASH_ANDROID_RUNTIME = "E/AndroidRuntime";


    /**
     * ANR开始出现的标志位
     */
    public static final String ANR_START_TAG = ": Application is not responding:" ;

    /**
     * ANR的ANRManager标志位TAG
     */
    public static final String ANR_MANAGER_TAG = "E/ANRManager" ;

    /**
     * ANR包名出现的标志位
     */
    public static final String ANR_PACKAGE_TAG = ": ANR in " ;

    /**
     * ANR原因出现的标志位
     */
    public static final String ANR_REASON_TAG = ": Reason:" ;

    /**
     * 应用程序无响应的描述
     */
    public static final String APPLICATION_NOT_RESPOND = "应用程序无响应【ANR】" ;
}
