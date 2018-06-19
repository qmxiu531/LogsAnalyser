package com.gionee.autotest.model;

/**
 * 死机重启的Model类
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-2 16:02:58
 */
public class DeviceDieOffModel {

    private String name ;

    private String project ;

    private String error ;

    private String description ;

    private int count ;

    private String log ;

    private String dbtime ;

    private String dbpath ;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDbtime() {
        return dbtime;
    }

    public void setDbtime(String dbtime) {
        this.dbtime = dbtime;
    }

    public String getDbpath() {
        return dbpath;
    }

    public void setDbpath(String dbpath) {
        this.dbpath = dbpath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceDieOffModel that = (DeviceDieOffModel) o;

        if (count != that.count) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (project != null ? !project.equals(that.project) : that.project != null) return false;
        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        return log != null ? log.equals(that.log) : that.log == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + (log != null ? log.hashCode() : 0);
        return result;
    }
}
