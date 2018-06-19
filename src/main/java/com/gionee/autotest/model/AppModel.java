package com.gionee.autotest.model;

/**
 * 应用的Model类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-29 16:58:31
 */
public class AppModel {

    private String pkg ;

    private String version ;

    private String appName ;

    public AppModel(String appName, String pkg, String version) {
        this.appName = appName;
        this.pkg = pkg;
        this.version = version;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppModel appModel = (AppModel) o;

        if (pkg != null ? !pkg.equals(appModel.pkg) : appModel.pkg != null) return false;
        if (version != null ? !version.equals(appModel.version) : appModel.version != null) return false;
        return appName != null ? appName.equals(appModel.appName) : appModel.appName == null;

    }

    @Override
    public int hashCode() {
        int result = pkg != null ? pkg.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (appName != null ? appName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AppModel{" +
                "appName='" + appName + '\'' +
                ", pkg='" + pkg + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
