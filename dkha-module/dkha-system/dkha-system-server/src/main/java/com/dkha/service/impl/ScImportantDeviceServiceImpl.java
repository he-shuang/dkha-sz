package com.dkha.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dkha.commons.mybatis.service.impl.CrudServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.UwbStatusInterface;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dao.ScDormitoryfloorDao;
import com.dkha.dao.ScImportantDeviceDao;
import com.dkha.dao.ScUwbLabelDao;
import com.dkha.dto.DeviceMassge;
import com.dkha.dto.ScImportantDeviceDTO;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitoryfloorEntity;
import com.dkha.entity.ScImportantDeviceEntity;
import com.dkha.enums.DeviceTypeEnum;
import com.dkha.enums.OperationTypeEnum;
import com.dkha.excel.ScImportantDeviceExcel;
import com.dkha.excel.listener.ScImportantDeviceDataListener;
import com.dkha.service.ScImportantDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 重要设备信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Service
public class ScImportantDeviceServiceImpl extends CrudServiceImpl<ScImportantDeviceDao, ScImportantDeviceEntity, ScImportantDeviceDTO> implements ScImportantDeviceService {
    @Autowired
    private ScDormitoryfloorServiceImpl scDormitoryfloorService;
    @Resource
    private ScUwbLabelDao scUwbLabelDao;
    @Autowired
    private ScDormitoryfloorDao scDormitoryfloorDao;
    @Autowired
    private ScDormitoryDao scDormitoryDao;
    @Value("${uwb.url2}")
    private String uwbUrl2;
    @Override
    public PageData<ScImportantDeviceDTO> page(Map<String, Object> params) {
        params.put("im_id", "desc");
        params.put("im_setupdate", "desc");
        paramsToLike(params,"imDevicename");
        IPage<ScImportantDeviceEntity> page =  baseDao.getPages(
                getPage(params, "im_setupdate", true),
                params);
        PageData<ScImportantDeviceDTO> pageData = getPageData(page, ScImportantDeviceDTO.class);
        List<ScImportantDeviceDTO> list = pageData.getList();
       /* int i = 0;
        for (ScImportantDeviceDTO scGatewaydcDTO : list) {
            int j = 0;
            String[] split = page.getRecords().get(i).getImSetupaddr().split(",");
            StringBuffer upaddr = new StringBuffer();

            if (split.length > 0) {
                ScDormitoryfloorEntity scDormitoryfloor = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                        .eq("df_floorid", split[0])
                        .eq("df_type", Constant.FAIL));
                upaddr.append(scDormitoryfloor.getDfFloorname());
                if (split.length > 1) {
                    ScDormitoryfloorEntity scDormitoryfloorEntity = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                            .eq("df_floorid", split[1])
                            .eq("df_type", Constant.SUCCESS)
                            .eq("df_parentid", scDormitoryfloor.getDfFloorid()));
                    upaddr.append("/" + scDormitoryfloorEntity.getDfFloorname());
                    if (split.length > 2) {
                        ScDormitoryEntity scDormitoryEntity = scDormitoryDao.selectOne(new QueryWrapper<ScDormitoryEntity>()
                                .eq("dr_id", split[2])
                                .eq("df_floorid", scDormitoryfloorEntity.getDfFloorid()));
                        upaddr.append("/" + scDormitoryEntity.getDrNum());
                    }
                }
            }
            scGatewaydcDTO.setImSetupaddr(upaddr.toString().split(","));
            i++;
        }*/

        return pageData;
    }

    public QueryWrapper<ScImportantDeviceEntity> getWrapper(Map<String, Object> params) {
        String gwName = (String) params.get("imDevicename");
        String gwState = (String) params.get("imStatus");

        QueryWrapper<ScImportantDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(gwName), "im_devicename", gwName);
        wrapper.like(StringUtils.isNotBlank(gwState), "im_status", gwState);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScImportantDeviceDTO dto) {
        ScImportantDeviceEntity entity = ConvertUtils.sourceToTarget(dto, ScImportantDeviceEntity.class);

        QueryWrapper<ScImportantDeviceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("im_devicesn",dto.getImDevicesn());
        ScImportantDeviceEntity scImportantDeviceEntity = baseDao.selectOne(queryWrapper);
        if(scImportantDeviceEntity != null){
            throw new RenException("设备序列号已存在,请重新输入");
        }
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uwb",dto.getUwb());
        scImportantDeviceEntity = baseDao.selectOne(queryWrapper);
        if(scImportantDeviceEntity != null){
            throw new RenException("设备UWB标签已存在,请重新输入");
        }

        // 新增角色的人员信息推送通知星网云联电围角色规则
        this.uwbPersonfenceCacheAdd(dto);
        // 设备新增推送通知星网云联
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.ADD.getCode());
//        this.sendMsg(dto,deviceMassge);
        // 人员信息有更新推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");

        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String join = String.join(",", dto.getImSetupaddr());
        entity.setImSetupaddr(join);

        entity.setImStatus(0);
        entity.setImSetupdate(new Date());
        insert(entity);

    }

    @Override
    public ScImportantDeviceDTO get(Long id) {
        ScImportantDeviceEntity entity = baseDao.selectById(id);

        ScImportantDeviceDTO scImportantDeviceDTO = ConvertUtils.sourceToTarget(entity, ScImportantDeviceDTO.class);
        if (scImportantDeviceDTO != null) {

            scImportantDeviceDTO.setImSetupaddr(entity.getImSetupaddr().split(","));
        }
        return scImportantDeviceDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScImportantDeviceDTO dto) {
        ScImportantDeviceEntity entity = ConvertUtils.sourceToTarget(dto, ScImportantDeviceEntity.class);

        ScImportantDeviceEntity devicesn = baseDao.selectOne(new QueryWrapper<ScImportantDeviceEntity>()
                .eq("im_devicesn", dto.getImDevicesn()));

        if (devicesn != null) {
            if (!devicesn.getImId().equals(dto.getImId()) && devicesn.getImDevicesn().equals(dto.getImDevicesn())) {
                throw new RenException("设备序列号已存在，请重新输入");
            }
        }
        devicesn = baseDao.selectOne(new QueryWrapper<ScImportantDeviceEntity>()
                .eq("uwb", dto.getUwb()));
        if (devicesn != null) {
            if (!devicesn.getImId().equals(dto.getImId()) && devicesn.getUwb().equals(dto.getUwb())) {
                throw new RenException("设备UWB标签已存在,请重新输入");
            }
        }

        ScImportantDeviceDTO dtoOld = this.get(dto.getImId());
        // 更新角色的人员信息推送通知星网云联电围角色规则
        this.uwbPersonfenceCacheUpdate(dto, dtoOld);
        // 设备更新推送通知星网云联
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.UPDATE.getCode());
//        this.sendMsg(dto,deviceMassge);
        // 人员信息有更新推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");
        // 标签变动
        if(!"".equals(dto.getUwb()) && dto.getUwb() != null && !"".equals(dtoOld.getUwb()) && dtoOld.getUwb() != null && !dto.getUwb().equals(dtoOld.getUwb())){
            // 标签不一致推送通知星网云联电围相应标签的规则替换
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("oldId", Integer.parseInt(dtoOld.getUwb()));
            jsonParam.put("newId", Integer.parseInt(dto.getUwb()));
            CloseableHttpClientToInterface.uwbReplaceTag(uwbUrl2, jsonParam.toString(), "1");
        } else if("".equals(dto.getUwb()) && !"".equals(dtoOld.getUwb()) && dtoOld.getUwb() != null){
            // 删除推送通知星网云联电围解除相应标签的规则
            int[] arrParam = {Integer.parseInt(dtoOld.getUwb())};
            CloseableHttpClientToInterface.uwbUnbindTagAllFence(uwbUrl2, Arrays.toString(arrParam), "1");
        }

        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String join = String.join(",", dto.getImSetupaddr());
        entity.setImSetupaddr(join);

        updateById(entity);

    }

    @Override
    public boolean deleteById(Serializable id) {
        ScImportantDeviceDTO dtoOld = this.get(Long.valueOf(String.valueOf(id)));
        // 删除角色的人员信息推送通知星网云联电围角色规则
        this.uwbPersonfenceCacheDelete(dtoOld);
        // 设备删除推送通知星网云联
        ScImportantDeviceDTO dto = new ScImportantDeviceDTO();
        dto.setImId(Long.parseLong(String.valueOf(id)));
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.DELETE.getCode());
//        this.sendMsg(dto,deviceMassge);
        // 人员信息删除推送通知星网云联
        CloseableHttpClientToInterface.uwbPersonPush(uwbUrl2, "1");
        // 删除推送通知星网云联电围解除相应标签的规则
        int[] arrParam = {Integer.parseInt(dtoOld.getUwb())};
        CloseableHttpClientToInterface.uwbUnbindTagAllFence(uwbUrl2, Arrays.toString(arrParam), "1");

        return SqlHelper.retBool(baseDao.deleteById(id));
    }

    @Override
    public void importExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RenException("上传文件为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), ScImportantDeviceExcel.class, new ScImportantDeviceDataListener())
                    .sheet().headRowNumber(2).doRead();
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    /**
     * 重要设备的信息新增推送通知星网云联更新角色相关电围规则
     * @param dto
     */
    private void uwbPersonfenceCacheAdd(ScImportantDeviceDTO dto){
        String newUwbId = dto.getUwb();
        if(!"".equals(newUwbId) && newUwbId != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if("重要设备".equals(dict_label)){
                    curUwbPerRoleId = String.valueOf(curMap.get("dict_value"));
                    break;
                }
            }
            if(!"".equals(curUwbPerRoleId)) {
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonParamAdd = new JSONObject();
                jsonParamAdd.put(curUwbPerRoleId, new int[]{Integer.parseInt(newUwbId)});
                jsonParam.put("add", jsonParamAdd);
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
     * 重要设备的信息更新推送通知星网云联更新角色相关电围规则
     * @param dto
     * @param dtoOld
     */
    private void uwbPersonfenceCacheUpdate(ScImportantDeviceDTO dto, ScImportantDeviceDTO dtoOld){
        String newUwbId = dto.getUwb();
        String oldUwbId = dtoOld.getUwb();
        if(!newUwbId.equals(oldUwbId) && oldUwbId != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if("重要设备".equals(dict_label)){
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
     * 重要设备的信息删除推送通知星网云联更新角色相关电围规则
     * @param dtoOld
     */
    private void uwbPersonfenceCacheDelete(ScImportantDeviceDTO dtoOld){
        if(dtoOld != null && !"".equals(dtoOld.getUwb()) && dtoOld.getUwb() != null){
            // 星网对应的人员角色ID
            String curUwbPerRoleId = "";
            List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
            for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
                Map<String, Object> curMap = uwbPerRoleNumList.get(i);
                String dict_label = String.valueOf(curMap.get("dict_label"));
                if("重要设备".equals(dict_label)){
                    curUwbPerRoleId = String.valueOf(curMap.get("dict_value"));
                    break;
                }
            }
            if(!"".equals(curUwbPerRoleId)) {
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonParamDelete = new JSONObject();
                jsonParamDelete.put(curUwbPerRoleId, new int[]{Integer.parseInt(dtoOld.getUwb())});
                jsonParam.put("delete", jsonParamDelete);
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
     *
     * @param dto
     * @param deviceMassge
     */
    private void sendMsg(ScImportantDeviceDTO dto, DeviceMassge deviceMassge){
        String curUrlSaveUser = uwbUrl2 + "/message/message/mqWs";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("needRecord", false);
        jsonParam.put("type", "equipment");
        deviceMassge.setDeid(String.valueOf(dto.getImId()));
        deviceMassge.setDevicename(dto.getImDevicename());
        deviceMassge.setStatus(dto.getImStatus());
        deviceMassge.setType(DeviceTypeEnum.DEVICE_TYPE_ZY.value());
        jsonParam.put("message", JSON.toJSON(deviceMassge));
        CloseableHttpClientToInterface.doPost(curUrlSaveUser, "1", jsonParam.toString());
    }
}
