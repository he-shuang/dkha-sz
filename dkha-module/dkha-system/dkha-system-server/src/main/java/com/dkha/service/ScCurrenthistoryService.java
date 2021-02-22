package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScCurrenthistoryDTO;
import com.dkha.entity.ScCurrenthistoryEntity;
import java.util.List;
import java.util.Map;

/**
 * 电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScCurrenthistoryService extends BaseService<ScCurrenthistoryEntity> {

    List<ScCurrenthistoryDTO> getByRoomId(Long roomId, String startTime, String endTime);

    List<ScCurrenthistoryDTO> getByTop();
}
