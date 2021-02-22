package com.dkha.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.SpringContextUtils;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dao.ScDormitoryfloorDao;
import com.dkha.dao.ScTransformerdcDao;
import com.dkha.dao.ScTransformerroomDao;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.entity.ScDormitoryfloorEntity;
import com.dkha.entity.ScTransformerdcEntity;
import com.dkha.entity.ScTransformerroomEntity;
import com.dkha.excel.ScTransformerdcImportExcel;
import com.dkha.excel.ScTransformerroomExcel;
import com.dkha.service.ScTransformerdcService;
import com.dkha.service.ScTransformerroomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成都电科慧安
 * 导入互感器设备信息
 *
 * @program: school
 * @description: ScTransformerdcDataListener
 * @author: jinbiao
 * @create: 2020-08-29 17:34
 **/
@Slf4j
public class ScTransformerdcDataListener extends AnalysisEventListener<ScTransformerdcImportExcel> {

    /**
     * 批处理阈值
     */
    private static final int BATCH_COUNT = 2;
    List<ScTransformerdcImportExcel> list = new ArrayList<ScTransformerdcImportExcel>(BATCH_COUNT);
    private ScTransformerdcImportExcel sc = new ScTransformerdcImportExcel();
    //重复房间号
    private List<String> chNumList = new ArrayList<>();
    //检测端编号
    private List<String> floorList = new ArrayList<>();
    //合并单元格重要数据
    List mergeDataList = new ArrayList();

    @Override
    public void invoke(ScTransformerdcImportExcel scTransformerdcImportExcel, AnalysisContext analysisContext) {

        if (StringUtils.isEmpty(scTransformerdcImportExcel.getTfDevicename())) {
            if (StringUtils.isNotEmpty(sc.getTfDevicename())) {
                scTransformerdcImportExcel.setTfDevicename(sc.getTfDevicename());
            } else {
                scTransformerdcImportExcel.setTfDevicename(null);
            }
        }
        if (StringUtils.isEmpty(scTransformerdcImportExcel.getTfDevicesn())) {
            if (StringUtils.isNotEmpty(sc.getTfDevicesn())) {
                scTransformerdcImportExcel.setTfDevicesn(sc.getTfDevicesn());
            } else {
                scTransformerdcImportExcel.setTfDevicesn(null);
            }
        }
        if (StringUtils.isEmpty(scTransformerdcImportExcel.getTfIpgateway())) {
            if (StringUtils.isNotEmpty(sc.getTfIpgateway())) {
                scTransformerdcImportExcel.setTfIpgateway(sc.getTfIpgateway());
            } else {
                scTransformerdcImportExcel.setTfIpgateway(null);
            }
        }

        if (StringUtils.isEmpty(scTransformerdcImportExcel.getTfExpirydate())) {
            if (StringUtils.isNotEmpty(sc.getTfExpirydate())) {
                scTransformerdcImportExcel.setTfExpirydate(sc.getTfExpirydate());
            } else {
                scTransformerdcImportExcel.setTfExpirydate(null);
            }
        }
        list.add(scTransformerdcImportExcel);

      /*  if (StringUtils.isNotEmpty(scTransformerdcImportExcel.getTfDevicename())) {
            saveData();
            list.clear();
        }*/
        //跨 行处理
        if (null == sc || StringUtils.isNotEmpty(scTransformerdcImportExcel.getTfDevicename())) {
            BeanUtils.copyProperties(scTransformerdcImportExcel, sc);
        }
      /*  if (StringUtils.isNotEmpty(sc.getTfDevicename()) && !sc.getTfDevicename().equals(scTransformerdcImportExcel.getTfDevicename())) {
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
            List<ScTransformerdcImportExcel> excelList = new ArrayList<>();
            // TODO: 2020/8/29   错误提示信息
            //空
            Set nullList = new HashSet();
            //复复
            Set repeatList = new HashSet();
            //己过期
            Set expiredList = new HashSet();
            //己存在
            Set existList = new HashSet();
            //不存在
            Set nExistList = new HashSet();
            //IP格式不正确
            Set errorIpList = new HashSet();
            //检测端编号不是数字
            Set nNumList = new HashSet();
            for (ScTransformerdcImportExcel scTransformerdcImportExcel : list) {
                ScTransformerdcImportExcel sentity = new ScTransformerdcImportExcel();

                try {
                    sentity = excelCheck(scTransformerdcImportExcel, sentity);

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
                    if (e.getMessage().contains(Constant.existString)) {
                        nExistList.add(e.getMessage().replaceAll(Constant.existString, ""));
                    }
                    if (e.getMessage().contains(Constant.nNum)) {
                        nNumList.add(e.getMessage().replaceAll(Constant.nNum, ""));
                    }
                    if (e.getMessage().contains(Constant.errorIp)) {
                        errorIpList.add(e.getMessage().replaceAll(Constant.errorIp, ""));
                    }
                    continue;
                }

                excelList.add(sentity);
            }
            if (CollectionUtils.isNotEmpty(excelList)) {
                ScTransformerdcService scTransformerdcService = SpringContextUtils.getBean(ScTransformerdcService.class);
                List tfidList = new ArrayList();
                ScTransformerroomService scList = SpringContextUtils.getBean(ScTransformerroomService.class);
                Long tfId = null;
                for (ScTransformerdcImportExcel scTransformerdcImportExcel : excelList) {
                    if (null == scTransformerdcImportExcel.getTfId()) {

                        ScTransformerdcEntity scTransformerdcEntities = ConvertUtils.sourceToTarget(scTransformerdcImportExcel, ScTransformerdcEntity.class);
                        scTransformerdcEntities.setTfExpirydate(scTransformerdcImportExcel.getScDate());
                        if (StringUtils.isNotEmpty(scTransformerdcEntities.getTfDevicetype())) {
                            scTransformerdcService.insert(scTransformerdcEntities);
                            tfId = scTransformerdcEntities.getTfId();
                        }
                        List<ScTransformerroomEntity> scTransformerroomEntities = ConvertUtils.sourceToTarget(scTransformerdcImportExcel.getListTfroom(), ScTransformerroomEntity.class);
                        Long finalTfId = tfId;
                        scTransformerroomEntities.stream().peek(x -> x.setTfId(finalTfId)).collect(Collectors.toList());
                        scList.insertBatch(scTransformerroomEntities);
                    } else {
                        List<ScTransformerroomEntity> scTransformerroomEntities = ConvertUtils.sourceToTarget(scTransformerdcImportExcel.getListTfroom(), ScTransformerroomEntity.class);
                        scTransformerroomEntities.stream().peek(x -> x.setTfId(scTransformerdcImportExcel.getTfId()));
                        scList.insertBatch(scTransformerroomEntities);
                    }
                }
            }
           /* if (CollectionUtils.isNotEmpty(errorList)) {
                return errorList;
            }*/

            if (CollectionUtils.isNotEmpty(nullList)) {
                throw new RenException("必填数据不能为空" + nullList);
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
            if (CollectionUtils.isNotEmpty(existList)) {
                throw new RenException("己存在的数据:" + existList);
            }
            if (CollectionUtils.isNotEmpty(nNumList)) {
                throw new RenException("检测端编号不是数字:" + nNumList);
            }
            if (CollectionUtils.isNotEmpty(errorIpList)) {
                throw new RenException("IP格式错误:" + errorIpList);
            }

        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    private ScTransformerdcImportExcel excelCheck(ScTransformerdcImportExcel sc, ScTransformerdcImportExcel sentity) throws ParseException {
        //互感器设备信息
        ScTransformerdcDao scTransformerdcDao = SpringContextUtils.getBean(ScTransformerdcDao.class);
        //互感器与房间关系 sc_transformerroom
        ScTransformerroomDao scTransformerroomDao = SpringContextUtils.getBean(ScTransformerroomDao.class);
        /**
         * 楼层楼栋信息处理
         */
        ScDormitoryfloorDao scDormitoryfloorDao = SpringContextUtils.getBean(ScDormitoryfloorDao.class);
        //房间信息
        ScDormitoryDao scDormitoryDao = SpringContextUtils.getBean(ScDormitoryDao.class);

        Integer maxTfrPortaddr = 0;
        List<ScTransformerroomExcel> list = new ArrayList<>();
        if (StringUtils.isEmpty(sc.getTfDevicename())) {
            throw new RenException("设备名称为空");
        }
        if (StringUtils.isNotEmpty(sc.getTfDevicesn())) {
            QueryWrapper<ScTransformerdcEntity> queryWrapper = new QueryWrapper<>();
            List<ScTransformerdcEntity> mdbList = scTransformerdcDao.selectList(queryWrapper
                    .eq("tf_devicesn", sc.getTfDevicesn()));

            if (mdbList.size() >= 1) {
                throw new RenException("重复设备序列号" + sc.getTfDevicesn());
            }

        } else {
            throw new RenException("设备序列号为空");
        }
        if (null == sc.getTfExpirydate()) {
            throw new RenException("有效期为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(sc.getTfExpirydate());
        if (System.currentTimeMillis() > parse.getTime()) {
            throw new RenException("己过期" + sc.getTfExpirydate());
        }

        /**
         * 安装位置处理
         */
        if (StringUtils.isEmpty(sc.getDfFloorname())
                || StringUtils.isEmpty(sc.getChNum())
                || StringUtils.isEmpty(sc.getDfName())) {
            throw new RenException("安装位置为空");
        }
        //楼栋信息
        ScDormitoryfloorEntity dfName = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                .eq("df_floorname", sc.getDfName())
                .eq("df_type", Constant.FAIL));
        if (null == dfName) {
            throw new RenException("楼栋:" + sc.getDfName() + "不存在");
        }
        //楼层信息
        ScDormitoryfloorEntity scDormitoryfloorEntity = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                .eq("df_floorname", sc.getDfFloorname())
                .eq("df_type", Constant.SUCCESS)
                .eq("df_parentid", dfName.getDfFloorid()));
        if (null == scDormitoryfloorEntity) {
            throw new RenException("楼层:" + sc.getDfFloorname() + "不存在");
        }

        //房间信息
        ScDormitoryEntity scDormitoryEntity = scDormitoryDao.selectOne(new QueryWrapper<ScDormitoryEntity>()
                .eq("dr_num", sc.getChNum())
                .eq("df_floorid", scDormitoryfloorEntity.getDfFloorid()));
        if (null == scDormitoryEntity) {
            throw new RenException("房间:" + sc.getChNum() + "不存在");
        }
        List<ScTransformerroomEntity> scTransformerroomEntities = scTransformerroomDao.selectList(null);
        if (CollectionUtils.isNotEmpty(scTransformerroomEntities)) {
            List<Long> collect = scTransformerroomEntities.stream().map(x -> x.getDrId()).collect(Collectors.toList());
            if (collect.contains(scDormitoryEntity.getDrId())) {
                throw new RenException("重复房间号" + sc.getChNum());
            }
        }
        if (chNumList.contains(sc.getChNum())) {
            throw new RenException("重复房间号为" + sc.getChNum());
        }
        chNumList.add(sc.getChNum());


        /**
         * 检测端编号
         * sc_transformerdc
         *  1.电流互感器与房间号 一对多关系 不重复
         *  2. 合并单元格中的房间号存入chNumList中避免重复
         *
         */
        ScTransformerdcEntity scTransformerdcEntity = scTransformerdcDao.selectOne(new QueryWrapper<ScTransformerdcEntity>()
                .eq("tf_devicename", sc.getTfDevicename())
                .eq("tf_devicesn", sc.getTfDevicesn())
                .eq("tf_status", Constant.SUCCESS));
        if (null == scTransformerdcEntity) {
//            throw new RenException("设备:" + sc.getTfDevicename() + "不存在或己离线");
        } else {
            //设备与房间相关信息
            List<ScTransformerroomEntity> tfsnList = scTransformerroomDao.selectList(new QueryWrapper<ScTransformerroomEntity>()
                    .eq("tf_id", scTransformerdcEntity.getTfId()));
            if (CollectionUtils.isNotEmpty(tfsnList)) {
                maxTfrPortaddr = tfsnList.stream().max(Comparator.comparingInt(x -> x.getTfrPortaddr())).get().getTfrPortaddr();

                for (ScTransformerroomEntity scTransformerroomEntity : tfsnList) {
                    if (scTransformerroomEntity.getDrId().equals(scDormitoryEntity.getDrId())
                            && scTransformerroomEntity.getTfrPortaddr().equals(sc.getTfrPortaddr())) {
                        throw new RenException("检测端编号:" + sc.getTfrPortaddr() + "己存在");
                    }
                }
            }
            if (floorList.contains(sc.getChNum())) {
                throw new RenException("重复检测端编号为:" + sc.getChNum());
            } else {
                floorList.add(sc.getChNum());
            }
        }
        if (!Constant.PATTERNNUM.matcher(sc.getChNum()).matches()) {
            throw new RenException("检测端编号不是数字:" + sc.getTfIpgateway());
        }
        if (StringUtils.isEmpty(sc.getTfIpgateway())) {
            throw new RenException("IP为空");
        }
        if (!Constant.PATTERNIP.matcher(sc.getTfIpgateway()).matches()) {
            throw new RenException("IP格式不正确:" + sc.getTfIpgateway());
        }

        ScTransformerroomExcel scTransformerroomExcel = new ScTransformerroomExcel();
        /**
         * 检测端编号
         */
        BeanUtils.copyProperties(sc, sentity);
        if (null != scTransformerdcEntity) {
            sentity.setTfId(scTransformerdcEntity.getTfId());
            scTransformerroomExcel.setTfId(scTransformerdcEntity.getTfId());
        }
        sentity.setTfStatus(Constant.FAIL);
        sentity.setTfSetupdate(new Date());
        sentity.setTfSetupaddr(dfName.getDfFloorid().toString()
                + "," + scDormitoryfloorEntity.getDfFloorid().toString());
        sentity.setDfFloorid(scDormitoryEntity.getDfFloorid());
        sentity.setScDate(parse);
        //关联关系
        scTransformerroomExcel.setTfrPortaddr(Integer.valueOf(sc.getTfrPortaddr()));
        scTransformerroomExcel.setDrId(scDormitoryEntity.getDrId());
        scTransformerroomExcel.setTfDevicesn(sc.getTfDevicesn());
        scTransformerroomExcel.setTfrDrroomno(sc.getChNum());
        scTransformerroomExcel.setTfrRelationdate(new Date());
        list.add(scTransformerroomExcel);
        sentity.setListTfroom(list);
        /**
         * 合并单元格重复数据处理
         *   合并单元格必填数据不同时清理
         */


        if (CollectionUtils.isEmpty(mergeDataList)) {
            mergeDataList.add(sc.getTfDevicesn());
        } else {
            if (!mergeDataList.contains(sc.getTfDevicesn())) {
                mergeDataList.clear();
                chNumList.clear();
                floorList.clear();
            }
        }
        return sentity;
    }
}
