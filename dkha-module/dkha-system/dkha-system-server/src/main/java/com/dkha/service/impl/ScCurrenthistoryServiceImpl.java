package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;

import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScCurrenthistoryDao;
import com.dkha.dto.ScCurrenthistoryDTO;
import com.dkha.entity.ScCurrenthistoryEntity;
import com.dkha.service.ScCurrenthistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScCurrenthistoryServiceImpl extends BaseServiceImpl<ScCurrenthistoryDao, ScCurrenthistoryEntity> implements ScCurrenthistoryService {

    @Override
    public List<ScCurrenthistoryDTO> getByRoomId(Long roomId, String startTime, String endTime) {

        List<ScCurrenthistoryEntity> entity = baseDao.getByRoomId(roomId,startTime,endTime);
        return ConvertUtils.sourceToTarget(entity, ScCurrenthistoryDTO.class);
    }

    @Override
    public List<ScCurrenthistoryDTO> getByTop() {
        List<ScCurrenthistoryEntity> entity =  baseDao.getByTop();
        return   ConvertUtils.sourceToTarget(entity, ScCurrenthistoryDTO.class);
    }

}
