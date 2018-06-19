package com.gionee.autotest.model;

import java.util.List;

/**
 * 任务执行结果的Model
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-3 16:55:09
 */
public class ResultModel {

    private List<AppCrashModel>  appCrashModel ;
    
    private List<AppANRModel>  appANRS ;

	private List<DeviceDieOffModel> deviceDieOffModel ;

    private CaseInfo caseInfo ;

    public List<AppANRModel> getAppANRS() {
		return appANRS;
	}

	public void setAppANRS(List<AppANRModel> appANRS) {
		this.appANRS = appANRS;
	}

    public List<AppCrashModel> getAppCrashModel() {
        return appCrashModel;
    }

    public void setAppCrashModel(List<AppCrashModel> appCrashModel) {
        this.appCrashModel = appCrashModel;
    }

    public List<DeviceDieOffModel> getDeviceDieOffModel() {
        return deviceDieOffModel;
    }

    public void setDeviceDieOffModel(List<DeviceDieOffModel> deviceDieOffModel) {
        this.deviceDieOffModel = deviceDieOffModel;
    }

    public CaseInfo getCaseInfo() {
        return caseInfo;
    }

    public void setCaseInfo(CaseInfo caseInfo) {
        this.caseInfo = caseInfo;
    }
}
