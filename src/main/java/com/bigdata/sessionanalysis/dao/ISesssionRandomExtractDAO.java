package com.bigdata.sessionanalysis.dao;

import com.bigdata.sessionanalysis.domain.SessionRandomExtract;

/**
 * @Author Taten
 * @Description session随机抽取模块DAO接口
 **/
public interface ISesssionRandomExtractDAO {
    /**
     * 插入session随机抽取
     *
     * @param sessionRandomExtract
     */
    void insert(SessionRandomExtract sessionRandomExtract);
}
