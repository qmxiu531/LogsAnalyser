package com.gionee.autotest.runner;

import com.gionee.autotest.html.HtmlReporter;
import com.gionee.autotest.model.*;
import com.gionee.autotest.runnable.AppANRCallable;
import com.gionee.autotest.runnable.AppCrashCallable;
import com.gionee.autotest.runnable.CaseLogCallable;
import com.gionee.autotest.runnable.DeviceDieOffCallable;
import com.gionee.autotest.util.Log;
import com.gionee.autotest.util.LogFileFilter;
import com.gionee.autotest.util.ResultLogFileFilter;
import com.gionee.autotest.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 处理Log的逻辑类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-26 15:13:56
 */
public class AnalysisLog {

	private static final String TAG = AnalysisLog.class.getSimpleName();

	private File[] files;

	private int type;

	private File out_directory;

	private File work_dirc;

	// private String project ;

	private List<Future> results;

	public AnalysisLog(Configuration configuration) {
		ConfigurationInjector.setConfiguration(configuration);
		this.files = configuration.getLogFiles();
		this.type = configuration.getType();
		this.out_directory = configuration.getOutDir();
		this.work_dirc = configuration.getWorkDir();
		// this.project = configuration.getProject() ;
	}

	public void analysis() throws Exception {
		Log.i(TAG, "参数正常，进入处理逻辑");
		long start = System.currentTimeMillis();
		clearOutResultDirectory();
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		results = new ArrayList<Future>();
		List<Callable> executors = parseExecutors();

		for (Callable r : executors) {
			results.add(executorService.submit(r));
		}

		executorService.shutdown();
		while (!executorService.isTerminated()) {
			Log.i("等待停止");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ResultModel result = parseResults(results);
		HtmlReporter.createHtml(result);
		long end = System.currentTimeMillis();
		Log.i(TAG, "执行时间 ： " + (end - start) / 1000.0);
		Log.i(TAG, "解析执行完毕，关闭线程池");
	}

	private ResultModel parseResults(List<Future> results) throws Exception {
		ResultModel result = new ResultModel();

		switch (type) {
		case 0:
			if (results.get(0) != null)
				result.setAppCrashModel((List<AppCrashModel>) results.get(0).get());
			if (results.get(1) != null)
				result.setAppANRS((List<AppANRModel>) results.get(1).get());
			if (results.get(2) != null)
				result.setDeviceDieOffModel((List<DeviceDieOffModel>) results.get(2).get());
			if (results.get(3) != null) 
				result.setCaseInfo((CaseInfo) results.get(3).get());
			break;
		case 1:
			if (results.get(0) != null)
				result.setAppCrashModel((List<AppCrashModel>) results.get(0).get());
			result.setAppANRS((List<AppANRModel>) results.get(1).get());
			break;
		case 2:
			if (results.get(0) != null)
				result.setDeviceDieOffModel((List<DeviceDieOffModel>) results.get(0).get());
			break;
		case 3:
			if (results.get(0) != null)
				result.setCaseInfo((CaseInfo) results.get(0).get());
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 根据给定的type创建执行的ExecutorService
	 *
	 * @return
	 */
	public List<Callable> parseExecutors() {
		List<Callable> executors = new ArrayList<Callable>();
		switch (type) {
		case 0:
			executors.add(new AppCrashCallable());
			executors.add(new AppANRCallable());
			executors.add(new DeviceDieOffCallable());
			executors.add(new CaseLogCallable());
			break;
		case 1:
			executors.add(new AppCrashCallable());
			break;
		case 2:
			executors.add(new DeviceDieOffCallable());
			break;
		case 3:
			executors.add(new CaseLogCallable());
			break;
		default:
			break;
		}
		return executors;
	}

	public static class Builder {

		private File log_file;

		private File log_directory;

		private File[] files;

		private int type;

		private File out_directory;

		private File work_dir;

		private String serverUrl;

		private String project;

		private String buildId;

		private String jobName;

		private String rom;

		private String mobileTag;

		private String subject;

		private String tag;

		private String testType;

		private String reportUrl;

		private int errorTimes = 3;

		public Builder withLogFile(File log_file) {
			this.log_file = log_file;
			return this;
		}

		public Builder withLogDirectory(File log_directory) {
			this.log_directory = log_directory;
			return this;
		}

		public Builder withType(int type) {
			this.type = type;
			return this;
		}

		public Builder withOutDirectory(File out_directory) {
			this.out_directory = out_directory;
			return this;
		}

		public Builder withWorkDir(File work_dir) {
			this.work_dir = work_dir;
			return this;
		}

		public Builder withServerUrl(String serverUrl) {
			this.serverUrl = serverUrl;
			return this;
		}

		public Builder withProject(String project) {
			this.project = project;
			return this;
		}

		public Builder withBuildID(String buildID) {
			this.buildId = buildID;
			return this;
		}

		public Builder withJobName(String jobName) {
			this.jobName = jobName;
			return this;
		}

		public Builder withMobileTag(String mobileTag) {
			this.mobileTag = mobileTag;
			return this;
		}

		public Builder withRom(String rom) {
			this.rom = rom;
			return this;
		}

		public Builder withSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public Builder withTag(String tag) {
			this.tag = tag;
			return this;
		}

		public Builder withTestType(String testType) {
			this.testType = testType;
			return this;
		}

		public Builder withReportUrl(String reportUrl) {
			this.reportUrl = reportUrl;
			return this;
		}

		public Builder withErrorTimes(int errorTimes) {
			this.errorTimes = errorTimes;
			return this;
		}

		public AnalysisLog build() {
			// 判定Log解析文件或者文件夹是否为空，或者两者都指定
			if (log_file == null && log_directory == null) {
				throw new IllegalArgumentException("Please target a log file or directory at least !!!");
			}
			if (log_file != null && log_directory != null) {
				throw new IllegalArgumentException("Can not define log_file and log_directory at the same time !!!");
			}
			// 解析指定目录下的Log文件
			if (log_directory != null) {
				File dir = log_directory;
				if (!dir.exists()) {
					throw new IllegalArgumentException(dir.getPath() + " is not exists !!!");
				}
				if (!dir.isDirectory()) {
					throw new IllegalArgumentException(dir.getPath() + " is not a directory !!!");
				}

				files = Util.listFiles(log_directory, new LogFileFilter());

			}
			// 解析指定文件，有可能指定的为多个文件
			if (log_file != null) {
				Log.i(TAG, "log_file : " + log_file.getPath());
				if (log_file.getPath().contains(",")) {
					String[] args = log_file.getPath().split(",");
					files = new File[args.length];
					int count = 0;
					for (String arg : args) {
						files[count] = new File(arg);
						count++;
					}
				} else {
					files = new File[1];
					files[0] = log_file;
				}
			}
			// 判断给定的Log解析文件是否存在，并且是否有效
			if (files == null) {
				throw new IllegalArgumentException("请指定要解析Log的目录或者文件!!!");
			}

			if (files != null) {
				for (File file : files) {
					if (!file.exists()) {
						throw new IllegalArgumentException(file.getPath() + "文件不存在!!!");
					} else if (!file.isFile()) {
						throw new IllegalArgumentException(file.getPath() + "不是文件!!!");
					}
				}
			}
			// 判断解析的类型
			if (!(type >= 0 && type < 4)) {
				throw new IllegalArgumentException("Type is error , must be 0-3 !!!");
			}
			// 判断指定输出目录是否存在，并且是否是文件
			if (out_directory != null) {
				if (out_directory.exists() && out_directory.isFile()) {
					throw new IllegalArgumentException(
							out_directory.getPath() + " is a file ," + " please target a directory!!!");
				}
			}
			// 判断工作目录是否有效
			if (work_dir != null) {
				if (!work_dir.exists() || work_dir.isFile()) {
					throw new IllegalArgumentException(work_dir.getPath() + " should be a directory and must exists .");
				}
				File shouldInnerMail = new File(work_dir, "shouldInnerMail.txt");
				if (shouldInnerMail.exists()) {
					Log.i("清除shouldInnerMail.txt文件存在，删除");
					shouldInnerMail.delete();
				}
			}
			// 处理服务器路径
			if (serverUrl != null) {
				// 判断是否链接目录以/结尾，如果不是，则加上
				// TODO 处理本地 暂时屏蔽
				/*
				 * if (!serverUrl.endsWith("/")){ serverUrl = serverUrl + "/" ;
				 * }
				 */
			}
			Configuration configuration = new Configuration(files, type, out_directory, work_dir, serverUrl, project,
					buildId, jobName, mobileTag, rom, subject, tag, testType, reportUrl, errorTimes);
			return new AnalysisLog(configuration);
		}
	}

	/**
	 * 在执行之前清理工作 ，清除之前保存的结果文件
	 */
	public void clearOutResultDirectory() {
		if (out_directory != null) {
			// 如果文件输出路径不存在，创建
			if (!out_directory.exists()) {
				out_directory.mkdirs();
			}
			// 如果文件输出路径存在，里面有子文件，过滤删除
			else if (out_directory.exists() && out_directory.isDirectory()) {
				File[] files = out_directory.listFiles(new ResultLogFileFilter());
				for (File file : files) {
					Log.i(TAG, "删除文件 : " + file.getName());
					file.delete();
				}
			}
		}
	}
}
