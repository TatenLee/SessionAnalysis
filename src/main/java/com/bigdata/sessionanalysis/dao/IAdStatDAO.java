package com.bigdata.sessionanalysis.dao;

import com.bigdata.sessionanalysis.domain.AdStat;

import java.util.List;

/**
 * @Author Taten
 * @Description 广告实时统计DAO接口
 **/
public interface IAdStatDAO {
    void updateBatch(List<AdStat> adStats);
}
