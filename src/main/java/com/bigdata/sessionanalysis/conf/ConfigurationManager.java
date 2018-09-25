package com.bigdata.sessionanalysis.conf;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Taten
 * @Description 配置管理组件
 * 提供外界获取某个配置key对应的value的方法
 **/
public class ConfigurationManager {
    // private来修饰，避免外界访问来更改值
    private static Properties prop = new Properties();

    /**
     * 静态代码块用于初始化类，为类的属性初始化
     */
    static {
        try {
            // 通过调用类加载器（ClassLoader）的getResourceAsStream方法获取指定文件的输入流
            InputStream in = ConfigurationManager.class.getResourceAsStream("props.properties");
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定key对应的value
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    /**
     * 获取整数类型的配置项
     *
     * @param key
     * @return
     */
    public static Integer getInteger(String key) {
        String value = getProperty(key);

        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取Long类型的配置项
     *
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        String value = getProperty(key);

        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }

    /**
     * 获取布尔类型的配置项
     *
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key) {
        String value = getProperty(key);

        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
