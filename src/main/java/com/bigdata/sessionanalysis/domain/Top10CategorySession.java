package com.bigdata.sessionanalysis.domain;

/**
 * @Author Taten
 * @Description Top10活跃品类session
 **/
public class Top10CategorySession {
    private int taskId;
    private int categoryId;
    private String sessionId;
    private int clickCount;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
