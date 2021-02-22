package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScFaceverificationDTO;
import com.dkha.entity.ScFaceverificationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 刷脸或卡记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@Mapper
public interface ScFaceverificationDao extends BaseDao<ScFaceverificationEntity> {
    /**
     * 获取分页数据
     * @param params
     * @return
     */
    List<ScFaceverificationEntity> getMyList(Map<String, Object> params);

    /**
     * 获取分页总数
     * @param params
     * @return
     */
    Long getMyCount(Map<String, Object> params);

    Integer selectDayCount(@Param("date") Date date, @Param("type") int type);

    ScFaceverificationEntity getLastData(@Param("scStdid") String scStdid);

    List<ScFaceverificationDTO> getLimit();
}
