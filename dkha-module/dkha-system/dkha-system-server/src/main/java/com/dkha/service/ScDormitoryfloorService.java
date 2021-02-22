package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScDormitoryDTO;
import com.dkha.dto.ScDormitoryfloorDTO;
import com.dkha.dto.UwbFloorDTO;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitoryfloorEntity;


import java.util.List;
import java.util.Map;

/**
 * 楼栋，楼层信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScDormitoryfloorService extends BaseService<ScDormitoryfloorEntity> {

    PageData<ScDormitoryfloorDTO> page(Map<String, Object> params);

    List<ScDormitoryfloorDTO> list(Map<String, Object> params);

    ScDormitoryfloorDTO get(Long id,Integer type);

    void save(ScDormitoryfloorDTO dto);

    void update(ScDormitoryfloorDTO dto);

    void delete(Long id,Integer type);

    List<Map<String, Object>> getTreeList(Integer dfPurpose);

    List<Map<String, Object>> roomTree(Integer dfPurpose);

    List<ScDormitoryDTO> getByFloorId(Long floorId);

    String findName(String s);

    /**
     * 获取uwb的楼层信息
     * @return Json格式字符串
     */
    List<UwbFloorDTO>  uwbbuildingtree();

    List<Map<String, Object>> roleTree();

}
