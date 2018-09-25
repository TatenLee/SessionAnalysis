package com.bigdata.sessionanalysis.dao.impl;

import com.bigdata.sessionanalysis.dao.IAdStatDAO;
import com.bigdata.sessionanalysis.domain.AdStat;
import com.bigdata.sessionanalysis.jdbc.JDBCHelper;
import com.bigdata.sessionanalysis.model.AdStatQueryResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Taten
 * @Description 广告实时统计DAO实现类
 **/
public class AdStatDAOImpl implements IAdStatDAO {
    @Override
    public void updateBatch(List<AdStat> adStats) {
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();

        /*
            区分哪些是要插入的，哪些是要更新的
         */
        String selectSQL = "SELECT count(*) FROM ad_stat " +
                "WHERE date = ? " +
                "AND province = ? " +
                "AND city = ? " +
                "AND ad_id = ?";

        List<AdStat> insertAdStats = new ArrayList<>();
        List<AdStat> updateAdStats = new ArrayList<>();

        for (AdStat adStat : adStats) {
            AdStatQueryResult queryResult = new AdStatQueryResult();

            Object[] selectParams = new Object[]{
                    adStat.getProvince(),
                    adStat.getCity(),
                    adStat.getAdId()
            };

            jdbcHelper.executeQuery(selectSQL, selectParams, new JDBCHelper.QueryCallback() {
                @Override
                public void process(ResultSet rs) throws Exception {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        queryResult.setCount(count);
                    }
                }
            });

            int count = queryResult.getCount();

            if (count > 0) {
                updateAdStats.add(adStat);
            } else {
                insertAdStats.add(adStat);
            }
        }

         /*
            执行批量插入操作
         */
        String insertSQL = "INSERT INTO ad_stat VALUES(?, ?, ?, ?, ?)";

        List<Object[]> insertParamsList = new ArrayList<>();

        for (AdStat updateAdStat : updateAdStats) {
            Object[] params = new Object[]{
                    updateAdStat.getClickCount(),
                    updateAdStat.getDate(),
                    updateAdStat.getProvince(),
                    updateAdStat.getCity(),
                    updateAdStat.getAdId()
            };

            insertParamsList.add(params);
        }

        jdbcHelper.executeBatch(insertSQL, insertParamsList);

        /*
            执行批量更新操作
         */
        String updateSQL = "UPDATE ad_stat " +
                "SET click_count = ? " +
                "FROM ad_stat " +
                "WHERE date = ? " +
                "AND province = ? " +
                "AND city = ? " +
                "AND ad_id = ?";

        List<Object[]> updateParamsList = new ArrayList<>();

        for (AdStat inserAdStat : insertAdStats) {
            Object[] params = new Object[]{
                    inserAdStat.getDate(),
                    inserAdStat.getProvince(),
                    inserAdStat.getCity(),
                    inserAdStat.getAdId(),
                    inserAdStat.getClickCount()
            };

            updateParamsList.add(params);
        }

        jdbcHelper.executeBatch(updateSQL, updateParamsList);
    }
}
