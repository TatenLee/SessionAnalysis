package com.bigdata.sessionanalysis.dao;

import com.bigdata.sessionanalysis.domain.AdUserClickCount;

import java.util.List;

/**
 * @Author Taten
 * @Description 用户广告点击量接口
 **/
public interface IAdUserClientCountDAO {
    /**
     * 批量更新用户广告点击量
     *
     * @param adUserClickCounts
     */
    void updateBatch(List<AdUserClickCount> adUserClickCounts);

    /**
     * 根据多个key查询用户广告点击量
     *
     * @param date   日期
     * @param userId 用户id
     * @param adId   广告id
     */
    void findClickCountByMultiKey(String date, int userId, int adId);
}
