package com.bigdata.sessionanalysis.dao.impl;

import com.bigdata.sessionanalysis.dao.ITaskDAO;
import com.bigdata.sessionanalysis.domain.Task;
import com.bigdata.sessionanalysis.jdbc.JDBCHelper;

import java.sql.ResultSet;

/**
 * @Author Taten
 * @Description 任务管理DAO实现类
 **/
public class TaskDAOImpl implements ITaskDAO {
    @Override
    public Task findById(int taskId) {
        Task task = new Task();

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();

        Object[] params = new Object[]{
                taskId
        };

        String sql = "select * from task where task_id = ?";

        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {
            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    task.setTaskId(rs.getInt(1));
                    task.setTaskName(rs.getString(2));
                    task.setCreateTime(rs.getString(3));
                    task.setStartTime(rs.getString(4));
                    task.setFinishTime(rs.getString(5));
                    task.setTaskType(rs.getString(6));
                    task.setTaskStatus(rs.getString(7));
                    task.setTaskParam(rs.getString(8));
                }
            }
        });

        return task;
    }
}
