package com.dkha.service;

import com.dkha.dto.ScCurrenthistoryDTO;
import com.dkha.dto.ScFaceverificationDTO;
import com.dkha.dto.ScRoomcurrentEverydayDTO;
import com.dkha.dto.ScStudentsOutHistoryDTO;
import com.dkha.entity.ScCurrenthistorySumEntity;

import java.util.List;
import java.util.Map;

/**
 * Copyright(C) 2013-2020 电科惠安公司 Inc.ALL Rights Reserved.
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-08-29 10:21
 */
public interface IndexService {

    Map<String, Object> dataInfo();

    Map<String, Object> aidooreight();

    Map<String, Object> visitorrecordStatistics();

    Map<String, Object> layeredStatistics();

    Map<String, Object> dormitoryCheckInStatistics();

    List<ScStudentsOutHistoryDTO> notReturnedStatistics();

    List<ScFaceverificationDTO> dormitoryPeerRecord();

    List<Map<String, Object>> visitorInformation();

    List<ScRoomcurrentEverydayDTO> roomCurrentAlarm();

    List<Map<String, Object>> temperatureWarning();

    List<ScFaceverificationDTO> twoBuildingPeerRecord();

    Map<String, Object> uwbLabelType();

    List<ScCurrenthistorySumEntity> getByTop();

    List<Map<String, Object>> thermalTop();
}
