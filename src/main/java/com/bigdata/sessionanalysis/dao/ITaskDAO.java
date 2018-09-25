package com.bigdata.sessionanalysis.dao;

import com.bigdata.sessionanalysis.domain.Task;

/**
 * @Author Taten
 * @Description 任务管理DAO接口
 **/
public interface ITaskDAO {
    /**
     * 根据主键查询任务
     *
     * @param taskId
     * @return
     */
    Task findById(int taskId);
}
