package com.gionee.autotest.model;

import java.util.List;

/**
 * Case Fail类型的Model类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-27 09:28:40
 */
public class CaseModel extends BaseModel{

    private String stmCaseId = "N/A" ;

    private String procedure ;

    private String caseLog ;

    private List<String> failPhotos ;

    private int count = 1 ;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

/*    public String getCaseLog() {
        return caseLog;
    }

    public void setCaseLog(String caseLog) {
        this.caseLog = caseLog;
    }*/

    public List<String> getFailPhotos() {
        return failPhotos;
    }

    public void setFailPhotos(List<String> failPhotos) {
        this.failPhotos = failPhotos;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getStmCaseId() {
        return stmCaseId;
    }

    public void setStmCaseId(String stmCaseId) {
        this.stmCaseId = stmCaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaseModel caseModel = (CaseModel) o;

/*        if (name != null ? !name.equals(caseModel.name) : caseModel.name != null) return false;
        if (stmCaseId != null ? !stmCaseId.equals(caseModel.stmCaseId) : caseModel.stmCaseId != null) return false;
        if (procedure != null ? !procedure.equals(caseModel.procedure) : caseModel.procedure != null) return false;*/
        return name != null ? name.equals(caseModel.name) : caseModel.name == null;

    }

    @Override
    public int hashCode() {
/*        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (stmCaseId != null ? stmCaseId.hashCode() : 0);
        result = 31 * result + (procedure != null ? procedure.hashCode() : 0);
        result = 31 * result + (caseLog != null ? caseLog.hashCode() : 0);*/
        return name != null ? name.hashCode() : 0;
    }
}
