package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScGatebusdeviceDTO;
import com.dkha.dto.ScTransformerroomDTO;
import com.dkha.entity.ScTransformerroomEntity;


import java.util.List;
import java.util.Map;

/**
 * 互感器宿舍关联关系
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScTransformerroomService extends BaseService<ScTransformerroomEntity> {

    PageData<ScTransformerroomDTO> page(Map<String, Object> params);

    List<ScTransformerroomDTO> list(Map<String, Object> params);

    ScTransformerroomDTO get(String id);

    void save(ScTransformerroomDTO dto);

    void update(ScTransformerroomDTO dto);

    void delete(String[] ids);

    /**
     * 根据电流互感器设备ID删除所关联的房间信息
     */
    void deleteByGateId(String tfId);

    /**
     * 根据电流互感器ID查询所关联的房间信息关联数据
     */
    List<ScTransformerroomDTO> findAllById(String id);

    /**
     * 根据传入的房间关联ID查询房间编号
     */
    ScTransformerroomEntity selectRoomById(Long drId);

    /**
     * 删除空房间
     */
    void deleteByNull();

    /**
     * 根据电流互感器查询房间
     */
    List<ScTransformerroomEntity> selectByTfId(Long tfId);
}
