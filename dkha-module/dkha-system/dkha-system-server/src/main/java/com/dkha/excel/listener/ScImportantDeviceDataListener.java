package com.dkha.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.SpringContextUtils;
import com.dkha.commons.tools.utils.UwbStatusInterface;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dao.ScDormitoryfloorDao;
import com.dkha.dao.ScImportantDeviceDao;
import com.dkha.dao.ScUwbLabelDao;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitoryfloorEntity;
import com.dkha.entity.ScImportantDeviceEntity;
import com.dkha.entity.ScStudentsEntity;
import com.dkha.enums.ScUwbperRoleIdToUwbIdEnum;
import com.dkha.excel.ScImportantDeviceExcel;
import com.dkha.service.ScImportantDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 功能描述 导入重要设备信息
 *
 * @author jinbiao
 * @date 2020/9/1
 * @param: null
 * @return
 */
@Slf4j
public class ScImportantDeviceDataListener extends AnalysisEventListener<ScImportantDeviceExcel> {

   /* private ScModbusdevicedcDao scModbusdevicedcDao;
    private ScModbusdevicedcService scModbusdevicedcService;*/
    /**
     * 批处理阈值
     */
    private static final int BATCH_COUNT = 2;
    List<ScImportantDeviceExcel> list = new ArrayList<ScImportantDeviceExcel>(BATCH_COUNT);
    /**
     * 楼层楼栋信息处理
     */
    ScDormitoryfloorDao scDormitoryfloorDao = SpringContextUtils.getBean(ScDormitoryfloorDao.class);
    //房间信息
    ScDormitoryDao scDormitoryDao = SpringContextUtils.getBean(ScDormitoryDao.class);
    // UWB标签绑定
    ScUwbLabelDao scUwbLabelDao = SpringContextUtils.getBean(ScUwbLabelDao.class);
    @Value("${uwb.url2}")
    private String uwbUrl2;
    @Override
    public void invoke(ScImportantDeviceExcel scImportantDeviceExcel, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(scImportantDeviceExcel));
        list.add(scImportantDeviceExcel);
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        log.info("所有数据解析完成！");
    }

    private void saveData() {
        try {
            //错误数据
//            List<ScModbusdevicedcExcel> errorList = new ArrayList<>();
            //正确数据
            List<ScImportantDeviceEntity> excelList = new ArrayList<>();
            // TODO: 2020/8/29   错误提示信息
            //空
            Set nullList = new HashSet();
            //复复
            Set repeatList = new HashSet();
            //己过期
            Set expiredList = new HashSet();
            //不存在
            Set nExistList = new HashSet();
            for (ScImportantDeviceExcel scImportantDeviceExcel : list) {
                ScImportantDeviceEntity sentity = new ScImportantDeviceEntity();

                try {
                    sentity = excelCheck(scImportantDeviceExcel, sentity);
                } catch (Exception e) {
                    if (e.getMessage().contains(Constant.nullString)) {
                        nullList.add(e.getMessage());
                    }
//                    scModbusdevicedcExcel.setMessage(e.getMessage());
//                    nullList.add(scModbusdevicedcExcel);
                    if (e.getMessage().contains(Constant.repeatString)) {
                        repeatList.add(e.getMessage().replaceAll(Constant.repeatString, ""));
                    }
                    if (e.getMessage().contains(Constant.expiredString)) {
                        expiredList.add(e.getMessage().replaceAll(Constant.expiredString, ""));
                    }
                    if (e.getMessage().contains(Constant.nExistString)) {
                        nExistList.add(e.getMessage().replaceAll(Constant.nExistString, ""));
                    }
                    continue;
                }

                excelList.add(sentity);
            }
            if (CollectionUtils.isNotEmpty(excelList)) {
                uwbPersonfenceCacheBatchUpdate(excelList);
                ScImportantDeviceService scImportantDeviceService = SpringContextUtils.getBean(ScImportantDeviceService.class);
                scImportantDeviceService.insertBatch(excelList);
            }
           /* if (CollectionUtils.isNotEmpty(errorList)) {
                return errorList;
            }*/
            if (CollectionUtils.isNotEmpty(nullList)) {
                throw new RenException("必填数据不能为空"+nullList);
            }
            if (CollectionUtils.isNotEmpty(repeatList)) {
                throw new RenException("重复数据为:" + repeatList);
            }
            if (CollectionUtils.isNotEmpty(expiredList)) {
                throw new RenException("己过期时间:" + repeatList);
            }
            if (CollectionUtils.isNotEmpty(nExistList)) {
                throw new RenException("不存在的数据为:" + nExistList);
            }

        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    private ScImportantDeviceEntity excelCheck(ScImportantDeviceExcel sc, ScImportantDeviceEntity sentity) throws ParseException {

        if (StringUtils.isEmpty(sc.getImDevicename())) {
            throw new RenException("设备名称为空");
        }
        if (StringUtils.isNotEmpty(sc.getImDevicesn())) {
            QueryWrapper<ScImportantDeviceEntity> queryWrapper = new QueryWrapper<>();
            ScImportantDeviceDao scImportantDeviceDao = SpringContextUtils.getBean(ScImportantDeviceDao.class);
            List<ScImportantDeviceEntity> mdbList = scImportantDeviceDao.selectList(queryWrapper
                    .eq("im_devicesn", sc.getImDevicesn()));

            if (mdbList.size() >= 1) {
                throw new RenException("重复设备序列号为" + sc.getImDevicesn());
            }

        } else {
            throw new RenException("设备序列号为空");
        }

        if (null == sc.getImExpirydate()) {
            throw new RenException("有效期为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(sc.getImExpirydate());
        if (System.currentTimeMillis() > parse.getTime()) {
            throw new RenException("己过期" + sc.getImExpirydate());
        }
        if (StringUtils.isEmpty(sc.getImSetupaddr())) {
            throw new RenException("安装位置为空");
        }
        String[] split = sc.getImSetupaddr().split("-");
        if (split.length < 1 || StringUtils.isEmpty(split[0])) {
            throw new RenException("楼栋为空");
        }
        if (split.length < 2 || StringUtils.isEmpty(split[1])) {
            throw new RenException("楼层为空");
        }
   /*     if (split.length < 3 || StringUtils.isEmpty(split[2])) {
            throw new RenException("房间号为空");
        }*/
        //楼栋信息
        ScDormitoryfloorEntity dfName = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                .eq("df_floorname", split[0])
                .eq("df_type", Constant.FAIL));
        if (null == dfName) {
            throw new RenException("楼栋:" + split[0] + "不存在");
        }
        //楼层信息
        ScDormitoryfloorEntity scDormitoryfloorEntity = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                .eq("df_floorname", split[1])
                .eq("df_type", Constant.SUCCESS)
                .eq("df_parentid",dfName.getDfFloorid()));
        if (null == scDormitoryfloorEntity) {
            throw new RenException("楼层:" + split[1] + "不存在");
        }

        if (StringUtils.isEmpty(sc.getUwb())){
            throw new RenException("uwb编号为空");
        }
        if (!Constant.PATTERNNUM.matcher(sc.getUwb()).matches()) {
            throw new RenException("uwb编号不是数字:" + sc.getUwb());
        }
        //房间信息
   /*     ScDormitoryEntity scDormitoryEntity = scDormitoryDao.selectOne(new QueryWrapper<ScDormitoryEntity>()
                .eq("dr_num", split[2])
                .eq("df_floorid", scDormitoryfloorEntity.getDfFloorid()));
        if (null == scDormitoryEntity) {
            throw new RenException("房间:" + split[2] + "不存在");
        }*/
        BeanUtils.copyProperties(sc, sentity);
        sentity.setImSetupaddr(dfName.getDfFloorid()+","+scDormitoryfloorEntity.getDfFloorid());
        sentity.setImStatus(Constant.FAIL);
        sentity.setImExpirydate(parse);
        sentity.setImSetupdate(new Date());
        return sentity;
    }

    /**
     * 重要设备的信息批量更新推送通知星网云联更新角色相关电围规则
     * @param entityList
     */
    private void uwbPersonfenceCacheBatchUpdate(List<ScImportantDeviceEntity> entityList){
        // 星网对应的人员角色ID
        List<Map<String,Object>> uwbPerRoleNumList = scUwbLabelDao.getUwbPerRole();
        Map<Integer, String> map = new HashMap<>();
        for(int i = 0,sizei = uwbPerRoleNumList.size();i < sizei;i++) {
            Map<String, Object> curMap = uwbPerRoleNumList.get(i);
            String dict_label = String.valueOf(curMap.get("dict_label"));
            String dict_value = String.valueOf(curMap.get("dict_value"));
            if("重要设备".equals(dict_label)){
                map.put(ScUwbperRoleIdToUwbIdEnum.IMPEQUIPMENT.code(), dict_value);
                break;
            }
        }

        Map<String, List<Integer>> mapUwbPerRoleId = new HashMap<>();
        for(ScImportantDeviceEntity entity : entityList){
            String curDepartmentId = map.get(ScUwbperRoleIdToUwbIdEnum.STUDENT.code());
            List<Integer> curObj = mapUwbPerRoleId.get(curDepartmentId);
            if(curObj == null){
                List<Integer> newList = new ArrayList<>();
                newList.add(Integer.valueOf(entity.getUwb()));
                mapUwbPerRoleId.put(curDepartmentId, newList);
            } else {
                curObj.add(Integer.valueOf(entity.getUwb()));
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