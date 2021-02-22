package com.dkha.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScDormitorypersonInfoDTO;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.excel.ScDormitorypersonExcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 宿舍当前入住人员信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScDormitorypersonDao extends BaseDao<ScDormitorypersonEntity> {

    IPage<ScDormitorypersonInfoDTO> findPage(@Param("page") IPage<ScDormitorypersonEntity> page, @Param("params") Map<String, Object> params);

    List<ScDormitorypersonInfoDTO> list(@Param("params") Map<String, Object> params);

    Integer countByFloorId(@Param("floorId") String floorId,@Param("isOut") String isOut);

    /**
     * 宿舍男女统计
     * @param sex
     * @return
     */
    Integer countBySex(@Param("sex") String sex);

    /**
     * 根据房间号查询设备数据
     * @param drNum
     * @return
     */
    List<FvScDeviceEntity> getByDrNum(String drNum);

    List<ScDormitorypersonExcel> exportInfoExcel();

    /**
     * 获取字典码表中的学校名称
     * @param scSchool
     * @return
     */
    String getDictBySchool(@Param("scSchool")String scSchool);

    String getDrNumBySn(@Param("scNo") String scNo);
}
