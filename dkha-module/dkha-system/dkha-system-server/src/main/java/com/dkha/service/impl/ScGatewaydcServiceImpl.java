package com.dkha.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.redis.RedisUtils;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dao.ScDormitoryfloorDao;
import com.dkha.dao.ScGatebusdeviceDao;
import com.dkha.dao.ScGatewaydcDao;
import com.dkha.dto.DeviceMassge;
import com.dkha.dto.ScGatebusdeviceDTO;
import com.dkha.dto.ScGatebusdeviceNumDTO;
import com.dkha.dto.ScGatewaydcDTO;
import com.dkha.entity.*;
import com.dkha.enums.DeviceTypeEnum;
import com.dkha.enums.OperationTypeEnum;
import com.dkha.excel.ScGatewaydcImportExcel;
import com.dkha.excel.listener.ScGatewaydcDataListener;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.service.ScGatebusdeviceService;
import com.dkha.service.ScGatewaydcService;
import com.dkha.service.ScModbusdevicedcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 网关302设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScGatewaydcServiceImpl extends BaseServiceImpl<ScGatewaydcDao, ScGatewaydcEntity> implements ScGatewaydcService {

    @Autowired
    private ScGatebusdeviceService scGatebusdeviceService;
    @Autowired
    private ScModbusdevicedcService scModbusdevicedcService;
    @Autowired
    private ScDormitoryfloorServiceImpl scDormitoryfloorService;
    @Autowired
    private ScGatebusdeviceDao scGatebusdeviceDao;
    @Autowired
    private ScDormitoryfloorDao scDormitoryfloorDao;
    @Autowired
    private ScDormitoryDao scDormitoryDao;
    @Autowired
    private SysAdminFeignClient sysAdminFeignClient;

    @Autowired
    private RedisUtils redisUtils;
    @Value("${uwb.url2}")
    private String uwbUrl2;

    @Override
    public PageData<ScGatewaydcDTO> page(Map<String, Object> params) {
        params.put("mbd_devicetype", "asc");
        params.put("gw_name", "asc");
        paramsToLike(params,"gwName");
        IPage<ScGatewaydcEntity> page = baseDao.getPages(
                getPage(params, "mbd_devicetype", true),
                params);
        PageData<ScGatewaydcDTO> pageData = getPageData(page, ScGatewaydcDTO.class);
        return pageData;
    }

    @Override
    public List<ScGatewaydcDTO> list(Map<String, Object> params) {
        List<ScGatewaydcEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScGatewaydcDTO.class);
    }

    private QueryWrapper<ScGatewaydcEntity> getWrapper(Map<String, Object> params) {
        String gwName = (String) params.get("gwName");
        String gwState = (String) params.get("gwState");

        QueryWrapper<ScGatewaydcEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(gwName), "gw_name", gwName);
        wrapper.like(StringUtils.isNotBlank(gwState), "gw_state", gwState);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScGatewaydcDTO get(String id) {
        ScGatewaydcEntity entity = baseDao.selectById(id);
        //根据id查询302网关设备的数据
        ScGatewaydcDTO scGatewaydcDTO = ConvertUtils.sourceToTarget(entity, ScGatewaydcDTO.class);
        if (scGatewaydcDTO != null) {
            //根据id查询302网关设备下的所有RS485总线
            List<ScGatebusdeviceDTO> scGatebusdeviceAll = scGatebusdeviceService.findAllById(id);
            scGatewaydcDTO.setScGatebusdeviceDTOList(scGatebusdeviceAll);
            scGatewaydcDTO.setGwSetupaddr(entity.getGwSetupaddr().split(","));
        }
        return scGatewaydcDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScGatewaydcDTO dto) {
        ScGatewaydcEntity entity = ConvertUtils.sourceToTarget(dto, ScGatewaydcEntity.class);

        ScGatewaydcEntity devicesn = baseDao.selectOne(new QueryWrapper<ScGatewaydcEntity>()
                .eq("gw_sn", dto.getGwSn()));
//        ScGatewaydcEntity devicename = baseDao.selectOne(new QueryWrapper<ScGatewaydcEntity>()
//                .eq("gw_name",dto.getGwName()));

        if (devicesn != null) {
            throw new RenException("该设备序列号已存在，请重新输入");
        }
//        if (devicename!=null){
//            throw new RenException("该设备名称已存在，请重新输入");
//        }

        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getGwSetupaddr();
        for (String addr : tfSetupaddr) {
            entity.setGwSetupaddr(entity.getGwSetupaddr() == null || entity.getGwSetupaddr().trim().length() == 0 ?
                    addr :
                    entity.getGwSetupaddr() + "," + addr);
        }
        entity.setGwSetupdate(new Date());

        //302网关设备存入
        insert(entity);

        //新增网关设备时新增RS485总线
        List<ScGatebusdeviceDTO> scGatebusdeviceDTOList = dto.getScGatebusdeviceDTOList();
        //PIR设备封装集合
        List<String> pirIds = new ArrayList<>();
        //智能灯控设备封装集合
        List<String> intelligentIds = new ArrayList<>();
        //PM设备封装集合
        List<String> pmIds = new ArrayList<>();
        for (ScGatebusdeviceDTO scGatebusdeviceDTO : scGatebusdeviceDTOList) {
            //最多添加32个设备
            if (scGatebusdeviceDTOList.size() > 32) {
                throw new RenException("设备数量超过上限，最多添加32个设备");
            }
            if (StringUtils.isBlank(String.valueOf(scGatebusdeviceDTO.getGbdAddr()))) {
                throw new RenException("接口编号不能为空");
            }
            //添加PIR设备
            if (scGatebusdeviceDTO.getGbdDevicetype().equals("0")) {
                if (pirIds.size() > 0 && pirIds.contains(scGatebusdeviceDTO.getMbdId())) {
                    throw new RenException("PIR设备重复");
                }
                pirIds.add(scGatebusdeviceDTO.getMbdId());
            }
            //添加智能灯控设备
            if (scGatebusdeviceDTO.getGbdDevicetype().equals("1")) {
//                if (intelligentIds.size() > 0 && intelligentIds.contains(scGatebusdeviceDTO.getMbdId())) {
//                    throw new RenException("智能灯控设备重复");
//                }
                intelligentIds.add(scGatebusdeviceDTO.getMbdId());
            }
            //添加PM设备
            if (scGatebusdeviceDTO.getGbdDevicetype().equals("2")) {
                if (pmIds.size() > 0 && pmIds.contains(scGatebusdeviceDTO.getMbdId())) {
                    throw new RenException("PM设备重复");
                }
                pmIds.add(scGatebusdeviceDTO.getMbdId());
            }
            //同一总线同个接口编码最多出现3个
            /*  List<ScGatebusdeviceNumDTO> scGatebusdeviceNumDTOList = ConvertUtils.sourceToTarget(scGatebusdeviceDTOList, ScGatebusdeviceNumDTO.class);
          for (int i = 0; i < scGatebusdeviceNumDTOList.size(); i++) {
                if (Collections.frequency(scGatebusdeviceNumDTOList, scGatebusdeviceNumDTOList.get(i)) >= 3) {
                    throw new RenException("同一总线编号:" + scGatebusdeviceNumDTOList.get(i).getGbdLineNum() + "下同个接口编码:" + scGatebusdeviceNumDTOList.get(i).getGbdAddr() + "最多出现三个");
                }
            }*/
            //新增对应的302网关设备
            scGatebusdeviceDTO.setGbdId(String.valueOf(entity.getGwId()));
            //新增对应的302网关设备编号
            scGatebusdeviceDTO.setGbdDevicesn(entity.getGwSn());
            scModbusdevicedcService.updateNetwork(Long.valueOf(scGatebusdeviceDTO.getMbdId()));
        }
//        // 星网云联推送
//        DeviceMassge deviceMassge = new DeviceMassge();
//        deviceMassge.setOperation(OperationTypeEnum.ADD.getCode());
//        sendMsg(dto,deviceMassge);
        //RS485总线存入
        List<ScGatebusdeviceEntity> scGatebusdeviceEntities = ConvertUtils.sourceToTarget(scGatebusdeviceDTOList, ScGatebusdeviceEntity.class);
        scGatebusdeviceService.insertBatch(scGatebusdeviceEntities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScGatewaydcDTO dto) {
        ScGatewaydcEntity entity = ConvertUtils.sourceToTarget(dto, ScGatewaydcEntity.class);

        ScGatewaydcEntity devicesn = baseDao.selectOne(new QueryWrapper<ScGatewaydcEntity>()
                .eq("gw_sn", dto.getGwSn()));
//        ScGatewaydcEntity devicename = baseDao.selectOne(new QueryWrapper<ScGatewaydcEntity>()
//                .eq("gw_name", dto.getGwName()));

        if (devicesn != null) {
            if (!devicesn.getGwId().equals(dto.getGwId()) && devicesn.getGwSn().equals(dto.getGwSn())) {
                throw new RenException("该设备序列号已存在，请重新输入");
            }
        }
//        if (devicename!=null){
//            if (!devicesn.getGwId().equals(dto.getGwId())&&devicename.getGwName().equals(dto.getGwName())){
//                throw new RenException("该设备名称已存在，请重新输入");
//            }
//        }

        //修改网关设备时新增RS485总线
        List<ScGatebusdeviceDTO> scGatebusdeviceDTOList = dto.getScGatebusdeviceDTOList();
        //PIR设备封装集合
        List<String> pirIds = new ArrayList<>();
        //智能灯控设备封装集合
        List<String> intelligentIds = new ArrayList<>();
        //PM设备封装集合
        List<String> pmIds = new ArrayList<>();
        for (ScGatebusdeviceDTO scGatebusdeviceDTO : scGatebusdeviceDTOList) {
            //最多添加32个设备
            if (scGatebusdeviceDTOList.size() > 32) {
                throw new RenException("设备数量超过上限，最多添加32个设备");
            }
            if (StringUtils.isBlank(String.valueOf(scGatebusdeviceDTO.getGbdAddr()))) {
                throw new RenException("接口编号不能为空");
            }
            //添加PIR设备
            if (scGatebusdeviceDTO.getGbdDevicetype().equals("0")) {
                if (pirIds.size() > 0 && pirIds.contains(scGatebusdeviceDTO.getMbdId())) {
                    throw new RenException("PIR设备重复");
                }
                pirIds.add(scGatebusdeviceDTO.getMbdId());
            }
            //添加智能灯控设备
            if (scGatebusdeviceDTO.getGbdDevicetype().equals("1")) {
//                if (intelligentIds.size() > 0 && intelligentIds.contains(scGatebusdeviceDTO.getMbdId())) {
//                    throw new RenException("智能灯控设备重复");
//                }
                intelligentIds.add(scGatebusdeviceDTO.getMbdId());
            }
            //添加PM设备
            if (scGatebusdeviceDTO.getGbdDevicetype().equals("2")) {
                if (pmIds.size() > 0 && pmIds.contains(scGatebusdeviceDTO.getMbdId())) {
                    throw new RenException("PM设备重复");
                }
                pmIds.add(scGatebusdeviceDTO.getMbdId());
            }
            //同一总线同个接口编码最多出现3个
          /*  List<ScGatebusdeviceNumDTO> scGatebusdeviceNumDTOList = ConvertUtils.sourceToTarget(scGatebusdeviceDTOList, ScGatebusdeviceNumDTO.class);
            for (int i = 0; i < scGatebusdeviceNumDTOList.size(); i++) {
                if (Collections.frequency(scGatebusdeviceNumDTOList, scGatebusdeviceNumDTOList.get(i)) >= 3) {
                    throw new RenException("同一总线编号:" + scGatebusdeviceNumDTOList.get(i).getGbdLineNum() + "下同个接口编码:" + scGatebusdeviceNumDTOList.get(i).getGbdAddr() + "最多出现三个");
                }
            }*/
            //新添对应的302网关设备
            scGatebusdeviceDTO.setGbdId(String.valueOf(entity.getGwId()));
            //新添对应的302网关设备编号
            scGatebusdeviceDTO.setGbdDevicesn(entity.getGwSn());
            scModbusdevicedcService.updateNetwork(Long.valueOf(scGatebusdeviceDTO.getMbdId()));
            scGatebusdeviceService.update(scGatebusdeviceDTO);
        }

        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getGwSetupaddr();
        for (String addr : tfSetupaddr) {
            entity.setGwSetupaddr(entity.getGwSetupaddr() == null || entity.getGwSetupaddr().trim().length() == 0 ?
                    addr :
                    entity.getGwSetupaddr() + "," + addr);
        }
        entity.setGwSetupdate(new Date());

        //302网关设备修改
        updateById(entity);
        // 星网云联推送
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.UPDATE.getCode());
        sendMsg(dto,deviceMassge);
        //RS485总线删除
        scGatebusdeviceService.deleteByGateId(String.valueOf(entity.getGwId()));
        //RS485总线新增
        List<ScGatebusdeviceEntity> scGatebusdeviceEntities = ConvertUtils.sourceToTarget(scGatebusdeviceDTOList, ScGatebusdeviceEntity.class);
        scGatebusdeviceService.insertBatch(scGatebusdeviceEntities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        //修改设备是否组网状态
        List<ScGatebusdeviceEntity> scGatebusdeviceEntityList = scGatebusdeviceDao.selectList(new QueryWrapper<ScGatebusdeviceEntity>()
                .eq("gbd_id", id));
        List<Long> ids = new ArrayList<>();
        for (ScGatebusdeviceEntity entity : scGatebusdeviceEntityList) {
            ids.add(Long.valueOf(entity.getMbdId()));
        }
        if(ids.size() > 0){
            scModbusdevicedcService.updateBatchNetwork(0,ids);
        }
        //删除挂载设备
        scGatebusdeviceService.deleteByGateId(id);
        //物理删除
        baseDao.deleteById(id);

        // 星网云联推送
        ScGatewaydcDTO scGatewaydcDTO = new ScGatewaydcDTO();
        scGatewaydcDTO.setGwId(Long.valueOf(id));
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.DELETE.getCode());
        sendMsg(scGatewaydcDTO,deviceMassge);
    }


    @Override
    public void importExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RenException("上传文件为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), ScGatewaydcImportExcel.class, new ScGatewaydcDataListener()).sheet().headRowNumber(4).doRead();
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    public void sendMsg(ScGatewaydcDTO dto, DeviceMassge deviceMassge) {
        String curUrlSaveUser = uwbUrl2 + "/message/message/mqWs";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("needRecord", false);
        jsonParam.put("type", "equipment");
        deviceMassge.setDeid(String.valueOf(dto.getGwId()));
        deviceMassge.setDevicename(dto.getGwName());
        deviceMassge.setStatus(dto.getGwState());
        deviceMassge.setType(DeviceTypeEnum.DEVICE_TYPE_WG.value());
        jsonParam.put("message", JSON.toJSON(deviceMassge));
        CloseableHttpClientToInterface.doPost(curUrlSaveUser, "1", jsonParam.toString());
    }

    @Override
    public ScGatewaydcDTO getInfo(String id) {
        /**
         * 查询封装
         */
        QueryWrapper<ScGatewaydcEntity> queryWrapper = new QueryWrapper<>();
        ScGatewaydcEntity scGatewaydcEntity = this.selectById(id);
        ScGatewaydcDTO scGatewaydcDTO = ConvertUtils.sourceToTarget(scGatewaydcEntity, ScGatewaydcDTO.class);
        String[] split = scGatewaydcEntity.getGwSetupaddr().split(",");
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
        scGatewaydcDTO.setGwSetupaddr(upaddr.toString().split(","));

        List<ScGatebusdeviceEntity> scGatebusdeviceEntityList = scGatebusdeviceDao.selectList(new QueryWrapper<ScGatebusdeviceEntity>()
                .eq("gbd_id", id));
        List<ScGatebusdeviceDTO> scGatebusdeviceDTOS = ConvertUtils.sourceToTarget(scGatebusdeviceEntityList, ScGatebusdeviceDTO.class);
        List<ScGatebusdeviceDTO> newList = scGatebusdeviceDTOS.stream().peek(x -> {
            ScModbusdevicedcEntity scModbusdevicedcEntity = scModbusdevicedcService.selectById(x.getMbdId());
            if(scModbusdevicedcEntity != null){
                x.setMbdDevicename(scModbusdevicedcEntity.getMbdDevicename());
            }
            Result eqType = sysAdminFeignClient.getByType("eq_type");
            List<LinkedHashMap> eqTypeList = (List<LinkedHashMap>) eqType.getData();
            for (LinkedHashMap linkedHashMap : eqTypeList) {
                if (linkedHashMap.get("dictValue").equals(x.getGbdDevicetype())) {
                    x.setGbdDevicetype(linkedHashMap.get("dictLabel").toString());
                }
            }
        }).collect(Collectors.toList());
        scGatewaydcDTO.setScGatebusdeviceDTOList(newList);
        return scGatewaydcDTO;
    }

}
