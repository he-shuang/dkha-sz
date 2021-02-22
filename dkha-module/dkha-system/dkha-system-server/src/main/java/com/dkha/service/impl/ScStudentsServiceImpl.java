package com.dkha.service.impl;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelSheetVO;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.utils.UwbStatusInterface;
import com.dkha.dao.ScDormitorypersonDao;
import com.dkha.dao.ScStudentsDao;
import com.dkha.dao.ScUwbLabelDao;
import com.dkha.dto.ScStudentsDTO;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.entity.ScStudentsEntity;
import com.dkha.enums.ScUwbperRoleIdToUwbIdEnum;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.service.ScStudentsMqSenderService;
import com.dkha.service.ScStudentsService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * 学生档案信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScStudentsServiceImpl extends BaseServiceImpl<ScStudentsDao, ScStudentsEntity> implements ScStudentsService {

    @Autowired
    private SysAdminFeignClient sysAdminFeignClient;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private ScDormitorypersonDao scDormitorypersonDao;

    @Resource
    private ScUwbLabelDao scUwbLabelDao;
    @Value("${uwb.url2}")
    private String uwbUrl2;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    private ScStudentsMqSenderService scStudentsMqSenderService;


    @Override
    public PageData<ScStudentsDTO> page(Map<String, Object> params) {
        params.put(Constant.ORDER_FIELD,"sc_no");
        params.put(Constant.ORDER,"desc");
        params.put(Constant.ORDER_FIELD,Constant.CREATE_DATE);
        params.put(Constant.ORDER,"desc");
        IPage<ScStudentsEntity> page = baseDao.selectPage(
                getPage(params, null, false),
                getWrapper(params)
        );

        return getPageData(page, ScStudentsDTO.class);
    }

    @Override
    public List<ScStudentsDTO> list(Map<String, Object> params) {
        List<ScStudentsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScStudentsDTO.class);
    }

    private QueryWrapper<ScStudentsEntity> getWrapper(Map<String, Object> params){
        String scNo = (String)params.get("scNo");
        String scStuname = (String)params.get("scStuname");
        String scStatus=(String)params.get("scStatus");
        String scSchool=(String)params.get("scSchool");

        QueryWrapper<ScStudentsEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(scStuname), "sc_stuname", scStuname);
        wrapper.like(StringUtils.isNotBlank(scNo), "sc_no", scNo);
        wrapper.eq(StringUtils.isNotBlank(scStatus), "sc_status", scStatus);
        wrapper.eq(StringUtils.isNotBlank(scSchool), "sc_school", scSchool);
        return wrapper;
    }

    @Override
    public ScStudentsDTO get(String id) {
        ScStudentsEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScStudentsDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScStudentsDTO dto) {
        ScStudentsEntity entity = ConvertUtils.sourceToTarget(dto, ScStudentsEntity.class);
        QueryWrapper<ScStudentsEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("sc_no",dto.getScNo()).or().eq("sc_idno",dto.getScIdno());
        List<ScStudentsEntity> entityList = baseDao.selectList(queryWrapper);
        for (ScStudentsEntity studentsEntity : entityList) {
            if(studentsEntity.getScNo().equals(dto.getScNo())){
                throw new RenException("学号已存在,请重新输入");
            }
            if(studentsEntity.getScIdno().equals(dto.getScIdno())){
                throw new RenException("身份证号已存在,请重新输入");
            }
        }

        insert(entity);

        //发送消息队列测温平台
        entity.setScPhotoimg(url + "/" + bucketName + "/" + entity.getScPhotoimg());
        scStudentsMqSenderService.sendStudentsInfo(JSONObject.toJSONString(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScStudentsDTO dto) {
        ScStudentsEntity entity = ConvertUtils.sourceToTarget(dto, ScStudentsEntity.class);
        QueryWrapper<ScStudentsEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("sc_no",dto.getScNo()).or().eq("sc_idno",dto.getScIdno()).or().eq(StringUtils.isNotBlank(dto.getScUwbid()),"sc_uwbid",dto.getScUwbid()).or()
                .eq(StringUtils.isNotBlank(dto.getScRfcardid()),"sc_rfcardid",dto.getScRfcardid());;
        List<ScStudentsEntity> entityList = baseDao.selectList(queryWrapper);
//        if(StringUtils.isBlank(entity.getScGraduationdate())){
//            entity.setScGraduationdate(null);
//        }
        for (ScStudentsEntity studentsEntity : entityList) {
            if(!studentsEntity.getScStdid().equals(dto.getScStdid())) {
                if (studentsEntity.getScNo().equals(dto.getScNo())) {
                    throw new RenException("学号已存在,请重新输入");
                }
                if (studentsEntity.getScIdno().equals(dto.getScIdno())) {
                    throw new RenException("身份证号已存在,请重新输入");
                }

                if(StringUtils.isNotBlank(dto.getScUwbid()) && dto.getScUwbid().equals(studentsEntity.getScUwbid())){
                    throw new RenException("UWB标签已存在,请重新输入");
                }
                if(StringUtils.isNotBlank(dto.getScRfcardid()) && dto.getScRfcardid().equals(studentsEntity.getScRfcardid())){
                    throw new RenException("RFID已存在,请重新输入");
                }
            }
        }

        ScStudentsDTO dtoOld = this.get(String.valueOf(dto.getScStdid()));
        // 学生的信息更新推送通知星网云联更新角色相关电围规则
        this.uwbPersonfenceCacheUpdate(dto, dtoOld);
        // 人员信息有更新推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");
        // 标签变动
        if(!"".equals(dto.getScUwbid()) && dto.getScUwbid() != null && !"".equals(dtoOld.getScUwbid()) && dtoOld.getScUwbid() != null && !dto.getScUwbid().equals(dtoOld.getScUwbid())){
            // 标签不一致推送通知星网云联电围相应标签的规则替换
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("oldId", Integer.parseInt(dtoOld.getScUwbid()));
            jsonParam.put("newId", Integer.parseInt(dto.getScUwbid()));
            CloseableHttpClientToInterface.uwbReplaceTag(uwbUrl2, jsonParam.toString(), "1");
        } else if("".equals(dto.getScUwbid()) && !"".equals(dtoOld.getScUwbid()) && dtoOld.getScUwbid() != null){
            // 删除推送通知星网云联电围解除相应标签的规则
            int[] arrParam = {Integer.parseInt(dtoOld.getScUwbid())};
            CloseableHttpClientToInterface.uwbUnbindTagAllFence(uwbUrl2, Arrays.toString(arrParam), "1");
        }

        updateById(entity);

        //发送消息队列测温平台
        entity.setScPhotoimg(url + "/" + bucketName + "/" + entity.getScPhotoimg());
        scStudentsMqSenderService.sendStudentsInfo(JSONObject.toJSONString(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ScStudentsDTO dto = this.get(String.valueOf(id));
        // 学生的信息删除推送通知星网云联更新角色相关电围规则
        this.uwbPersonfenceCacheDelete(dto);
        // 人员信息有更新推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");
        // 删除推送通知星网云联电围解除相应标签的规则
        int[] arrParam = {Integer.parseInt(dto.getScUwbid())};
        CloseableHttpClientToInterface.uwbUnbindTagAllFence(uwbUrl2, Arrays.toString(arrParam), "1");

        baseDao.deleteById(id);
    }

    @Override
    public void importInfoExcel(MultipartFile file) {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        try {

            ExcelSheetVO excelSheetVO = ExcelUtils.importExcel(file.getInputStream(), suffix);
            List<List<Object>> dataList = excelSheetVO.getDataList();

            Result resultEducation = sysAdminFeignClient.getByType("education");
            Result resultStatus = sysAdminFeignClient.getByType("studentStatus");

            List<LinkedHashMap> dictDataEducation = (List<LinkedHashMap>) resultEducation.getData();
            List<LinkedHashMap> dictDataStatus = (List<LinkedHashMap>) resultStatus.getData();
            List<ScStudentsEntity> entityList = new ArrayList<>();
            //学号
            List<String> scNoList = new ArrayList<>();
            //身份证
            List<String> idNoList = new ArrayList<>();
            //重复身份证
            List<String> repeatIdNoList = new ArrayList<>();
            //重复学号
            List<String> repeatNoList = new ArrayList<>();

            for (List<Object> objects : dataList) {
                String scNo = objects.get(3).toString();
                for (int i = 0; i < entityList.size(); i++) {
                    ScStudentsEntity studentsEntity = entityList.get(i);
                    if(studentsEntity.getScIdno().equals(objects.get(1).toString())){
                        repeatIdNoList.add(objects.get(1).toString());
                    }
                    if(studentsEntity.getScNo().equals(scNo)){
                        repeatNoList.add(scNo);
                    }
                }

                QueryWrapper<ScStudentsEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sc_no",scNo);
                ScStudentsEntity scStudentsEntity = baseDao.selectOne(queryWrapper);
                if(scStudentsEntity == null){
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("sc_idno",objects.get(1).toString());
                    ScStudentsEntity studentsEntity = baseDao.selectOne(queryWrapper);
                    if(studentsEntity != null && objects.get(1).toString().equals(studentsEntity.getScIdno())){
                        idNoList.add(objects.get(1).toString());
                        //continue;
                    }
                    scStudentsEntity = new ScStudentsEntity();
                    scStudentsEntity.setScStuname(objects.get(0).toString());
                    scStudentsEntity.setScIdno(objects.get(1).toString());
                    scStudentsEntity.setScPhonenum(objects.get(2).toString());
                    scStudentsEntity.setScNo(scNo);
                    for (LinkedHashMap linkedHashMap : dictDataEducation) {
                        if(linkedHashMap.get("dictLabel").toString().equals(objects.get(4).toString())){
                            scStudentsEntity.setScEducation(StringUtils.isNotBlank(linkedHashMap.get("dictValue").toString()) ? Integer.valueOf(linkedHashMap.get("dictValue").toString()) : null);
                            break;
                        }
                    }
                    scStudentsEntity.setScRegisterdate(objects.get(5).toString());
                    for (LinkedHashMap linkedHashMap : dictDataStatus) {
                        if(linkedHashMap.get("dictLabel").toString().equals(objects.get(6).toString())){
                            scStudentsEntity.setScStatus(StringUtils.isNotBlank(linkedHashMap.get("dictValue").toString()) ? Integer.valueOf(linkedHashMap.get("dictValue").toString()) : null);
                            break;
                        }
                    }
                    int genderByIdCard = IdcardUtil.getGenderByIdCard(objects.get(1).toString());
                    scStudentsEntity.setScSex(genderByIdCard);
                    entityList.add(scStudentsEntity);
                }else{
                    scNoList.add(scNo);
                }

            }
            if(repeatIdNoList.size() > 0){
                throw new RenException("导入失败,以下身份证号重复: " + repeatIdNoList);
            }
            if(repeatNoList.size() > 0){
                throw new RenException("导入失败,以下学号重复: " + repeatNoList);
            }
            if(scNoList.size() > 0){
                throw new RenException("导入失败,以下学号已存在: " + scNoList);
            }
            if(idNoList.size() > 0){
                throw new RenException("导入失败,以下身份证号已存在: " + idNoList);
            }

            if(entityList.size() > 0){
                insertBatch(entityList);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RenException(e.getMessage());
        }
    }

    @Override
    public void importImg(MultipartFile[] file) {
        MultipartFile multipartFile = file[0];

        String suffix = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String originalFilename = multipartFile.getOriginalFilename();
        if(!originalFilename.contains("_")){
            throw new RenException("该图片命名格式不正确: " + originalFilename);
        }
        String[] s = null;
        try {
            s = originalFilename.split("_");
        }catch (Exception e){
            e.printStackTrace();
            throw new RenException("该图片命名格式不正确: " + originalFilename);
        }
        ScStudentsEntity studentsEntity = baseDao.selectOne(new QueryWrapper<ScStudentsEntity>().eq("sc_no", s[0]));
        if (studentsEntity != null){

            JSONObject jsonObject = null;
            try {
                jsonObject = minioUtil.uploadFile(multipartFile.getBytes(),"student", originalFilename.substring(0,originalFilename.indexOf(".")), suffix);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RenException("图片上传失败");
            }
            ScStudentsEntity entity = new ScStudentsEntity();
            entity.setScStdid(studentsEntity.getScStdid());
            entity.setScPhotoimg(jsonObject.getString("path"));
            baseDao.updateById(entity);
            //发送消息队列测温平台
            studentsEntity.setScPhotoimg(jsonObject.getString("url"));
            scStudentsMqSenderService.sendStudentsInfo(JSONObject.toJSONString(studentsEntity));
        }else{
            throw new RenException("该学生编号不存在:" + s[0]);
        }


//        List<String> scNo = new ArrayList<>();
//        List<String> noScNo = new ArrayList<>();
//        List<String> imgUrl = new ArrayList<>();
//        for (MultipartFile multipartFile : file) {
//            String suffix = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
//            String originalFilename = multipartFile.getOriginalFilename();
//            String[] s = null;
//            try {
//                s = originalFilename.split("_");
//            }catch (Exception e){
//                throw new RenException("该图片命名格式不正确: " + originalFilename);
//            }
//
//            scNo.add(s[0]);
//            try {
//                JSONObject jsonObject = minioUtil.uploadFile(multipartFile.getBytes(),"student", originalFilename.substring(0,originalFilename.indexOf(".")), suffix);
//                imgUrl.add(jsonObject.getString("url"));
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RenException("上传失败");
//            }
//        }
//        List<ScStudentsEntity> entityList = baseDao.getByScNos(scNo);
//        for (ScStudentsEntity studentsEntity : entityList) {
//            for (int i = 0; i < scNo.size(); i++) {
//                String s = scNo.get(i);
//                if(s.equals(studentsEntity.getScNo())){
//                    studentsEntity.setScPhotoimg(imgUrl.get(i));
//                    scNo.remove(s);
//                    break;
//                }
//            }
//        }
//
//        if(scNo.size() > 0) {
//            throw new RenException("该学生编号不存在:" + scNo);
//        }
//        if(entityList.size() == 0){
//            throw new RenException("该学生编号不存在:" + scNo);
//        }else{
//            updateBatchById(entityList);
//        }
//
//
//        //发送消息队列测温平台
//        for (ScStudentsEntity studentsEntity : entityList) {
//            scStudentsMqSenderService.sendStudentsInfo(JSONObject.toJSONString(studentsEntity));
//        }

    }

    @Override
    public void importRegisterInfoExcel(MultipartFile file) {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            List<String> scNoList = new ArrayList<>();

            List<String> uwbList = new ArrayList<>();
            List<String> fridList = new ArrayList<>();


            List<String> uwbListE = new ArrayList<>();
            List<String> fridListE = new ArrayList<>();

            List<String> uwbListC = new ArrayList<>();
            List<String> fridListC = new ArrayList<>();

            ExcelSheetVO excelSheetVO = ExcelUtils.importExcel(file.getInputStream(), suffix);
            List<List<Object>> dataList = excelSheetVO.getDataList();

            for (List<Object> objects : dataList) {
                String scNo = objects.get(1).toString();
                scNoList.add(scNo);
                if(uwbList.contains(objects.get(3).toString())){
                    uwbListC.add(objects.get(3).toString());
                }
                if(fridList.contains(objects.get(2).toString())){
                    fridListC.add(objects.get(2).toString());
                }
                uwbList.add(objects.get(3).toString());
                fridList.add(objects.get(2).toString());
            }
            QueryWrapper<ScStudentsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("sc_uwbid",uwbList).or().in("sc_rfcardid",fridList);
            List<ScStudentsEntity> scStudentsEntities = baseDao.selectList(queryWrapper);
            for (ScStudentsEntity studentsEntity : scStudentsEntities) {
                for (List<Object> objects : dataList) {
                    if(objects.get(2).toString().equals(studentsEntity.getScRfcardid())){
                        fridListE.add(studentsEntity.getScNo());
                    }
                    if(objects.get(3).toString().equals(studentsEntity.getScUwbid())){
                        uwbListE.add(studentsEntity.getScNo());
                    }
                }

            }
            if(fridListC.size() > 0){
                throw  new RenException("以下RFID重复,请确认后导入" + fridListC);
            }
            if(uwbListC.size() > 0){
                throw  new RenException("以下UWB标签重复,请确认后导入" + uwbListC);
            }

            if(fridListE.size() > 0){
                throw  new RenException("以下学生编号,RFID已存在,请确认后导入" + fridListE);
            }
            if(uwbListE.size() > 0){
                throw  new RenException("以下学生编号,UWB标签已存在,请确认后导入" + uwbListE);
            }

            List<ScStudentsEntity> entityList = baseDao.getByScNos(scNoList);
            for (ScStudentsEntity studentsEntity : entityList) {
                for (List<Object> objects : dataList) {
                    if(objects.get(1).toString().equals(studentsEntity.getScNo())){
                        studentsEntity.setScRfcardid(objects.get(2).toString());
                        studentsEntity.setScUwbid(objects.get(3).toString());
                        break;
                    }
                }
            }
            if(entityList.size() > 0){
                //  更新UWB角色电围规则
                this.uwbPersonfenceCacheBatchUpdate(entityList);
                updateBatchById(entityList);
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RenException(e.getMessage());
        }
    }

    @Override
    public List<ScStudentsDTO> getStudentsInfo(List<Long> id) {
        QueryWrapper<ScDormitorypersonEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("sc_stdid");
        List<ScDormitorypersonEntity> scDormitorypersonEntities = scDormitorypersonDao.selectList(queryWrapper);
        List<Long> pIds = new ArrayList<>();
        if(scDormitorypersonEntities != null && scDormitorypersonEntities.size() > 0){
            for (ScDormitorypersonEntity scDormitorypersonEntity : scDormitorypersonEntities) {
                    pIds.add(scDormitorypersonEntity.getScStdid());
            }
        }
        if(id != null && id.size()>0){
            for (int i = 0; i < id.size(); i++) {
                pIds.remove(id.get(i));
            }
        }
        QueryWrapper<ScStudentsEntity> entityQueryWrapper = new QueryWrapper<>();
        entityQueryWrapper.notIn(pIds.size() > 0,"sc_stdid",pIds);
        return ConvertUtils.sourceToTarget(baseDao.selectList(entityQueryWrapper),ScStudentsDTO.class);
        //return baseDao.getStudentsInfo(pIds,flag);

    }

    @Override
    public List<ScStudentsDTO> getStudentsByIds(List<Long> id) {
        List<ScStudentsEntity> studentsEntities = new ArrayList<>();
        for (Long aLong : id) {
            QueryWrapper<ScStudentsEntity> entityQueryWrapper = new QueryWrapper<>();
            entityQueryWrapper.eq("sc_stdid",aLong);
            ScStudentsEntity scStudentsEntity = baseDao.selectOne(entityQueryWrapper);
            studentsEntities.add(scStudentsEntity);
        }
//        entityQueryWrapper.notIn("sc_stdid",id);
//        List<ScStudentsEntity> scStudentsEntities = baseDao.selectList(entityQueryWrapper);
        return ConvertUtils.sourceToTarget(studentsEntities,ScStudentsDTO.class);
        //return baseDao.getStudentsInfo(pIds,flag);

    }

    @Override
    public List<String> getAllId() {
        return baseDao.getAllId();
    }

    /**
     * 学生的信息更新推送通知星网云联更新角色相关电围规则
     * @param dto
     * @param dtoOld
     */
    private void uwbPersonfenceCacheUpdate(ScStudentsDTO dto, ScStudentsDTO dtoOld){
        String newUwbId = dto.getScUwbid();
        String oldUwbId = dtoOld.getScUwbid();
        if(!newUwbId.equals(oldUwbId) && oldUwbId != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if("学生".equals(dict_label)){
                    curUwbPerRoleId = String.valueOf(curMap.get("dict_value"));
                    break;
                }
            }
            if(!"".equals(curUwbPerRoleId)) {
                JSONObject jsonParam = new JSONObject();
                if(!"".equals(newUwbId) && newUwbId != null){
                    JSONObject jsonParamAdd = new JSONObject();
                    jsonParamAdd.put(curUwbPerRoleId, new int[]{Integer.parseInt(newUwbId)});
                    jsonParam.put("add", jsonParamAdd);
                }
                if(!"".equals(oldUwbId) && oldUwbId != null){
                    JSONObject jsonParamDelete = new JSONObject();
                    jsonParamDelete.put(curUwbPerRoleId, new int[]{Integer.parseInt(oldUwbId)});
                    jsonParam.put("delete", jsonParamDelete);
                }
                System.out.println(jsonParam.toString());
                String result = CloseableHttpClientToInterface.uwbPersonfenceCache(uwbUrl2, jsonParam.toString(), "1");

                String curStatus = UwbStatusInterface.UwbStatus(result);
                JSONObject statusObject = JSONObject.parseObject(curStatus);
                Integer statusCode = statusObject.getInteger("code");
                // 异常
                if (statusCode.intValue() != 0) {
                    throw new RenException(statusObject.getString("msg"));
                }
            }
        }
    }

    /**
     * 学生的信息删除推送通知星网云联更新角色相关电围规则
     * @param dto
     */
    private void uwbPersonfenceCacheDelete(ScStudentsDTO dto){
        if(!"".equals(dto.getScUwbid()) && dto.getScUwbid() != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if("学生".equals(dict_label)){
                    curUwbPerRoleId = String.valueOf(curMap.get("dict_value"));
                    break;
                }
            }
            if(!"".equals(curUwbPerRoleId)){
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonParamDelete = new JSONObject();
                jsonParamDelete.put(curUwbPerRoleId, new int[]{Integer.parseInt(dto.getScUwbid())});
                jsonParam.put("delete", jsonParamDelete);
                System.out.println(jsonParam.toString());
                String result = CloseableHttpClientToInterface.uwbPersonfenceCache(uwbUrl2, jsonParam.toString(), "1");

                String curStatus = UwbStatusInterface.UwbStatus(result);
                JSONObject statusObject = JSONObject.parseObject(curStatus);
                Integer statusCode = statusObject.getInteger("code");
                // 异常
                if(statusCode.intValue() != 0){
                    throw new RenException(statusObject.getString("msg"));
                }
            }
        }
    }

    /**
     * 学生的信息批量更新推送通知星网云联更新角色相关电围规则
     * @param entityList
     */
    private void uwbPersonfenceCacheBatchUpdate(List<ScStudentsEntity> entityList){
        // 星网对应的人员角色ID
        List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
        Map<Integer, String> map = new HashMap<>();
        for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
            Map<String, Object> curMap = uwbPerRoleNumList.get(i);
            String dict_label = String.valueOf(curMap.get("dict_label"));
            String dict_value = String.valueOf(curMap.get("dict_value"));
            if("学生".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.STUDENT.code(), dict_value);
                break;
            }
        }

        Map<String, List<Integer>> mapUwbPerRoleId = new HashMap<>();
        for(ScStudentsEntity entity : entityList){
            String curDepartmentId = map.get(ScUwbperRoleIdToUwbIdEnum.STUDENT.code());
            List<Integer> curObj = mapUwbPerRoleId.get(curDepartmentId);
            if(curObj == null){
                List<Integer> newList = new ArrayList<>();
                newList.add(Integer.valueOf(entity.getScUwbid()));
                mapUwbPerRoleId.put(curDepartmentId, newList);
            } else {
                curObj.add(Integer.valueOf(entity.getScUwbid()));
            }
        }

        if (mapUwbPerRoleId.size() > 0){
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonParamAdd = new JSONObject();
            for (Map.Entry<String, List<Integer>> entry : mapUwbPerRoleId.entrySet()) {
                String curKey = entry.getKey();
                List<Integer> curObj = entry.getValue();
                Integer[] newArr = new Integer[curObj.size()];
                jsonParamAdd.put(curKey, curObj.toArray(newArr));
            }
            jsonParam.put("add", jsonParamAdd);

            String result = CloseableHttpClientToInterface.uwbPersonfenceCache(uwbUrl2, jsonParam.toString(), "1");

            String curStatus = UwbStatusInterface.UwbStatus(result);
            JSONObject statusObject = JSONObject.parseObject(curStatus);
            Integer statusCode = statusObject.getInteger("code");
            // 异常
            if (statusCode.intValue() != 0) {
                throw new RenException(statusObject.getString("msg"));
            }
        }
    }
}
