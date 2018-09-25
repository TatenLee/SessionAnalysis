package com.bigdata.sessionanalysis.dao.impl;

import com.bigdata.sessionanalysis.dao.IAdBlacklistDAO;
import com.bigdata.sessionanalysis.domain.AdBlacklist;
import com.bigdata.sessionanalysis.jdbc.JDBCHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Taten
 * @Description 广告黑名单DAO实现类
 **/
public class AdBlacklistDAOImpl implements IAdBlacklistDAO {
    /**
     * 批量插入广告黑名单用户
     *
     * @param adBlacklists
     */
    @Override
    public void insertBatch(List<AdBlacklist> adBlacklists) {
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();

        String sql = "INSERT INTO ad_blacklist VALUES(?)";

        List<Object[]> paramsList = new ArrayList<>();

        for (AdBlacklist adBlacklist : adBlacklists) {
            Object[] params = new Object[]{adBlacklist.getUserId()};
            paramsList.add(params);
        }

        jdbcHelper.executeBatch(sql, paramsList);
    }

    /**
     * 查询所有广告黑名单用户
     *
     * @return
     */
    @Override
    public List<AdBlacklist> findAll() {
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();

        String sql = "SELECT * from ad_blacklist";

        List<AdBlacklist> adBlacklists = new ArrayList<>();

        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {
            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()) {
                    int userId = rs.getInt(1);

                    AdBlacklist adBlacklist = new AdBlacklist();

                    adBlacklist.setUserId(userId);

                    adBlacklists.add(adBlacklist);
                }
            }
        });

        return adBlacklists;
    }
}
