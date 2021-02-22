package com.dkha.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.SpringContextUtils;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dao.ScDormitoryfloorDao;
import com.dkha.dao.ScModbusdevicedcDao;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitoryfloorEntity;
import com.dkha.entity.ScModbusdevicedcEntity;
import com.dkha.excel.ScModbusdevicedcExcel;
import com.dkha.service.ScModbusdevicedcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 功能描述 智能控灯设备
 *
 * @author jinbiao
 * @date 2020/9/1
 * @param: null
 * @return
 */
@Slf4j
public class ScModbusdevicedcSLDataListener extends AnalysisEventListener<ScModbusdevicedcExcel> {

   /* private ScModbusdevicedcDao scModbusdevicedcDao;
    private ScModbusdevicedcService scModbusdevicedcService;*/
    /**
     * 批处理阈值
     */
    private static final int BATCH_COUNT = 2;
    List<ScModbusdevicedcExcel> list = new ArrayList<ScModbusdevicedcExcel>(BATCH_COUNT);
    /**
     * 楼层楼栋信息处理
     */
    ScDormitoryfloorDao scDormitoryfloorDao = SpringContextUtils.getBean(ScDormitoryfloorDao.class);
    //房间信息
    ScDormitoryDao scDormitoryDao = SpringContextUtils.getBean(ScDormitoryDao.class);

    @Override
    public void invoke(ScModbusdevicedcExcel scModbusdevicedcExcel, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(scModbusdevicedcExcel));
        list.add(scModbusdevicedcExcel);
     /*   if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }*/
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
            List<ScModbusdevicedcEntity> excelList = new ArrayList<>();
            // TODO: 2020/8/29   错误提示信息
            //空
            Set nullList = new HashSet();
            //复复
            Set repeatList = new HashSet();
            //己过期
            Set expiredList = new HashSet();
            //不存在
            Set nExistList = new HashSet();
            for (ScModbusdevicedcExcel scModbusdevicedcExcel : list) {
                ScModbusdevicedcEntity sentity = new ScModbusdevicedcEntity();

                try {
                    sentity = excelCheck(scModbusdevicedcExcel, sentity);
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
                ScModbusdevicedcService scModbusdevicedcService = SpringContextUtils.getBean(ScModbusdevicedcService.class);
                scModbusdevicedcService.insertBatch(excelList);
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
            if (CollectionUtils.isNotEmpty(nExistList)) {
                throw new RenException("不存在的数据为:" + nExistList);
            }
            if (CollectionUtils.isNotEmpty(expiredList)) {
                throw new RenException("己过期时间:" + repeatList);
            }

        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    private ScModbusdevicedcEntity excelCheck(ScModbusdevicedcExcel sc, ScModbusdevicedcEntity sentity) throws ParseException {

        if (StringUtils.isEmpty(sc.getMbdDevicename())) {
            throw new RenException("设备名称为空");
        }
        if (StringUtils.isNotEmpty(sc.getMbdDevicesn())) {
            QueryWrapper<ScModbusdevicedcEntity> queryWrapper = new QueryWrapper<>();
            ScModbusdevicedcDao scModbusdevicedcDao = SpringContextUtils.getBean(ScModbusdevicedcDao.class);
            List<ScModbusdevicedcEntity> mdbList = scModbusdevicedcDao.selectList(queryWrapper
                    .eq("mbd_devicesn", sc.getMbdDevicesn()));

            if (mdbList.size() >= 1) {
                throw new RenException("重复设备序列号" + sc.getMbdDevicesn());
            }

        } else {
            throw new RenException("设备序列号为空");
        }
        if (null == sc.getMbdExpirydate()) {
            throw new RenException("有效期为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(sc.getMbdExpirydate());
        if (System.currentTimeMillis() > parse.getTime()) {
            throw new RenException("己过期" + sc.getMbdExpirydate());
        }
        if (StringUtils.isEmpty(sc.getMbdUwbaddr())) {
            throw new RenException("安装位置为空");
        }
        String[] split = sc.getMbdUwbaddr().split("-");
        ScDormitoryfloorEntity dfName = new ScDormitoryfloorEntity();
        if (split.length < 1 || StringUtils.isEmpty(split[0])) {
            throw new RenException("楼栋为空");
        } else {
            //楼栋信息
            dfName = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                    .eq("df_floorname", split[0])
                    .eq("df_type", Constant.FAIL));
            if (null == dfName) {
                throw new RenException("楼栋:" + split[0] + "不存在");
            }
        }
        ScDormitoryfloorEntity scDormitoryfloorEntity = new ScDormitoryfloorEntity();
        if (split.length > 1) {
            //楼层信息
            scDormitoryfloorEntity = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                    .eq("df_floorname", split[1])
                    .eq("df_type", Constant.SUCCESS)
                    .eq("df_parentid",dfName.getDfFloorid()));
            if (null == scDormitoryfloorEntity) {
                throw new RenException("楼层:" + split[1] + "不存在");
            }
        }
        ScDormitoryEntity scDormitoryEntity = new ScDormitoryEntity();
        if (split.length > 2) {
            //房间信息
            scDormitoryEntity = scDormitoryDao.selectOne(new QueryWrapper<ScDormitoryEntity>()
                    .eq("dr_num", split[2])
                    .eq("df_floorid", scDormitoryfloorEntity.getDfFloorid()));
            if (null == scDormitoryEntity) {
                throw new RenException("房间:" + split[2] + "不存在");
            }
        }


        BeanUtils.copyProperties(sc, sentity);
        sentity.setMbdUwbaddr(dfName.getDfFloorid().toString() + (null != scDormitoryfloorEntity.getDfFloorid() ? "," +
                scDormitoryfloorEntity.getDfFloorid().toString() : "") + (null != scDormitoryEntity.getDrId() ? "," +
                scDormitoryEntity.getDrId().toString() : ""));
        sentity.setMbdNetwork(Constant.FAIL);
        sentity.setMbdDevicetype(1);
        sentity.setMdbStatus(Constant.FAIL);
        sentity.setMbdSetupdate(new Date());
        sentity.setMbdExpirydate(parse);
        return sentity;
    }
}