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
import com.dkha.dao.ScUwbLabelDao;
import com.dkha.dao.ScWorkersarchivesDao;
import com.dkha.dto.ScStudentsDTO;
import com.dkha.dto.ScWorkersarchivesDTO;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.entity.ScStudentsEntity;
import com.dkha.entity.ScWorkersarchivesEntity;
import com.dkha.enums.ScUwbperRoleIdToUwbIdEnum;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.service.ScWorkersarchivesService;
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
 * 教职工档案
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScWorkersarchivesServiceImpl extends BaseServiceImpl<ScWorkersarchivesDao, ScWorkersarchivesEntity> implements ScWorkersarchivesService {

    @Autowired
    private SysAdminFeignClient sysAdminFeignClient;
    @Autowired
    private ScDormitorypersonDao scDormitorypersonDao;

    @Autowired
    private MinioUtil minioUtil;

    @Resource
    private ScUwbLabelDao scUwbLabelDao;
    @Value("${uwb.url2}")
    private String uwbUrl2;


    @Override
    public PageData<ScWorkersarchivesDTO> page(Map<String, Object> params) {
        IPage<ScWorkersarchivesEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScWorkersarchivesDTO.class);
    }

    @Override
    public List<ScWorkersarchivesDTO> list(Map<String, Object> params) {
        List<ScWorkersarchivesEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScWorkersarchivesDTO.class);
    }

    private QueryWrapper<ScWorkersarchivesEntity> getWrapper(Map<String, Object> params){
        String scWaname = (String)params.get("scWaname");
        String scEmpno = (String)params.get("scEmpno");
        String scStatus = (String)params.get("scStatus");
        QueryWrapper<ScWorkersarchivesEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(scEmpno), "sc_empno", scEmpno);
        wrapper.like(StringUtils.isNotBlank(scWaname), "sc_waname", scWaname);
        wrapper.eq(StringUtils.isNotBlank(scStatus), "sc_status", scStatus);
        return wrapper;
    }

    @Override
    public ScWorkersarchivesDTO get(String id) {
        ScWorkersarchivesEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScWorkersarchivesDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScWorkersarchivesDTO dto) {
        ScWorkersarchivesEntity entity = ConvertUtils.sourceToTarget(dto, ScWorkersarchivesEntity.class);
        QueryWrapper<ScWorkersarchivesEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("sc_empno",dto.getScEmpno()).or().eq("sc_idno",dto.getScIdno());
        List<ScWorkersarchivesEntity> entityList = baseDao.selectList(queryWrapper);
        for (ScWorkersarchivesEntity scWorkersarchivesEntity : entityList) {
            if(scWorkersarchivesEntity.getScEmpno().equals(dto.getScEmpno())){
                throw new RenException("职工编号已存在,请重新输入");
            }
            if(scWorkersarchivesEntity.getScIdno().equals(dto.getScIdno())){
                throw new RenException("职工身份证号已存在,请重新输入");
            }
        }
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScWorkersarchivesDTO dto) {
        ScWorkersarchivesEntity entity = ConvertUtils.sourceToTarget(dto, ScWorkersarchivesEntity.class);
        QueryWrapper<ScWorkersarchivesEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("sc_empno",dto.getScEmpno()).or().eq("sc_idno",dto.getScIdno()).or().eq(StringUtils.isNotBlank(dto.getScUwbid()),"sc_uwbid",dto.getScUwbid()).or()
                .eq(StringUtils.isNotBlank(dto.getScRfcardid()),"sc_rfcardid",dto.getScRfcardid());
        List<ScWorkersarchivesEntity> entityList = baseDao.selectList(queryWrapper);
        for (ScWorkersarchivesEntity scWorkersarchivesEntity : entityList) {
            if(!scWorkersarchivesEntity.getScWaid().equals(dto.getScWaid())) {
                if (scWorkersarchivesEntity.getScEmpno().equals(dto.getScEmpno())) {
                    throw new RenException("职工编号已存在,请重新输入");
                }
                if (scWorkersarchivesEntity.getScIdno().equals(dto.getScIdno())) {
                    throw new RenException("职工身份证号已存在,请重新输入");
                }

                if(StringUtils.isNotBlank(dto.getScUwbid()) && dto.getScUwbid().equals(scWorkersarchivesEntity.getScUwbid())){
                    throw new RenException("UWB标签已存在,请重新输入");
                }
                if(StringUtils.isNotBlank(dto.getScRfcardid()) && dto.getScRfcardid().equals(scWorkersarchivesEntity.getScRfcardid())){
                    throw new RenException("RFID已存在,请重新输入");
                }
            }
        }

        ScWorkersarchivesDTO dtoOld = this.get(String.valueOf(dto.getScWaid()));
        // 职工的信息更新推送通知星网云联更新角色相关电围规则
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
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ScWorkersarchivesDTO dto = this.get(String.valueOf(id));
        // 职工的信息删除推送通知星网云联更新角色相关电围规则
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

            Result staffStatus = sysAdminFeignClient.getByType("staffStatus");
            Result employType = sysAdminFeignClient.getByType("employType");

            List<LinkedHashMap> staffStatusList = (List<LinkedHashMap>) staffStatus.getData();
            List<LinkedHashMap> employTypeList = (List<LinkedHashMap>) employType.getData();
            List<ScWorkersarchivesEntity> entityList = new ArrayList<>();
            //职工编号
            List<String> empnoList = new ArrayList<>();
            //身份证
            List<String> idNoList = new ArrayList<>();
            //重复身份证
            List<String> repeatIdNoList = new ArrayList<>();
            //重复职工编号
            List<String> repeatNoList = new ArrayList<>();

            for (List<Object> objects : dataList) {
                String empno = objects.get(3).toString();
                for (int i = 0; i < entityList.size(); i++) {
                    ScWorkersarchivesEntity scWorkersarchivesEntity = entityList.get(i);
                    if(scWorkersarchivesEntity.getScIdno().equals(objects.get(1).toString())){
                        repeatIdNoList.add(objects.get(1).toString());
                    }
                    if(scWorkersarchivesEntity.getScEmpno().equals(empno)){
                        repeatNoList.add(empno);
                    }
                }

                QueryWrapper<ScWorkersarchivesEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sc_empno",empno);

                ScWorkersarchivesEntity scWorkersarchivesEntity = baseDao.selectOne(queryWrapper);
                if(scWorkersarchivesEntity == null){
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("sc_idno",objects.get(1).toString());
                    ScWorkersarchivesEntity scWorkersarchivesEntity1 = baseDao.selectOne(queryWrapper);
                    if(scWorkersarchivesEntity1 != null && objects.get(1).toString().equals(scWorkersarchivesEntity1.getScIdno())){
                        idNoList.add(objects.get(1).toString());
                        //continue;
                    }

                    scWorkersarchivesEntity = new ScWorkersarchivesEntity();
                    scWorkersarchivesEntity.setScWaname(objects.get(0).toString());
                    scWorkersarchivesEntity.setScIdno(objects.get(1).toString());
                    scWorkersarchivesEntity.setScPhonenum(objects.get(2).toString());
                    scWorkersarchivesEntity.setScEmpno(empno);
                    //职业类型
                    for (LinkedHashMap linkedHashMap : employTypeList) {
                        if(linkedHashMap.get("dictLabel").toString().equals(objects.get(4).toString())){
                            scWorkersarchivesEntity.setScEmptype(StringUtils.isNotBlank(linkedHashMap.get("dictValue").toString()) ? Integer.valueOf(linkedHashMap.get("dictValue").toString()) : null);
                            break;
                        }
                    }
                    scWorkersarchivesEntity.setScHiredate(objects.get(5).toString());
                    //在职状态
                    for (LinkedHashMap linkedHashMap : staffStatusList) {
                        if(linkedHashMap.get("dictLabel").toString().equals(objects.get(6).toString())){
                            scWorkersarchivesEntity.setScStatus(StringUtils.isNotBlank(linkedHashMap.get("dictValue").toString()) ? Integer.valueOf(linkedHashMap.get("dictValue").toString()) : null);
                            break;
                        }
                    }
                    int genderByIdCard = IdcardUtil.getGenderByIdCard(objects.get(1).toString());
                    scWorkersarchivesEntity.setScWasex(genderByIdCard);
                    entityList.add(scWorkersarchivesEntity);
                }else{
                    empnoList.add(empno);
                }
            }
            if(repeatIdNoList.size() > 0){
                throw new RenException("导入失败,以下身份证号重复: " + repeatIdNoList);
            }
            if(repeatNoList.size() > 0){
                throw new RenException("导入失败,以下职工编号重复: " + repeatNoList);
            }
            if(empnoList.size() > 0){
                throw new RenException("导入失败,以下职工编号已存在: " + empnoList);
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
            throw new RenException("该图片命名格式不正确: " + originalFilename);
        }
        ScWorkersarchivesEntity scWorkersarchivesEntity = baseDao.selectOne(new QueryWrapper<ScWorkersarchivesEntity>().eq("sc_empno", s[0]));
        if (scWorkersarchivesEntity != null){

            JSONObject jsonObject = null;
            try {
                jsonObject = minioUtil.uploadFile(multipartFile.getBytes(),"staff", originalFilename.substring(0,originalFilename.indexOf(".")), suffix);
            } catch (Exception e) {
                throw new RenException("图片上传失败");
            }
            ScWorkersarchivesEntity entity = new ScWorkersarchivesEntity();
            entity.setScWaid(scWorkersarchivesEntity.getScWaid());
            entity.setScPhotoimg(jsonObject.getString("path"));
            baseDao.updateById(entity);

        }else{
            throw new RenException("该职工编号不存在:" + s[0]);
        }


//        List<String> empno = new ArrayList<>();
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
//            empno.add(s[0]);
//            try {
//                JSONObject jsonObject = minioUtil.uploadFile(multipartFile.getBytes(),"staff", originalFilename.substring(0,originalFilename.indexOf(".")), suffix);
//                imgUrl.add(jsonObject.getString("url"));
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RenException("上传失败");
//            }
//        }
//        List<ScWorkersarchivesEntity> entityList = baseDao.getByScNos(empno);
//        for (ScWorkersarchivesEntity scWorkersarchivesEntity : entityList) {
//            for (int i = 0; i < empno.size(); i++) {
//                String s = empno.get(i);
//                if(s.equals(scWorkersarchivesEntity.getScEmpno())){
//                    scWorkersarchivesEntity.setScPhotoimg(imgUrl.get(i));
//                    empno.remove(s);
//                    break;
//                }
//            }
//        }
//        if(empno.size() > 0) {
//            throw new RenException("该职工编号不存在:" + empno);
//        }
//        if(entityList.size() == 0){
//            throw new RenException("该职工编号不存在:" + empno);
//        }else{
//            updateBatchById(entityList);
//        }
    }

    @Override
    public void importRegisterInfoExcel(MultipartFile file) {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            List<String> empnoList = new ArrayList<>();
            List<String> uwbList = new ArrayList<>();
            List<String> fridList = new ArrayList<>();


            List<String> uwbListE = new ArrayList<>();
            List<String> fridListE = new ArrayList<>();

            List<String> uwbListC = new ArrayList<>();
            List<String> fridListC = new ArrayList<>();
            ExcelSheetVO excelSheetVO = ExcelUtils.importExcel(file.getInputStream(), suffix);
            List<List<Object>> dataList = excelSheetVO.getDataList();

            for (List<Object> objects : dataList) {
                String empno = objects.get(1).toString();
                empnoList.add(empno);
                if(uwbList.contains(objects.get(3).toString())){
                    uwbListC.add(objects.get(3).toString());
                }
                if(fridList.contains(objects.get(2).toString())){
                    fridListC.add(objects.get(2).toString());
                }

                uwbList.add(objects.get(3).toString());
                fridList.add(objects.get(2).toString());
            }
            QueryWrapper<ScWorkersarchivesEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("sc_uwbid",uwbList).or().in("sc_rfcardid",fridList);
            List<ScWorkersarchivesEntity> scWorkersarchivesEntities = baseDao.selectList(queryWrapper);
            for (ScWorkersarchivesEntity scWorkersarchivesEntity : scWorkersarchivesEntities) {
                for (List<Object> objects : dataList) {
                    if(objects.get(2).toString().equals(scWorkersarchivesEntity.getScRfcardid())){
                        fridListE.add(scWorkersarchivesEntity.getScEmpno());
                    }
                    if(objects.get(3).toString().equals(scWorkersarchivesEntity.getScUwbid())){
                        uwbListE.add(scWorkersarchivesEntity.getScEmpno());
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
                throw  new RenException("以下职工编号,RFID已存在,请确认后导入" + fridListE);
            }
            if(uwbListE.size() > 0){
                throw  new RenException("以下职工编号,UWB标签已存在,请确认后导入" + uwbListE);
            }

            List<ScWorkersarchivesEntity> entityList = baseDao.getByScNos(empnoList);
            for (ScWorkersarchivesEntity scWorkersarchivesEntity : entityList) {
                for (List<Object> objects : dataList) {
                    if(objects.get(1).toString().equals(scWorkersarchivesEntity.getScEmpno())){
                        scWorkersarchivesEntity.setScRfcardid(objects.get(2).toString());
                        scWorkersarchivesEntity.setScUwbid(objects.get(3).toString());
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
    public List<ScWorkersarchivesDTO> getWorkesByIds(List<Long> id) {

        List<ScWorkersarchivesEntity> workersarchivesEntities = new ArrayList<>();
        for (Long aLong : id) {
            QueryWrapper<ScWorkersarchivesEntity> entityQueryWrapper = new QueryWrapper<>();
            entityQueryWrapper.eq("sc_waid",aLong);
            ScWorkersarchivesEntity scWorkersarchivesEntity = baseDao.selectOne(entityQueryWrapper);
            workersarchivesEntities.add(scWorkersarchivesEntity);
        }
        return ConvertUtils.sourceToTarget(workersarchivesEntities,ScWorkersarchivesDTO.class);
        //return baseDao.getStudentsInfo(pIds,flag);
    }

    @Override
    public List<String> getAllId() {
        return baseDao.getAllId();
    }

    /**
     * 职工的信息更新推送通知星网云联更新角色相关电围规则
     * @param dto
     * @param dtoOld
     */
    private void uwbPersonfenceCacheUpdate(ScWorkersarchivesDTO dto, ScWorkersarchivesDTO dtoOld){
        String newUwbId = dto.getScUwbid();
        String oldUwbId = dtoOld.getScUwbid();
        if(!newUwbId.equals(oldUwbId) && oldUwbId != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if(("教师".equals(dict_label) && dto.getScEmptype() == 0) || ("保洁".equals(dict_label) && dto.getScEmptype() == 1) || ("保安".equals(dict_label) && dto.getScEmptype() == 2)){
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
     * 职工的信息删除推送通知星网云联更新角色相关电围规则
     * @param dto
     */
    private void uwbPersonfenceCacheDelete(ScWorkersarchivesDTO dto){
        if(dto != null && !"".equals(dto.getScUwbid()) && dto.getScUwbid() != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if(("教师".equals(dict_label) && dto.getScEmptype() == 0) || ("保洁".equals(dict_label) && dto.getScEmptype() == 1) || ("保安".equals(dict_label) && dto.getScEmptype() == 2)){
                    curUwbPerRoleId = String.valueOf(curMap.get("dict_value"));
                    break;
                }
            }
            if(!"".equals(curUwbPerRoleId)){
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonParamDelete = new JSONObject();
                jsonParamDelete.put(curUwbPerRoleId, new int[]{Integer.parseInt(dto.getScUwbid())});
                jsonParam.put("delete", jsonParamDelete);

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
     * 职工的信息批量更新推送通知星网云联更新角色相关电围规则
     * @param entityList
     */
    private void uwbPersonfenceCacheBatchUpdate(List<ScWorkersarchivesEntity> entityList){
            // 星网对应的人员角色ID
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            Map<Integer, String> map = new HashMap<>();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                String dict_value = String.valueOf(curMap.get("dict_value"));
                if("学生".equals(dict_label)){
                    map.put(ScUwbperRoleIdToUwbIdEnum.STUDENT.code(), dict_value);
                } else if("教师".equals(dict_label)){
                    map.put(ScUwbperRoleIdToUwbIdEnum.TEACHER.code(), dict_value);
                } else if("保洁".equals(dict_label)){
                    map.put(ScUwbperRoleIdToUwbIdEnum.CLEANING.code(), dict_value);
                } else if("保安".equals(dict_label)){
                    map.put(ScUwbperRoleIdToUwbIdEnum.SECURITYSTAFF.code(), dict_value);
                } else if("重要设备".equals(dict_label)){
                    map.put(ScUwbperRoleIdToUwbIdEnum.IMPEQUIPMENT.code(), dict_value);
                } else if("访客".equals(dict_label)){
                    map.put(ScUwbperRoleIdToUwbIdEnum.VISITOR.code(), dict_value);
                }
            }

            Map<String, List<Integer>> mapUwbPerRoleId = new HashMap<>();
            for(ScWorkersarchivesEntity entity : entityList){
                String curDepartmentId = map.get(entity.getScEmptype());
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
