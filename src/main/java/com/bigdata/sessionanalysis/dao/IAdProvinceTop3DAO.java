package com.bigdata.sessionanalysis.dao;

import com.bigdata.sessionanalysis.domain.AdProvinceTop3;

import java.util.List;

/**
 * @Author Taten
 * @Description 各省份Top3热门广告DAO接口
 **/
public interface IAdProvinceTop3DAO {
    void updateBatch(List<AdProvinceTop3> adProvinceTop3s);
}
