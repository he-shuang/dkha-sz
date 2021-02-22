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
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dao.ScDormitoryfloorDao;
import com.dkha.dao.ScModbusdevicedcDao;
import com.dkha.dto.DeviceMassge;
import com.dkha.dto.ScModbusdevicedcDTO;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitoryfloorEntity;
import com.dkha.entity.ScModbusdevicedcEntity;
import com.dkha.enums.OperationTypeEnum;
import com.dkha.excel.listener.ScModbusdevicedcPIRDataListener;
import com.dkha.excel.ScModbusdevicedcExcel;
import com.dkha.excel.listener.ScModbusdevicedcPMDataListener;
import com.dkha.excel.listener.ScModbusdevicedcSLDataListener;
import com.dkha.service.ScModbusdevicedcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScModbusdevicedcServiceImpl extends BaseServiceImpl<ScModbusdevicedcDao, ScModbusdevicedcEntity> implements ScModbusdevicedcService {

    @Autowired
    private ScDormitoryfloorServiceImpl scDormitoryfloorService;
    @Autowired
    private ScModbusdevicedcDao scModbusdevicedcDao;
    @Autowired
    private ScDormitoryfloorDao scDormitoryfloorDao;
    @Autowired
    private ScDormitoryDao scDormitoryDao;
    @Value("${uwb.url2}")
    private String uwbUrl2;
    @Override
    public PageData<ScModbusdevicedcDTO> page(Map<String, Object> params) {
        paramsToLike(params,"mbdDevicename");
        IPage<ScModbusdevicedcEntity> page = baseDao.getPages(
                getPage(params, null, true),
                params);
        PageData<ScModbusdevicedcDTO> pageData = getPageData(page, ScModbusdevicedcDTO.class);
        return pageData;
    }

    @Override
    public List<ScModbusdevicedcDTO> list(Map<String, Object> params) {
        List<ScModbusdevicedcEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScModbusdevicedcDTO.class);
    }

    private QueryWrapper<ScModbusdevicedcEntity> getWrapper(Map<String, Object> params) {
        //设备名称查询条件
        String mbdDevicename = (String) params.get("mbdDevicename");
        //设备状态查询条件
        String mdbStatus = (String) params.get("mdbStatus");
        //设备类型查询
        String mbdDevicetype = (String) params.get("mbdDevicetype");

        QueryWrapper<ScModbusdevicedcEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(mbdDevicename), "mbd_devicename", mbdDevicename);
        wrapper.like(StringUtils.isNotBlank(mdbStatus), "mdb_status", mdbStatus);
        wrapper.like(StringUtils.isNotBlank(mbdDevicetype), "mbd_devicetype", mbdDevicetype);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScModbusdevicedcDTO get(String id) {
        ScModbusdevicedcEntity entity = baseDao.selectById(id);

        ScModbusdevicedcDTO scModbusdevicedcDTO = ConvertUtils.sourceToTarget(entity, ScModbusdevicedcDTO.class);
        if (scModbusdevicedcDTO != null) {
            scModbusdevicedcDTO.setMbdUwbaddr(entity.getMbdUwbaddr().split(","));
        }
        return scModbusdevicedcDTO;
    }

    @Override
    public ScModbusdevicedcDTO info(String id) {
        ScModbusdevicedcEntity entity = baseDao.selectById(id);

        ScModbusdevicedcDTO scModbusdevicedcDTO = ConvertUtils.sourceToTarget(entity, ScModbusdevicedcDTO.class);
        scModbusdevicedcDTO.setMbdUwbaddr(entity.getMbdUwbaddr().split(","));
        String[] split = entity.getMbdUwbaddr().split(",");
        int j = 0;
        for (String s : split) {
            String name = scDormitoryfloorService.findName(s);
            Arrays.fill(split, j, j + 1, name);
            j++;
        }
        scModbusdevicedcDTO.setMbdUwbaddr(split);
        return scModbusdevicedcDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScModbusdevicedcDTO dto) {
        ScModbusdevicedcEntity entity = ConvertUtils.sourceToTarget(dto, ScModbusdevicedcEntity.class);

        ScModbusdevicedcEntity devicesn = baseDao.selectOne(new QueryWrapper<ScModbusdevicedcEntity>()
                .eq("mbd_devicesn", dto.getMbdDevicesn()));
//        ScModbusdevicedcEntity devicename = baseDao.selectOne(new QueryWrapper<ScModbusdevicedcEntity>()
//                .eq("mbd_devicename",dto.getMbdDevicename()));

        if (devicesn != null) {
            throw new RenException("该设备序列号已存在，请重新输入");
        }
//        if (devicename!=null){
//            throw new RenException("该设备名称已存在，请重新输入");
//        }

        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getMbdUwbaddr();
        for (String addr : tfSetupaddr) {
            entity.setMbdUwbaddr(entity.getMbdUwbaddr() == null || entity.getMbdUwbaddr().trim().length() == 0 ?
                    addr :
                    entity.getMbdUwbaddr() + "," + addr);
        }
        entity.setMbdSetupdate(new Date());

        insert(entity);
        // 星网云联推送
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.ADD.getCode());
//        sendMsg(dto,deviceMassge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScModbusdevicedcDTO dto) {
        ScModbusdevicedcEntity entity = ConvertUtils.sourceToTarget(dto, ScModbusdevicedcEntity.class);

        ScModbusdevicedcEntity devicesn = baseDao.selectOne(new QueryWrapper<ScModbusdevicedcEntity>()
                .eq("mbd_devicesn", dto.getMbdDevicesn()));
//        ScModbusdevicedcEntity devicename = baseDao.selectOne(new QueryWrapper<ScModbusdevicedcEntity>()
//                .eq("mbd_devicename", dto.getMbdDevicename()));

        if (devicesn != null) {
            if (!devicesn.getMbdId().equals(dto.getMbdId()) && devicesn.getMbdDevicesn().equals(dto.getMbdDevicesn())) {
                throw new RenException("该设备序列号已存在，请重新输入");
            }
        }
//        if (devicename!=null){
//            if (!devicesn.getMbdId().equals(dto.getMbdId())&&devicename.getMbdDevicename().equals(dto.getMbdDevicename())){
//                throw new RenException("该设备名称已存在，请重新输入");
//            }
//        }

        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getMbdUwbaddr();
        for (String addr : tfSetupaddr) {
            entity.setMbdUwbaddr(entity.getMbdUwbaddr() == null || entity.getMbdUwbaddr().trim().length() == 0 ?
                    addr :
                    entity.getMbdUwbaddr() + "," + addr);
        }
        entity.setMbdSetupdate(new Date());

        updateById(entity);
        // 推送设备信息星网云联用
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.UPDATE.getCode());
//        sendMsg(dto,deviceMassge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        //逻辑删除
        //logicDelete(ids, ScModbusdevicedcEntity.class);

        //物理删除
        baseDao.deleteById(id);
        // 推送设备信息星网云联用
        ScModbusdevicedcDTO scModbusdevicedcDTO = new ScModbusdevicedcDTO();
        scModbusdevicedcDTO.setMbdId(Long.valueOf(id));
        DeviceMassge deviceMassge = new DeviceMassge();
        deviceMassge.setOperation(OperationTypeEnum.DELETE.getCode());
        sendMsg(scModbusdevicedcDTO,deviceMassge);
    }

    @Override
    public List<ScModbusdevicedcDTO> getTypeMessage(String type) {
        return baseDao.getTypeMessage(type);
    }

    @Override
    public void updateNetwork(Long mbdId) {
        baseDao.updateNetwork(mbdId);
    }

    @Override
    public void importPIRExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RenException("上传文件为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), ScModbusdevicedcExcel.class, new ScModbusdevicedcPIRDataListener())
                    .sheet().headRowNumber(2).doRead();
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    @Override
    public void importPMExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RenException("上传文件为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), ScModbusdevicedcExcel.class, new ScModbusdevicedcPMDataListener())
                    .sheet().headRowNumber(2).doRead();
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    @Override
    public void importSLExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RenException("上传文件为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), ScModbusdevicedcExcel.class, new ScModbusdevicedcSLDataListener())
                    .sheet().headRowNumber(2).doRead();
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    @Override
    public void updateBatchNetwork(int network, List<Long> ids) {
        baseDao.updateBatchNetwork(network,ids);
    }

    public void sendMsg(ScModbusdevicedcDTO dto,DeviceMassge deviceMassge){
        String curUrlSaveUser = uwbUrl2 + "/message/message/mqWs";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("needRecord", false);
        jsonParam.put("type", "equipment");
        deviceMassge.setDeid(String.valueOf(dto.getMbdId()));
        deviceMassge.setDevicename(dto.getMbdDevicename());
        deviceMassge.setStatus(dto.getMdbStatus());
        deviceMassge.setType(String.valueOf(dto.getMbdDevicetype()));
        jsonParam.put("message", JSON.toJSON(deviceMassge));
        CloseableHttpClientToInterface.doPost(curUrlSaveUser, "1", jsonParam.toString());
    }
}
