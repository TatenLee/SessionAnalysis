package com.bigdata.sessionanalysis.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigdata.sessionanalysis.conf.ConfigurationManager;
import com.bigdata.sessionanalysis.constant.Constants;

/**
 * @Author Taten
 * @Description 参数工具类
 **/
public class ParamUtils {
    /**
     * 从命令行参数中提取任务id
     *
     * @param args     命令行参数
     * @param taskType
     * @return 任务id
     */
    public static Integer getTaskIdFromArgs(String[] args, String taskType) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);

        if (local) {
            return ConfigurationManager.getInteger(taskType);
        } else {
            try {
                if (null != args && args.length > 0) {
                    return Integer.valueOf(args[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 从JSON对象中提取参数
     *
     * @param jsonObject JSON对象
     * @param field
     * @return 参数
     */
    public static String getParam(JSONObject jsonObject, String field) {
        JSONArray jsonArray = jsonObject.getJSONArray(field);

        if (null != jsonArray && jsonArray.size() > 0) {
            return jsonArray.getString(0);
        } else {
            return null;
        }
    }
}
