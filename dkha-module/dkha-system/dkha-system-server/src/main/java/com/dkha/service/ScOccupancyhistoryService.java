package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScOccupancyhistoryDTO;
import com.dkha.entity.ScOccupancyhistoryEntity;



import java.util.List;
import java.util.Map;

/**
 * 某房间的入住历史记录
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScOccupancyhistoryService extends BaseService<ScOccupancyhistoryEntity> {

    PageData<ScOccupancyhistoryDTO> page(Map<String, Object> params);

    List<ScOccupancyhistoryDTO> list(Map<String, Object> params);

    ScOccupancyhistoryDTO get(String id);

    void save(ScOccupancyhistoryDTO dto);

    void update(ScOccupancyhistoryDTO dto);

    void delete(String[] ids);
}
