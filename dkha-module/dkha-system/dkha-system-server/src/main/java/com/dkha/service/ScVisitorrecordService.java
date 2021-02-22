package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScUwbLabelToInfoDTO;
import com.dkha.dto.ScVisitorrecordDTO;
import com.dkha.entity.ScVisitorrecordEntity;


import java.util.List;
import java.util.Map;

/**
 * 访客记录表
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScVisitorrecordService extends BaseService<ScVisitorrecordEntity> {

    PageData<ScVisitorrecordDTO> page(Map<String, Object> params);

    List<ScVisitorrecordDTO> list(Map<String, Object> params);

    ScVisitorrecordDTO get(String id);

    void save(ScVisitorrecordDTO dto);

    void update(ScVisitorrecordDTO dto);

    void delete(String[] ids);

    /**
     * 访客还卡
     * @param vrId 访客记录ID
     * @param userId 操作人ID
     */
    void returnCard(String vrId, Long userId);

    /**
     * 获取同一楼层未还卡uwbId
     * @param dfFloorid 楼层ID
     */
    List<Long> getListUwbId(Long dfFloorid);

    /**
     * 访客记录历史轨迹访客信息
     * @param vrId 访客记录ID
     */
    ScUwbLabelToInfoDTO getMyScUwbLabelToInfo(String vrId);
}
