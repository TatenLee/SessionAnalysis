package com.bigdata.sessionanalysis.dao;

import com.bigdata.sessionanalysis.domain.AdClickTrend;

import java.util.List;

/**
 * @Author Taten
 * @Description 广告点击趋势DAO接口
 **/
public interface IAdClickTrendDAO {
    void updateBatch(List<AdClickTrend> adClickTrends);
}
