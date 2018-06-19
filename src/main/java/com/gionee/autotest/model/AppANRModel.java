package com.gionee.autotest.model;

/**
 * 应用程序报错类的Model类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-29 16:33:19
 */
public class AppANRModel extends BaseModel {

    private int count ;

    private String error ;

    private String version ;

    private String description ;

    private String longMsg ;

    private String time;//lsq
    
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLongMsg() {
        return longMsg;
    }

    public void setLongMsg(String longMsg) {
        this.longMsg = longMsg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppANRModel)) return false;

        AppANRModel that = (AppANRModel) o;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getError() != null ? !getError().equals(that.getError()) : that.getError() != null)
            return false;
        return !(getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null) ;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getError() != null ? getError().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
