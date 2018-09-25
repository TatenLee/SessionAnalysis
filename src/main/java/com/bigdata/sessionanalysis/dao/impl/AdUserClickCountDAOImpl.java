package com.bigdata.sessionanalysis.dao.impl;

import com.bigdata.sessionanalysis.dao.IAdUserClientCountDAO;
import com.bigdata.sessionanalysis.domain.AdUserClickCount;
import com.bigdata.sessionanalysis.jdbc.JDBCHelper;
import com.bigdata.sessionanalysis.model.AdUserClickCountQueryResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Taten
 * @Description 用户广告点击量DAO实现类
 **/
public class AdUserClickCountDAOImpl implements IAdUserClientCountDAO {
    @Override
    public void updateBatch(List<AdUserClickCount> adUserClickCounts) {
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();

        /*
            区分哪些是要插入的，哪些是要更新的
         */
        String selectSQL = "SELECT count(*) FROM ad_user_click_count " +
                "WHERE date = ? AND user_id = ? AND ad_id = ?";

        List<AdUserClickCount> insertAdUserClickCounts = new ArrayList<>();
        List<AdUserClickCount> updateAdUserClickCounts = new ArrayList<>();

        for (AdUserClickCount adUserClickCount : adUserClickCounts) {
            AdUserClickCountQueryResult queryResult = new AdUserClickCountQueryResult();

            Object[] params = new Object[]{
                    adUserClickCount.getDate(),
                    adUserClickCount.getUserId(),
                    adUserClickCount.getAdId()
            };

            jdbcHelper.executeQuery(selectSQL, params, new JDBCHelper.QueryCallback() {
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
                updateAdUserClickCounts.add(adUserClickCount);
            } else {
                insertAdUserClickCounts.add(adUserClickCount);
            }
        }

        /*
            执行批量插入
         */
        String insertSQL = "INSERT INTO ad_user_click_count values(?, ?, ?, ?)";

        List<Object[]> insertParamsList = new ArrayList<>();

        for (AdUserClickCount adUserClickCount : insertAdUserClickCounts) {
            Object[] insertParams = new Object[]{
                    adUserClickCount.getDate(),
                    adUserClickCount.getUserId(),
                    adUserClickCount.getAdId(),
                    adUserClickCount.getClickCount()
            };

            insertParamsList.add(insertParams);
        }

        jdbcHelper.executeBatch(insertSQL, insertParamsList);
    }

    @Override
    public void findClickCountByMultiKey(String date, int userId, int adId) {

    }
}
