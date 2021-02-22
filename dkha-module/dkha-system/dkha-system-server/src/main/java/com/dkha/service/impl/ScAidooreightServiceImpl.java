
package com.dkha.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.aidoor.EightInchDoorConstants;
import com.dkha.aidoor.TransPictureResult;
import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.enums.SuperAdminEnum;
import com.dkha.commons.tools.excel.ExcelSheetVO;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.redis.RedisUtils;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.controller.ScAidooreightController;
import com.dkha.dao.ScAidooreightDao;
import com.dkha.dao.ScAidooreightPersonlistDao;
import com.dkha.dao.ScDormitoryfloorDao;
import com.dkha.dao.ScVisitorrecordDao;
import com.dkha.dto.*;
import com.dkha.dto.doorcontrol.AddPersonRequestInfo;
import com.dkha.dto.doorcontrol.DeviceFingerPrint;
import com.dkha.dto.doorcontrol.DeviceSetingInfo;
import com.dkha.dto.doorcontrol.PackageAuthorityDto;
import com.dkha.entity.*;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.model.aidoor.request.*;
import com.dkha.model.aidoor.response.BaseResponse;
import com.dkha.model.aidoor.response.RespBase;
import com.dkha.service.*;
import com.dkha.utils.Md5Utils;
import com.dkha.utils.UnicodeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 智能设备主要分为：8英寸智能门禁设备
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-14
 */
@Service
@RefreshScope
public class ScAidooreightServiceImpl extends BaseServiceImpl<ScAidooreightDao, ScAidooreightEntity> implements ScAidooreightService {
    private static final Logger log = LoggerFactory.getLogger(ScAidooreightServiceImpl.class);
    @Value("${eightdoor.mangerip}")
    private String mangerip;

    @Value("${eightdoor.port}")
    private String port;

    @Value("${minio.url}")
    private String miniourl;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ScDormitoryfloorDao scDormitoryfloorDao;

    @Autowired
    private SysAdminFeignClient sysAdminFeignClient;

    @Autowired
    private ScStudentsService scStudentsService;

    @Autowired
    private ScWorkersarchivesService scWorkersarchivesService;

    @Autowired
    private ScAidooreightPersonlistService scAidooreightPersonlistService;

    @Autowired
    private ScAidooreightPersonlistDao scAidooreightPersonlistDao;

    @Autowired
    MinioUtil minioUtil;
    @Autowired
    private ScVisitorrecordDao scVisitorrecordDao;

   @Autowired
   private RedisUtils redisUtils;

    @Autowired
   private ScAidooreightversionService scAidooreightversionService;

    @Resource
    private ScDeptDoorService scDeptDoorService;

    @Override
    public PageData<ScAidooreightDTO> page(Map<String, Object> params) {
        paramsToLike(params, "devicename");
        paramsToLike(params, "aeClientip");

        UserDetail user = SecurityUser.getUser();
        params.put("deptId",user.getDeptId());

        IPage<ScAidooreightEntity> page = baseDao.findPage(
                getPage(params, "", false), params
        );

        return getPageData(page, ScAidooreightDTO.class);
    }

    @Override
    public List<ScAidooreightDTO> list(Map<String, Object> params) {
        List<ScAidooreightEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScAidooreightDTO.class);
    }

    private QueryWrapper<ScAidooreightEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<ScAidooreightEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScAidooreightDTO get(String id) {
        ScAidooreightEntity entity = baseDao.selectById(id);
        ScAidooreightDTO scAidooreightDTO = ConvertUtils.sourceToTarget(entity, ScAidooreightDTO.class);
        return scAidooreightDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScAidooreightDTO dto) {
        ScAidooreightEntity ae_clientip = baseDao.selectOne(new QueryWrapper<ScAidooreightEntity>().eq("ae_clientip", dto.getAeClientip()));
        if (ae_clientip != null){
            throw new RenException("IP地址重复");
        }
        ScAidooreightEntity entity = ConvertUtils.sourceToTarget(dto, ScAidooreightEntity.class);
        entity.setAeSetupdate(new Date());
        entity.setAeState(0);
        insert(entity);

        addDoorPromise(entity.getAeId(),dto.getDeptIdList());
        try {

            DeviceFingerPrint fingerPrintInfo = getFingerPrintInfo(entity.getAeId().toString());
            if (fingerPrintInfo == null) {
                throw new RenException("设备连接失败");
            }
            boolean b = connetDoorConnet(entity.getAeId().toString());
            if(!b){
                throw new RenException("设备连接失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RenException("设备连接失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScAidooreightDTO dto) {
        ScAidooreightEntity entity = ConvertUtils.sourceToTarget(dto, ScAidooreightEntity.class);
        ScAidooreightEntity ae_clientip = baseDao.selectOne(new QueryWrapper<ScAidooreightEntity>().eq("ae_clientip", dto.getAeClientip()));
        if (ae_clientip != null && !ae_clientip.getAeId().equals(dto.getAeId())){
            throw new RenException("IP地址重复");
        }
        try {

            boolean b = disconnectToDevice(entity.getAeId().toString());
            if(b){
                log.error("断开设备连接失败");
            }

            DeviceFingerPrint fingerPrintInfo = getFingerPrintInfo(entity.getAeId().toString());
            if (fingerPrintInfo == null) {
                throw new RenException("设备连接失败");
            }
            b = connetDoorConnet(entity.getAeId().toString());
            if(!b){
                throw new RenException("设备连接失败");
            }
            entity.setAeState(0);
        }catch (Exception e){
            e.printStackTrace();
            throw new RenException(e.getMessage());
        }
        updateById(entity);

        addDoorPromise(dto.getAeId(),dto.getDeptIdList());
    }

    private void addDoorPromise(Long aeId , List<Long> deptIdList){

        if (!deptIdList.isEmpty()){
            //添加设备部门权限
            UserDetail user = SecurityUser.getUser();
            ScDeptDoorEntity deptDoorEntity;
            for (Long deptId : deptIdList){
                deptDoorEntity = new ScDeptDoorEntity();
                deptDoorEntity.setDoorId(aeId);
                deptDoorEntity.setDeptId(deptId);
                deptDoorEntity.setCreator(user.getId());
                boolean b = scDeptDoorService.addOrUpdate(deptDoorEntity);
                if (!b){
                    throw new RuntimeException("操作失败");
                }
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScAidooreightEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void importInfoExcel(MultipartFile file) {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            ExcelSheetVO excelSheetVO = ExcelUtils.importExcel(file.getInputStream(), suffix);
            List<List<Object>> dataList = excelSheetVO.getDataList();
            Result resultEducation = sysAdminFeignClient.getByType("accessEquipType");
            List<LinkedHashMap> dictData = (List<LinkedHashMap>) resultEducation.getData();
            for (List<Object> objects : dataList) {
                ScAidooreightEntity scAidooreightEntity = new ScAidooreightEntity();
                scAidooreightEntity.setAeClientip(objects.get(0).toString());
                scAidooreightEntity.setAeClientport(Integer.valueOf(objects.get(1).toString()));
                scAidooreightEntity.setAeMacaddress(objects.get(2).toString());
                scAidooreightEntity.setAeSerialnumber(objects.get(3).toString());
                scAidooreightEntity.setAeDevicename(objects.get(4).toString());
                for (LinkedHashMap linkedHashMap : dictData) {
                    if (linkedHashMap.get("dictLabel").toString().equals(objects.get(5).toString())) {
                        scAidooreightEntity.setAeSigntype(Integer.valueOf(linkedHashMap.toString()));
                        break;
                    }

                    scAidooreightEntity.setAeSignkey(objects.get(6).toString());
                    scAidooreightEntity.setAeExpirydate(DateUtils.parse(objects.get(7).toString(), DateUtils.DATE_PATTERN));
                    List<String> setupaddr = new ArrayList<>();
                    ScDormitoryfloorEntity scDormitoryfloorEntity = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                            .eq("df_floorname", objects.get(8).toString())
                            .eq("df_type", 0));
                    if (scDormitoryfloorEntity != null) {
                        setupaddr.add(scDormitoryfloorEntity.getDfFloorid().toString());
                        scDormitoryfloorEntity = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                                .eq("df_floorname", objects.get(9).toString()).eq("df_parentid", scDormitoryfloorEntity.getDfFloorid())
                                .eq("df_type", 1));
                        if (scDormitoryfloorEntity != null) {
                            setupaddr.add(scDormitoryfloorEntity.getDfFloorid().toString());
                        }
                    }

                    if (setupaddr.size() > 0) {
                        scAidooreightEntity.setAeSetupaddr(String.join(",", setupaddr));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException(e.getMessage());
        }
    }

    @Override
    public List<ScAidooreightDTO> listNoStopDevice() {
        List<ScAidooreightEntity> entities = baseDao.selectList(new QueryWrapper<ScAidooreightEntity>().ne("ae_state", 1));
        return ConvertUtils.sourceToTarget(entities,ScAidooreightDTO.class);
    }

//**********************************************************************************************************

    /**
     * 配置服务器地址
     *
     * @param ip   IP地址
     * @param port 端口号
     * @param api  操作的url地址
     * @return
     */
    private URI getUri(String ip, String port, String api) {
        URI uri = null;
        try {
            uri = new URI("http://" + ip + ":" + port + api);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private RequestEntity getRequestEntity(URI uri, String json, String singkey) {
        return getRequestEntity(uri, json, json, singkey);
    }

    /**
     * 统一设置header中的“sign”（签名校验参数）
     *
     * @param json
     * @return
     */
    private HttpHeaders getHttpHeaders(String json, String singnKey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("sign", getHeaderSign(json, singnKey));
        return httpHeaders;
    }

    /**
     * 将请求串和signKey加密后得到sign
     *
     * @param requestJson
     * @param signKey
     * @return
     */
    private String getHeaderSign(String requestJson, String signKey) {
        String strMd5 = requestJson + signKey;
        return Md5Utils.encode(strMd5);
    }

    /**
     * 模拟生成signKey
     *
     * @return
     */
    private String getSignKey(String singKey) {
        if (StringUtils.isEmpty(singKey)) {
            return "dkha@123456";
        } else {
            return singKey;
        }

    }
    private  void transStudentInfoToDevice(ScAidooreightEntity aidooreightEntity, ScStudentsDTO  scStudentsDTO,     List<ScAidooreightPersonlistDTO> personlistDTOS){

        URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
                EightInchDoorConstants.URI_PERSON_MANAGE_ADD);

        PersonAdd personAdd = new PersonAdd();
        personAdd.setPersonSerial(scStudentsDTO.getScStdid().toString());
        personAdd.setTotal(1);  //人数
        personAdd.setPersonName(scStudentsDTO.getScStuname());
        personAdd.setPersonIdentifier(scStudentsDTO.getScIdno());
        personAdd.setIcCardNo(scStudentsDTO.getScNo()); //这里应该写IC卡号，但是该设备没有IC卡
        personAdd.setPersonInfoType(3);
        String json = new Gson().toJson(personAdd);
        //如接口2.7备注说明，参数“faceList”不参与“sign”的生成
        RequestEntity.BodyBuilder bodyBuilder = RequestEntity
                .post(uri)
                .headers(getHttpHeaders(json, aidooreightEntity.getAeSignkey()))
                .contentType(MediaType.TEXT_PLAIN);
        //构造人脸图片
        List<PersonAddFace> faceList = new ArrayList<>();
        PersonAddFace face = new PersonAddFace();
        face.setImageName(UnicodeUtils.string2Unicode(scStudentsDTO.getScStuname())+ "_" + scStudentsDTO.getScIdno());

        //String base64 = FileUtils.image2Base64ByMino(miniourl, bucketName, scStudentsDTO.getScPhotoimg());
        String base64 =minioUtil.downloadFiletoBase64(scStudentsDTO.getScPhotoimg());
        String md5 = Md5Utils.encode(base64);
        face.setImageBase64(base64);
        face.setImageMD5(md5);
        faceList.add(face);
        personAdd.setFaceList(faceList);
        //发送请求时，需要将中文转成Unicode再执行exchange请求
        String personName3 = UnicodeUtils.string2Unicode(scStudentsDTO.getScStuname());
        personAdd.setPersonName(personName3);
        String json2 = new Gson().toJson(personAdd).replace("\\\\", "\\");
        RequestEntity requestEntity = bodyBuilder.body(json2);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if (respBase.getCode() == 200) {
                // 记录下发人员信息
                ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
                scpersondto.setAeId(aidooreightEntity.getAeId());
                scpersondto.setAeSerialnumber(aidooreightEntity.getAeSerialnumber());
                scpersondto.setPersontype(3);
                scpersondto.setUsername(scStudentsDTO.getScStuname());
                scpersondto.setPhotoimg(scStudentsDTO.getScPhotoimg());
                scpersondto.setUserid(scStudentsDTO.getScStdid());
                scpersondto.setUserno(scStudentsDTO.getScNo());
                scpersondto.setSex(scStudentsDTO.getScSex());
                scpersondto.setUpdateDate(new Date());
                personlistDTOS.add(scpersondto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

   private void transTeacherInfoToDevice(ScAidooreightEntity aidooreightEntity, ScWorkersarchivesDTO workersarchivesDTO ,     List<ScAidooreightPersonlistDTO> personlistDTOS){

       URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
               EightInchDoorConstants.URI_PERSON_MANAGE_ADD);
       PersonAdd personAdd = new PersonAdd();
       personAdd.setPersonSerial(workersarchivesDTO.getScWaid().toString());
       personAdd.setTotal(1);  //人数
       personAdd.setPersonName(workersarchivesDTO.getScWaname());
       personAdd.setPersonIdentifier(workersarchivesDTO.getScIdno());
       personAdd.setIcCardNo(workersarchivesDTO.getScEmpno()); //这里应该写IC卡号，但是该设备没有IC卡
       personAdd.setPersonInfoType(3);
       String json = new Gson().toJson(personAdd);
       //如接口2.7备注说明，参数“faceList”不参与“sign”的生成
       RequestEntity.BodyBuilder bodyBuilder = RequestEntity
               .post(uri)
               .headers(getHttpHeaders(json, aidooreightEntity.getAeSignkey()))
               .contentType(MediaType.TEXT_PLAIN);
       //构造人脸图片
       List<PersonAddFace> faceList = new ArrayList<>();
       PersonAddFace face = new PersonAddFace();
       face.setImageName(UnicodeUtils.string2Unicode(workersarchivesDTO.getScWaname()) + "_" + workersarchivesDTO.getScIdno());

       // String base64 = FileUtils.image2Base64ByMino(miniourl, bucketName, workersarchivesDTO.getScPhotoimg());
       String base64 =minioUtil.downloadFiletoBase64(workersarchivesDTO.getScPhotoimg());
       String md5 = Md5Utils.encode(base64);
       face.setImageBase64(base64);
       face.setImageMD5(md5);
       faceList.add(face);
       personAdd.setFaceList(faceList);
       //发送请求时，需要将中文转成Unicode再执行exchange请求
       String personName3 = UnicodeUtils.string2Unicode(workersarchivesDTO.getScWaname());
       personAdd.setPersonName(personName3);
       String json2 = new Gson().toJson(personAdd).replace("\\\\", "\\");
       RequestEntity requestEntity = bodyBuilder.body(json2);
       try {
           ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
           String result = responseEntity.getBody();
           RespBase respBase = new Gson().fromJson(result, RespBase.class);
           if (respBase.getCode() == 200) {
               // 记录下发人员信息
               ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
               scpersondto.setAeId(aidooreightEntity.getAeId());
               scpersondto.setAeSerialnumber(aidooreightEntity.getAeSerialnumber());
               scpersondto.setPersontype(workersarchivesDTO.getScEmptype());
               scpersondto.setUsername(workersarchivesDTO.getScWaname());
               scpersondto.setPhotoimg(workersarchivesDTO.getScPhotoimg());
               scpersondto.setUserid(workersarchivesDTO.getScWaid());
               scpersondto.setUserno(workersarchivesDTO.getScEmpno());
               scpersondto.setSex(workersarchivesDTO.getScWasex());
               scpersondto.setUpdateDate(new Date());
               personlistDTOS.add(scpersondto);
           }
       }catch (Exception e){
           e.printStackTrace();
       }

   }

   private void transVisitorRecord(ScAidooreightEntity aidooreightEntity,  ScVisitorrecordEntity dto  ,  List<ScAidooreightPersonlistDTO> personlistDTOS){
       AddPersonRequestInfo addPersonRequestInfo = new AddPersonRequestInfo();
       addPersonRequestInfo.setTotal(1);
       addPersonRequestInfo.setPersonSerial(dto.getVrId().toString());
       addPersonRequestInfo.setPersonName(dto.getVrName());
       addPersonRequestInfo.setPersonIdentifier(dto.getVrIdno());
       addPersonRequestInfo.setImagepath(dto.getVrPhoneimg());
       addPersonRequestInfo.setPersonInfoType(3);
       addPersonRequestInfo.setIcCardNo(dto.getVrUwbid());
       boolean b =addPersonToDeivce(aidooreightEntity.getAeId().toString(), addPersonRequestInfo);
       if(b){
           ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
           scpersondto.setAeId(aidooreightEntity.getAeId());
           scpersondto.setAeSerialnumber(aidooreightEntity.getAeSerialnumber());
           scpersondto.setPersontype(4);
           scpersondto.setUsername(dto.getVrName());
           scpersondto.setPhotoimg(dto.getVrPhoneimg());
           scpersondto.setUserid(dto.getVrId());
           scpersondto.setUserno(dto.getVrId().toString());
           scpersondto.setSex(dto.getVrSex());
           scpersondto.setUpdateDate(new Date());
           personlistDTOS.add(scpersondto);
       }
   }


    //**********************************************************************************************************

    private RequestEntity getRequestEntity(URI uri, String signJson, String bodyJson, String singkey) {
        return RequestEntity
                .post(uri)
                .headers(getHttpHeaders(signJson, singkey))
                .contentType(MediaType.TEXT_PLAIN)
                .body(bodyJson);
    }

    @Override
    public DeviceFingerPrint getFingerPrintInfo(String aeid) {
        //通过主键进行查询实体
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {

            //设备必须设置IP地址和端口
            if (StringUtils.isNoneBlank(aidooreightEntity.getAeClientip()) &&
                    StringUtils.isNoneBlank(aidooreightEntity.getAeClientport().toString())) {

                URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
                        EightInchDoorConstants.URI_EQUIPMENT_GET_FINGER_PRINT_INFO);
                //判断加密密匙是否存在
                if (StringUtils.isEmpty(aidooreightEntity.getAeSignkey())) {
                    aidooreightEntity.setAeSignkey(getSignKey(aidooreightEntity.getAeSignkey()));
                }
                String json = "";
                RequestEntity requestEntity = getRequestEntity(uri, json, aidooreightEntity.getAeSignkey());
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                    String result = responseEntity.getBody();
                    BaseResponse<DeviceFingerPrint> baseResponse = new Gson().fromJson(result, new TypeToken<BaseResponse<DeviceFingerPrint>>() {
                    }.getType());
                    //请求成功需要进行处理
                    if (baseResponse != null && baseResponse.getCode() == EightInchDoorConstants.CODE_RESPONSE_SUCCESS) {
                        DeviceFingerPrint info = baseResponse.getData();
                        aidooreightEntity.setAeMacaddress(info.getMacAddress());
                        baseDao.updateById(aidooreightEntity);
                        return info;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                return null;
            }
        }
        return null;

    }

    @Override
    public boolean connetDoorConnet(String aeid) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {

            Connect connect = new Connect();
            connect.setDeviceId((int)(aidooreightEntity.getAeId().longValue()));
            connect.setIp(mangerip);   //管理端ip地址，一般为本机或者服务器地址
            connect.setPort(Integer.parseInt(port));           //管理端端口，一般为本机或者服务器端口号
            connect.setMacAddress(aidooreightEntity.getAeMacaddress());
            connect.setSerialNumber(aidooreightEntity.getAeSerialnumber());
            connect.setSignType(aidooreightEntity.getAeSigntype());
            connect.setSignKey(aidooreightEntity.getAeSignkey());
            String json = new Gson().toJson(connect);
            URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(), EightInchDoorConstants.URI_EQUIPMENT_CONNECT);
            RequestEntity requestEntity = getRequestEntity(uri, json, aidooreightEntity.getAeSignkey());
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                BaseResponse<String> baseResponse = new Gson().fromJson(result, new TypeToken<BaseResponse<String>>() {
                }.getType());
                if (baseResponse != null && baseResponse.getCode() == EightInchDoorConstants.CODE_RESPONSE_SUCCESS) {
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean addPersonToDeivce(String aeid, AddPersonRequestInfo personRequestInfo) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {
            URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                            EightInchDoorConstants.URI_PERSON_MANAGE_ADD);

            PersonAdd personAdd = new PersonAdd();
            personAdd.setPersonSerial(personRequestInfo.getPersonSerial());
            personAdd.setTotal(personRequestInfo.getTotal());  //人数
            personAdd.setPersonName(personRequestInfo.getPersonName());
            personAdd.setPersonIdentifier(personRequestInfo.getPersonIdentifier());
            personAdd.setIcCardNo(personRequestInfo.getIcCardNo());
            personAdd.setPersonInfoType(personRequestInfo.getPersonInfoType());
            String json = new Gson().toJson(personAdd);
            //如接口2.7备注说明，参数“faceList”不参与“sign”的生成
            RequestEntity.BodyBuilder bodyBuilder = RequestEntity
                    .post(uri)
                    .headers(getHttpHeaders(json,aidooreightEntity.getAeSignkey()))
                    .contentType(MediaType.TEXT_PLAIN);
            //构造人脸图片
            List<PersonAddFace> faceList = new ArrayList<>();
            PersonAddFace face = new PersonAddFace();
            face.setImageName(UnicodeUtils.string2Unicode(personRequestInfo.getPersonName())+"_"+personRequestInfo.getPersonIdentifier());

          //  String base64 = FileUtils.image2Base64ByMino(miniourl,bucketName,personRequestInfo.getImagepath());
            String base64 = minioUtil.downloadFiletoBase64(personRequestInfo.getImagepath());
            String md5 = Md5Utils.encode(base64);
            face.setImageBase64(base64);
            face.setImageMD5(md5);
            faceList.add(face);
            personAdd.setFaceList(faceList);
            //发送请求时，需要将中文转成Unicode再执行exchange请求
            String personName3 =   UnicodeUtils.string2Unicode(personRequestInfo.getPersonName());
            personAdd.setPersonName(personName3);
            String json2 = new Gson().toJson(personAdd).replace("\\\\", "\\");
            RequestEntity requestEntity = bodyBuilder.body(json2);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);

                if (200 == respBase.getCode()) {
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
        return false;
    }

    @Override
    public boolean deletePersonFromDeviceBySerial(String aeid, String personSerial) {


        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {

            URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),EightInchDoorConstants.URI_PERSON_MANAGE_DELETE);
            PersonAdd person = new PersonAdd();
            person.setTotal(1);
            person.setPersonSerial(personSerial);
            String json = new Gson().toJson(person);
            RequestEntity requestEntity = getRequestEntity(uri, json,aidooreightEntity.getAeSignkey());
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                //200 删除成功  1311 设备端不存在该人员
                if (200 == respBase.getCode() || 1311 == respBase.getCode()) {
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean cleanDeviceData(String aeid) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {
            URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                    EightInchDoorConstants.URI_EQUIPMENT_CLEAN_DATA);
            String json = "{}";
            RequestEntity requestEntity = getRequestEntity(uri, json,aidooreightEntity.getAeSignkey());
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if (200 == respBase.getCode()) {
                    scAidooreightPersonlistDao.delete(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id", aeid));
                    aidooreightEntity.setAeFacetotal(0);
                    baseDao.updateById(aidooreightEntity);
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean rebootDevice(String aeid) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {
            URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                    EightInchDoorConstants.URI_EQUIPMENT_REBOOT);
            String json = "{}";
            RequestEntity requestEntity = getRequestEntity(uri, json,aidooreightEntity.getAeSignkey());
            try {
                ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
                String result = response.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if (200 == respBase.getCode()) {
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    @Async
    public  List<TransPictureResult> transAllPictureToAllDevice() {
        List<ScAidooreightEntity> listdoor = baseDao.listNoStopDevice();
        List<TransPictureResult> listTransPict = new ArrayList<>();
        if(listdoor!=null&&listdoor.size()>0) {
            Map<String, Object> params = new HashMap<>();
            params.put("scStatus", "1");
            List<ScStudentsDTO> listDto = scStudentsService.list(params);

            params.clear();
            params.put("scStatus", "0");
            List<ScWorkersarchivesDTO> listWorkdto = scWorkersarchivesService.list(params);

            for (ScAidooreightEntity aidooreightEntity : listdoor) {
                aidooreightEntity.setAeTransstate(1);
                baseDao.updateById(aidooreightEntity);

                TransPictureResult transPictureResult = new TransPictureResult();
                int total = 0;
                int success = 0;
                int fail = 0;

                /**
                 * 循环下发图片到指定的设备中
                 */
                long time = System.currentTimeMillis();

                total += listDto.size();

                List<ScAidooreightPersonlistDTO> personlistDTOS = new ArrayList<>();

                for (ScStudentsDTO scStudentsDTO : listDto) {

                    URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
                            EightInchDoorConstants.URI_PERSON_MANAGE_ADD);

                    PersonAdd personAdd = new PersonAdd();
                    personAdd.setPersonSerial(scStudentsDTO.getScStdid().toString());
                    personAdd.setTotal(1);  //人数
                    personAdd.setPersonName(scStudentsDTO.getScStuname());
                    personAdd.setPersonIdentifier(scStudentsDTO.getScIdno());
                    personAdd.setIcCardNo(scStudentsDTO.getScNo()); //这里应该写IC卡号，但是该设备没有IC卡
                    personAdd.setPersonInfoType(3);
                    String json = new Gson().toJson(personAdd);
                    //如接口2.7备注说明，参数“faceList”不参与“sign”的生成
                    RequestEntity.BodyBuilder bodyBuilder = RequestEntity
                            .post(uri)
                            .headers(getHttpHeaders(json, aidooreightEntity.getAeSignkey()))
                            .contentType(MediaType.TEXT_PLAIN);
                    //构造人脸图片
                    List<PersonAddFace> faceList = new ArrayList<>();
                    PersonAddFace face = new PersonAddFace();
                    face.setImageName(UnicodeUtils.string2Unicode(scStudentsDTO.getScStuname()) + "_" + scStudentsDTO.getScIdno());

                    //String base64 = FileUtils.image2Base64ByMino(miniourl, bucketName, scStudentsDTO.getScPhotoimg());
                    String base64 =minioUtil.downloadFiletoBase64(scStudentsDTO.getScPhotoimg());
                    String md5 = Md5Utils.encode(base64);
                    face.setImageBase64(base64);
                    face.setImageMD5(md5);
                    faceList.add(face);
                    personAdd.setFaceList(faceList);
                    //发送请求时，需要将中文转成Unicode再执行exchange请求
                    String personName3 = UnicodeUtils.string2Unicode(scStudentsDTO.getScStuname());
                    personAdd.setPersonName(personName3);
                    String json2 = new Gson().toJson(personAdd).replace("\\\\", "\\");
                    RequestEntity requestEntity = bodyBuilder.body(json2);
                    try {
                        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                        String result = responseEntity.getBody();
                        RespBase respBase = new Gson().fromJson(result, RespBase.class);
                        if (respBase.getCode() == 200) {
                            // 记录下发人员信息
                            ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
                            scpersondto.setAeId(aidooreightEntity.getAeId());
                            scpersondto.setAeSerialnumber(aidooreightEntity.getAeSerialnumber());
                            scpersondto.setPersontype(3);
                            scpersondto.setUsername(scStudentsDTO.getScStuname());
                            scpersondto.setPhotoimg(scStudentsDTO.getScPhotoimg());
                            scpersondto.setUserid(scStudentsDTO.getScStdid());
                            scpersondto.setUserno(scStudentsDTO.getScNo());
                            scpersondto.setSex(scStudentsDTO.getScSex());
                            scpersondto.setUpdateDate(new Date());
                            personlistDTOS.add(scpersondto);
                            success++;
                        } else {
                            fail++;
                        }
                    }catch (Exception e){
                        fail++;
                        e.printStackTrace();
                    }
                }
                total += listWorkdto.size();
                for (ScWorkersarchivesDTO workersarchivesDTO : listWorkdto) {

                    URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
                            EightInchDoorConstants.URI_PERSON_MANAGE_ADD);

                    PersonAdd personAdd = new PersonAdd();
                    personAdd.setPersonSerial(workersarchivesDTO.getScWaid().toString());
                    personAdd.setTotal(1);  //人数
                    personAdd.setPersonName(workersarchivesDTO.getScWaname());
                    personAdd.setPersonIdentifier(workersarchivesDTO.getScIdno());
                    personAdd.setIcCardNo(workersarchivesDTO.getScEmpno()); //这里应该写IC卡号，但是该设备没有IC卡
                    personAdd.setPersonInfoType(3);
                    String json = new Gson().toJson(personAdd);
                    //如接口2.7备注说明，参数“faceList”不参与“sign”的生成
                    RequestEntity.BodyBuilder bodyBuilder = RequestEntity
                            .post(uri)
                            .headers(getHttpHeaders(json, aidooreightEntity.getAeSignkey()))
                            .contentType(MediaType.TEXT_PLAIN);
                    //构造人脸图片
                    List<PersonAddFace> faceList = new ArrayList<>();
                    PersonAddFace face = new PersonAddFace();
                    face.setImageName(UnicodeUtils.string2Unicode(workersarchivesDTO.getScWaname()) + "_" + workersarchivesDTO.getScIdno());

                   // String base64 = FileUtils.image2Base64ByMino(miniourl, bucketName, workersarchivesDTO.getScPhotoimg());
                    String base64 =minioUtil.downloadFiletoBase64(workersarchivesDTO.getScPhotoimg());
                    String md5 = Md5Utils.encode(base64);
                    face.setImageBase64(base64);
                    face.setImageMD5(md5);
                    faceList.add(face);
                    personAdd.setFaceList(faceList);
                    //发送请求时，需要将中文转成Unicode再执行exchange请求
                    String personName3 = UnicodeUtils.string2Unicode(workersarchivesDTO.getScWaname());
                    personAdd.setPersonName(personName3);
                    String json2 = new Gson().toJson(personAdd).replace("\\\\", "\\");
                    RequestEntity requestEntity = bodyBuilder.body(json2);
                    try {
                        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                        String result = responseEntity.getBody();
                        RespBase respBase = new Gson().fromJson(result, RespBase.class);
                        if (respBase.getCode() == 200) {
                            // 记录下发人员信息
                            ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
                            scpersondto.setAeId(aidooreightEntity.getAeId());
                            scpersondto.setAeSerialnumber(aidooreightEntity.getAeSerialnumber());
                            scpersondto.setPersontype(workersarchivesDTO.getScEmptype());
                            scpersondto.setUsername(workersarchivesDTO.getScWaname());
                            scpersondto.setPhotoimg(workersarchivesDTO.getScPhotoimg());
                            scpersondto.setUserid(workersarchivesDTO.getScWaid());
                            scpersondto.setUserno(workersarchivesDTO.getScEmpno());
                            scpersondto.setSex(workersarchivesDTO.getScWasex());
                            scpersondto.setUpdateDate(new Date());
                            personlistDTOS.add(scpersondto);
                            success++;
                        } else {
                            fail++;
                        }
                    }catch (Exception e){
                        fail++;
                        e.printStackTrace();
                    }
                }

                if(personlistDTOS.size() > 0){
                    scAidooreightPersonlistService.insertBatchAndUpdate(personlistDTOS);
                }
                long end = System.currentTimeMillis() - time;
                transPictureResult.setSeconds((int) (end / 1000));
                transPictureResult.setMsg("成功下发");
                transPictureResult.setDeviceName(aidooreightEntity.getAeDevicename());
                transPictureResult.setDeviceid(aidooreightEntity.getAeId().toString());
                Integer count = scAidooreightPersonlistDao.selectCount(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id", aidooreightEntity.getAeId()));
                aidooreightEntity.setAeFacetotal(count);
                aidooreightEntity.setAeFacedowntime(new Date());
                aidooreightEntity.setAeTransstate(0);
                baseDao.updateById(aidooreightEntity);//更新人脸设备的下发数据

                transPictureResult.setTransTotal(total);
                transPictureResult.setSuccessTotal(success);
                transPictureResult.setFailTotal(fail);

                listTransPict.add(transPictureResult);
            }
        }
          return listTransPict;
    }

    @Override
    @Async
    public TransPictureResult transAllPictureToDevice(String aeid) {
        TransPictureResult transPictureResult =new TransPictureResult();

        ScAidooreightEntity aidooreightEntity = baseDao.selectOne(new QueryWrapper<ScAidooreightEntity>().eq("ae_transstate",0).eq("ae_id",aeid));
        int total=0;
        int success=0;
        int fail=0;
        if (aidooreightEntity != null) {
            aidooreightEntity.setAeTransstate(1);
            baseDao.updateById(aidooreightEntity);

            /**
             * 循环下发图片到指定的设备中
             */
            long time = System.currentTimeMillis();
            Map<String, Object> params =new HashMap<>();
            params.put("scStatus","1");
            List<ScStudentsDTO>  listDto=  scStudentsService.list(params);
            total+=listDto.size();

            List<ScAidooreightPersonlistDTO> personlistDTOS = new ArrayList<>();
            for(ScStudentsDTO scStudentsDTO : listDto){
                URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                        EightInchDoorConstants.URI_PERSON_MANAGE_ADD);

                PersonAdd personAdd = new PersonAdd();
                personAdd.setPersonSerial(scStudentsDTO.getScStdid().toString());
                personAdd.setTotal(1);  //人数
                personAdd.setPersonName(scStudentsDTO.getScStuname());
                personAdd.setPersonIdentifier(scStudentsDTO.getScIdno());
                personAdd.setIcCardNo(scStudentsDTO.getScNo()); //这里应该写IC卡号，但是该设备没有IC卡
                personAdd.setPersonInfoType(3);
                String json = new Gson().toJson(personAdd);
                //如接口2.7备注说明，参数“faceList”不参与“sign”的生成
                RequestEntity.BodyBuilder bodyBuilder = RequestEntity
                        .post(uri)
                        .headers(getHttpHeaders(json,aidooreightEntity.getAeSignkey()))
                        .contentType(MediaType.TEXT_PLAIN);
                //构造人脸图片
                List<PersonAddFace> faceList = new ArrayList<>();
                PersonAddFace face = new PersonAddFace();
                face.setImageName(UnicodeUtils.string2Unicode(scStudentsDTO.getScStuname())+"_"+scStudentsDTO.getScIdno());

                //String base64 = FileUtils.image2Base64ByMino(miniourl,bucketName,scStudentsDTO.getScPhotoimg());
                String base64 =minioUtil.downloadFiletoBase64(scStudentsDTO.getScPhotoimg());

                String md5 = Md5Utils.encode(base64);
                face.setImageBase64(base64);
                face.setImageMD5(md5);
                faceList.add(face);
                personAdd.setFaceList(faceList);
                //发送请求时，需要将中文转成Unicode再执行exchange请求
                String personName3 =   UnicodeUtils.string2Unicode(scStudentsDTO.getScStuname());
                personAdd.setPersonName(personName3);
                String json2 = new Gson().toJson(personAdd).replace("\\\\", "\\");
                RequestEntity requestEntity = bodyBuilder.body(json2);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                    String result = responseEntity.getBody();
                    RespBase respBase = new Gson().fromJson(result, RespBase.class);
                    if (respBase.getCode() == 200) {

                        // 记录下发人员信息
                        ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
                        scpersondto.setAeId(aidooreightEntity.getAeId());
                        scpersondto.setAeSerialnumber(aidooreightEntity.getAeSerialnumber());
                        scpersondto.setPersontype(3);
                        scpersondto.setUsername(scStudentsDTO.getScStuname());
                        scpersondto.setPhotoimg(scStudentsDTO.getScPhotoimg());
                        scpersondto.setUserid(scStudentsDTO.getScStdid());
                        scpersondto.setUserno(scStudentsDTO.getScNo());
                        scpersondto.setSex(scStudentsDTO.getScSex());
                        scpersondto.setUpdateDate(new Date());
                        personlistDTOS.add(scpersondto);
                        success++;
                        // log.info("下发成功 " + success);
                    } else {
                        fail++;
                        // log.info("下发失败 " + fail);
                    }
                }catch (Exception e){
                    fail++;
                    e.printStackTrace();
                }
             }


            params.clear();
            params.put("scStatus","0");
            List<ScWorkersarchivesDTO>  listWorkdto=  scWorkersarchivesService.list(params);
            total+=listWorkdto.size();
            for(ScWorkersarchivesDTO workersarchivesDTO : listWorkdto){

                URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                        EightInchDoorConstants.URI_PERSON_MANAGE_ADD);

                PersonAdd personAdd = new PersonAdd();
                personAdd.setPersonSerial(workersarchivesDTO.getScWaid().toString());
                personAdd.setTotal(1);  //人数
                personAdd.setPersonName(workersarchivesDTO.getScWaname());
                personAdd.setPersonIdentifier(workersarchivesDTO.getScIdno());
                personAdd.setIcCardNo(workersarchivesDTO.getScEmpno()); //这里应该写IC卡号，但是该设备没有IC卡
                personAdd.setPersonInfoType(3);
                String json = new Gson().toJson(personAdd);
                //如接口2.7备注说明，参数“faceList”不参与“sign”的生成
                RequestEntity.BodyBuilder bodyBuilder = RequestEntity
                        .post(uri)
                        .headers(getHttpHeaders(json,aidooreightEntity.getAeSignkey()))
                        .contentType(MediaType.TEXT_PLAIN);
                //构造人脸图片
                List<PersonAddFace> faceList = new ArrayList<>();
                PersonAddFace face = new PersonAddFace();
                face.setImageName(UnicodeUtils.string2Unicode(workersarchivesDTO.getScWaname())+"_"+workersarchivesDTO.getScIdno());

                //String base64 = FileUtils.image2Base64ByMino(miniourl,bucketName,workersarchivesDTO.getScPhotoimg());
                String base64 =minioUtil.downloadFiletoBase64(workersarchivesDTO.getScPhotoimg());
                String md5 = Md5Utils.encode(base64);
                face.setImageBase64(base64);
                face.setImageMD5(md5);
                faceList.add(face);
                personAdd.setFaceList(faceList);
                //发送请求时，需要将中文转成Unicode再执行exchange请求
                String personName3 =   UnicodeUtils.string2Unicode(workersarchivesDTO.getScWaname());
                personAdd.setPersonName(personName3);
                String json2 = new Gson().toJson(personAdd).replace("\\\\", "\\");
                RequestEntity requestEntity = bodyBuilder.body(json2);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                    String result = responseEntity.getBody();
                    RespBase respBase = new Gson().fromJson(result, RespBase.class);
                    if (respBase.getCode() == 200) {
                        // 记录下发人员信息
                        ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
                        scpersondto.setAeId(aidooreightEntity.getAeId());
                        scpersondto.setAeSerialnumber(aidooreightEntity.getAeSerialnumber());
                        scpersondto.setPersontype(workersarchivesDTO.getScEmptype());
                        scpersondto.setUsername(workersarchivesDTO.getScWaname());
                        scpersondto.setPhotoimg(workersarchivesDTO.getScPhotoimg());
                        scpersondto.setUserid(workersarchivesDTO.getScWaid());
                        scpersondto.setUserno(workersarchivesDTO.getScEmpno());
                        scpersondto.setSex(workersarchivesDTO.getScWasex());
                        scpersondto.setUpdateDate(new Date());
                        personlistDTOS.add(scpersondto);
                        success++;
                    } else {
                        fail++;
                    }
                }catch (Exception e){
                    fail++;
                    e.printStackTrace();
                }
            }
            if(personlistDTOS.size() > 0){
                scAidooreightPersonlistService.insertBatchAndUpdate(personlistDTOS);
            }
            long end = System.currentTimeMillis() - time;
            transPictureResult.setSeconds((int)(end/1000));
            transPictureResult.setMsg("成功下发");
            transPictureResult.setDeviceName(aidooreightEntity.getAeDevicename());
            transPictureResult.setDeviceid(aidooreightEntity.getAeId().toString());

            aidooreightEntity.setAeTransstate(0);
            Integer count = scAidooreightPersonlistDao.selectCount(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id", aeid));
            aidooreightEntity.setAeFacetotal(count);
            aidooreightEntity.setAeFacedowntime(new Date());
            baseDao.updateById(aidooreightEntity);//更新人脸设备的下发数据

        }else {
            transPictureResult.setSeconds(0);
            transPictureResult.setCode(-1);
            transPictureResult.setMsg("设备未查询到");
        }
        transPictureResult.setTransTotal(total);
        transPictureResult.setSuccessTotal(success);
        transPictureResult.setFailTotal(fail);
        return transPictureResult;
    }

    @Override
    public boolean disconnectToDevice(String aeid) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        try {
            if (aidooreightEntity != null) {
                URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
                        EightInchDoorConstants.URI_EQUIPMENT_DISCONNECT);
                Disconnect disconnect = new Disconnect();
                String msgMd5 = Md5Utils.encode(aidooreightEntity.getAeDeviceid() + "|" + aidooreightEntity.getAeSignkey());
                disconnect.setVerification(msgMd5);
                String json = new Gson().toJson(disconnect);
                RequestEntity requestEntity = getRequestEntity(uri, json, json, aidooreightEntity.getAeSignkey());
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if (respBase.getCode() == 200) {
                    aidooreightEntity.setAeState(-1);
                    baseDao.updateById(aidooreightEntity);
                    return true;
                } else {
                    log.error("设备：{} 断开状态:{}", aidooreightEntity.getAeClientip(), respBase.getCode());
                }
            }
        }catch (Exception ex){
            log.error("设备断开错误:{}", ex.getMessage());
        }
        return false;
    }
    private boolean disconnectToDeviceEX( ScAidooreightEntity aidooreightEntity ) {
        try {
            if (aidooreightEntity != null) {
                URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
                        EightInchDoorConstants.URI_EQUIPMENT_DISCONNECT);
                Disconnect disconnect = new Disconnect();
                String msgMd5 = Md5Utils.encode(aidooreightEntity.getAeDeviceid() + "|" + aidooreightEntity.getAeSignkey());
                disconnect.setVerification(msgMd5);
                String json = new Gson().toJson(disconnect);
                RequestEntity requestEntity = getRequestEntity(uri, json, json, aidooreightEntity.getAeSignkey());
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if (respBase.getCode() == 200) {
                    aidooreightEntity.setAeState(-1);
                    baseDao.updateById(aidooreightEntity);
                    return true;
                } else {
                    log.error("设备：{} 断开状态:{}", aidooreightEntity.getAeClientip(), respBase.getCode());
                }
            }
        }catch (Exception ex){
            log.error("设备断开错误:{}", ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean disconnectAllDevice(){

        int sucess=0;
        int failed=0;

        List<ScAidooreightEntity> listdoor = baseDao.listNoStopDevice();

        if(listdoor!=null&&listdoor.size()>0) {
            log.error("》》开始执行批量断开：设备总数:{} 断开成功:{}，端口失败:{}",listdoor.size(),sucess,failed);
            for (ScAidooreightEntity aidooreightEntity : listdoor) {
                if(disconnectToDeviceEX(aidooreightEntity))
                {
                    sucess++;
                }else
                {
                    failed++;
                }
            }
            log.error("《《结束批量断开设备总数:{} 断开成功:{}，端口失败:{}",listdoor.size(),sucess,failed);
        }
        return  true;
    }

    public boolean setDoorAuthority(ScAidooreightEntity scAidooreightEntity,String personSerial,int days,String workingdays){
      if(scAidooreightEntity!=null){
          URI uri = getUri(scAidooreightEntity.getAeClientip(),scAidooreightEntity.getAeClientport().toString(),
                  EightInchDoorConstants.URI_PERSON_MANAGE_DOOR_AUTHORITY_V2);
          DoorAuthorityV2 authority = new DoorAuthorityV2();
          authority.setPersonSerial(personSerial);
          List<DoorAuthorityV2.DoorAuthorityDetail> authorityDetails = new ArrayList<>();
          DoorAuthorityV2.DoorAuthorityDetail authorityDetail = new DoorAuthorityV2.DoorAuthorityDetail();
          LocalDate today = LocalDate.now();
          DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate endday= today.plusDays(days);
          authorityDetail.setStartDate(dtf2.format(today));
          authorityDetail.setEndDate(dtf2.format(endday));
          if(workingdays!=null&&workingdays.length()>0)
          {
              authorityDetail.setWorkingDays(workingdays);
          }
          List<DoorAuthorityV2.DoorAuthorityDetail.TimeAuthority> timeAuthorityList = new ArrayList<>();

          authorityDetail.setTimeRangeList(timeAuthorityList);
          authorityDetails.add(authorityDetail);
          authority.setAuthorityDetails(authorityDetails);
          String json = new Gson().toJson(authority);
          RequestEntity requestEntity = getRequestEntity(uri, json,json,scAidooreightEntity.getAeSignkey());
          try {
              ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
              String result = responseEntity.getBody();
              RespBase respBase = new Gson().fromJson(result, RespBase.class);
              if (respBase.getCode() == 200) {
                  return true;
              }
          }catch (Exception e){
              e.printStackTrace();
          }
      }
        return false;
    }

    @Override
    public boolean openRemoteDoor(String aeid) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {
            URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                    EightInchDoorConstants.URI_EQUIPMENT_OPEN_DOOR);
            String json = "{}";
            RequestEntity requestEntity = getRequestEntity(uri, json,json,aidooreightEntity.getAeSignkey());
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            String result = response.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if (200 == respBase.getCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Async
    public Boolean transFaceListToAiDoor(FaceListDataToDoorDTO listDataToDoorDTO) {
        List<ScAidooreightPersonlistDTO> personlistDTOS = new ArrayList<>();
        List<String> listdAeid=   listDataToDoorDTO.getAeids();
        if(listdAeid!=null&&listdAeid.size()>0){
          for(String aeid : listdAeid){
              ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
              if(aidooreightEntity!=null)
              {
                  personlistDTOS.clear();
                  //根据类型进行查询不同的表
                  if (listDataToDoorDTO.getUsertype()==1){
                      //查询学生
                      List<String> userids = listDataToDoorDTO.getUserids();
                      for(String uid :userids){
                          ScStudentsDTO scStudentsDTO = scStudentsService.get(uid);
                          transStudentInfoToDevice(aidooreightEntity,scStudentsDTO,personlistDTOS);
                      }
                  }

                  if (listDataToDoorDTO.getUsertype()==2){
                      //查询教师
                      List<String> userids = listDataToDoorDTO.getUserids();
                      for(String uid :userids){
                          ScWorkersarchivesDTO workersarchivesDTO = scWorkersarchivesService.get(uid);
                          transTeacherInfoToDevice(aidooreightEntity,workersarchivesDTO,personlistDTOS);
                      }
                  }
                  if (listDataToDoorDTO.getUsertype()==3){
                      //访客
                      List<String> userids = listDataToDoorDTO.getUserids();
                      for(String uid :userids){
                          ScVisitorrecordEntity scVisitorrecordEntity = scVisitorrecordDao.selectById(uid);
                          transVisitorRecord(aidooreightEntity,scVisitorrecordEntity,personlistDTOS);
                      }
                  }
                  if(personlistDTOS.size() > 0){
                      scAidooreightPersonlistService.insertBatchAndUpdate(personlistDTOS);
                      Integer count = scAidooreightPersonlistDao.selectCount(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id", aeid));
                      aidooreightEntity.setAeFacetotal(count);
                      aidooreightEntity.setAeFacedowntime(new Date());
                      baseDao.updateById(aidooreightEntity);
                  }

              }
          }
          return  true;
        }
        return false;
    }

    @Override
    public JSONObject getDoorSetting(String aeid) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
        if (aidooreightEntity != null) {

             String doorsetting=(String) redisUtils.get(aidooreightEntity.getAeId().toString());
             if(doorsetting!=null&&doorsetting.trim().length()>0){
                 return  JSONObject.parseObject(doorsetting);
             }else {
                 URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                         EightInchDoorConstants.URI_EQUIPMENT_GET_SETTING);
                 Connect connect = new Connect();
                 connect.setSerialNumber(aidooreightEntity.getAeSerialnumber());
                 connect.setMacAddress(aidooreightEntity.getAeMacaddress());
                 String json = new Gson().toJson(connect);
                 RequestEntity requestEntity = getRequestEntity(uri, json,json,aidooreightEntity.getAeSignkey());
                 try {
                     ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                     String result = responseEntity.getBody();
                     JSONObject jsonObject = JSONObject.parseObject(result);
                     JSONObject data = jsonObject.getJSONObject("data");
                     if (data != null) {
                         redisUtils.set(aidooreightEntity.getAeId().toString(), data.toJSONString(),3600);
                     }
                     return data;
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
        }
        return null;
    }

    @Override
    public boolean setDoorSetting(DeviceSetingInfo deviceSetingInfo) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(deviceSetingInfo.getAeId());
        if (aidooreightEntity != null) {
            URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                    EightInchDoorConstants.URI_EQUIPMENT_SETTING);
            Setting setting = new Setting();
            BeanUtils.copyProperties(deviceSetingInfo,setting);
            if(deviceSetingInfo.getIsOpenTemp() != null){
                setting.setOpenTemp(deviceSetingInfo.getIsOpenTemp());
            }
            if(deviceSetingInfo.getIsOpenRadar() != null){
                setting.setOpenRadar(deviceSetingInfo.getIsOpenRadar());
            }

            String signJson = new Gson().toJson(setting);
            //对系统中出现的公司名称，设备名称出现的中文进行uicode转码
            String nameUnicode = UnicodeUtils.string2Unicode(deviceSetingInfo.getCompanyName());
            setting.setCompanyName(nameUnicode);
            String  deviceNameUnicode = UnicodeUtils.string2Unicode(deviceSetingInfo.getDeviceName());
            setting.setDeviceName(deviceNameUnicode);
            String  bottomtitle = UnicodeUtils.string2Unicode(deviceSetingInfo.getBottomTitle());
            setting.setBottomTitle(bottomtitle);

            String bodyJson = new Gson().toJson(setting).replace("\\\\", "\\");
            //加密成“sign”时需要原数据，发送请求时，需要将中文转成Unicode再执行exchange请求
            RequestEntity requestEntity = getRequestEntity(uri, signJson, bodyJson,aidooreightEntity.getAeSignkey());
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            //log.error("设备配置结果： "+JSONObject.toJSONString(responseEntity));
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if(200==respBase.getCode()) {
                redisUtils.delete(aidooreightEntity.getAeId().toString());
                return  true;
            }
        }
        return false;
    }

    @Override
    public List<ScAidooreightDTO> getAll() {
        List<ScAidooreightDTO> dtos = new ArrayList<>();
        //超级管理员数据不过滤
        UserDetail user = SecurityUser.getUser();
        QueryWrapper<ScAidooreightEntity> queryWrapper = new QueryWrapper<>();
        if(user.getSuperAdmin() != SuperAdminEnum.YES.value()){
            queryWrapper.ne("ae_devicetype", 3);
        }
        List<ScAidooreightEntity> entities = baseDao.selectList(queryWrapper);
        for (ScAidooreightEntity entity : entities) {
            ScAidooreightDTO scAidooreightDTO = new ScAidooreightDTO();
            scAidooreightDTO.setAeId(entity.getAeId());
            scAidooreightDTO.setAeDevicename(entity.getAeDevicename());
            dtos.add(scAidooreightDTO);
        }
        return dtos;
    }

    @Override
    public List<ScAidooreightDTO> getAllByType(String type) {
        List<ScAidooreightDTO> dtos = new ArrayList<>();
        //超级管理员数据不过滤
        UserDetail user = SecurityUser.getUser();
        QueryWrapper<ScAidooreightEntity> queryWrapper = new QueryWrapper<>();
        if(type.equals("3")){
            queryWrapper.eq("ae_devicetype", type);
        }else{
            queryWrapper.ne("ae_devicetype", type);
        }


        List<ScAidooreightEntity> entities = baseDao.selectList(queryWrapper);
        for (ScAidooreightEntity entity : entities) {
            ScAidooreightDTO scAidooreightDTO = new ScAidooreightDTO();
            scAidooreightDTO.setAeId(entity.getAeId());
            scAidooreightDTO.setAeDevicename(entity.getAeDevicename());
            dtos.add(scAidooreightDTO);
        }
        return dtos;
    }

    @Override
    public boolean checkPackageAuthority(PackageAuthorityDto packageAuthority) {
        ScAidooreightEntity aidooreightEntity = baseDao.selectById(packageAuthority.getAeId());
        if (aidooreightEntity != null) {

            URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                    EightInchDoorConstants.URI_PACKAGE_AUTHORITY);
            PackageAuthority authority = new PackageAuthority();
            authority.setVersionCode(packageAuthority.getVersionCode());
            authority.setPackageName(packageAuthority.getPackageName());
            authority.setVersionName(UnicodeUtils.string2Unicode(packageAuthority.getVersionName()));
            String json = new Gson().toJson(authority);
            RequestEntity requestEntity = getRequestEntity(uri, json,json,aidooreightEntity.getAeSignkey());
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            String result = responseEntity.getBody();
            RespBase respBase = new Gson().fromJson(result, RespBase.class);
            if(200==respBase.getCode()) {
                return  true;
            }
        }
        return false;
    }
    public boolean checkPackageAuthority(PackageAuthorityDto packageAuthority, ScAidooreightEntity aidooreightEntity) {
        if (aidooreightEntity != null) {
            try {
                URI uri = getUri(aidooreightEntity.getAeClientip(), aidooreightEntity.getAeClientport().toString(),
                        EightInchDoorConstants.URI_PACKAGE_AUTHORITY);
                PackageAuthority authority = new PackageAuthority();
                authority.setVersionCode(packageAuthority.getVersionCode());
                authority.setPackageName(packageAuthority.getPackageName());
                // authority.setVersionName(UnicodeUtils.string2Unicode(packageAuthority.getVersionName()));
                String json = new Gson().toJson(authority);
                RequestEntity requestEntity = getRequestEntity(uri, json, json, aidooreightEntity.getAeSignkey());
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if (200 == respBase.getCode()) {
                    return true;
                }
            }catch (Exception ex){
                log.error("检测8寸门禁包是否符合升级异常：{}",ex.getMessage());
            }
        }
        return false;
    }

    @Override
    public int updateAllDeviceApk() {
        //循环更新最新设备版本
        // 获取所有最新设备
        int fialdCount=0;
        for(int i=0;i<=2;i++){
            ScAidooreightversionEntity aidoorversion = scAidooreightversionService.getLastVersionByType(i);

            if(aidoorversion!=null && aidoorversion.getAevUpdatefilepath().trim().length()>0){
                String filepath=  aidoorversion.getAevUpdatefilepath();
                String fileName=    filepath.substring(filepath.lastIndexOf("/"));

                String  serverpath = MinioUtil.getUploadPath();
                String localsavepath=serverpath + fileName;
                File file=new File(localsavepath);
                if(!file.exists()) {
                    localsavepath = minioUtil.downloadFileToServeLocal(aidoorversion.getAevUpdatefilepath(), fileName);
                }

                QueryWrapper<ScAidooreightEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("ae_mainboard", i);
                List<ScAidooreightEntity> entities = baseDao.selectList(wrapper);
                int successUpdatecount=0;
                if(entities!=null) {
                    PackageAuthorityDto packageAuthority =new PackageAuthorityDto();
                    packageAuthority.setPackageName(aidoorversion.getAevPackname());
                    packageAuthority.setVersionCode(aidoorversion.getAevVersioncode());
                    packageAuthority.setVersionName(aidoorversion.getAevVersion());
                    for (ScAidooreightEntity entity : entities) {
                        try {
                            boolean disconnet= disconnectToDevice(entity.getAeId().toString()); //断开链接
                            if(disconnet) {
                                boolean bconnect = connetDoorConnet(entity.getAeId().toString()); //重连设备，防止链接不成功
                                if (!bconnect) {
                                    //链接设备不成功！
                                    log.error("升级APK重来设备{}不成功!", entity.getAeClientip());
                                }
                            }else
                            {
                                log.error("升级APK断开设备链接{}不成功!", entity.getAeClientip());
                            }
                            if (checkPackageAuthority(packageAuthority, entity)) {
                                if (packageTransfer(entity, localsavepath)) {
                                    successUpdatecount++;
                                } else {
                                    fialdCount++;
                                }
                            } else {
                                successUpdatecount++;
                            }
                        }catch (Exception ex){
                            log.error("升级所以8寸设备apk异常：{}",ex.getMessage());
                        }

                    }
                    aidoorversion.setUpdatetotal(aidoorversion.getUpdatetotal()+successUpdatecount);
                    aidoorversion.setLastUpdatetime(new Date());
                    scAidooreightversionService.updateById(aidoorversion);
                }
            }
        }
        return fialdCount;
    }

    @Override
    public int uploadNewVersionAPKfileOfIds(String[] ids) {

        int fialdCount=0;
        Map<String,ScAidooreightversionEntity> mapdoor=new HashMap<>();
        Map<String,Integer> successUpCount=new HashMap<>();
        for(Integer i=0;i<=2;i++) {
            ScAidooreightversionEntity aidoorversion = scAidooreightversionService.getLastVersionByType(i);
            if (aidoorversion != null && aidoorversion.getAevUpdatefilepath().trim().length() > 0) {
                String filepath=  aidoorversion.getAevUpdatefilepath();
                String fileName=    filepath.substring(filepath.lastIndexOf("/"));
                String  serverpath = MinioUtil.getUploadPath();
                String localsavepath=serverpath  + fileName;
                File file=new File(localsavepath);
                localsavepath = minioUtil.downloadFileToServeLocal(aidoorversion.getAevUpdatefilepath(), fileName);
                mapdoor.put(i.toString(), aidoorversion);
                successUpCount.put(i.toString(),0);
            }
        }
        if(mapdoor!=null && mapdoor.size()>0){
            for(String aeid :ids){

                ScAidooreightEntity aidooreightEntity = baseDao.selectById(aeid);
                if (aidooreightEntity != null) {
                    try {
                        boolean disconnet= disconnectToDevice(aeid); //断开链接
                        if(disconnet) {
                            boolean bconnect = connetDoorConnet(aeid); //重连设备，防止链接不成功
                            if (!bconnect) {
                                //链接设备不成功！
                                log.error("升级APK重来设备{}不成功!", aidooreightEntity.getAeClientip());
                            }
                        }else
                        {
                            log.error("升级APK断开设备链接{}不成功!", aidooreightEntity.getAeClientip());
                        }

                        ScAidooreightversionEntity scAidooreightversionEntity = mapdoor.get(aidooreightEntity.getAeMainboard().toString());
                        PackageAuthorityDto packageAuthority = new PackageAuthorityDto();
                        packageAuthority.setPackageName(scAidooreightversionEntity.getAevPackname()); //包名称
                        packageAuthority.setVersionCode(scAidooreightversionEntity.getAevVersioncode()); //版本code
                        packageAuthority.setVersionName(scAidooreightversionEntity.getAevVersion()); //版本名称

                        String filepath = scAidooreightversionEntity.getAevUpdatefilepath(); //升级apk本地存放地址

                        String fileName = filepath.substring(filepath.lastIndexOf("/"));
                        String serverpath = MinioUtil.getUploadPath();
                        String localsavepath = serverpath + fileName;
                        File file = new File(localsavepath);
                        //判断文件是否已下载
                        if (!file.exists()) {
                            localsavepath = minioUtil.downloadFileToServeLocal(scAidooreightversionEntity.getAevUpdatefilepath(), fileName);
                        }

                        Integer integer = successUpCount.get(aidooreightEntity.getAeMainboard().toString());

                        if (checkPackageAuthority(packageAuthority, aidooreightEntity)) { //检测升级

                            if (packageTransfer(aidooreightEntity, localsavepath)) {
                                integer++; //成功数量
                            } else {
                                fialdCount++;
                            }

                        } else {
                            integer++;
                        }

                        successUpCount.put(aidooreightEntity.getAeMainboard().toString(), integer);

                    }catch (Exception ex){
                        log.error("多选设备升级失败：{},失败原因{}",aidooreightEntity.getAeClientip(),ex.getMessage());
                    }
                }
            }
            //更新成功的数据流
            for(Integer i=0;i<=2;i++) {
                ScAidooreightversionEntity aidoorversion = mapdoor.get(i.toString());
                if(aidoorversion!=null) {
                    Integer successUpdatecount = successUpCount.get(i.toString());
                    aidoorversion.setUpdatetotal(aidoorversion.getUpdatetotal() + successUpdatecount);
                    aidoorversion.setLastUpdatetime(new Date());
                    scAidooreightversionService.updateById(aidoorversion);
                }
            }
        }
        return fialdCount;
    }



    /**
     *  传输apk包
     * @param scAidooreightEntity
     * @param apkPath
     * @return
     */
    public boolean packageTransfer(ScAidooreightEntity scAidooreightEntity ,String apkPath) {
        if(scAidooreightEntity!=null) {
            URI uri = getUri(scAidooreightEntity.getAeClientip(),scAidooreightEntity.getAeClientport().toString(),
                    EightInchDoorConstants.URI_PACKAGE_TRANSFER);

            FileSystemResource fileSystemResource = new FileSystemResource(apkPath);
            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("file", fileSystemResource);
            RequestEntity requestEntity = RequestEntity
                    .post(uri)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(form);
            try {

                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if(200==respBase.getCode()) {
                    return  true;
                }

            }catch (Exception e){
                   log.error("升级8寸设备传输apk异常{}",e.getMessage());
            }

        }
        return  false;
    }

    @Override
    public void updateAllMachineTime() {
        List<ScAidooreightDTO> listdoor = this.list(null);

        if(listdoor!=null&&listdoor.size()>0) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for(ScAidooreightDTO scdoordto:listdoor) {
                URI uri = getUri( scdoordto.getAeClientip(),scdoordto.getAeClientport().toString(), EightInchDoorConstants.URI_EQUIPMENT_SETDEVICETIME);
                SetDeviceTime setDeviceTime = new SetDeviceTime();
                //String strtime="{\"macAddress\":\"B0:02:47:30:FB:2E\",\"deviceSN\":\"NZ53MUMPJ7\",\"systemDateTime\":\"2020-09-26 17:12:13\"}";
                setDeviceTime.setMacAddress(scdoordto.getAeMacaddress());
                setDeviceTime.setDeviceSN(scdoordto.getAeSerialnumber());
                String nowtimestr=sdf.format(new Date());
                setDeviceTime.setSystemDateTime(nowtimestr);
                String json = new Gson().toJson(setDeviceTime);
                RequestEntity requestEntity = getRequestEntity(uri, json,json,scdoordto.getAeSignkey());
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            }
        }
    }

    @Override
    public void updateMachineTimeBy(ScAidooreightDTO scdoordto) {

        if(scdoordto!=null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                URI uri = getUri(scdoordto.getAeClientip(), scdoordto.getAeClientport().toString(), EightInchDoorConstants.URI_EQUIPMENT_SETDEVICETIME);
                SetDeviceTime setDeviceTime = new SetDeviceTime();
                //String strtime="{\"macAddress\":\"B0:02:47:30:FB:2E\",\"deviceSN\":\"NZ53MUMPJ7\",\"systemDateTime\":\"2020-09-26 17:12:13\"}";
                setDeviceTime.setMacAddress(scdoordto.getAeMacaddress());
                setDeviceTime.setDeviceSN(scdoordto.getAeSerialnumber());
                String nowtimestr = sdf.format(new Date());
                log.error("同步设备时间: " + nowtimestr);
                setDeviceTime.setSystemDateTime(nowtimestr);
                String json = new Gson().toJson(setDeviceTime);
                RequestEntity requestEntity = getRequestEntity(uri, json, json, scdoordto.getAeSignkey());
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
            }catch (Exception ex){
                log.error("同步设备"+scdoordto.getAeClientip()+"时间,失败:"+ex.getMessage());
            }
        }
    }

    @Override
    public void batchUpatePws(AidooreightPassword aidooreightPassword) {
        Long[] ids = aidooreightPassword.getIds();
        for (Long id : ids) {
            JSONObject doorSetting = getDoorSetting(id.toString());
            log.error("设备ID: " +id + " 参数 : " + doorSetting);
            if(doorSetting != null){
                DeviceSetingInfo deviceSetingInfo = JSONObject.parseObject(doorSetting.toJSONString(), DeviceSetingInfo.class);
                //通反射设置对象空字符串转null
                Class<? extends DeviceSetingInfo> aClass = deviceSetingInfo.getClass();
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    if (declaredField.getGenericType().toString().equals(
                            "class java.lang.String")) {
                        declaredField.setAccessible(true);
                        try {
                            Object o = declaredField.get(deviceSetingInfo);
                            if (o != null && o.toString().trim().equals("")) {
                                declaredField.set(deviceSetingInfo, null);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

                deviceSetingInfo.setAeId(id.toString());
                deviceSetingInfo.setDevicePassword(aidooreightPassword.getPassword());
                setDoorSetting(deviceSetingInfo);
            }
        }

        //TODO 新逻辑
       /* Long[] ids = aidooreightPassword.getIds();
        for (Long id : ids) {
            DeviceSetingInfo deviceSetingInfo = new DeviceSetingInfo();
            deviceSetingInfo.setAeId(id.toString());
            deviceSetingInfo.setDevicePassword(aidooreightPassword.getPassword());
            ScAidooreightEntity aidooreightEntity = baseDao.selectById(deviceSetingInfo.getAeId());
            if (aidooreightEntity != null) {
                URI uri = getUri(aidooreightEntity.getAeClientip(),aidooreightEntity.getAeClientport().toString(),
                        EightInchDoorConstants.URI_EQUIPMENT_SETTING);
                Setting setting = new Setting();
                BeanUtils.copyProperties(deviceSetingInfo,setting);
                String signJson = new Gson().toJson(setting);
                // 新版本的
                String bodyJson = new Gson().toJson(setting).replace("\\\\", "\\");
                //加密成“sign”时需要原数据，发送请求时，需要将中文转成Unicode再执行exchange请求
                RequestEntity requestEntity = getRequestEntity(uri, signJson, bodyJson,aidooreightEntity.getAeSignkey());
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                //log.error("设备配置结果： "+JSONObject.toJSONString(responseEntity));
                String result = responseEntity.getBody();
                RespBase respBase = new Gson().fromJson(result, RespBase.class);
                if(200==respBase.getCode()) {
                    redisUtils.delete(aidooreightEntity.getAeId().toString());
                }else {
                    throw new RenException("修改失败");
                }
            }
        }*/
    }

}
