package com.bigdata.sessionanalysis.dao.impl;

import com.bigdata.sessionanalysis.dao.IAdClickTrendDAO;
import com.bigdata.sessionanalysis.domain.AdClickTrend;
import com.bigdata.sessionanalysis.jdbc.JDBCHelper;
import com.bigdata.sessionanalysis.model.AdClickTrendQueryResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Taten
 * @Description 广告点击趋势DAO实现类
 **/
public class AdClickTrendDAOImpl implements IAdClickTrendDAO {
    @Override
    public void updateBatch(List<AdClickTrend> adClickTrends) {
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();

        /*
            区分出来哪些数据是要插入的，哪些数据是要更新的
            比如，通常来说，同一个key的数据（比如rdd，包含了多条相同的key）通常是在一个分区内
            一般不会出现重复插入，但是根据业务需求来
            如果说可能出现key重复插入的情况，给一个create_time字段
            J2EE系统在查询的时候直接查询最新的数据即可（规避掉重复插入的问题）
         */
        String selectSQL = "SELECT count(*) FROM ad_click_trend" +
                "WHERE date = ?" +
                "AND hour = ?" +
                "AND minute = ?" +
                "AND ad_id = ?";

        List<AdClickTrend> insertAdClickTrends = new ArrayList<>();
        List<AdClickTrend> updateAdClickTrends = new ArrayList<>();

        for (AdClickTrend adClickTrend : adClickTrends) {
            AdClickTrendQueryResult queryResult = new AdClickTrendQueryResult();

            Object[] selectParams = new Object[]{
                    adClickTrend.getDate(),
                    adClickTrend.getHour(),
                    adClickTrend.getMinute(),
                    adClickTrend.getAdId()
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
                updateAdClickTrends.add(adClickTrend);
            } else {
                insertAdClickTrends.add(adClickTrend);
            }
        }

        /*
            执行批量插入操作
         */
        String insertSQL = "INSERT INTO ad_click_trend values(?, ?, ?, ?, ?)";

        List<Object[]> insertParamList = new ArrayList<>();

        for (AdClickTrend adClickTrend : insertAdClickTrends) {
            Object[] params = new Object[]{
                    adClickTrend.getClickCount(),
                    adClickTrend.getDate(),
                    adClickTrend.getHour(),
                    adClickTrend.getMinute(),
                    adClickTrend.getAdId()
            };

            insertParamList.add(params);
        }

        jdbcHelper.executeBatch(insertSQL, insertParamList);

        /*
            执行批量更新操作
         */
        String updateSQL = "UPDATE ad_click_trend " +
                "SET click_count = ? " +
                "WHERE date = ? " +
                "AND hour = ? " +
                "AND minute = ? " +
                "AND ad_id = ?";

        List<Object[]> updateParamList = new ArrayList<>();

        for (AdClickTrend adClickTrend : updateAdClickTrends) {
            Object[] params = new Object[]{
                    adClickTrend.getClickCount(),
                    adClickTrend.getDate(),
                    adClickTrend.getHour(),
                    adClickTrend.getMinute(),
                    adClickTrend.getAdId()
            };

            updateParamList.add(params);
        }

        jdbcHelper.executeBatch(updateSQL, updateParamList);
    }
}
