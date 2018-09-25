package com.bigdata.sessionanalysis.utils;

import com.alibaba.fastjson.JSONObject;
import com.bigdata.sessionanalysis.conf.ConfigurationManager;
import com.bigdata.sessionanalysis.constant.Constants;
import com.bigdata.sessionanalysis.test.MockData;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;

/**
 * @Author Taten
 * @Description Spark工具类
 **/
public class SparkUtils {
    /**
     * 根据当前是否是本地测试的配置决定如何设置SparkConf的master
     *
     * @param conf
     */
    public static void setMaster(SparkConf conf) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);

        if (local) {
            conf.setMaster("local");
        }
    }

    /**
     * 获取SQLContext
     * 如果spark.local设置为true就创建SQLContext，false就创建HiveContext
     *
     * @param sc
     * @return
     */
    public static SQLContext getSQLContext(SparkContext sc) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);

        if (local) {
            return new SQLContext(sc);
        } else {
            return new HiveContext(sc);
        }
    }

    /**
     * 生成模拟数据
     * 如果spark.local配置设置为true则生成模拟数据，false则不生成
     *
     * @param sc
     * @param sqlContext
     */
    public static void mockData(JavaSparkContext sc, SQLContext sqlContext) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);

        if (local) {
            MockData.mock(sc, sqlContext);
        }
    }

    /**
     * 获取指定日期范围内的用户行为数据RDD
     *
     * @param sqlContext
     * @param taskParam
     * @return
     */
    public static JavaRDD<Row> getActionRDDByDateRange(SQLContext sqlContext, JSONObject taskParam) {
        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);

        String endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE);

        String sql = "SELECT * FROM user_visit_action" +
                "WHERE date >= '" + startDate + "' " +
                "AND date <= '" + endDate + "' ";
//                "AND session_id not in ('', '', '')"

        Dataset<Row> actionDF = sqlContext.sql(sql);

        /*
            比如说，Spark SQL默认就给第一个stage设置了20个task，但是根据数据量以及算法的复杂度
            实际上最好用1000个task去并行执行
            所以可以对Spark SQL刚刚查询出来的RDD执行repartition重分区操作
         */
//        return actionDF.javaRDD().repartition(1000);

        return actionDF.javaRDD();
    }
}
