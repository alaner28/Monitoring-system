package com.sipc.monitoringsystem.video;

import com.sipc.monitoringsystem.util.RecordUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class MonitorCommon {

    /**
     *k:monitorId v:RecordUtil
     */
    public static final Map<Integer, RecordUtil> monitorMap = new HashMap<>();

}
