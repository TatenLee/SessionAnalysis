package com.bigdata.sessionanalysis.dao;

import com.bigdata.sessionanalysis.domain.AreaTop3Product;

import java.util.List;

/**
 * @Author Taten
 * @Description 各区域Top3热门商品DAO接口
 **/
public interface IAreaTop3ProductDAO {
    void insertBatch(List<AreaTop3Product> areaTop3Products);
}
