package com.bigdata.sessionanalysis.dao.factory;

import com.bigdata.sessionanalysis.dao.ITaskDAO;
import com.bigdata.sessionanalysis.dao.impl.TaskDAOImpl;

/**
 * @Author Taten
 * @Description DAO工厂类
 **/
public class DAOFactory {
    public static ITaskDAO getTaskDAO() {
        return new TaskDAOImpl();
    }
}
