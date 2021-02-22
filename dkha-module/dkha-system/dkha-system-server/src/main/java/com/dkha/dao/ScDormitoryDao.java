package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScDormitoryDTO;
import com.dkha.entity.ScDormitoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 房间及房间状态信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScDormitoryDao extends BaseDao<ScDormitoryEntity> {

    ScDormitoryEntity getById(Long id);

    ScDormitoryEntity getByScNum(String scNum);

    List<ScDormitoryEntity> getAll();
}
