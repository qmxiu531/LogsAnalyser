package com.gionee.autotest.html;

import com.gionee.autotest.model.*;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.Constant;
import com.gionee.autotest.util.Log;
import com.gionee.autotest.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

/**
 * 创建应用程序报错的html
 */
public class CreateHtmlFactory {

	private ResultModel result;

	private boolean isPass;

	List<AppCrashModel> appCrashs;
	List<AppANRModel> appANRs;
	List<DeviceDieOffModel> deviceDies;
	CaseInfo caseInfo;
	List<NAModel> naModels;
	int caseTotal;
	List<CaseModel> failCases;

	public CreateHtmlFactory(ResultModel result) {
		this.result = result;
		appCrashs = result.getAppCrashModel();
		appANRs = result.getAppANRS();
		deviceDies = result.getDeviceDieOffModel();
		caseInfo = result.getCaseInfo();
		if (caseInfo != null) {
			failCases = caseInfo.getFailedCases();
			caseTotal = caseInfo.getCaseTotal();
			naModels = caseInfo.getNaModels();
		}
	}

	/**
	 * 测试结论的html标签文本
	 * 
	 * @return
	 */
	public String getLogAnalysisResult(String reportName) {
		isPass = false;

		if (appCrashs != null && appCrashs.size() > 0 || deviceDies != null && deviceDies.size() > 0
				|| failCases != null && failCases.size() > 0 || caseTotal == 0 || isPhoneBugHtmlExist()) {
			isPass = false;
		} else {
			isPass = true;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>测试结论:</h2>");
		sb.append("<table width='95%'><tbody><tr><td><H3><FONT color=" + (isPass ? "green" : "red")
				+ ">&nbsp;&nbsp;&nbsp;&nbsp;" + (isPass ? HtmlCommon.RESULT_PASS : HtmlCommon.RESULT_FAIL)
				+ "</FONT></H3></td>" + "<td align='right' style='font-size: 100%;font-weight:bold;'>"
				+ "<a href='http://cloud.autotest.gionee.com:8686/DataAnalysis/?listFrame=/DataAnalysis/DataAnalysis/LogAnalysisAction.action?operateType=query;url="
				+ reportName
				+ "'><img border='0' src='http://cloud.autotest.gionee.com:8686/MobilePool/images/dealTest.jpg'></a></td>"
				+ "</tr></tbody></table>");
		return sb.toString();
	}

	/**
	 * 测试详情的html标签文本
	 * 
	 * @return
	 */
	public String getLogAnalysisSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>测试详情:</h2>");
		sb.append("<table width='95%'><tr><td><H3>&nbsp;&nbsp;&nbsp;&nbsp;"
				+ (caseTotal > 0 ? ("测试Case数：共" + caseTotal + "条") : ("测试Case数：共0条")) + "</H3></td></tr></table>");
		if (caseTotal == 0) {
			try {
				Log.i("测试总数为：0，生成不内发的文件");
				Configuration config = ConfigurationInjector.configuration();
				File workDir = config.getWorkDir();
				File shouldInnerMail = new File(workDir, "shouldInnerMail.txt");
				shouldInnerMail.createNewFile();
				if (shouldInnerMail.exists()) {
					Log.i("生成shouldInnerMail.txt成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String content = getPhoneBugHtmlException();
		if (isPhoneBugHtmlExist() && !content.equals("")) {
			createHasDeviceDieOffTxt();
			sb.append("<table width='95%'><tr><td><H3><FONT color=red>&nbsp;&nbsp;&nbsp;&nbsp;" + "注：" + content
					+ "</FONT></H3></td></tr></table>");
		}
		return sb.toString();
	}

	public static boolean isPhoneBugHtmlExist() {
		Configuration config = ConfigurationInjector.configuration();
		File workDir = config.getWorkDir();
		File phoneBug = new File(workDir.getPath() + File.separator + "phoneerror", "PhoneBug.html");
		Log.i("phoneBug path : " + phoneBug.getPath());
		if (phoneBug.exists()) {
			return true;
		}
		return false;
	}

	private static String getPhoneBugHtmlException() {
		String content = "";
		Configuration config = ConfigurationInjector.configuration();
		File workDir = config.getWorkDir();
		File phoneBug = new File(workDir.getPath() + File.separator + "phoneerror", "PhoneBug.html");
		Log.i("phoneBug path : " + phoneBug.getPath());
		if (phoneBug.exists()) {
			try {
				FileReader reader = new FileReader(phoneBug);
				BufferedReader br = new BufferedReader(reader);
				String str;
				while ((str = br.readLine()) != null) {
					if (str.contains("PHONE_ERROR_CONTENT")) {
						content = str.split("PHONE_ERROR_CONTENT")[1];
						Log.i("PhoneBug.html 内容 ： " + content);
						break;
					}
				}
				br.close();
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * Log下载地址的html标签文本
	 * 
	 * @return
	 */
	public String getLogAnalysisAddress() {
		StringBuilder sb = new StringBuilder();
		if (failCases != null && failCases.size() > 0) {
			sb.append("<h4>Fail Case LOG下载地址：</h4>");
			// http://18.8.10.110:8686/DataAnalysis/report/ota/GN3002/GBL7356A01_A_T0604_BasicTest_new_121_logFailCases.html
			Configuration configuration = ConfigurationInjector.configuration();
			String url = configuration.getServerUrl() + configuration.getProject() + "_" + configuration.getJobName()
					+ "_" + configuration.getBuildId() + "_" + Constant.FAIL_CASE_FILE;
			sb.append("<h4><a href='" + url + "'>" + url + "</a></h4>");
		}
		return sb.toString();
	}

	/**
	 * 获取失败Case详情的下载地址
	 * 
	 * @return
	 */
	public String getAttachmentAddress() {
		StringBuilder sb = new StringBuilder();
		if (failCases != null && failCases.size() > 0) {

			// ftp://autotest:autotest@192.168.110.95/Log/debugOta/GN8002/5.0.16/BBL7516A_T0094_GN8002_9999_CaseFailDetails/BBL7516A_T0094_GN8002_9999_CaseFailDetails.html
			Configuration configuration = ConfigurationInjector.configuration();
			String url = "ftp://autotest_ftp:888888@ftp.autotest.gionee.com/autoTestReport/report/log/"
					+ configuration.getTestType() + "/" + configuration.getMobileTag() + "/" + configuration.getRom()
					+ "/" + configuration.getProject() + "_" + configuration.getMobileTag() + "_"
					+ configuration.getBuildId() + "_CaseFailDetails" + "/"
					+ String.format("%s_CaseFailDetails.html", configuration.getProject() + "_"
							+ configuration.getMobileTag() + "_" + configuration.getBuildId());
			sb.append("<h4>Case失败详情附件下载地址：</h4>");
			sb.append("<h4><a href='" + url + "'>" + url + "</a></h4>");
		}
		return sb.toString();
	}

	/**
	 * 返回所有的异常信息的Log文件地址
	 * 
	 * @return
	 */
	public String getLogResultAddress() {
		StringBuilder sb = new StringBuilder();
		// 如果未通过 则添加所有Log分析结果下载地址
		if (appCrashs != null && appCrashs.size() > 0 || deviceDies != null && deviceDies.size() > 0
				|| failCases != null && failCases.size() > 0) {
			sb.append("<h4>LOG分析结果下载地址：</h4>");
			// http://18.8.10.110:8686/DataAnalysis/report/ota/GN3002/GBL7356A01_A_T0604_BasicTest_new_121_log_analysis_result.txt
			Configuration configuration = ConfigurationInjector.configuration();
			String url = configuration.getServerUrl() + configuration.getProject() + "_" + configuration.getJobName()
					+ "_" + configuration.getBuildId() + "_" + Constant.RESULT_LOG_FILE;
			sb.append("<h4><a href='" + url + "'>" + url + "</a></h4>");
		}
		return sb.toString();
	}

	/**
	 * 获取备份Log下载地址
	 * 
	 * @return
	 */
	public String getBackUpLogAddress() {
		StringBuilder sb = new StringBuilder();
		if (caseTotal > 0) {
			sb.append("<h4>全部Log路径：</h4>");
			// ftp://autotest:autotest@192.168.110.95/Log/debugOta/GN8002/5.0.16/LOG-BBL7516A_T0103-GN8002-3871.zip
			Configuration configuration = ConfigurationInjector.configuration();
			String url = "ftp://autotest:autotest@192.168.110.95/Log/" + configuration.getTestType() + "/"
					+ configuration.getMobileTag() + "/" + configuration.getRom() + "/"
					+ String.format("LOG-%s.zip", configuration.getProject() + "-" + configuration.getMobileTag() + "-"
							+ configuration.getBuildId());
			sb.append("<h4><a href='" + url + "'>" + url + "</a></h4>");
		}
		return sb.toString();
	}

	/**
	 * 返回测试结果的路径
	 * 
	 * @return
	 */
	public String getReportUrlText() {
		Configuration configuration = ConfigurationInjector.configuration();
		if (configuration.getReportUrl() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("<h4>测试结果路径:</h4>");
			sb.append(
					"<h4><a href='" + configuration.getReportUrl() + "'>" + configuration.getReportUrl() + "</a></h4>");
			return sb.toString();
		}
		return "";
	}

	/**
	 * 返回分析结果的html文本标签,如果全部为Pass，则不显示失败详情列表
	 * 
	 * @return
	 */
	public String getAnalysisResult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>失败详情：</h2>\n");
		sb.append(createCrashHtml());
		sb.append(createANRHtml());
		sb.append(createDeviceDieOffHtml());
		sb.append(createCaseHtml());
		// sb.append(getLogAnalysisAddress()) ;
		sb.append(getAttachmentAddress());
		sb.append(getLogResultAddress());
		return sb.toString();
	}

	/**
	 * 创建应用报错的html标签文本
	 * 
	 * @return
	 */
	public String createCrashHtml() {
		boolean hasAppCrashs = appCrashs != null && appCrashs.size() > 0;
		StringBuilder sb = new StringBuilder();
		if (!hasAppCrashs) {
			sb.append(createPassHtml("1.应用报错"));
		} else {
			int size = appCrashs.size();
			sb.append("<H2 style=\"FONT-WEIGHT: bold; COLOR: black\">&nbsp;&nbsp;&nbsp;&nbsp;1.应用报错:&nbsp;&nbsp;");
			sb.append("<span style=\"FONT-WEIGHT: bold; COLOR: Red;margin-left:30px\">共 " + size + " 个</span>");
			sb.append("</H2>\n");
			sb.append("<TABLE class=details cellSpacing=2 cellPadding=5 width=\"95%\" border=0>\n");
			sb.append("<TBODY>\n");
			sb.append("<TR vAlign=top>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>序号</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P15\"' style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>应用名称</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>版本</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P25\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>错误描述</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P45\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>错误信息</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>出现次数</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>首次出现时间</STRONG></TD>\n");
			sb.append("</TR>\n");
			int count = 1;
			for (AppCrashModel appCrash : appCrashs) {
					sb.append("<TR valign=\"top\" style=\"BACKGROUND: #bfbfbf; FONT-WEIGHT: bold; COLOR: green\">\n");
					sb.append("<TD >" + count + "</TD>\n");
					sb.append("<TD >" + appCrash.getName() + "</TD>\n");
					sb.append("<TD >" + appCrash.getVersion() + "</TD>\n");
					sb.append("<TD >" + appCrash.getError() + "[" + appCrash.getDescription() + "]" + "</TD>\n");
					sb.append("<TD >" + formatLogOutput(appCrash.getLog()) + "</TD>\n");
					sb.append("<TD >" + appCrash.getCount() + "</TD>\n");
					sb.append("<TD >" + appCrash.getTime() + "</TD>\n");
					sb.append("</TR>\n");
					count++;
			}
			System.out.println("写入Crash的次数到auto.properties文件中");
			Util.writeProperties("crashCounts", size + "");
			sb.append("</TBODY></TABLE>\n");
		}
		return sb.toString();
	}

	/**
	 * 创建应用无响应的html标签文本
	 * 
	 * @return
	 */
	public String createANRHtml() {
		boolean hasAppCrashs = appANRs != null && appANRs.size() > 0;
		StringBuilder sb = new StringBuilder();
		if (!hasAppCrashs) {
			sb.append(createPassHtml("2.应用无响应"));
		} else {
			int size = appANRs.size();
			sb.append("<H2 style=\"FONT-WEIGHT: bold; COLOR: black\">&nbsp;&nbsp;&nbsp;&nbsp;2.应用无响应:&nbsp;&nbsp;");
			sb.append("<span style=\"FONT-WEIGHT: bold; COLOR: Red;margin-left:30px\">共 " + size + " 个</span>");
			sb.append("</H2>\n");
			sb.append("<TABLE class=details cellSpacing=2 cellPadding=5 width=\"95%\" border=0>\n");
			sb.append("<TBODY>\n");
			sb.append("<TR vAlign=top>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>序号</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P15\"' style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>应用名称</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>版本</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P25\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>错误描述</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P45\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>错误信息</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>出现次数</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>首次出现时间</STRONG></TD>\n");
			sb.append("</TR>\n");
			int count = 1;
			for (AppANRModel appANRs : appANRs) {
					sb.append("<TR valign=\"top\" style=\"BACKGROUND: #bfbfbf; FONT-WEIGHT: bold; COLOR: green\">\n");
					sb.append("<TD >" + count + "</TD>\n");
					sb.append("<TD >" + appANRs.getName() + "</TD>\n");
					sb.append("<TD >" + appANRs.getVersion() + "</TD>\n");
					sb.append("<TD >" + appANRs.getError() + "[" + appANRs.getDescription() + "]" + "</TD>\n");
					sb.append("<TD >" + formatLogOutput(appANRs.getLog()) + "</TD>\n");
					sb.append("<TD >" + appANRs.getCount() + "</TD>\n");
					sb.append("<TD >" + appANRs.getTime() + "</TD>\n");
					sb.append("</TR>\n");
					count++;
			}
			System.out.println("写入ANR的次数到auto.properties文件中");
			Util.writeProperties("ANRCounts", size + "");
			sb.append("</TBODY></TABLE>\n");
		}
		return sb.toString();
	}



	/**
	 * 创建测试通过的html标签文本
	 * 
	 * @return
	 */
	private String createPassHtml(String title) {
		StringBuilder sb = new StringBuilder();
		sb.append("<H2 style=\"FONT-WEIGHT: bold; COLOR: black\">&nbsp;&nbsp;&nbsp;&nbsp;" + title + ":&nbsp;&nbsp;");
		sb.append("<span style=\"FONT-WEIGHT: bold; COLOR: Green;margin-left:30px\">无</span>");
		sb.append("</H2>\n");
		return sb.toString();
	}

	/**
	 * 在工作区根目录创建死机重启的txt文件用于外发邮件控制
	 */
	private void createHasDeviceDieOffTxt() {
		try {
			Log.i("创建死机重启txt文件");
			Configuration config = ConfigurationInjector.configuration();
			File fileWorkDir = config.getWorkDir();
			File dieOffAndRebootTxt = new File(fileWorkDir, "dieOffAndReboot.txt");
			if (!dieOffAndRebootTxt.exists()) {
				dieOffAndRebootTxt.createNewFile();

			}
			FileWriter fw = new FileWriter(dieOffAndRebootTxt, true);
			for (DeviceDieOffModel model : deviceDies) {
				Log.i("写死机重启的数据到dieOffAndReboot.txt");
				String db_time = model.getDbtime();
				String db_path = model.getDbpath();
				fw.write("db_time:" + db_time + "##db_path:" + db_path);
			}
			fw.flush();
			fw.close();
		} catch (Exception e) {
			Log.i("创建死机重启的txt文件出现异常");
			e.printStackTrace();
		}
	}

	/**
	 * 创建死机重启的html标签文本
	 * 
	 * @return
	 */
	public String createDeviceDieOffHtml() {
		boolean hasDeviceDieOff = deviceDies != null && deviceDies.size() > 0;

		StringBuilder sb = new StringBuilder();
		if (!hasDeviceDieOff) {
			sb.append(createPassHtml("3.死机重启"));
		} else {

			createHasDeviceDieOffTxt();

			int size = deviceDies.size();
			sb.append("<H2 style=\"FONT-WEIGHT: bold; COLOR: black\">&nbsp;&nbsp;&nbsp;&nbsp;2.死机重启:&nbsp;&nbsp;");
			sb.append("<span style=\"FONT-WEIGHT: bold; COLOR: Red;margin-left:30px\">共 " + size + " 项</span>");
			sb.append("</H2>\n");
			sb.append("<TABLE class=details cellSpacing=2 cellPadding=5 width=\"95%\" border=0>\n");
			sb.append("<TBODY>\n");
			sb.append("<TR vAlign=top>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>序号</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P15\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>类型</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P30\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>错误描述</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P45\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>错误信息</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>出现次数</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P5\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>首次出现时间</STRONG></TD>\n");
			sb.append("</TR>\n");
			int count = 1;
			for (DeviceDieOffModel dieOffModel : deviceDies) {
				sb.append("<TR valign=\"top\" style=\"BACKGROUND: #bfbfbf; FONT-WEIGHT: bold; COLOR: green\">\n");
				sb.append("<TD >" + count + "</TD>\n");
				sb.append("<TD >" + dieOffModel.getName() + "</TD>\n");
				sb.append("<TD >" + dieOffModel.getError() + "[" + dieOffModel.getDescription() + "]" + "</TD>\n");
				sb.append("<TD >" + formatLogOutput(dieOffModel.getLog()) + "</TD>\n");
				sb.append("<TD >" + dieOffModel.getCount() + "</TD>\n");
				sb.append("<TD >" + dieOffModel.getDbtime() + "</TD>\n");
				sb.append("</TR>\n");
				count++;
			}

			Util.writeProperties("deadRebootCounts", size + "");

			sb.append("</TBODY></TABLE>\n");
		}
		return sb.toString();
	}

	private String formatLogOutput(String text) {
		if (text == null || text.equals(""))
			return "";
		StringBuilder sb = new StringBuilder();
		String[] texts = text.split("\n");
		for (String t : texts) {
			sb.append(t + "<br/>");
			if (t.startsWith("ErrorMsg:")) {
				// no more log trace
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 创建Case的html标签文本
	 */
	public String createCaseHtml() {
		StringBuilder sb = new StringBuilder();
		if ((failCases == null || (failCases != null && failCases.size() == 0))
				&& (naModels == null || (naModels != null && naModels.size() == 0))) {
			sb.append(createPassHtml("3.Case列表"));
		} else {
			int size = 0;
			sb.append("<H2 style=\"FONT-WEIGHT: bold; COLOR: black\">&nbsp;&nbsp;&nbsp;&nbsp;3.Case列表: &nbsp;&nbsp;");
			sb.append("</H2>\n");
			if (failCases != null && failCases.size() > 0) {
				size = failCases.size();
				sb.append("<table width='95%'><tr><td><H3><FONT color=\"red\">&nbsp;&nbsp;&nbsp;&nbsp;失败数 : 共" + size
						+ "条</FONT></H3></td></tr></table>");
				sb.append(
						"<table width='95%'><tr><td><H3><FONT color=\"black\">&nbsp;&nbsp;&nbsp;&nbsp;注：请测试下载附件，确认bug后提交bug库，步骤不清楚的可与自动化组联系</FONT></H3></td></tr></table>");

			}
			if (naModels != null && naModels.size() > 0) {
				int total = 0;
				for (NAModel naModel : naModels) {
					total += naModel.getCaseName().size();
				}

				sb.append(
						"<h3 style=\"FONT-WEIGHT: bold; COLOR: red\">&nbsp;&nbsp;&nbsp;&nbsp;未执行Case信息:&nbsp;&nbsp;&nbsp;&nbsp;共："
								+ total + "条");
				sb.append("<TABLE class=details cellSpacing=2 cellPadding=5 width=\"95%\" border=0>\n");
				sb.append("<TBODY>\n");
				sb.append("<TR vAlign=top>\n");
				sb.append(
						"<TD class=\"cell P20\" style=\"BACKGROUND: #a6caf0;FONT-WEIGHT: bold; COLOR: black\"  align=center vertical-align=\"middle\"><STRONG>SN</STRONG></TD>\n");
				sb.append(
						"<TD class=\"cell P40\" style=\"BACKGROUND: #a6caf0;FONT-WEIGHT: bold; COLOR: black\"  align=center vertical-align=\"middle\"><STRONG>原因类型</STRONG></TD>\n");
				sb.append(
						"<TD class=\"cell P40\" style=\"BACKGROUND: #a6caf0;FONT-WEIGHT: bold; COLOR: black\"  align=center vertical-align=\"middle\"><STRONG>case条数</STRONG></TD>\n");
				sb.append("</TR>\n");
				int num = 1;
				for (NAModel naModel : naModels) {
					int count = naModel.getCaseName().size();
					String reason = naModel.getReason();
					sb.append("<TR valign=\"top\" style=\"BACKGROUND: #bfbfbf; FONT-WEIGHT: bold; COLOR: red\">\n");
					sb.append("<TD >" + num + "</TD>\n");
					sb.append("<TD >" + reason + "</TD>\n");
					sb.append("<TD >" + count + "</TD>\n");
					num++;
				}
				sb.append("</TBODY></TABLE></h3>\n");
				sb.append(
						"<table width='95%'><tr><td><H3><FONT color=\"black\">&nbsp;&nbsp;&nbsp;&nbsp;注：由于测试环境未具备导致未执行，请测试另安排执行</FONT></H3></td></tr></table>");
			}

		}
		return sb.toString();
	}

	/**
	 * 创建Fail Case的html文件
	 */
	public String getFailCaseContentHtml() {
		boolean haveFailCase = failCases != null && failCases.size() > 0;

		StringBuilder sb = new StringBuilder();
		if (!haveFailCase) {
			sb.append("<h2>无失败Case</h2>\n");
		} else {
			sb.append("<TABLE class=details cellSpacing=2 cellPadding=5 width=\"95%\" border=0>\n");
			sb.append("<TBODY>\n");
			sb.append("<TR vAlign=top>\n");
			sb.append(
					"<TD class=\"cell P10\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>STM ID</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P10\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>Case ID</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P45\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>操作步骤</STRONG></TD>\n");
			sb.append(
					"<TD class=\"cell P35\" style=\"BACKGROUND: #a6caf0\"  align=center vertical-align=\"middle\"><STRONG>现象</STRONG></TD>\n");
			sb.append("</TR>\n");
			// String jobName =
			// ConfigurationInjector.configuration().getJobName() ;
			// String buildID =
			// ConfigurationInjector.configuration().getBuildId() ;
			for (CaseModel caseModel : failCases) {
				sb.append("<TR valign=\"top\" style=\"BACKGROUND: #bfbfbf; FONT-WEIGHT: bold; COLOR: green\">\n");
				sb.append("<TD >" + caseModel.getStmCaseId() + "</TD>\n");
				sb.append("<TD >" + caseModel.getName() + "</TD>\n");
				String procedure = caseModel.getProcedure() + "\n" + caseModel.getLog();
				sb.append("<TD >" + formatLogOutput(procedure) + "</TD>\n");
				sb.append("<TD >");
				if (caseModel.getFailPhotos() != null) {
					List<String> failPhotos = caseModel.getFailPhotos();
					Collections.sort(failPhotos);
					/*
					 * Log.i("xxxxxxxxxxxxxxxxxxx"); for (String file :
					 * failPhotos){ Log.i("photo name : " + file); }
					 */
					String photoname = convertFailFilePath(failPhotos.get(failPhotos.size() - 1));
					// Log.i("show photo name : " + photoname);
					sb.append("<img style=\"padding:5px\" src=images/" + photoname + " width=300 height=500/>\n");
					/*
					 * for (String file : caseModel.getFailPhotos()){
					 * //http://18.8.10.110:8686/DataAnalysis/report/log/BVT/
					 * BVT_2576/Case_47297_2016-03-10_06-43-26.png
					 *//*
						 * sb.append("<img style=\"padding:5px\" src=" +
						 * Constant.LOG_PICTURE_URL_PREFIX + jobName + "/" +
						 * jobName + "_" + buildID + "/" + file +
						 * " width=200 height=300/>\n");
						 *//*
						 * sb.append("<img style=\"padding:5px\" src=images/" +
						 * convertFailFilePath(file) +
						 * " width=200 height=300/>\n"); }
						 */
					sb.append("</TD>\n");
					sb.append("</TR>\n");
				}
			}
			sb.append("</TBODY></TABLE>\n");
		}
		return sb.toString();
	}

	private String convertFailFilePath(String filename) {
		// Log.i("file name : " + filename);
		if (filename.contains(".jpg")) {
			// Log.i("file name : jpg" );
			return filename.split(".jpg")[0] + "_compress.jpg";
		}
		// Log.i("file name : png" );
		return filename.split(".png")[0] + "_compress.png";
	}

}
