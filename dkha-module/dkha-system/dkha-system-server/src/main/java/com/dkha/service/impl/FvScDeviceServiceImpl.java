package com.dkha.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.aidoor.request.FiveRecord;
import com.dkha.aidoor.request.FiveRecordList;
import com.dkha.commons.dynamic.datasource.annotation.DataSource;
import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.http.RestTemplateUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.rabbitmq.MQQueueNameConfig;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.FvScDeviceDao;
import com.dkha.dao.ScAidoorfivePersonlistDao;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dao.ScStudentsDao;
import com.dkha.dto.*;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScAidoorfivePersonlistEntity;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.enums.AlarmTypeEnum;
import com.dkha.model.aidoor.response.RespBase;
import com.dkha.mq.FiveDoor.FaceRegisterDataSender;
import com.dkha.service.FvScDeviceService;
import com.google.common.primitives.Doubles;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 设备表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Service
//5寸门禁多数据源配置 深圳使用
@DataSource("slave2")
public class FvScDeviceServiceImpl extends BaseServiceImpl<FvScDeviceDao, FvScDeviceEntity> implements FvScDeviceService {
    private static final Logger log = LoggerFactory.getLogger(FvScDeviceServiceImpl.class);
    //5寸门禁多数据源配置 深圳使用
    @Value("${fivedoor.url}")
    private String fivedoorUrlIPAndPort;
    //minio地址
    @Value("${minio.url}")
    private String minioUrl;

    @Autowired
    MinioUtil minioUtil;

    @Autowired
    private FvScDeviceService fvScDeviceService;

    @Autowired
    private FvScDeviceDao fvScDeviceDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ScDormitoryDao scDormitoryDao;

    @Autowired
    private ScStudentsDao scStudentsDao;

    @Autowired
    private ScAidoorfivePersonlistDao scAidoorfivePersonlistDao;

    @Autowired
    private FaceRegisterDataSender faceRegisterDataSender;


    private String MQTTAPI = "/open/mqtt/api.php";
    private String STOREAPI = "/open/store/api.php";
    private String ONLINE_DEVICE = "data.user.onlinedevices.get";
    private String FACE_REGISTER = "mqtt.face.batch.register";
    private String DATAKEY_ADD = "data.key.add";
    private String MQTTKEY_UPDATE = "mqtt.key.update";
    private String DATAKEY_DELETE = "data.key.delete";
    private String MQTTKEY_CLEAR = "mqtt.key.clear";
    private String MQTT_SCREENSAVE = "mqtt.screensave.set";
    private String MQTTDOOR_OPEN = "mqtt.door.open";
    private String MQTTDOOR_RESET = "mqtt.device.reset";
    private String MQTTKEY_GET = "/open/mqtt/api.php?method=data.keys.get";
    private String MQTTSTATE_GET = "/open/mqtt/api.php?method=mqtt.device.state.get";

    //  private String mqttUrl = "http://192.168.8.55/open/mqtt/api.php";

    @Override
    @DataSource()
    public PageData<FvScDeviceDTO> page(Map<String, Object> params) {
        // 转换成like
        paramsToLike(params, "fName");
        paramsToLike(params, "fserial");
        // 分页
        int inowpage = Integer.parseInt((String) params.get("page"));
        int ipagesize = Integer.parseInt((String) params.get("limit"));
        int scol = ipagesize * (inowpage - 1);
        params.put("scol", scol);
        params.put("ipagesize", ipagesize);
        // 查询
        List<FvScDeviceEntity> list = baseDao.getMyList(params);
        //总数
        long total = 0;
        total = baseDao.getMyCount(params);

        // 获取5门禁下发人数
        Set<Integer> mySet2 = new HashSet<>();
        for (FvScDeviceEntity entity : list) {
            mySet2.add(entity.getFId());
        }
        for (FvScDeviceEntity entity : list) {
            //调用获取权限的接口
            String addStr = "serial=" + entity.getFSerial()
                    + "&password=" + entity.getFPassword()
                    + "&pageIndex=" + "1"
                    + "&pageSize=" + "1";
            URI ur = null;
            try {
                ur = new URI(fivedoorUrlIPAndPort + MQTTKEY_GET);
            } catch (Exception e) {

            }
            RequestEntity addRequestEntity = RequestEntity
                    .post(ur)
                    .headers(getHttpHeaders(entity.getFSerial(), entity.getFPassword()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(addStr);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(addRequestEntity, String.class);
                String result = responseEntity.getBody();
                entity.setNumberPer(0);
                String[] splitstr = result.split("\n");
                if (splitstr != null && splitstr.length > 0) {

                    for (String str : splitstr) {
                        String pattern = "^\\{.*\\}$";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(str);
                        if (m.matches()) {
                            RespBase respBase = new Gson().fromJson(str, RespBase.class);
                            if (0 == respBase.getCode()) {
                                LinkedTreeMap s = (LinkedTreeMap) respBase.getData();

                                Double numberPer = (Double) s.get("recordCount");

                                entity.setNumberPer((int) (numberPer.doubleValue()));
                                entity.setFFaceTotal(entity.getNumberPer());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //调用获取权限的接口
            String stateStr = "serial=" + entity.getFSerial()
                    + "&password=" + entity.getFPassword()
                    + "&userId=" + "1";
            URI stateUrl = null;
            try {
                stateUrl = new URI(fivedoorUrlIPAndPort + MQTTSTATE_GET);
            } catch (Exception e) {

            }
            RequestEntity stateRequestEntity = RequestEntity
                    .post(stateUrl)
                    .headers(getHttpHeaders(entity.getFSerial(), entity.getFPassword()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(stateStr);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(stateRequestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if (0 == respBase.getCode()) {
                    String data = respBase.getData().toString();
                    //根据设备在线情况接口判断设备是否在线并且将具体数据在数据库进行更新
                    if (data.contains("true")) {
                        entity.setFState(0);
                        baseDao.updateById(entity);
                    } else {
                        entity.setFState(-1);
                        baseDao.updateById(entity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 设备下发人脸数
//            Object numberPer = eqToPerNumMap.get(entity.getFId());
//            if (numberPer == null) {
//                entity.setNumberPer(0);
//            } else {
//                entity.setNumberPer(Integer.valueOf(String.valueOf(numberPer)));
//            }
            entity.setFAddDate(new Date(entity.getFAddTime() * 1000L));
        }

        return getPageData(list, total, FvScDeviceDTO.class);
    }

    @Override
    public FvScDeviceDTO getMyOne(String fId) {
        FvScDeviceEntity entity = baseDao.getMyOne(fId);

        entity.setFAddDate(new Date(entity.getFAddTime() * 1000L));
        return ConvertUtils.sourceToTarget(entity, FvScDeviceDTO.class);
    }

    @Override
    public PageData<FvScDeviceFaceDTO> pageEqToFace(Map<String, Object> params) {
        // 分页
        int inowpage = Integer.parseInt((String) params.get("page"));
        int ipagesize = Integer.parseInt((String) params.get("limit"));
        int scol = ipagesize * (inowpage - 1);
        params.put("scol", scol);
        params.put("ipagesize", ipagesize);
        // 查询
        List<FvScDeviceFaceDTO> list = baseDao.getMyListEqToFace(params);
        for (FvScDeviceFaceDTO dto : list) {
            dto.setFacedownDate(new Date(dto.getFacedowntime() * 1000L));
        }
        long total = baseDao.getMyCountEqToFace(params);
        return new PageData<>(list, total);
    }

    private HttpHeaders getHttpHeaders(String serial, String password) {
        String str = serial + ":" + password;
        String encode = new BASE64Encoder().encode(str.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encode + "");
        httpHeaders.add("User-Agent", "PostmanRuntime/7.26.5");
        return httpHeaders;
    }

    @Override
    @DataSource()
    public void saveEqToFace(DoorAndPersonListDTO doorAndPersonListDTO, List<ScAidoorfivePersonlistEntity> listfivePerson) throws IOException, URISyntaxException, InterruptedException {
        //房间设备的信息列表
        List<AidoorfivePassword> fiveDoors = doorAndPersonListDTO.getFiveDoors();
        //下发人员信息
        List<String> ids = doorAndPersonListDTO.getIds();
        //根据学生ID列表获取学生列表和房间信息
        List<Map<Object, Object>> studentsList = scStudentsDao.selectDoorByIds(ids);

        for (AidoorfivePassword fiveDoor : fiveDoors) {
            for (Map<Object, Object> objectStringMap : studentsList) {
                //人员下发
                if (fiveDoor.getFName().equals(objectStringMap.get("dr_num"))) {
                    String userIdByTenHex = getEightLengthUserID().toUpperCase();
                    /*添加人脸照片到存储端*/

                    String userIdHex = PutFaceToDoorDevice(objectStringMap, fiveDoor, userIdByTenHex);
                    if (userIdHex != null && userIdHex.length() > 0) {
                        //门禁设备下发记录赋值
                        ScAidoorfivePersonlistEntity scAidoorfivePersonlistEntity = new ScAidoorfivePersonlistEntity();
                        scAidoorfivePersonlistEntity.setSerial(fiveDoor.getFSerial());
                        scAidoorfivePersonlistEntity.setPassword(fiveDoor.getFPassword());
                        scAidoorfivePersonlistEntity.setPhotoimg(objectStringMap.get("sc_headphotoimg").toString());
                        scAidoorfivePersonlistEntity.setUsername(objectStringMap.get("sc_stuname").toString());
                        scAidoorfivePersonlistEntity.setUserId(objectStringMap.get("sc_stdid").toString());
                        scAidoorfivePersonlistEntity.setImgId(userIdHex);
                        scAidoorfivePersonlistEntity.setUserno(objectStringMap.get("sc_no").toString());
                        scAidoorfivePersonlistEntity.setSex(Integer.valueOf(objectStringMap.get("sc_sex").toString()));
                        scAidoorfivePersonlistEntity.setUpdateDate(new Date());
                        scAidoorfivePersonlistEntity.setDownfaceTime(new Date());
                        scAidoorfivePersonlistEntity.setStatus(-1);
                        scAidoorfivePersonlistEntity.setDrNum(objectStringMap.get("dr_num").toString());
                        scAidoorfivePersonlistEntity.setStatusNote("正在上传人脸照片到存储");
                        scAidoorfivePersonlistDao.insert(scAidoorfivePersonlistEntity);
                        //传入人脸注册参数
                        JSONObject faceJsonObject = new JSONObject();
                        faceJsonObject.put("fiveDoor", fiveDoor);
                        faceJsonObject.put("userId", userIdHex);
                        faceJsonObject.put("sctdid", objectStringMap.get("sc_stdid").toString());
                        //将设备序列号和设备密码以及人脸ID放进消息队列
                        // faceRegisterDataSender.pushFaceRegisterDataToQueue(faceJsonObject.toString());
                        faceRegisterAfterPromiss(fiveDoor, objectStringMap.get("sc_stdid").toString(), userIdByTenHex);

                    } else {

                        throw new RenException("学生：" + objectStringMap.get("sc_stuname").toString() + " 图片上传设备端失败！");
                    }
                }
            }
        }
    }

    @Override
    @DataSource()
    public void saveEqToFaceTwo(DoorAndPersonListDTO doorAndPersonListDTO, List<ScAidoorfivePersonlistEntity> listfivePerson) throws IOException, URISyntaxException, InterruptedException {

        List<Map<Object, Object>> persons = new ArrayList<>();

        //房间设备的信息列表
        List<AidoorfivePassword> fiveDoors = doorAndPersonListDTO.getFiveDoors();
        //下发人员信息
        List<String> ids = doorAndPersonListDTO.getIds();

        //根据学生ID列表获取学生列表和房间信息
        List<Map<Object, Object>> studentsList = scStudentsDao.selectDoorByIds(ids);

        for (Map<Object, Object> map : studentsList) {
            Map<Object, Object> person = new HashMap<>();
            person.put("userId", map.get("sc_stdid").toString());
            person.put("sc_headphotoimg", map.get("sc_headphotoimg").toString());
            person.put("userName", map.get("sc_stuname").toString());
            person.put("userno", map.get("sc_no").toString());
            person.put("sex", map.get("sc_sex").toString());
            persons.add(person);
        }

        List<Map<Object, Object>> wokersList = scStudentsDao.selectWorkerByIds(ids);
        for (Map<Object, Object> map : wokersList) {
            Map<Object, Object> person = new HashMap<>();
            person.put("userId", map.get("sc_waid").toString());
            person.put("sc_headphotoimg", map.get("sc_photoimg").toString());
            person.put("userName", map.get("sc_waname").toString());
            person.put("userno", map.get("sc_empno").toString());
            person.put("sex", map.get("sc_wasex").toString());
            persons.add(person);
        }

        for (AidoorfivePassword fiveDoor : fiveDoors) {

            //调用获取权限的接口，查询人脸下发数量，判断是否超出人脸最大上限
            String addStr = "serial=" + fiveDoor.getFSerial()
                    + "&password=" + fiveDoor.getFPassword()
                    + "&pageIndex=" + "1"
                    + "&pageSize=" + "1";
            URI ur = null;
            try {
                ur = new URI(fivedoorUrlIPAndPort + MQTTKEY_GET);
            } catch (Exception e) {

            }
            RequestEntity addRequestEntity = RequestEntity
                    .post(ur)
                    .headers(getHttpHeaders(fiveDoor.getFSerial(), fiveDoor.getFPassword()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(addStr);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(addRequestEntity, String.class);
                String result = responseEntity.getBody();
                String[] splitstr = result.split("\n");
                if (splitstr != null && splitstr.length > 0) {
                    for (String str : splitstr) {
                        String pattern = "^\\{.*\\}$";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(str);
                        if (m.matches()) {
                            RespBase respBase = new Gson().fromJson(str, RespBase.class);
                            if (0 == respBase.getCode()) {
                                LinkedTreeMap s = (LinkedTreeMap) respBase.getData();

                                Double numberPer = (Double) s.get("recordCount");
                                if ((int) (numberPer.doubleValue())>=1000){
                                    throw new RenException("门禁设备：" + fiveDoor.getFName() + " 人脸权限超出1000张容量，无法再次下发！");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RenException("设备连接失败，疑似设备异常，请检查");
            }

            for (Map<Object, Object> objectStringMap : persons) {
                //首先判断记录当中是否存在人脸
                QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", objectStringMap.get("userId").toString());
                queryWrapper.eq("serial", fiveDoor.getFSerial());
                queryWrapper.eq("password", fiveDoor.getFPassword());
                List<ScAidoorfivePersonlistEntity> list = scAidoorfivePersonlistDao.selectList(queryWrapper);
                if (list.size() == 0) {
                    //人员下发
                    String userIdByTenHex = getEightLengthUserID().toUpperCase();
                    /*添加人脸照片到存储端*/
                    String userIdHex = PutFaceToDoorDevice(objectStringMap, fiveDoor, userIdByTenHex);
                    if (userIdHex != null && userIdHex.length() > 0) {
                        //门禁设备下发记录赋值
                        ScAidoorfivePersonlistEntity scAidoorfivePersonlistEntity = new ScAidoorfivePersonlistEntity();
                        scAidoorfivePersonlistEntity.setSerial(fiveDoor.getFSerial());
                        scAidoorfivePersonlistEntity.setPassword(fiveDoor.getFPassword());
                        scAidoorfivePersonlistEntity.setPhotoimg(objectStringMap.get("sc_headphotoimg").toString());
                        scAidoorfivePersonlistEntity.setUsername(objectStringMap.get("userName").toString());
                        scAidoorfivePersonlistEntity.setUserId(objectStringMap.get("userId").toString());
                        scAidoorfivePersonlistEntity.setImgId(userIdHex);
                        scAidoorfivePersonlistEntity.setUserno(objectStringMap.get("userno").toString());
                        scAidoorfivePersonlistEntity.setSex(Integer.valueOf(objectStringMap.get("sex").toString()));
                        scAidoorfivePersonlistEntity.setUpdateDate(new Date());
                        scAidoorfivePersonlistEntity.setDownfaceTime(new Date());
                        scAidoorfivePersonlistEntity.setStatus(-1);
                        scAidoorfivePersonlistEntity.setDrNum(fiveDoor.getFName());
                        scAidoorfivePersonlistEntity.setStatusNote("正在上传人脸照片到存储");
                        scAidoorfivePersonlistDao.insert(scAidoorfivePersonlistEntity);
                        //传入人脸注册参数
                        JSONObject faceJsonObject = new JSONObject();
                        faceJsonObject.put("fiveDoor", fiveDoor);
                        faceJsonObject.put("userId", userIdHex);
                        faceJsonObject.put("sctdid", objectStringMap.get("userId").toString());
                        //将设备序列号和设备密码以及人脸ID放进消息队列
                        // faceRegisterDataSender.pushFaceRegisterDataToQueue(faceJsonObject.toString());
                        faceRegisterAfterPromiss(fiveDoor, objectStringMap.get("userId").toString(), userIdByTenHex);
                    } else {
                        throw new RenException("人员：" + objectStringMap.get("userName").toString() + " 图片上传设备端失败！");
                    }
                } else {
                    //先删除再下发
                    deletePermissionFace(fiveDoor.getFSerial(),fiveDoor.getFPassword(),list.get(0).getImgId());
                    scAidoorfivePersonlistDao.delete(queryWrapper);
                    //人员下发
                    String userIdByTenHex = getEightLengthUserID().toUpperCase();
                    /*添加人脸照片到存储端*/
                    String userIdHex = PutFaceToDoorDevice(objectStringMap, fiveDoor, userIdByTenHex);
                    if (userIdHex != null && userIdHex.length() > 0) {
                        //门禁设备下发记录赋值
                        ScAidoorfivePersonlistEntity scAidoorfivePersonlistEntity = new ScAidoorfivePersonlistEntity();
                        scAidoorfivePersonlistEntity.setSerial(fiveDoor.getFSerial());
                        scAidoorfivePersonlistEntity.setPassword(fiveDoor.getFPassword());
                        scAidoorfivePersonlistEntity.setPhotoimg(objectStringMap.get("sc_headphotoimg").toString());
                        scAidoorfivePersonlistEntity.setUsername(objectStringMap.get("userName").toString());
                        scAidoorfivePersonlistEntity.setUserId(objectStringMap.get("userId").toString());
                        scAidoorfivePersonlistEntity.setImgId(userIdHex);
                        scAidoorfivePersonlistEntity.setUserno(objectStringMap.get("userno").toString());
                        scAidoorfivePersonlistEntity.setSex(Integer.valueOf(objectStringMap.get("sex").toString()));
                        scAidoorfivePersonlistEntity.setUpdateDate(new Date());
                        scAidoorfivePersonlistEntity.setDownfaceTime(new Date());
                        scAidoorfivePersonlistEntity.setStatus(-1);
                        scAidoorfivePersonlistEntity.setDrNum(fiveDoor.getFName());
                        scAidoorfivePersonlistEntity.setStatusNote("正在上传人脸照片到存储");
                        scAidoorfivePersonlistDao.insert(scAidoorfivePersonlistEntity);
                        //传入人脸注册参数
                        JSONObject faceJsonObject = new JSONObject();
                        faceJsonObject.put("fiveDoor", fiveDoor);
                        faceJsonObject.put("userId", userIdHex);
                        faceJsonObject.put("sctdid", objectStringMap.get("userId").toString());
                        //将设备序列号和设备密码以及人脸ID放进消息队列
                        // faceRegisterDataSender.pushFaceRegisterDataToQueue(faceJsonObject.toString());
                        faceRegisterAfterPromiss(fiveDoor, objectStringMap.get("userId").toString(), userIdByTenHex);
                    } else {
                        throw new RenException("人员：" + objectStringMap.get("userName").toString() + " 图片上传设备端失败！");
                    }
                }
            }
        }

    }

    /**
     * 2020-10-29 -16:51 注册权限
     *
     * @param fiveDoor
     * @param sctdid
     * @param userIdHex
     */
    private void faceRegisterAfterPromiss(AidoorfivePassword fiveDoor, String sctdid, String userIdHex) {

        QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("serial", fiveDoor.getFSerial());
        queryWrapper.eq("password", fiveDoor.getFPassword());
        queryWrapper.eq("user_id", sctdid);
        List<ScAidoorfivePersonlistEntity> listaidoor = scAidoorfivePersonlistDao.selectList(queryWrapper);
        ScAidoorfivePersonlistEntity aidoorentity = null;
        if (listaidoor != null) {
            aidoorentity = listaidoor.get(0);
        }
        //1. 批量注册
        int result = exRegisterPermissonDoor(fiveDoor, userIdHex);
        try {
            if (result == -1) {
                if (aidoorentity != null) {
                    aidoorentity.setCompleteTime(new Date());
                    aidoorentity.setStatus(0);
                    aidoorentity.setStatusNote("批量注册人脸失败");
                }
                scAidoorfivePersonlistDao.updateById(aidoorentity);
                return;
            } else if (result == 1) {
                if (aidoorentity != null) {
                    aidoorentity.setCompleteTime(new Date());
                    aidoorentity.setStatus(0);
                    aidoorentity.setStatusNote("照片规格不支持，请重新导入学生照片！");
                }
                scAidoorfivePersonlistDao.updateById(aidoorentity);
                return;
            } else if (result == 1017) {
                if (aidoorentity != null) {
                    aidoorentity.setCompleteTime(new Date());
                    aidoorentity.setStatus(0);
                    aidoorentity.setStatusNote("上轮批量注册还在进行中");
                }
                scAidoorfivePersonlistDao.updateById(aidoorentity);
                return;
            } else if (result == 1019) {
                if (aidoorentity != null) {
                    aidoorentity.setCompleteTime(new Date());
                    aidoorentity.setStatus(0);
                    aidoorentity.setStatusNote("照片识别失败，人脸注册失败");
                }
                scAidoorfivePersonlistDao.updateById(aidoorentity);
                return;
            }
            Thread.sleep(8000);
        } catch (InterruptedException inex) {
            log.error("5寸门禁批量注册异常" + inex.getMessage());
        }
        /*添加人脸权限*/
        boolean addface = AddUserFacePermission(fiveDoor, userIdHex);
        if (addface) {
            /*更新人脸权限*/
            boolean updatePermision = UpdatePermissonDoor(fiveDoor, userIdHex);
            if (updatePermision) {
                if (aidoorentity != null) {
                    aidoorentity.setCompleteTime(new Date());
                    aidoorentity.setStatus(1);
                    aidoorentity.setStatusNote("人脸下发成功");
                }
            } else {
                if (aidoorentity != null) {
                    aidoorentity.setCompleteTime(new Date());
                    aidoorentity.setStatus(0);
                    aidoorentity.setStatusNote("更新权限失败");
                }
            }
            scAidoorfivePersonlistDao.updateById(aidoorentity);
        } else {
            if (aidoorentity != null) {
                aidoorentity.setCompleteTime(new Date());
                aidoorentity.setStatus(0);
                aidoorentity.setStatusNote("添加权限失败");
                scAidoorfivePersonlistDao.updateById(aidoorentity);
            }
        }
    }


    @Override
    public boolean deletePermissionFace(String serial, String password, String userId) throws URISyntaxException {
        /*1. 删除人脸*/
        String url = fivedoorUrlIPAndPort + STOREAPI;
        String variable = "?/face/" + userId + ".jpg";
        URI deletefaceUri = new URI(url + variable);

        RequestEntity deleteRequestEntity = RequestEntity
                .delete(deletefaceUri)
                .headers(getHttpHeaders(serial, password))
                .build();
        try {
            ResponseEntity<String> deleteResponseEntity = restTemplate.exchange(deleteRequestEntity, String.class);
            String deleteResult = deleteResponseEntity.getBody();

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*2.删除人脸权限*/
        String deleteUrl = "?method=" + DATAKEY_DELETE;
        String deleteStr = "serial=" + serial
                + "&password=" + password
                + "&keyType=" + "8"
                + "&keyCount=" + "1"
                + "&keyId=" + userId;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        try{
            JSONObject post = RestTemplateUtils.post(fivedoorUrlIPAndPort + MQTTAPI + deleteUrl, httpHeaders, deleteStr, JSONObject.class);
        }catch (Exception e){
            throw new RenException("存在相关人脸，所存在人脸权限删除失败！");
        }

        /*更新人脸权限*/
        AidoorfivePassword aidoorfivePassword = new AidoorfivePassword();
        aidoorfivePassword.setFSerial(serial);
        aidoorfivePassword.setFPassword(password);
        boolean updatePermision = UpdatePermissonDoor(aidoorfivePassword, userId);

        if (updatePermision) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePermissionFaceAll(String serial, String password, List<ScAidoorfivePersonlistEntity> listfivePerson) throws URISyntaxException {
        /*1. 循环删除人脸权限*/
        if (listfivePerson != null && listfivePerson.size() > 0) {
            for (ScAidoorfivePersonlistEntity spe : listfivePerson) {
                /*1. 删除人脸*/
                String url = fivedoorUrlIPAndPort + STOREAPI;
                String variable = "?/face/" + spe.getImgId() + ".jpg";
                URI deletefaceUri = new URI(url + variable);

                RequestEntity deleteRequestEntity = RequestEntity
                        .delete(deletefaceUri)
                        .headers(getHttpHeaders(serial, password))

                        .build();

                //  JSONObject jsondelete = RestTemplateUtils.delete(url + variable, getHttpHeaders(serial, password), JSONObject.class);
                try {
                    ResponseEntity<String> deleteResponseEntity = restTemplate.exchange(deleteRequestEntity, String.class);
                    String deleteResult = deleteResponseEntity.getBody();
                    if (false) {
                        throw new RenException("学生:" + spe.getUsername() + "人脸权限删除失败");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RenException("设备连接失败，疑似设备异常，请检查");
                }
            }
        }

        /*2. 删除人脸权限*/
        String deleteUrl = "?method=" + DATAKEY_DELETE;
        String deleteStr = "serial=" + serial
                + "&password=" + password
                + "&keyType=" + "0"
                + "&keyCount=" + "1"
                + "&keyId=" + "0";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        JSONObject post = RestTemplateUtils.post(fivedoorUrlIPAndPort + MQTTAPI + deleteUrl, httpHeaders, deleteStr, JSONObject.class);
        Object codeobj = post.get("code");
        if (codeobj != null && codeobj.toString().equalsIgnoreCase("0")) {

            /*3. 人脸权限清空*/
            String clearUrl = "?method=" + MQTTKEY_CLEAR;
            String clearStr = "serial=" + serial
                    + "&password=" + password + "&userId=" + "1";

            URI clearUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + clearUrl);
            RequestEntity updateRequestEntity = RequestEntity
                    .post(clearUri)
                    .headers(getHttpHeaders(serial, password))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(clearStr);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(updateRequestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if (0 == respBase.getCode()) {
                    return true;
                }
                return false;

            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            return false;
        }

    }

    @Override
    @DataSource()
    public void setUpScree(MultipartFile file) throws URISyntaxException, IOException {

        //获取所有宿舍房间
        List<ScDormitoryEntity> roomAll = scDormitoryDao.getAll();
        for (ScDormitoryEntity dormitoryEntity : roomAll) {

            List<FvScDeviceEntity> byDrNum = fvScDeviceService.getByDrNum(dormitoryEntity.getDrNum());

            if (byDrNum.size() > 0) {

                /*设备上传屏保*/
                String uploadUrl = fivedoorUrlIPAndPort + STOREAPI;

                long l = System.currentTimeMillis();
                long userId = l / 1000;

                String uploadVariable = "?/pic/" + userId + ".jpg";
                URI uri = new URI(uploadUrl + uploadVariable);
                RequestEntity requestEntity = RequestEntity
                        .put(uri)
                        .headers(getHttpHeaders(byDrNum.get(0).getFSerial(), byDrNum.get(0).getFPassword()))
                        .body(file.getBytes());
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                    String result = responseEntity.getBody();
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RenException("设备连接失败，疑似设备异常，请检查");
                }

                /*设置屏保*/
                //屏保字符由四位房间号组成
                String drNum = dormitoryEntity.getDrNum();
                if (dormitoryEntity.getDrNum().length() < 4) {
                    drNum = 0 + dormitoryEntity.getDrNum();
                }
                String txt = drNum.substring(0, 2) + "," + drNum.substring(2, drNum.length());

                String screeUrl = "?method=" + MQTT_SCREENSAVE;
                String screeStr = "serial=" + byDrNum.get(0).getFSerial()
                        + "&password=" + byDrNum.get(0).getFPassword()
                        + "&userId=" + String.valueOf(Long.parseLong(String.valueOf(userId), 16))
                        + "&type=" + "1"
                        + "&line=" + "2"
                        + "&location=" + "2"
                        + "&font=" + "1"
                        + "&jpg=" + userId + ".jpg"
                        + "&txt=" + txt;

                URI addUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + screeUrl);
                RequestEntity screeRequestEntity = RequestEntity
                        .post(addUri)
                        .headers(getHttpHeaders(byDrNum.get(0).getFSerial(), byDrNum.get(0).getFPassword()))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(screeStr);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(screeRequestEntity, String.class);
                    String result = responseEntity.getBody();
                    RespBase respBase = new Gson().fromJson(result, RespBase.class);
                    if (0 == respBase.getCode()) {
                        System.out.println(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RenException("设备连接失败，疑似设备异常，请检查");
                }
            }
        }

    }

    @Override
    @DataSource()
    public void insertBatchAndUpdate(List<ScAidoorfivePersonlistEntity> personlistDTOS) {
        baseDao.insertBatchAndUpdate(personlistDTOS);
    }

    @Override
    public void doorOpen(String serial, String password) throws URISyntaxException {
        /*人脸开门*/
        String updateUrl = "?method=" + MQTTDOOR_OPEN;
        String updateStr = "serial=" + serial
                + "&password="
                + password + "&userId=" + "3001";
        URI updateUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + updateUrl);
        RequestEntity updateRequestEntity = RequestEntity
                .post(updateUri)
                .headers(getHttpHeaders(serial, password))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(updateStr);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(updateRequestEntity, String.class);
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if (0 == respBase.getCode()) {
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException("设备连接失败，疑似设备异常，请检查");
        }
    }

    @Override
    public boolean doorReset(String serial, String password) throws URISyntaxException {
        /*人脸开门*/
        String updateUrl = "?method=" + MQTTDOOR_RESET;
        String updateStr = "serial=" + serial
                + "&password="
                + password + "&userId=" + "3001";
        URI updateUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + updateUrl);
        RequestEntity updateRequestEntity = RequestEntity
                .post(updateUri)
                .headers(getHttpHeaders(serial, password))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(updateStr);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(updateRequestEntity, String.class);
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if (0 == respBase.getCode()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException("设备连接失败，疑似设备异常，请检查");
        }
    }

    @Override
    public String faceRegister(String serial, String userId, String status) throws URISyntaxException, InterruptedException {

        userId = leftPading(Integer.toHexString(Integer.parseInt(userId)), "0", 8);
        //判断人脸是否注册成功
        if (status.equals("1")) {
            FvScDeviceEntity deviceEntity = baseDao.getBySerial(serial);
            //赋予权限添加所需参数
            AidoorfivePassword fiveDoor = new AidoorfivePassword();
            fiveDoor.setFSerial(deviceEntity.getFSerial());
            fiveDoor.setFPassword(deviceEntity.getFPassword());
            fiveDoor.setFName(deviceEntity.getFName());
            /*添加人脸权限*/
            boolean addface = AddUserFacePermission(fiveDoor, userId);
            if (addface) {
                /*更新人脸权限*/
                boolean updatePermision = UpdatePermissonDoor(fiveDoor, userId);
                return userId;
            }
        } else {
            throw new RenException("人脸注册失败");
        }
        return null;
    }

    @Override
    public FvScDeviceEntity getBySerial(String serial) {
        return baseDao.getBySerial(serial);
    }

    //    @RabbitListener(queues = MQQueueNameConfig.FACE_REGISTER_QUEUE)
    public void mqGet(String msg, Channel channel, Message message) throws IOException {
        log.info("人脸注册参数数据: " + msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String fiveDoor = jsonObject.get("fiveDoor").toString();
            String userId = jsonObject.get("userId").toString();
            String sctdid = jsonObject.get("sctdid").toString();

            AidoorfivePassword aidoorfivePassword = JSONUtil.toBean(fiveDoor, AidoorfivePassword.class);
            faceRegisterAfterPromiss(aidoorfivePassword, sctdid, userId);
//            boolean registerPermision = RegisterPermissonDoor(aidoorfivePassword, userId);
//            if (!registerPermision) {
//                QueryWrapper<ScAidoorfivePersonlistEntity> wrapper = new QueryWrapper<>();
//                wrapper.eq("serial", aidoorfivePassword.getFSerial());
//                wrapper.eq("password", aidoorfivePassword.getFPassword());
//                wrapper.eq("img_id", userId);
//                ScAidoorfivePersonlistEntity selectPerson = scAidoorfivePersonlistDao.selectOne(wrapper);
//                selectPerson.setStatus(0);
//                scAidoorfivePersonlistDao.updateById(selectPerson);
//            }


            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //手工确认消息
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            if (log.isErrorEnabled()) {
                log.info("ACK_QUEUE_A 接受信息异常{}", e.getMessage());
            }
        }

    }

    @Override
    public List<FvScDeviceEntity> getByDrNum(String drNum) {
        List<FvScDeviceEntity> byDrNum = baseDao.getByDrNum(drNum);
        return byDrNum;
    }

    @Override
    public void userInsert(Map<String, Object> map) {
        baseDao.userInsert(map);
    }

    @Override
    public Integer findByFaceUrl(String result) {
        return baseDao.findByFaceUrl(result);
    }

    @Override
    public void personInsert(Map<String, Object> personMap) {
        baseDao.personInsert(personMap);
    }

    @Override
    public void deleteBypersonId(Integer personId) {
        baseDao.deleteBypersonId(personId);
    }

    @Override
    public void removeBypersonId(Integer personId) {
        baseDao.removeBypersonId(personId);
    }

    @Override
    public void clearBypersonId(Integer personId) {
        baseDao.clearBypersonId(personId);
    }

    @DataSource()
    public String getEightLengthUserID() {
        Integer userIdByTen = scStudentsDao.getUserId();
        return leftPading(Integer.toHexString(userIdByTen), "0", 8).toUpperCase();
    }


    public String leftPading(String strSrc, String flag, int strSrcLength) {
        String strReturn = "";
        String strtemp = "";
        int curLength = strSrc.trim().length();
        if (strSrc != null && curLength > strSrcLength) {
            strReturn = strSrc.trim().substring(0, strSrcLength);
        } else if (strSrc != null && curLength == strSrcLength) {
            strReturn = strSrc.trim();
        } else {

            for (int i = 0; i < (strSrcLength - curLength); i++) {
                strtemp = strtemp + flag;
            }

            strReturn = strtemp + strSrc.trim();
        }
        return strReturn;
    }

    /**
     * 添加学生人脸信息到设备
     *
     * @param objectStringMap
     * @param fiveDoor
     * @return
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    private String PutFaceToDoorDevice(Map<Object, Object> objectStringMap, AidoorfivePassword fiveDoor, String userIdHex) throws URISyntaxException {
        /*设备上传人脸*/
        //从minio下载照片到本地
        String filepath = objectStringMap.get("sc_headphotoimg").toString();
        String fileName = filepath.substring(filepath.lastIndexOf("/"));
        String localsavepath = minioUtil.downloadFileToServeLocal(filepath, fileName);
        File file = new File(localsavepath);
        //将照片转换为二进制数组
        byte[] bytes = image2byte(file.getPath());
        //调用接口上传人脸
        String variable = "?/face/" + userIdHex + ".jpg";
        URI uri = new URI(fivedoorUrlIPAndPort + STOREAPI + variable);
        RequestEntity requestEntity = RequestEntity
                .put(uri)
                .headers(getHttpHeaders(fiveDoor.getFSerial(), fiveDoor.getFPassword()))
                .body(bytes);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            String result = responseEntity.getBody();
            if (result != null && result.length() > 0) {
                return userIdHex;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException("设备连接失败，疑似设备异常，请检查");
        }
    }

    /**
     * 添加人脸权限
     *
     * @param fiveDoor
     * @param userId
     * @return
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    private boolean AddUserFacePermission(AidoorfivePassword fiveDoor, String userId) {
        /*人脸添加权限*/
        String addUrl = "?method=" + DATAKEY_ADD;
        //开始时间
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("UTC"));
        long startDate = now.getTime().getTime() / 1000;
        //结束时间
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 10);
        end.setTimeZone(TimeZone.getTimeZone("UTC"));
        long endDate = end.getTime().getTime() / 1000;

        String addStr = "serial=" + fiveDoor.getFSerial()
                + "&password=" + fiveDoor.getFPassword()
                + "&keyType=" + "8"
                + "&keyCount=" + "1"
                + "&keyId=" + userId
                + "&keyDate=" + String.valueOf(endDate)
                + "&startDate=" + String.valueOf(startDate)
                + "&periodNo=" + "0";

        try {


            URI addUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + addUrl);
            RequestEntity addRequestEntity = RequestEntity
                    .post(addUri)
                    .headers(getHttpHeaders(fiveDoor.getFSerial(), fiveDoor.getFPassword()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(addStr);

            ResponseEntity<String> responseEntity = restTemplate.exchange(addRequestEntity, String.class);
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            Object data = respBase.getData();
            //判断失败的人脸用户ID是否返回
            if (data != null) {
                String s = JSONUtils.toJSONString(data);
                JSONObject jsonObject = JSONObject.parseObject(s);
                if (jsonObject.get("fail") != null && userId.equals(jsonObject.get("fail").toString())) {
                    return false;
                }
                // log.error("===============》错误码返回:{}", jsonObject);
            }
            if (0 == respBase.getCode()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException("设备连接失败，疑似设备异常，请检查");
        }
        return false;
    }

    /**
     * @param fiveDoor
     * @param userId
     * @return
     */
    private int exRegisterPermissonDoor(AidoorfivePassword fiveDoor, String userId) {

        String id = String.valueOf(Integer.parseInt(userId.toString(), 16));

        /*注册人脸权限*/
        String updateUrl = "?method=" + FACE_REGISTER;
        String updateStr = "serial=" + fiveDoor.getFSerial()
                + "&password="
                + fiveDoor.getFPassword()
                + "&userId=" + id;

        try {
            URI updateUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + updateUrl);
            RequestEntity updateRequestEntity = RequestEntity
                    .post(updateUri)
                    .headers(getHttpHeaders(fiveDoor.getFSerial(), fiveDoor.getFPassword()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(updateStr);
            ResponseEntity<String> responseEntity = restTemplate.exchange(updateRequestEntity, String.class);
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            Object data = respBase.getData();
            //判断失败的人脸用户ID是否返回
            if (data != null) {
                String s = JSONUtils.toJSONString(data);
                JSONObject jsonObject = JSONObject.parseObject(s);
                if (jsonObject.get("fail") != null && id.equals(jsonObject.get("fail").toString())) {
                    return 1;
                }
                // log.error("=========================》错误码返回:{}", jsonObject);
            }
            if (0 == respBase.getCode()) {
                return 0;
            } else if (1017 == respBase.getCode()) {
                return 1017;
            } else if (1019 == respBase.getCode()) {
                return 1019;
            } else {
                return -1;
            }
        } catch (Exception e) {
            log.error("批量注册异常：" + e.getMessage());
            return -1;
        }
    }


    /**
     * 批量注册
     *
     * @param fiveDoor
     * @param userId
     * @return
     */
    private boolean RegisterPermissonDoor(AidoorfivePassword fiveDoor, String userId) {
        /*注册人脸权限*/
        String updateUrl = "?method=" + FACE_REGISTER;
        String updateStr = "serial=" + fiveDoor.getFSerial()
                + "&password="
                + fiveDoor.getFPassword()
                + "&userId=" + String.valueOf(Integer.parseInt(userId.toString(), 16));

        try {
            URI updateUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + updateUrl);
            RequestEntity updateRequestEntity = RequestEntity
                    .post(updateUri)
                    .headers(getHttpHeaders(fiveDoor.getFSerial(), fiveDoor.getFPassword()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(updateStr);
            ResponseEntity<String> responseEntity = restTemplate.exchange(updateRequestEntity, String.class);
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if (0 == respBase.getCode()) {
                return true;
            }
        } catch (Exception e) {
            log.error("批量注册异常：" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 更新权限
     *
     * @param fiveDoor
     * @param userId
     * @return
     * @throws URISyntaxException
     * @throws InterruptedException
     */

    private boolean UpdatePermissonDoor(AidoorfivePassword fiveDoor, String userId) {
        /*更新权限*/
        String updateUrl = "?method=" + MQTTKEY_UPDATE;
        String updateStr = "serial=" + fiveDoor.getFSerial()
                + "&password="
                + fiveDoor.getFPassword()
                + "&userId=" + String.valueOf(Integer.parseInt(userId.toString(), 16));

        try {
            URI updateUri = new URI(fivedoorUrlIPAndPort + MQTTAPI + updateUrl);
            RequestEntity updateRequestEntity = RequestEntity
                    .post(updateUri)
                    .headers(getHttpHeaders(fiveDoor.getFSerial(), fiveDoor.getFPassword()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(updateStr);
            ResponseEntity<String> responseEntity = restTemplate.exchange(updateRequestEntity, String.class);
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if (0 == respBase.getCode()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("更新权限错误：" + e.getMessage());
        }
    }

    /**
     * 图片转换为二进制数组
     *
     * @param path
     * @return
     */
    public byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }
}

