package com.dkha.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.UwbStatusInterface;
import com.dkha.dao.ScAidooreightDao;
import com.dkha.dao.ScAidooreightPersonlistDao;
import com.dkha.dao.ScUwbLabelDao;
import com.dkha.dao.ScVisitorrecordDao;
import com.dkha.dto.ScAidooreightPersonlistDTO;
import com.dkha.dto.ScUwbLabelToInfoDTO;
import com.dkha.dto.ScVisitorrecordDTO;
import com.dkha.dto.doorcontrol.AddPersonRequestInfo;
import com.dkha.entity.ScAidooreightEntity;
import com.dkha.entity.ScAidooreightPersonlistEntity;
import com.dkha.entity.ScDfmapbindingEntity;
import com.dkha.entity.ScVisitorrecordEntity;
import com.dkha.service.ScAidooreightPersonlistService;
import com.dkha.service.ScAidooreightService;
import com.dkha.service.ScDfmapbindingService;
import com.dkha.service.ScVisitorrecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 访客记录表
 *
 * @since v1.0.0 2020-08-23
 */
@Service
@Slf4j
public class ScVisitorrecordServiceImpl extends BaseServiceImpl<ScVisitorrecordDao, ScVisitorrecordEntity> implements ScVisitorrecordService {
    @Resource
    private ScUwbLabelDao scUwbLabelDao;
    @Autowired
    private ScDfmapbindingService scDfmapbindingService;
    @Value("${uwb.url}")
    private String uwbUrl;
    @Value("${uwb.url2}")
    private String uwbUrl2;

    @Autowired
    private ScAidooreightService scAidooreightService;

    @Autowired
    private ScAidooreightDao scAidooreightDao;

    @Autowired
    private ScAidooreightPersonlistService scAidooreightPersonlistService;

    @Autowired
    private ScAidooreightPersonlistDao scAidooreightPersonlistDao;

    @Override
    public PageData<ScVisitorrecordDTO> page(Map<String, Object> params) {
        // 转换成like
        paramsToLike(params, "vrName");

        // 分页
        int inowpage = Integer.parseInt((String) params.get("page"));
        int ipagesize = Integer.parseInt((String) params.get("limit"));
        int scol = ipagesize * (inowpage - 1);
        params.put("scol", scol);
        params.put("ipagesize", ipagesize);
        params.put("deviceNumber", params.get("deviceNumber"));
        // 查询
        List<ScVisitorrecordEntity> list = baseDao.getMyList(params);
        long total = baseDao.getMyCount(params);

        return getPageData(list, total, ScVisitorrecordDTO.class);
    }

    @Override
    public List<ScVisitorrecordDTO> list(Map<String, Object> params) {
        List<ScVisitorrecordEntity> entityList = baseDao.list(params);

        return ConvertUtils.sourceToTarget(entityList, ScVisitorrecordDTO.class);
    }

    private QueryWrapper<ScVisitorrecordEntity> getWrapper(Map<String, Object> params){

        QueryWrapper<ScVisitorrecordEntity> wrapper = new QueryWrapper<>();
        wrapper.apply("vr_vistorbegintime >=  #{startDate}");
        wrapper.apply("vr_vistorbegintime <=  #{endDate}");

        return wrapper;
    }

    @Override
    public ScVisitorrecordDTO get(String id) {
        ScVisitorrecordEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScVisitorrecordDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScVisitorrecordDTO dto) {
        ScVisitorrecordEntity entity = ConvertUtils.sourceToTarget(dto, ScVisitorrecordEntity.class);
        String vrUwbid = entity.getVrUwbid();
        QueryWrapper<ScVisitorrecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vr_uwbid",vrUwbid);
        queryWrapper.eq("vr_returnuwb",0);
        List<ScVisitorrecordEntity> scVisitorrecordEntities = baseDao.selectList(queryWrapper);
        if (scVisitorrecordEntities.size() > 0){
            throw new RenException("该卡已绑定");
        }

        // 更新电围指定访问楼层规则
        this.uwbFkRule(dto, true);
        // 访客的信息新增推送通知星网云联更新角色相关电围规则
        this.uwbPersonfenceCacheAdd(dto);
        // 人员信息有更新推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");

        insert(entity);

        List<ScAidooreightPersonlistDTO> personlistDTOS =  new ArrayList<>();
        List<ScAidooreightEntity> entities = scAidooreightDao.selectList(new QueryWrapper<ScAidooreightEntity>().eq("ae_devicetype", 1));
        for (ScAidooreightEntity scAidooreightEntity : entities) {
            AddPersonRequestInfo addPersonRequestInfo = new AddPersonRequestInfo();
            addPersonRequestInfo.setTotal(1);
            addPersonRequestInfo.setPersonSerial(entity.getVrId().toString());
            addPersonRequestInfo.setPersonName(entity.getVrName());
            addPersonRequestInfo.setPersonIdentifier(entity.getVrIdno());
            addPersonRequestInfo.setImagepath(entity.getVrPhoneimg());
            addPersonRequestInfo.setPersonInfoType(3);
            addPersonRequestInfo.setIcCardNo(entity.getVrUwbid());
            boolean b = scAidooreightService.addPersonToDeivce(scAidooreightEntity.getAeId().toString(), addPersonRequestInfo);
            if(!b){
                throw new RenException("人脸信息下发门禁失败");
            }else{
                ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
                scpersondto.setAeId(scAidooreightEntity.getAeId());
                scpersondto.setAeSerialnumber(scAidooreightEntity.getAeSerialnumber());
                scpersondto.setPersontype(4);
                scpersondto.setUsername(entity.getVrName());
                scpersondto.setPhotoimg(entity.getVrPhoneimg());
                scpersondto.setUserid(entity.getVrId());
                scpersondto.setUserno(entity.getVrId().toString());
                scpersondto.setSex(entity.getVrSex());
                scpersondto.setUpdateDate(new Date());
                personlistDTOS.add(scpersondto);
            }
            b = scAidooreightService.setDoorAuthority(scAidooreightEntity, entity.getVrId().toString(), 1, null);
            if(!b){
                log.error("人脸信息下发门禁失败");
            }
        }
        if(personlistDTOS.size() > 0){
            scAidooreightPersonlistService.insertBatchAndUpdate(personlistDTOS);
        }
        for (ScAidooreightEntity scAidooreightEntity : entities) {
            Integer count = scAidooreightPersonlistDao.selectCount(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id", scAidooreightEntity.getAeId()));
            scAidooreightEntity.setAeFacetotal(count);
            scAidooreightEntity.setAeFacedowntime(new Date());
        }
        scAidooreightService.updateBatchById(entities);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScVisitorrecordDTO dto) {
        ScVisitorrecordEntity entity = ConvertUtils.sourceToTarget(dto, ScVisitorrecordEntity.class);
        entity.setVrReturnuwb(0);
        String vrUwbid = entity.getVrUwbid();
        QueryWrapper<ScVisitorrecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vr_uwbid",vrUwbid);
        queryWrapper.eq("vr_returnuwb",0);
        List<ScVisitorrecordEntity> scVisitorrecordEntities = baseDao.selectList(queryWrapper);
        for (ScVisitorrecordEntity scVisitorrecordEntity : scVisitorrecordEntities) {
            if(!dto.getVrId().equals(scVisitorrecordEntity.getVrId()) && vrUwbid.equals(scVisitorrecordEntity.getVrUwbid())){
                throw new RenException("UWB编号已存在,请重新输入");
            }
        }

        // 更新电围指定访问楼层规则
        this.uwbFkRule(dto, true);
        // 访客的信息新增推送通知星网云联更新角色相关电围规则
        this.uwbPersonfenceCacheAdd(dto);
        // 人员信息有更新推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");

        List<ScAidooreightPersonlistDTO> personlistDTOS =  new ArrayList<>();
        List<ScAidooreightEntity> entities = scAidooreightDao.selectList(new QueryWrapper<ScAidooreightEntity>().eq("ae_devicetype", 1));
        for (ScAidooreightEntity scAidooreightEntity : entities) {
            AddPersonRequestInfo addPersonRequestInfo = new AddPersonRequestInfo();
            addPersonRequestInfo.setTotal(1);
            addPersonRequestInfo.setPersonSerial(dto.getVrId().toString());
            addPersonRequestInfo.setPersonName(dto.getVrName());
            addPersonRequestInfo.setPersonIdentifier(dto.getVrIdno());
            addPersonRequestInfo.setImagepath(dto.getVrPhoneimg());
            addPersonRequestInfo.setIcCardNo(dto.getVrUwbid());
            addPersonRequestInfo.setPersonInfoType(3);
            boolean b = scAidooreightService.addPersonToDeivce(scAidooreightEntity.getAeId().toString(), addPersonRequestInfo);
            if(!b){
                log.error("人脸信息下发门禁失败");
            }else{
                ScAidooreightPersonlistDTO scpersondto = new ScAidooreightPersonlistDTO();
                scpersondto.setAeId(scAidooreightEntity.getAeId());
                scpersondto.setAeSerialnumber(scAidooreightEntity.getAeSerialnumber());
                scpersondto.setPersontype(4);
                scpersondto.setUsername(dto.getVrName());
                scpersondto.setPhotoimg(dto.getVrPhoneimg());
                scpersondto.setUserid(dto.getVrId());
                scpersondto.setUserno(dto.getVrId().toString());
                scpersondto.setSex(dto.getVrSex());
                scpersondto.setUpdateDate(new Date());
                personlistDTOS.add(scpersondto);
            }
            b = scAidooreightService.setDoorAuthority(scAidooreightEntity, dto.getVrId().toString(), 1, null);
            if(!b){
                log.error("人脸信息下发门禁失败");
            }
        }
        if(personlistDTOS.size() > 0){
            scAidooreightPersonlistService.insertBatchAndUpdate(personlistDTOS);
        }
        for (ScAidooreightEntity scAidooreightEntity : entities) {
            Integer count = scAidooreightPersonlistDao.selectCount(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id", scAidooreightEntity.getAeId()));
            scAidooreightEntity.setAeFacetotal(count);
            scAidooreightEntity.setAeFacedowntime(new Date());
        }
        scAidooreightService.updateBatchById(entities);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScVisitorrecordEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void returnCard(String vrId, Long userId) {
        ScVisitorrecordDTO dto = this.get(vrId);
        // 更新电围指定访问楼层规则
        this.uwbFkRule(dto, false);
        // 访客的还卡推送通知星网云联更新角色相关电围规则
        this.uwbPersonfenceCacheReturnCard(dto);
        // 人员信息有更新推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");
        // 删除推送通知星网云联电围解除相应标签的规则
        int[] arrParam = {Integer.parseInt(dto.getVrUwbid())};
        CloseableHttpClientToInterface.uwbUnbindTagAllFence(uwbUrl2, Arrays.toString(arrParam), "1");

        ScVisitorrecordEntity scVisitorrecordEntity = new ScVisitorrecordEntity();
        scVisitorrecordEntity.setVrId(Long.valueOf(vrId));
        scVisitorrecordEntity.setVrReturnuwb(1);
        scVisitorrecordEntity.setVrVistorendtime(new Date());
        baseDao.updateById(scVisitorrecordEntity);
        List<ScAidooreightEntity> entities = scAidooreightDao.selectList(new QueryWrapper<ScAidooreightEntity>().eq("ae_devicetype", 1));
        for (ScAidooreightEntity entity : entities) {
            boolean b = scAidooreightService.deletePersonFromDeviceBySerial(entity.getAeId().toString(), vrId);
            if(b){
                scAidooreightPersonlistDao.delete(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id",entity.getAeId()).eq("userid",vrId));
                entity.setAeFacetotal(entity.getAeFacetotal() - 1);
            }

        }
        scAidooreightService.updateBatchById(entities);

    }

    @Override
    public List<Long> getListUwbId(Long dfFloorid) {
        return baseDao.getListUwbId(dfFloorid);
    }

    @Override
    public ScUwbLabelToInfoDTO getMyScUwbLabelToInfo(String vrId) {
        return baseDao.getMyScUwbLabelToInfo(vrId);
    }

    /**
     * 更新电围指定访问楼层规则
     * @param dto
     * @param flag  true 新增访客 false 访客还卡
     */
    private void uwbFkRule(ScVisitorrecordDTO dto, Boolean flag){
        List<Long> tagIds = this.getListUwbId(dto.getDfFloorid());
        String tagIdsStr = "";
        // 新增访客
        if(flag){ tagIdsStr += "," + dto.getVrUwbid(); }
        for(Long curId : tagIds){
            // 访客还卡
            if(!flag) { continue; }
            tagIdsStr += "," + curId;
        }
        if(!"".equals(tagIdsStr)){ tagIdsStr = tagIdsStr.substring(1, tagIdsStr.length()); }
        ScDfmapbindingEntity scDfmapbindingEntity = scDfmapbindingService.getByDfFloorid(dto.getDfFloorid());
        if(scDfmapbindingEntity == null){
            throw new RenException("没匹配上对应UWB楼层信息");
        }
        String result = CloseableHttpClientToInterface.uwbFkRule(uwbUrl2, tagIdsStr, scDfmapbindingEntity.getMapId(), scDfmapbindingEntity.getFloor(), "1");
        String curStatus = UwbStatusInterface.UwbStatus(result);
        JSONObject statusObject = JSONObject.parseObject(curStatus);
        Integer statusCode = statusObject.getInteger("code");
        // 异常
        if(statusCode.intValue() != 0){
            throw new RenException(statusObject.getString("msg"));
        }
    }

    /**
     * 访客的信息新增推送通知星网云联更新角色相关电围规则
     */
    private void uwbPersonfenceCacheAdd(ScVisitorrecordDTO dto){
        if(!"".equals(dto.getVrUwbid()) && dto.getVrUwbid() != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if("访客".equals(dict_label)){
                    curUwbPerRoleId = String.valueOf(curMap.get("dict_value"));
                    break;
                }
            }
            if(!"".equals(curUwbPerRoleId)){
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonParamAdd = new JSONObject();
                jsonParamAdd.put(curUwbPerRoleId, new int[]{Integer.parseInt(dto.getVrUwbid())});
                jsonParam.put("add", jsonParamAdd);
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
     * 访客的还卡推送通知星网云联更新角色相关电围规则
     * @param dto
     */
    private void uwbPersonfenceCacheReturnCard(ScVisitorrecordDTO dto){
        // 访客角色信息有更新推送通知星网云联更新电围规则
        if(!"".equals(dto.getVrUwbid()) && dto.getVrUwbid() != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if("访客".equals(dict_label)){
                    curUwbPerRoleId = String.valueOf(curMap.get("dict_value"));
                    break;
                }
            }
            if(!"".equals(curUwbPerRoleId)){
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonParamDelete = new JSONObject();
                jsonParamDelete.put(curUwbPerRoleId, new int[]{Integer.parseInt(dto.getVrUwbid())});
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

}
