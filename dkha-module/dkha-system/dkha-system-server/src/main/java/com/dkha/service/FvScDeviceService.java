package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.DoorAndPersonListDTO;
import com.dkha.dto.FvScDeviceDTO;
import com.dkha.dto.FvScDeviceFaceDTO;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScAidoorfivePersonlistEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * 设备表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
public interface FvScDeviceService extends BaseService<FvScDeviceEntity> {

    PageData<FvScDeviceDTO> page(Map<String, Object> params);

    /**
     * 通过设备ID获取设备信息
     * @param fId
     * @return
     */
    FvScDeviceDTO getMyOne(String fId);

    /**
     * 获取设备对应的下发人员分页数据
     * @param params
     * @return
     */
    PageData<FvScDeviceFaceDTO> pageEqToFace(Map<String, Object> params);

    /**
     * 设备对应的下发人员数据存入
     */
    void saveEqToFace(DoorAndPersonListDTO doorAndPersonListDTO,   List<ScAidoorfivePersonlistEntity> listfivePerson) throws IOException, URISyntaxException, InterruptedException;

    /**
     * 二楼设备对应的下发人员数据存入
     */
    void saveEqToFaceTwo(DoorAndPersonListDTO doorAndPersonListDTO,   List<ScAidoorfivePersonlistEntity> listfivePerson) throws IOException, URISyntaxException, InterruptedException;

    /**
     * 删除权限
     * @param serial
     * @param password
     * @param userId
     */
    boolean deletePermissionFace(String serial, String password, String userId) throws URISyntaxException;

    /**
     * 删除所有权限
     * @param serial
     * @param password
     */
    boolean deletePermissionFaceAll(String serial, String password, List<ScAidoorfivePersonlistEntity> listfivePerson) throws URISyntaxException;

    /**
     * 设置屏保
     * @param file
     */
    void setUpScree(MultipartFile file) throws URISyntaxException, IOException;

    /**
     *新增下发人员记录
     * @param personlistDTOS
     */
    void insertBatchAndUpdate(List<ScAidoorfivePersonlistEntity> personlistDTOS);

    /**
     * 远程开门
     * @param serial
     * @param password
     */
    void doorOpen(String serial, String password) throws URISyntaxException;

    /**
     * 重启设备
     * @param serial
     * @param password
     */
    boolean doorReset(String serial, String password) throws URISyntaxException;

    /**
     * 根据房间号查询相关门禁设备数据
     * @param drNum
     * @return
     */
    List<FvScDeviceEntity> getByDrNum(String drNum);

    /**
     * 人员资料新增
     * @param map
     */
    void userInsert(Map<String, Object> map);

    /**
     * 根据人脸图片地址查询库中人员资料id
     * @param result
     * @return
     */
    Integer findByFaceUrl(String result);

    /**
     * 新增人员关联表
     * @param personMap
     */
    void personInsert(Map<String, Object> personMap);

    /**
     * 删除人员资料
     * @param personId
     */
    void deleteBypersonId(Integer personId);

    /**
     * 删除通行记录
     * @param personId
     */
    void removeBypersonId(Integer personId);

    /**
     * 删除人员关联
     * @param personId
     */
    void clearBypersonId(Integer personId);

    /**
     * 接收人脸注册状态
     * @param serial
     * @param userId
     */
    String faceRegister(String serial, String userId, String status) throws URISyntaxException, InterruptedException;

    /**
     * 根据设备序列号查询相关设备
     * @param serial
     * @return
     */
    FvScDeviceEntity getBySerial(String serial);

}