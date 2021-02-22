package com.dkha.service;

import com.alibaba.fastjson.JSONObject;
import com.dkha.aidoor.TransPictureResult;
import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.AidooreightPassword;
import com.dkha.dto.FaceListDataToDoorDTO;
import com.dkha.dto.ScAidooreightDTO;
import com.dkha.dto.doorcontrol.AddPersonRequestInfo;
import com.dkha.dto.doorcontrol.DeviceFingerPrint;
import com.dkha.dto.doorcontrol.DeviceSetingInfo;
import com.dkha.dto.doorcontrol.PackageAuthorityDto;
import com.dkha.entity.ScAidooreightEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 智能设备主要分为：8英寸智能门禁设备
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-14
 */
public interface ScAidooreightService extends BaseService<ScAidooreightEntity> {

    PageData<ScAidooreightDTO> page(Map<String, Object> params);

    List<ScAidooreightDTO> list(Map<String, Object> params);

    ScAidooreightDTO get(String id);

    void save(ScAidooreightDTO dto);

    void update(ScAidooreightDTO dto);

    void delete(String[] ids);

    void importInfoExcel(MultipartFile file);

    /**
     * 获取未停用的设备列表
     * @return
     */
    List<ScAidooreightDTO> listNoStopDevice();

    /**
     * 1) 获取设备端指纹信息
     * 提供设备指纹信息获取功能，当只知道设备端IP和端口时，可通过此接口获取设备指纹信息。
     * @Parame aeid  ScAidooreightEntity中的主键
     */
    DeviceFingerPrint getFingerPrintInfo(String aeid);

    /**
     * 2) 与设备端建立连接
     * 提供设备连接功能，是所有接口的先决接口，在调用其它接口前，需要先成功调用此接口。本系统支持事件回调，要使用回调功能，需要正确设置服务器的ip和端口号。
     * @Parame aeid  ScAidooreightEntity中的主键
     * @return
     */
    boolean connetDoorConnet(String aeid);

    /**
     * 6) 添加单个人员至设备端
     *  用于将管理端人员信息推送到设备端，设备端得到人员信息后会对人员图片进行人脸特征提取，并在本地进行人员识别；
     *  如果接口返回“704”错误时，请检查各个参数是否符合要求；
     * @param personAdd 需要添加的人员信息
     * @param aeid ScAidooreightEntity中的主键
     * @return
     */
    boolean addPersonToDeivce(String aeid, AddPersonRequestInfo personAdd);

    /**
     *  8) 删除设备端人员
     *     删除设备端人员信息和人脸信息
     * @param personSerial  人员编号
     * @return
     */

    boolean deletePersonFromDeviceBySerial(String aeid,String personSerial);

    /**
     * 13) 重置设备端
     *  重置设备端的设置参数及数据库数据。重置后设备端参数及数据库为首次连接以后的状态。
     */
    boolean cleanDeviceData(String aeid);

    /**
     * 14) 重启设备端
     * 重启设备端（设备端必须有Root权限）
     */
    boolean rebootDevice(String aeid);

    /**
     *  15) 给所有的设备下发图片信息
     * @return
     */
    List<TransPictureResult> transAllPictureToAllDevice();

    /**
     * 16）给指定的设备下发所有学生和教职工图片
     * @param aeid
     * @return
     */
    TransPictureResult transAllPictureToDevice(String aeid);

    /**
     * 18) 断开指定连接设备
     * @param aeid
     * @return
     */
    boolean disconnectToDevice(String aeid);

    /**
     * 18-2） 断开所有的连接
     * @return
     */
    boolean disconnectAllDevice();
    /**
     * 19） 设置通行权限，通行有效日期，通行的时间段：默认为00-23:59
     * @param scAidooreightEntity
     * @param userid  用户ID
     * @param days   从今日开始的有效天数
     * @param workingdays  1、用数字1-7表示周一到周日，并用英文逗号分隔；如“1,3,5,6”表示权限只在周一、周三、周五和周六生效，在周二、周四和周日时不生效；
     *                     2、值为null（空对象）时，表示每天都生效；
     *                     3、值为""（英文双引号）时， 表示权限禁止；4、值为“1,2,3,4,5”时，表示工作日
     * @return
     */
     boolean setDoorAuthority(ScAidooreightEntity scAidooreightEntity, String userid, int days,String workingdays);

    /**
     * 远程开门
     * @param aeid
     * @return
     */
     boolean openRemoteDoor(String aeid);

    Boolean transFaceListToAiDoor(FaceListDataToDoorDTO listDataToDoorDTO);

    /**
     * 获取某设备的配置信息
     * @param aeid 设备主键ID
     * @return
     */
    JSONObject getDoorSetting(String aeid);

    /**
     * 设置某设备的配置信息
     * @param deviceSetingInfo 配置实体
     * @return
     */
   boolean setDoorSetting(DeviceSetingInfo deviceSetingInfo);

    List<ScAidooreightDTO> getAll();

    List<ScAidooreightDTO> getAllByType(String type);

    /**
     * 判断某设备的Apk版本是否合法
     * @param packageAuthority
     * @return
     */
    boolean checkPackageAuthority(PackageAuthorityDto packageAuthority);

    /**
     * 更新所有设备的APK
     * @return
     */
     int  updateAllDeviceApk();

    /**
     * 更新指定设备列表的APK
     * @param ids
     * @return
     */
    int  uploadNewVersionAPKfileOfIds(String []ids);

    /**
     * 设置所有8寸门禁机的时间
     */
     void updateAllMachineTime() ;
    /**
     * 设置指定门禁进行更新时间
     */
    void updateMachineTimeBy(ScAidooreightDTO dto);

    void batchUpatePws(AidooreightPassword aidooreightPassword);
}