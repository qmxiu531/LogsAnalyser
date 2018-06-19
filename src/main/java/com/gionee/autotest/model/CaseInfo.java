package com.gionee.autotest.model;

import com.gionee.autotest.html.CreateHtmlFactory;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计Case的状态信息
 */
public class CaseInfo {

    private int caseTotal ;

    private List<NAModel> naModels ;

    private List<CaseModel> failedCases ;

    public List<NAModel> getNaModels() {
        return naModels;
    }

    public void setNaModels(List<NAModel> naModels) {
        this.naModels = naModels;
    }

    public int getCaseTotal() {
        return caseTotal;
    }

    public void setCaseTotal(int caseTotal) {
        this.caseTotal = caseTotal;
    }

    public List<CaseModel> getFailedCases() {
        /*boolean isPhoneBugExist = CreateHtmlFactory.isPhoneBugHtmlExist() ;
        Log.i("CaseInfo isPhoneBugExist ：" + isPhoneBugExist);*/
        //TODO CHANGE 3 TO 1
        int count = ConfigurationInjector.configuration().getErrorTimes() ;
        Log.i("errorTimes  : " + count);
        if(failedCases != null && failedCases.size() > 0){
            Log.i("CaseInfo" , " FailedCases size : " + failedCases.size());
            List<CaseModel> filterdCase = new ArrayList<CaseModel>() ;
            for(CaseModel model : failedCases){
                Log.i("CaseInfo" , "Case Name : " + model.getName() + " Case count : " + model.getCount());
                if (model.getCount() >= count){
                    filterdCase.add(model) ;
                }
            }
            return filterdCase ;
        }
        return null;
    }

    public void setFailedCases(List<CaseModel> failedCases) {
        this.failedCases = failedCases;
    }
}
