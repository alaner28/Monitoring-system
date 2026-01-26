package com.sipc.monitoringsystem.service;


import com.sipc.monitoringsystem.model.dto.param.Monitor.CreateMonitorParam;
import com.sipc.monitoringsystem.model.dto.param.Monitor.UpdateMonitorParam;
import com.sipc.monitoringsystem.model.po.Monitor.Monitor;

import java.util.List;

public interface MonitorService {
    List<Monitor> getMonitorList();

    Monitor getMonitorById(Integer id);

    Integer createMonitor(CreateMonitorParam createMonitorParam);

    Boolean updateMonitor(UpdateMonitorParam updateMonitorParam);

    String getMonitorIPById(Integer id);

    String getMonitorImg(Integer id);

    Boolean switchMonitor(Integer id);

    Boolean updateLeaders(String oldName,String newName);

    Boolean deleteMonitor(Integer id);
}
