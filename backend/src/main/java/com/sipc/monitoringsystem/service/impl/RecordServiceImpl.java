package com.sipc.monitoringsystem.service.impl;

import com.sipc.monitoringsystem.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sipc.monitoringsystem.video.MonitorCommon.monitorMap;

@Service
@Slf4j
public class RecordServiceImpl implements RecordService {
    @Override
    public String getRecord( int monitorId) {
        return monitorMap.get(monitorId).getClipLink();
    }
}
