package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.SysDictDataDTO;
import com.dkha.entity.DictData;
import com.dkha.entity.SysDictDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据
 */
@Mapper
public interface SysDictDataDao extends BaseDao<SysDictDataEntity> {

    /**
     * 字典数据列表
     */
    List<DictData> getDictDataList();

    List<SysDictDataDTO> getByType(@Param("type") String type);
}
