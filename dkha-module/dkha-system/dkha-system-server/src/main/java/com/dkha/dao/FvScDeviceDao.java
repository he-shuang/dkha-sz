package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.FvScDeviceFaceDTO;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScAidoorfivePersonlistEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 设备表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Mapper
public interface FvScDeviceDao extends BaseDao<FvScDeviceEntity> {
    /**
     * 获取分页数据
     * @param params
     * @return
     */
    List<FvScDeviceEntity> getMyList(Map<String, Object> params);

    /**
     * 获取分页总数
     * @param params
     * @return
     */
    Long getMyCount(Map<String, Object> params);

    /**
     * 通过设备ID获取设备信息
     * @param fId
     * @return
     */
    FvScDeviceEntity getMyOne(@Param("fId") String fId);

    /**
     * 获取5门禁超级用户
     * @return
     */
    Map<String, Object> getSuperUser();

    /**
     * 获取5门禁相应设备对应的下发人脸数量
     * @param setIds 设备ID集合
     * @return
     */
    List<Map<String, Object>> getEqToPerNum(@Param("setIds") Set<Integer> setIds);

    /**
     * 获取设备对应的下发人员分页数据
     * @param params
     * @return
     */
    List<FvScDeviceFaceDTO> getMyListEqToFace(Map<String, Object> params);

    /**
     * 获取设备对应的下发人员分页总数
     * @param params
     * @return
     */
    Long getMyCountEqToFace(Map<String, Object> params);

    /**
     * 新增入住人员记录
     * @param personlistDTOS
     */
    void insertBatchAndUpdate(List<ScAidoorfivePersonlistEntity> personlistDTOS);


    void insertFivePersonListEntity(ScAidoorfivePersonlistEntity personentity);




    /**
     * 根据房间号查询设备MQTT系列号和密码
     * @param drNum
     * @return
     */
    List<FvScDeviceEntity> getByDrNum(String drNum);

    /**
     *人员资料新增
     * @param map
     */
    void userInsert(@Param("map") Map<String, Object> map);

    /**
     * 根据人脸图片地址查询相应人员资料ID
     * @param result
     * @return
     */
    Integer findByFaceUrl(@Param("result") String result);

    /**
     * 新增人员关联表
     * @param personMap
     */
    void personInsert(@Param("personMap") Map<String, Object> personMap);

    /**
     * 删除人员资料
     * @param personId
     */
    void deleteBypersonId(@Param("personId") Integer personId);

    /**
     * 删除通行记录
     * @param personId
     */
    void removeBypersonId(Integer personId);

    /**
     * 清除人员关联
     * @param personId
     */
    void clearBypersonId(Integer personId);

    /**
     * 根据设备序列号查询相关设备
     * @param serial
     * @return
     */
    FvScDeviceEntity getBySerial(@Param("serial")String serial);
}