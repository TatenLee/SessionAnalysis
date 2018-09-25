package com.bigdata.sessionanalysis.domain;

/**
 * @Author Taten
 * @Description 各省份热门广告Top3
 **/
public class AdProvinceTop3 {
    private String date;
    private String province;
    private int adId;
    private int clickCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
