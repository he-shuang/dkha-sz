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
import com.dkha.dao.ScPersonidequipDao;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitoryfloorEntity;
import com.dkha.entity.ScPersonidequipEntity;
import com.dkha.excel.ScPersonidequipImportExcel;
import com.dkha.service.ScPersonidequipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 功能描述 导入人证设备信息
 *
 * @author jinbiao
 * @date 2020/9/1
 * @param: null
 * @return
 */
@Slf4j
public class ScPersonidequipDataListener extends AnalysisEventListener<ScPersonidequipImportExcel> {

   /* private ScModbusdevicedcDao scModbusdevicedcDao;
    private ScModbusdevicedcService scModbusdevicedcService;*/
    /**
     * 批处理阈值
     */
    private static final int BATCH_COUNT = 2;
    List<ScPersonidequipImportExcel> list = new ArrayList<ScPersonidequipImportExcel>(BATCH_COUNT);
    /**
     * 楼层楼栋信息处理
     */
    ScDormitoryfloorDao scDormitoryfloorDao = SpringContextUtils.getBean(ScDormitoryfloorDao.class);
    //房间信息
    ScDormitoryDao scDormitoryDao = SpringContextUtils.getBean(ScDormitoryDao.class);
    @Override
    public void invoke(ScPersonidequipImportExcel scPersonidequipImportExcel, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(scPersonidequipImportExcel));
        list.add(scPersonidequipImportExcel);
        /*if (list.size() >= BATCH_COUNT) {
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
            List<ScPersonidequipEntity> excelList = new ArrayList<>();
            // TODO: 2020/8/29   错误提示信息
            //空
            Set nullList = new HashSet();
            //复复
            Set repeatList = new HashSet();
            //己过期
            Set expiredList = new HashSet();
            //不存在
            Set nExistList = new HashSet();
            //IP格式不正确
            Set errorIpList = new HashSet();
            for (ScPersonidequipImportExcel scPersonidequipImportExcel : list) {
                ScPersonidequipEntity sentity = new ScPersonidequipEntity();

                try {
                    sentity = excelCheck(scPersonidequipImportExcel, sentity);
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
                    if (e.getMessage().contains(Constant.errorIp)) {
                        errorIpList.add(e.getMessage().replaceAll(Constant.errorIp, ""));
                    }
                    continue;
                }

                excelList.add(sentity);
            }
            if (CollectionUtils.isNotEmpty(excelList)) {
                ScPersonidequipService scPersonidequipService = SpringContextUtils.getBean(ScPersonidequipService.class);
                scPersonidequipService.insertBatch(excelList);
            }
           /* if (CollectionUtils.isNotEmpty(errorList)) {
                return errorList;
            }*/
            if (CollectionUtils.isNotEmpty(nullList)) {
                throw new RenException("必填数据不能为空:"+nullList);
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
            if (CollectionUtils.isNotEmpty(errorIpList)) {
                throw new RenException("IP格式错误:" + errorIpList);
            }
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    private ScPersonidequipEntity excelCheck(ScPersonidequipImportExcel sc, ScPersonidequipEntity sentity) throws ParseException {

        if (StringUtils.isEmpty(sc.getPieDevicename())) {
            throw new RenException("设备名称为空");
        }
        if (StringUtils.isNotEmpty(sc.getPieEquipsn())) {
            QueryWrapper<ScPersonidequipEntity> queryWrapper = new QueryWrapper<>();
            ScPersonidequipDao scPersonidequipDao = SpringContextUtils.getBean(ScPersonidequipDao.class);
            List<ScPersonidequipEntity> mdbList = scPersonidequipDao.selectList(queryWrapper
                    .eq("pie_equipsn", sc.getPieEquipsn()));

            if (mdbList.size() >= 1) {
                throw new RenException("重复设备序列号" + sc.getPieEquipsn());
            }

        } else {
            throw new RenException("设备序列号为空");
        }
        if (StringUtils.isEmpty(sc.getPieIpaddr())){
            throw new RenException("ip地址为空");
        }

        if (!Constant.PATTERNIP.matcher(sc.getPieIpaddr()).matches()) {
            throw new RenException("IP格式不正确:" + sc.getPieIpaddr());
        }
        if (null == sc.getPieExpirydate()) {
            throw new RenException("有效期为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(sc.getPieExpirydate());
        if (System.currentTimeMillis() > parse.getTime()) {
            throw new RenException("己过期" + sc.getPieExpirydate());
        }
        if (StringUtils.isEmpty(sc.getPieSetupaddr())) {
            throw new RenException("安装位置为空");
        }

        String[] split = sc.getPieSetupaddr().split("-");
        if (split.length < 1 || StringUtils.isEmpty(split[0])) {
            throw new RenException("楼栋为空");
        }
        if (split.length < 2 || StringUtils.isEmpty(split[1])) {
            throw new RenException("楼层为空");
        }
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
               BeanUtils.copyProperties(sc, sentity);
        sentity.setPieStatus(Constant.FAIL);
        sentity.setPieSetupaddr(dfName.getDfFloorid()+","+scDormitoryfloorEntity.getDfFloorid());
        sentity.setPieIsinitial(Constant.FAIL);
        sentity.setPieSetupdate(new Date());
        sentity.setPieExpirydate(parse);

        return sentity;
    }
}