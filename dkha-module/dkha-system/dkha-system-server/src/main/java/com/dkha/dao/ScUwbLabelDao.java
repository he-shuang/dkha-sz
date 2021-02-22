package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScUwbLabelToInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 获取UWB标签绑定的信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScUwbLabelDao extends BaseDao<ScUwbLabelToInfoDTO> {

    /**
     * 获取标签数据
     * @return
     */
    List<ScUwbLabelToInfoDTO> getMyList();

    /**
     * 获取UWB人员角色编号
     * @return
     */
    List<Map<String,Object>> getUwbPerRole();
}
