package com.dkha.service.impl;

import cn.hutool.core.util.IdcardUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelSheetVO;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dao.ScTransformerdcDao;
import com.dkha.dto.*;
import com.dkha.entity.*;
import com.dkha.enums.DeviceTypeEnum;
import com.dkha.enums.OperationTypeEnum;
import com.dkha.excel.ScModbusdevicedcExcel;
import com.dkha.excel.ScTransformerdcImportExcel;
import com.dkha.excel.listener.ScModbusdevicedcSLDataListener;
import com.dkha.excel.listener.ScTransformerdcDataListener;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.service.ScTransformerdcService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 互感器设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScTransformerdcServiceImpl extends BaseServiceImpl<ScTransformerdcDao, ScTransformerdcEntity> implements ScTransformerdcService {

    @Autowired
    private SysAdminFeignClient sysAdminFeignClient;

    @Autowired
    private ScTransformerroomServiceImpl scTransformerroomService;
    @Autowired
    private ScDormitoryfloorServiceImpl scDormitoryfloorService;
    @Autowired
    private ScDormitoryServiceImpl scDormitoryService;
    @Autowired
    private ScTransformerdcServiceImpl scTransformerdcService;
    @Value("${uwb.url2}")
    private String uwbUrl2;
    @Override
    public PageData<ScTransformerdcDTO> page(Map<String, Object> params) {
        params.put("tf_id", "desc");
        params.put("tf_setupdate", "desc");
        IPage<ScTransformerdcEntity> page = baseDao.findPage(
                getPage(params, "tf_id", false), params
        );
        PageData<ScTransformerdcDTO> pageData = getPageData(page, ScTransformerdcDTO.class);
        List<ScTransformerdcDTO> list = pageData.getList();
        int i = 0;
        for (ScTransformerdcDTO scTransformerdcDTO : list) {
            int j = 0;
            String[] split = page.getRecords().get(i).getTfSetupaddr().split(",");
            for (String s : split) {
                String name = scDormitoryfloorService.findName(s);
                Arrays.fill(split, j, j + 1, name);
                j++;
            }
            scTransformerdcDTO.setTfSetupaddr(split);
            i++;
        }
        return pageData;
    }

    @Override
    public List<ScTransformerdcDTO> list(Map<String, Object> params) {
        List<ScTransformerdcEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScTransformerdcDTO.class);
    }

    private QueryWrapper<ScTransformerdcEntity> getWrapper(Map<String, Object> params) {
        //设备名称查询条件
        String tfDevicename = (String) params.get("tfDevicename");
        //设备状态查询条件
        String tfStatus = (String) params.get("tfStatus");

        QueryWrapper<ScTransformerdcEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(tfDevicename), "tf_devicename", tfDevicename);
        wrapper.like(StringUtils.isNotBlank(tfStatus), "tf_status", tfStatus);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScTransformerdcDTO get(String id) {
        ScTransformerdcEntity entity = baseDao.selectById(id);
        //根据ID查询电流互感器相关的数据
        ScTransformerdcDTO scTransformerdcDTO = ConvertUtils.sourceToTarget(entity, ScTransformerdcDTO.class);
        if (scTransformerdcDTO != null) {
            List<ScTransformerroomDTO> scTransformerroomAll = scTransformerroomService.findAllById(id);
            scTransformerdcDTO.setScTransformerroomDTOList(scTransformerroomAll);
            String[] split = entity.getTfSetupaddr().split(",");
            scTransformerdcDTO.setTfSetupaddr(entity.getTfSetupaddr().split(","));
        }
        return scTransformerdcDTO;
    }

    @Override
    public ScTransformerdcDTO info(String id) {
        ScTransformerdcEntity entity = baseDao.selectById(id);
        //根据ID查询电流互感器相关的数据
        ScTransformerdcDTO scTransformerdcDTO = ConvertUtils.sourceToTarget(entity, ScTransformerdcDTO.class);
        List<ScTransformerroomDTO> scTransformerroomAll = scTransformerroomService.findAllById(id);
        scTransformerdcDTO.setScTransformerroomDTOList(scTransformerroomAll);
        String[] split = entity.getTfSetupaddr().split(",");
        int j = 0;
        for (String s : split) {
            String name = scDormitoryfloorService.findName(s);
            Arrays.fill(split, j, j + 1, name);
            j++;
        }
        scTransformerdcDTO.setTfSetupaddr(split);
        return scTransformerdcDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScTransformerdcDTO dto) {
        ScTransformerdcEntity entity = ConvertUtils.sourceToTarget(dto, ScTransformerdcEntity.class);

        ScTransformerdcEntity devicesn = baseDao.selectOne(new QueryWrapper<ScTransformerdcEntity>()
                .eq("tf_devicesn", dto.getTfDevicesn()));
//        ScTransformerdcEntity devicename = baseDao.selectOne(new QueryWrapper<ScTransformerdcEntity>()
//                .eq("tf_devicename",dto.getTfDevicename()));

        if (devicesn != null) {
            throw new RenException("该设备序列号已存在，请重新输入");
        }
//        if (devicename!=null){
//            throw new RenException("该设备名称已存在，请重新输入");
//        }

        List<ScTransformerroomDTO> scTransformerroomDTOList = dto.getScTransformerroomDTOList();
        for (ScTransformerroomDTO scTransformerroomDTO : scTransformerroomDTOList) {
            ScTransformerroomEntity scTransformerroomEntity = scTransformerroomService.selectRoomById(scTransformerroomDTO.getDrId());
            if (scTransformerroomEntity != null) {
                throw new RenException("房间号:" + scTransformerroomEntity.getTfrDrroomno() + "已经拥有电流设备");
            }
        }
        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getTfSetupaddr();
        for (String addr : tfSetupaddr) {
            entity.setTfSetupaddr(entity.getTfSetupaddr() == null || entity.getTfSetupaddr().trim().length() == 0 ?
                    addr :
                    entity.getTfSetupaddr() + "," + addr);
        }
        entity.setTfSetupdate(new Date());
        //电流互感器新增
        insert(entity);

        //新增电流互感器所关联房间
        //增添房间关联信息
        for (ScTransformerroomDTO scTransformerroomDTO : scTransformerroomDTOList) {
            if (scTransformerroomDTO.getDrId() != null) {
                ScDormitoryEntity scDormitoryEntity = scDormitoryService.selectById(scTransformerroomDTO.getDrId());
                scTransformerroomDTO.setTfrDrroomno(scDormitoryEntity.getDrNum());
                scTransformerroomDTO.setTfDevicesn(entity.getTfDevicesn());
                scTransformerroomDTO.setTfId(entity.getTfId());
                scTransformerroomDTO.setTfrRelationdate(new Date());
            }
        }
        //互感器与房间关联信息新增
        List<ScTransformerroomEntity> scTransformerroomEntities = ConvertUtils.sourceToTarget(scTransformerroomDTOList, ScTransformerroomEntity.class);
        scTransformerroomService.insertBatch(scTransformerroomEntities);
        scTransformerroomService.deleteByNull();
        // 推送设备信息星网云联用
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.ADD.getCode());
//        sendMsg(dto,deviceMassge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScTransformerdcDTO dto) {
        ScTransformerdcEntity entity = ConvertUtils.sourceToTarget(dto, ScTransformerdcEntity.class);

//        ScTransformerdcEntity devicename = baseDao.selectOne(new QueryWrapper<ScTransformerdcEntity>()
//                .eq("tf_devicename",dto.getTfDevicename()));
        ScTransformerdcEntity devicesn = baseDao.selectOne(new QueryWrapper<ScTransformerdcEntity>()
                .eq("tf_devicesn", dto.getTfDevicesn()));

        if (devicesn != null) {
            if (!devicesn.getTfId().equals(dto.getTfId()) && devicesn.getTfDevicesn().equals(dto.getTfDevicesn())) {
                throw new RenException("该设备序列号已存在，请重新输入");
            }
        }
//        if (devicename!=null){
//            if (!devicesn.getTfId().equals(dto.getTfId())&&devicename.getTfDevicename().equals(dto.getTfDevicename())){
//                throw new RenException("该设备名称已存在，请重新输入");
//            }
//        }

        //新增电流互感器所关联房间
        List<ScTransformerroomDTO> scTransformerroomDTOList = dto.getScTransformerroomDTOList();
        for (ScTransformerroomDTO scTransformerroomDTO : scTransformerroomDTOList) {
            ScTransformerroomEntity scTransformerroomEntity = scTransformerroomService.selectRoomById(scTransformerroomDTO.getDrId());
//            if(scTransformerroomDTO.getTfId() == null && scTransformerroomEntity != null){
//                throw new RenException("房间号:" + scTransformerroomEntity.getTfrDrroomno() + "已经拥有电流设备");
//            }
            if (scTransformerroomEntity != null && !scTransformerroomEntity.getTfId().equals(dto.getTfId())) {
                throw new RenException("房间号:" + scTransformerroomEntity.getTfrDrroomno() + "已经拥有电流设备");
            }
        }

        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getTfSetupaddr();
        for (String addr : tfSetupaddr) {
            entity.setTfSetupaddr(entity.getTfSetupaddr() == null || entity.getTfSetupaddr().trim().length() == 0 ?
                    addr :
                    entity.getTfSetupaddr() + "," + addr);
        }
        //电流互感器修改
        updateById(entity);

        //增添房间关联信息
        for (ScTransformerroomDTO scTransformerroomDTO : scTransformerroomDTOList) {
            if (scTransformerroomDTO.getDrId() != null) {
                ScDormitoryEntity scDormitoryEntity = scDormitoryService.selectById(scTransformerroomDTO.getDrId());
                scTransformerroomDTO.setTfrDrroomno(scDormitoryEntity.getDrNum());
                scTransformerroomDTO.setTfDevicesn(entity.getTfDevicesn());
                scTransformerroomDTO.setTfId(entity.getTfId());
                scTransformerroomDTO.setTfrRelationdate(new Date());
            }
        }
        scTransformerroomService.deleteByGateId(String.valueOf(entity.getTfId()));
        //互感器与房间关联信息修改
        List<ScTransformerroomEntity> scTransformerroomEntities = ConvertUtils.sourceToTarget(scTransformerroomDTOList, ScTransformerroomEntity.class);
        scTransformerroomService.insertBatch(scTransformerroomEntities);
        scTransformerroomService.deleteByNull();
        // 推送设备信息星网云联用
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.UPDATE.getCode());
//        sendMsg(dto,deviceMassge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        //逻辑删除
        //logicDelete(ids, ScTransformerdcEntity.class);

        scTransformerroomService.deleteByGateId(id);
        //物理删除
        baseDao.deleteById(id);
        // 推送设备信息星网云联用
        ScTransformerdcDTO dto = new ScTransformerdcDTO();
        dto.setTfId(Long.valueOf(id));
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.DELETE.getCode());
//        sendMsg(dto,deviceMassge);
    }

   /* @Override
    public void importInfoExcel(MultipartFile file) {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        try {

            ExcelSheetVO excelSheetVO = ExcelUtils.importExcel(file.getInputStream(), suffix);
            List<List<Object>> dataList = excelSheetVO.getDataList();

//            Result resultEducation = sysAdminFeignClient.getByType("education");
//            Result resultStatus = sysAdminFeignClient.getByType("studentStatus");
//
//            List<LinkedHashMap> dictDataEducation = (List<LinkedHashMap>) resultEducation.getData();
//            List<LinkedHashMap> dictDataStatus = (List<LinkedHashMap>) resultStatus.getData();
            List<ScTransformerdcEntity> entityList = new ArrayList<>();
            for (List<Object> objects : dataList) {
                String tfDevicesn = objects.get(1).toString();
                QueryWrapper<ScTransformerdcEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tf_devicesn", tfDevicesn);
                ScTransformerdcEntity scTransformerdcEntity = baseDao.selectOne(queryWrapper);
                if (scTransformerdcEntity == null) {
                    scTransformerdcEntity = new ScTransformerdcEntity();
//                    scTransformerdcEntity.setScStuname(objects.get(0).toString());
//                    scTransformerdcEntity.setScIdno(objects.get(1).toString());
//                    scTransformerdcEntity.setScPhonenum(objects.get(2).toString());
                    scTransformerdcEntity.setTfDevicesn(tfDevicesn);
//                    for (LinkedHashMap linkedHashMap : dictDataEducation) {
//                        if(linkedHashMap.get("dictLabel").toString().equals(objects.get(4).toString())){
//                            scStudentsEntity.setScEducation(StringUtils.isNotBlank(linkedHashMap.get("dictValue").toString()) ? Integer.valueOf(linkedHashMap.get("dictValue").toString()) : null);
//                            break;
//                        }
//                    }
//                    scStudentsEntity.setScRegisterdate(objects.get(5).toString());
//                    for (LinkedHashMap linkedHashMap : dictDataStatus) {
//                        if(linkedHashMap.get("dictLabel").toString().equals(objects.get(6).toString())){
//                            scStudentsEntity.setScStatus(StringUtils.isNotBlank(linkedHashMap.get("dictValue").toString()) ? Integer.valueOf(linkedHashMap.get("dictValue").toString()) : null);
//                            break;
//                        }
//                    }
//                    int genderByIdCard = IdcardUtil.getGenderByIdCard(objects.get(1).toString());
//                    scStudentsEntity.setScSex(genderByIdCard);
                    entityList.add(scTransformerdcEntity);
                }
            }
            if (entityList.size() > 0) {
                insertBatch(entityList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException("导入失败,请检查Excel数据!");
        }
    }*/

    @Override
    public void importInfoExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RenException("上传文件为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), ScTransformerdcImportExcel.class, new ScTransformerdcDataListener())
                    .sheet().headRowNumber(3).doRead();
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }
    public void sendMsg(ScTransformerdcDTO dto,DeviceMassge deviceMassge){
        String curUrlSaveUser = uwbUrl2 + "/message/message/mqWs";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("needRecord", false);
        jsonParam.put("type", "equipment");
        AlarmMassge alarmMassge = new AlarmMassge();
        deviceMassge.setDeid(String.valueOf(dto.getTfId()));
        deviceMassge.setDevicename(dto.getTfDevicename());
        deviceMassge.setStatus(dto.getTfStatus());
        deviceMassge.setType(DeviceTypeEnum.DEVICE_TYPE_DL.value());
        jsonParam.put("message", JSON.toJSON(alarmMassge));
        CloseableHttpClientToInterface.doPost(curUrlSaveUser, "1", jsonParam.toString());
    }
}
