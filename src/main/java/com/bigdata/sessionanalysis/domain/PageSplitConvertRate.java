package com.bigdata.sessionanalysis.domain;

/**
 * @Author Taten
 * @Description 页面切片转化率
 **/
public class PageSplitConvertRate {
    private int taskId;
    private String convertRate;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(String convertRate) {
        this.convertRate = convertRate;
    }
}
