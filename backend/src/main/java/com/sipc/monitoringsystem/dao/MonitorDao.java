package com.sipc.monitoringsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.sipc.monitoringsystem.model.po.Monitor.Monitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MonitorDao extends BaseMapper<Monitor> {

    @Select("UPDATE monitor SET alarm_cnt = alarm_cnt + 1 WHERE id = #{id};\n")
    void MonitorAlarmCntPlusOne(@Param("id") Integer id);

    @Select("""
            UPDATE monitor
            SET running = 1 - monitor.running
            WHERE id = #{id};
            """)
    void MonitorRunningSwitch(@Param("id") Integer id);
}
