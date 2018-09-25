package com.bigdata.sessionanalysis.domain;

/**
 * @Author Taten
 * @Description 广告用户点击量
 **/
public class AdUserClickCount {
    private String date;
    private int userId;
    private int adId;
    private int clickCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
