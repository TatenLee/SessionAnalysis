package com.bigdata.sessionanalysis.dao.impl;

import com.bigdata.sessionanalysis.dao.IAdProvinceTop3DAO;
import com.bigdata.sessionanalysis.domain.AdProvinceTop3;
import com.bigdata.sessionanalysis.jdbc.JDBCHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Taten
 * @Description 各省份Top3热门广告DAO实现类
 **/
public class AdProvinceTop3DAOImpl implements IAdProvinceTop3DAO {
    @Override
    public void updateBatch(List<AdProvinceTop3> adProvinceTop3s) {
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();

        /*
            先做一次去重（date_province）
         */
        List<String> dateProvinces = new ArrayList<>();

        for (AdProvinceTop3 adProvinceTop3 : adProvinceTop3s) {
            String date = adProvinceTop3.getDate();
            String province = adProvinceTop3.getProvince();
            String key = date + "_" + province;

            if (!dateProvinces.contains(key)) {
                dateProvinces.add(key);
            }
        }

        /*
            根据去重后的date和province进行批量删除操作
         */
        String deleteSQL = "DELETE FROM ad_province";

        List<Object[]> deleteParamsList = new ArrayList<>();

        for (String dateProvince : dateProvinces) {
            String[] fields = dateProvince.split("_");
            String date = fields[0];
            String province = fields[1];

            String[] params = new String[]{date, province};

            deleteParamsList.add(params);
        }

        jdbcHelper.executeBatch(deleteSQL, deleteParamsList);

        /*
            批量插入传入进来的所有数据
         */
        String insertSQL = "INSERT INTO ad_province_top3 VALUES(?, ?, ?, ?)";

        List<Object[]> insertParamsList = new ArrayList<>();

        for (AdProvinceTop3 adProvinceTop3 : adProvinceTop3s) {
            Object[] params = new Object[]{
                    adProvinceTop3.getDate(),
                    adProvinceTop3.getProvince(),
                    adProvinceTop3.getAdId(),
                    adProvinceTop3.getClickCount()
            };

            insertParamsList.add(params);
        }

        jdbcHelper.executeBatch(insertSQL, insertParamsList);
    }
}
