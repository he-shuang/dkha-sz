package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.GlobalValidationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

/**
 *
 * @author xiedong
 * @version v1.0
 * @date 2020-09-09 9:58
 */

@Mapper
public interface GlobalValidationDao{


    int checkUwb(@Param("id") Long id,@Param("uwb")String uwb);

    int checkRfid(@Param("id") Long id, @Param("rfid") String rfid);
}
