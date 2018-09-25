package com.bigdata.sessionanalysis.jdbc;

import com.bigdata.sessionanalysis.conf.ConfigurationManager;
import com.bigdata.sessionanalysis.constant.Constants;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author Taten
 * @Description JDBC辅助组件
 **/
public class JDBCHelper {
    /*
        第一步：在静态代码块中，直接加载数据库的驱动
        加载驱动，不是直接简单的使用com.mysql.jdbc.Driver就可以了
        之所以说，不要硬编码，它的原因就在于这里

        com.mysql.jdbc.Driver只代表了MySQL数据库的驱动
        那么，如果有一天，项目底层的数据库要进行迁移，比如迁移到Oracle或者是DB2，SQLServer等
        就必须很费劲的在代码中找到硬编码com.mysql.jdbc.Driver的地方
        然后改成其他数据库的驱动类的类名
        所以正规项目是不允许硬编码的，那样维护成本很高

        通常，使用一个常量接口中的某个常量来代表一个值
        然后在这个值改变的时候，只要改变常量接口中的常量对应的值就可以了

        项目要尽量做成可配置的
        就是说这个数据库驱动，更进一步，不只是放在常量接口中就可以了
        最好的方式是放在外部的配置文件中，和代码彻底分离
        常量接口中，只是包含了这个值对应的key的名字
    */
    static {
        try {
            String driver = ConfigurationManager.getProperty(Constants.JDBC_DRIVER);
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        第二步：实现JDBCHelper的单例模式
        之所以实现单例模式是因为它的内部要封装一个简单的内部的数据库连接池
        为了保证数据库连接池有且仅有一份，所以就通过单例的方式
        保证JDBCHelper只有一个实例，实例中只有一份数据库连接池
     */
    private static JDBCHelper instance = null;

    /**
     * 获取单例
     *
     * @return
     */
    public static JDBCHelper getInstance() {
        if (null == instance) {
            synchronized (JDBCHelper.class) {
                instance = new JDBCHelper();
            }
        }

        return instance;
    }

    // 数据库连接池
    private LinkedList<Connection> dataSource = new LinkedList<>();

    /*
        第三步：实现单例的过程中，创建唯一的数据库连接池
        私有化构造方法
        JDBCHelper在整个程序运行生命周期中，只会创建一次实例
        在这一次创建实例的过程中，就会调用JDBCHepler()构造方法
        此时，就可以在构造方法中去创建自己唯一的一个数据库连接池
     */
    private JDBCHelper() {
        /*
            首先第一步，获取数据库连接池的大小（数据库连接池中要放多少个数据连接）
            可以通过在配置文件中配置的方式来灵活的设定
         */
        int dataSourceSize = ConfigurationManager.getInteger(Constants.JDBC_DATASOURCE_SIZE);

        // 创建指定数量的数据库连接，并放入数据库连接池中
        for (int i = 0; i < dataSourceSize; i++) {
            boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);

            String url = null;
            String user = null;
            String password = null;

            if (local) {
                url = ConfigurationManager.getProperty(Constants.JDBC_URL);
                user = ConfigurationManager.getProperty(Constants.JDBC_USER);
                password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
            } else {
                url = ConfigurationManager.getProperty(Constants.JDBC_URL_PROD);
                user = ConfigurationManager.getProperty(Constants.JDBC_USER_PROD);
                password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD_PROD);
            }

            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                dataSource.push(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
        第四步：提供获取数据库连接的方法
        有可能获取的时候连接被用光了，暂时无法获取数据库连接
        所以需要实现一个简单的等待机制，等待获取到数据库连接
     */
    public synchronized Connection getConnection() {
        while (0 == dataSource.size()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return dataSource.poll();
    }

    /*
        第五步：开发增删改查的方法
        1. 执行增删改SQL语句的方法
        2. 执行查询SQL语句的方法
        3. 批量执行SQL语句的方法
     */

    /**
     * 执行增删改查SQL语句
     *
     * @param sql
     * @param params
     * @return 影响的行数
     */
    public int executeUpdate(String sql, Object[] params) {
        int rows = 0;
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            conn = this.getConnection();

            // 取消自动提交
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);

            if (null != params && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rows = ps.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                dataSource.push(conn);
            }
        }

        return rows;
    }

    /**
     * 执行查询SQL语句
     *
     * @param sql
     * @param params
     * @param callback
     */
    public void executeQuery(String sql, Object[] params, QueryCallback callback) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = this.getConnection();
            ps = conn.prepareStatement(sql);

            if (null != params && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }

            rs = ps.executeQuery();

            callback.process(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                dataSource.push(conn);
            }
        }

    }

    /*
        批量执行SQL语句是JDBC中的一个高级功能
        默认情况下，每次执行一条SQL语句就会通过网络连接向MySQL发送一次请求

        但是，如果再短时间内要执行多条结构完全一模一样的SQL，只是参数不同
        虽然使用PreparedStatement这种方式可以只编译一次SQL，提高性能
        但是还是对于每次SQL都要向MySQL发送一次网络请求

        可以通过批量执行SQL语句的功能优化这个性能
        一次性通过PreparedStatement发送多条SQL语句，比如100条，1000条，甚至上万条
        执行的时候，也仅仅编译一次就可以
        这种批量执行SQL语句的方式可以大大提升性能
     */

    /**
     * 批量执行SQL语句
     *
     * @param sql
     * @param paramList
     * @return
     */
    public int[] executeBatch(String sql, List<Object[]> paramList) {
        int[] row = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = this.getConnection();

            // 第一步：使用Connection对象，取消自动提交
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);

            // 第二步：使用PrepareStatement.addBatch()方法加入批量的SQL参数
            if (null != paramList && paramList.size() > 0) {
                for (Object[] params : paramList) {
                    for (int i = 0; i < params.length; i++) {
                        ps.setObject(i + 1, params[i]);
                    }
                    ps.addBatch();
                }
            }

            // 第三步：使用PrepareStatement.executeBatch()方法，执行批量的SQL语句
            row = ps.executeBatch();

            // 第四步：使用Connection对象，提交批量的SQL语句
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != conn) {
                dataSource.push(conn);
            }
        }

        return row;
    }

    /**
     * 静态内部类：查询回调接口
     */
    public static interface QueryCallback {
        /*
            处理查询结果
         */
        void process(ResultSet rs) throws Exception;
    }
}
