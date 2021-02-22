package com.dkha.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.commons.tools.utils.SpringContextUtils;
import com.dkha.dao.*;
import com.dkha.dto.UwbRegionDTO;
import com.dkha.entity.*;
import com.dkha.excel.ScGatebusdeviceImportExcel;
import com.dkha.excel.ScGatewaydcImportExcel;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.service.ScGatebusdeviceService;
import com.dkha.service.ScGatewaydcService;
import com.dkha.service.ScModbusdevicedcService;
import com.dkha.service.ScRegionConfigService;
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
 * 302网关设备导入监听
 *
 * @program: school
 * @description: ScTransformerdcDataListener
 * @author: jinbiao
 * @create: 2020-08-29 17:34
 **/
@Slf4j
public class ScGatewaydcDataListener extends AnalysisEventListener<ScGatewaydcImportExcel> {

    /**
     * 批处理阈值
     */
    private static final int BATCH_COUNT = 2;
    List<ScGatewaydcImportExcel> list = new ArrayList<ScGatewaydcImportExcel>(BATCH_COUNT);
    private ScGatewaydcImportExcel sc = new ScGatewaydcImportExcel();

    //重复通讯地址编码
    private List<Integer> addrList = new ArrayList<>();
    private Integer gbdLineNum = 0;
    //重复网关编号
    private List<String> gwSnList = new ArrayList<>();
    //合并单元格重要数据
    List mergeDataList = new ArrayList();
    //PIR,SL,PM
    ScModbusdevicedcDao scModbusdevicedcDao = SpringContextUtils.getBean(ScModbusdevicedcDao.class);
    //302设备
    ScGatewaydcDao scGatewaydcDao = SpringContextUtils.getBean(ScGatewaydcDao.class);
    //485总线下挂载设备
    ScGatebusdeviceDao scGatebusdeviceDao = SpringContextUtils.getBean(ScGatebusdeviceDao.class);
    /**
     * 楼层楼栋信息处理
     */
    ScDormitoryfloorDao scDormitoryfloorDao = SpringContextUtils.getBean(ScDormitoryfloorDao.class);
    //字典
    SysAdminFeignClient dataType = SpringContextUtils.getBean(SysAdminFeignClient.class);
    //uwb区域编码
    ScRegionConfigService scRegionConfigService = SpringContextUtils.getBean(ScRegionConfigService.class);
    //房间信息
    ScDormitoryDao scDormitoryDao = SpringContextUtils.getBean(ScDormitoryDao.class);

    //正确数据
    List<ScGatewaydcImportExcel> excelList = new ArrayList<>();
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
    //其它错误
    Set otherList = new HashSet<>();
    //IP格式不正确
    Set errorIpList = new HashSet();
    //检测端编号不是数字
    Set nNumList = new HashSet();
    //通讯地址编码不是数字
    Set gbdAddrList = new HashSet();
    //总线不是数字
    Set lineNumList = new HashSet();

    @Override
    public void invoke(ScGatewaydcImportExcel scGatewaydcImportExcel, AnalysisContext analysisContext) {
//        scGatewaydcImportExcel.setChNum(scGatewaydcImportExcel.getChNum().split(".")[0]);
        if (StringUtils.isEmpty(scGatewaydcImportExcel.getGwName())) {
            if (StringUtils.isNotEmpty(sc.getGwName())) {
                scGatewaydcImportExcel.setGwName(sc.getGwName());
            } else {
                scGatewaydcImportExcel.setGwName(null);
            }
        }
        if (StringUtils.isEmpty(scGatewaydcImportExcel.getGwSn())) {
            if (StringUtils.isNotEmpty(sc.getGwSn())) {
                scGatewaydcImportExcel.setGwSn(sc.getGwSn());
            } else {
                scGatewaydcImportExcel.setGwSn(null);
            }
        }
        if (StringUtils.isEmpty(scGatewaydcImportExcel.getGwIpgateway())) {
            if (StringUtils.isNotEmpty(sc.getGwIpgateway())) {
                scGatewaydcImportExcel.setGwIpgateway(sc.getGwIpgateway());
            } else {
                scGatewaydcImportExcel.setGwIpgateway(null);
            }
        }

        if (StringUtils.isEmpty(scGatewaydcImportExcel.getGwExpirydate())) {
            if (StringUtils.isNotEmpty(sc.getGwExpirydate())) {
                scGatewaydcImportExcel.setGwExpirydate(sc.getGwExpirydate());
            } else {
                scGatewaydcImportExcel.setGwExpirydate(null);
            }
        }
        if (StringUtils.isEmpty(scGatewaydcImportExcel.getDfName())) {
            if (StringUtils.isNotEmpty(sc.getDfName())) {
                scGatewaydcImportExcel.setDfName(sc.getDfName());
            } else {
                scGatewaydcImportExcel.setDfName(null);
            }
        }
        if (StringUtils.isEmpty(scGatewaydcImportExcel.getDfFloorname())) {
            if (StringUtils.isNotEmpty(sc.getDfFloorname())) {
                scGatewaydcImportExcel.setDfFloorname(sc.getDfFloorname());
            } else {
                scGatewaydcImportExcel.setDfFloorname(null);
            }
        }
        if (StringUtils.isEmpty(scGatewaydcImportExcel.getGbdLineNum())) {
            if (StringUtils.isNotEmpty(sc.getGbdLineNum())) {
                scGatewaydcImportExcel.setGbdLineNum(sc.getGbdLineNum());
            } else {
                scGatewaydcImportExcel.setGbdLineNum(null);
            }
        }
        list.add(scGatewaydcImportExcel);

        //跨 行处理
        if (null == sc || StringUtils.isNotEmpty(scGatewaydcImportExcel.getGwName())) {
            BeanUtils.copyProperties(scGatewaydcImportExcel, sc);

        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        log.info("所有数据解析完成！");
    }

    private void saveData() {
        try {

            for (ScGatewaydcImportExcel scGatewaydcImportExcel : list) {
                ScGatewaydcImportExcel sentity = new ScGatewaydcImportExcel();
                try {
                    sentity = excelCheck(scGatewaydcImportExcel, sentity);

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
                    if (e.getMessage().contains(Constant.lineNum)) {
                        lineNumList.add(e.getMessage().replaceAll(Constant.lineNum, ""));
                    }
                    if (e.getMessage().contains(Constant.gbdAddr)) {
                        gbdAddrList.add(e.getMessage().replaceAll(Constant.gbdAddr, ""));
                    }
                    if (e.getMessage().contains(Constant.otherError)) {
                        otherList.add(e.getMessage().replaceAll(Constant.otherError, ""));
                    }
                    continue;
                }

                excelList.add(sentity);
            }
            if (CollectionUtils.isNotEmpty(excelList)) {
                ScGatewaydcService scGatewaydcService = SpringContextUtils.getBean(ScGatewaydcService.class);
                List tfidList = new ArrayList();
                ScGatebusdeviceService scList = SpringContextUtils.getBean(ScGatebusdeviceService.class);
                /**
                 * 1. 302设备存储 合并单元格只做一次存储  insertType
                 * 2. 485通讯设备总线挂载设备存储 ，关联 302Id，设备类型根据
                 *      sLDevicesn，PIRDevicesn，PMDevicesn 某一列匹分设备类型存储
                 */
                String gwId = null;
                //修改设备状态 mbd_network 是否组网
                List<String> listNetwork = new ArrayList<>();
                /**
                 * 处理 合并单无格第一行错误 后面主表无法存储问题
                 */
                List<String> distictInsert = excelList.stream()
                        .filter(x -> null == x.getGwId() && StringUtils.isEmpty(x.getInsertType()))
                        .map(x -> x.getGwSn()).distinct()
                        .collect(Collectors.toList());
                for (ScGatewaydcImportExcel scGatewaydcImportExcel : excelList) {
                    if (null == scGatewaydcImportExcel.getGwId()) {
                        ScGatewaydcEntity scGatewaydcEntity = ConvertUtils.sourceToTarget(scGatewaydcImportExcel, ScGatewaydcEntity.class);
                        if (StringUtils.isNotEmpty(scGatewaydcImportExcel.getInsertType())) {
                            scGatewaydcEntity.setGwExpirydate(scGatewaydcImportExcel.getGwdate());
                            scGatewaydcService.insert(scGatewaydcEntity);
                            gwId = String.valueOf(scGatewaydcEntity.getGwId());
                            distictInsert.remove(scGatewaydcImportExcel.getGwSn());
                        } else if (distictInsert.contains(scGatewaydcImportExcel.getGwSn())) {
                            scGatewaydcEntity.setGwExpirydate(scGatewaydcImportExcel.getGwdate());
                            scGatewaydcService.insert(scGatewaydcEntity);
                            gwId = String.valueOf(scGatewaydcEntity.getGwId());
                            distictInsert.remove(scGatewaydcImportExcel.getGwSn());
                        }
                        List<ScGatebusdeviceEntity> scGatebusdeviceEntityList = ConvertUtils.sourceToTarget(scGatewaydcImportExcel.getSblist(), ScGatebusdeviceEntity.class);

                        String finalGwId = gwId;
                        scGatebusdeviceEntityList.stream().peek(x -> x.setGbdId(finalGwId)).collect(Collectors.toList());
                        scList.insertBatch(scGatebusdeviceEntityList);
                        List<String> mbdids = scGatebusdeviceEntityList.stream().map(x -> x.getMbdId()).collect(Collectors.toList());
                        listNetwork.addAll(mbdids);
                    } else {
                        List<ScGatebusdeviceEntity> scGatebusdeviceEntityList = ConvertUtils.sourceToTarget(scGatewaydcImportExcel.getSblist(), ScGatebusdeviceEntity.class);
                        scGatebusdeviceEntityList.stream().peek(x -> x.setGbdId(String.valueOf(scGatewaydcImportExcel.getGwId())));
                        scList.insertBatch(scGatebusdeviceEntityList);
                        List<String> mbdids = scGatebusdeviceEntityList.stream().map(x -> x.getMbdId()).collect(Collectors.toList());
                        listNetwork.addAll(mbdids);
                    }
                }
                if (CollectionUtils.isNotEmpty(listNetwork)) {
                    ScModbusdevicedcService sdservice = SpringContextUtils.getBean(ScModbusdevicedcService.class);
                    List<String> collect = listNetwork.stream().distinct().collect(Collectors.toList());
                    UpdateWrapper<ScModbusdevicedcEntity> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.set("mbd_network", Constant.SUCCESS).in("mbd_id", collect);
//                        updateWrapper.set("mbd_network", Constant.SUCCESS).eq("mbd_id", s);
                    ScModbusdevicedcEntity scModbusdevicedcEntity = new ScModbusdevicedcEntity();
                    sdservice.update(scModbusdevicedcEntity, updateWrapper);

                }
            }
           /* if (CollectionUtils.isNotEmpty(errorList)) {
                return errorList;
            }*/

            if (CollectionUtils.isNotEmpty(nullList)) {
                throw new RenException("必填数据不能为空" + nullList);
            }
            if (CollectionUtils.isNotEmpty(repeatList)) {
                repeatList.stream().distinct();
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
                errorIpList.stream().distinct().collect(Collectors.toList());
                throw new RenException("IP格式错误:" + errorIpList);
            }
            if (CollectionUtils.isNotEmpty(gbdAddrList)) {
                throw new RenException("通讯地址编码不是数字:" + gbdAddrList);
            }
            if (CollectionUtils.isNotEmpty(lineNumList)) {
                throw new RenException("总线不是数字:" + lineNumList);
            }
            if (CollectionUtils.isNotEmpty(lineNumList)) {
                throw new RenException("错误信息:" + lineNumList);
            }

        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }

    private ScGatewaydcImportExcel excelCheck(ScGatewaydcImportExcel sc, ScGatewaydcImportExcel sentity) throws ParseException {


        /**
         * 非空验证
         */
        List<ScGatebusdeviceImportExcel> list = new ArrayList<>();
        if (StringUtils.isEmpty(sc.getGwName())) {
            throw new RenException("网关设备名称为空");
        }
        //网关编号
        QueryWrapper<ScGatewaydcEntity> queryWrapper = new QueryWrapper<>();
        List<ScGatewaydcEntity> scgatewayList = scGatewaydcDao.selectList(queryWrapper
                .eq("gw_sn", sc.getGwSn()));
        if (StringUtils.isNotEmpty(sc.getGwSn())) {
            if (scgatewayList.size() >= 1) {
                throw new RenException("重复网关设备序列号" + sc.getGwSn());
            }

        } else {
            throw new RenException("网关设备编号为空");
        }

        if (StringUtils.isEmpty(sc.getGwIpgateway())) {
            throw new RenException("IP为空");
        }
        if (!Constant.PATTERNIP.matcher(sc.getGwIpgateway()).matches()) {
            throw new RenException("IP格式不正确:" + sc.getGwIpgateway());
        }

        //导入重复验证
      /*  if (gwSnList.contains(sc.getGwSn())) {
            throw new RenException("重复设备序列号" + sc.getGwSn());
        }*/

        if (StringUtils.isEmpty(sc.getPIRDevicesn())
                && StringUtils.isEmpty(sc.getSlDevicesn())
                && StringUtils.isEmpty(sc.getPMDevicesn())) {
            throw new RenException("设备编号为空");
        }
        List<ScModbusdevicedcEntity> pirEntities = new ArrayList<>();
        if (StringUtils.isNotEmpty(sc.getPIRDevicesn())) {
            pirEntities = scModbusdevicedcDao.selectList(new QueryWrapper<ScModbusdevicedcEntity>()
                    .eq("mbd_devicesn", sc.getPIRDevicesn())
                    .eq("mbd_devicetype", Constant.FAIL));
            if (pirEntities.size() < 1) {
                nExistList.add("设备序列号" + sc.getPIRDevicesn());
            }
        }
        List<ScModbusdevicedcEntity> slEntities = new ArrayList<>();
        if (StringUtils.isNotEmpty(sc.getSlDevicesn())) {
            slEntities = scModbusdevicedcDao.selectList(new QueryWrapper<ScModbusdevicedcEntity>()
                    .eq("mbd_devicesn", sc.getSlDevicesn())
                    .eq("mbd_devicetype", Constant.SUCCESS));
            if (slEntities.size() < 1) {
                nExistList.add("设备序列号" + sc.getSlDevicesn());
            }
        }
        List<ScModbusdevicedcEntity> pmEntities = new ArrayList<>();
        if (StringUtils.isNotEmpty(sc.getPMDevicesn())) {
            pmEntities = scModbusdevicedcDao.selectList(new QueryWrapper<ScModbusdevicedcEntity>()
                    .eq("mbd_devicesn", sc.getPMDevicesn())
                    .eq("mbd_devicetype", 2));
            if (pmEntities.size() < 1) {
                nExistList.add("设备序列号" + sc.getPMDevicesn());
            }
        }


        if (StringUtils.isNotEmpty(sc.getGbdLineNum())) {
            if (CollectionUtils.isNotEmpty(scgatewayList)) {
                List<ScGatebusdeviceEntity> scGatebusdeviceEntityList = scGatebusdeviceDao.selectList(new QueryWrapper<ScGatebusdeviceEntity>()
                        .eq("gbd_id", scgatewayList.get(0).getGwId())
                        .eq("gbd_line_num", sc.getGbdLineNum()));
                if (scGatebusdeviceEntityList.size() > 32) {
                    throw new RenException("其他错误:己有32总线");
                }
                if (scGatebusdeviceEntityList.size() + gbdLineNum > 32) {
                    throw new RenException("其他错误:己有32总线");
                }
            }
            if (gwSnList.size() == 32) {
                throw new RenException("其他错误:每个网关下最多挂载32个设备");
            }
//            sc.setInsertType(sc.getGbdLineNum());
        } else {
            throw new RenException("总线为空");
        }
        Result byType = dataType.getByType(Constant.dataType);
        List<LinkedHashMap> dictDataEducation = (List<LinkedHashMap>) byType.getData();
        List<Object> types = dictDataEducation.stream().map(x -> x.get("dictLabel")).collect(Collectors.toList());
        if (!types.contains(sc.getGbdLineNum())) {
            throw new RenException("总线:" + sc.getGbdLineNum() + "不存在");
        }
       /* if (!Constant.PATTERNNUM.matcher(sc.getGbdLineNum()).matches()) {
            throw new RenException("总线不是数字:" + sc.getGbdLineNum());
        }*/


        if (StringUtils.isEmpty(sc.getPIRDevicesnName())
                && StringUtils.isEmpty(sc.getSlDevicesnName())
                && StringUtils.isEmpty(sc.getPMDevicesnName())) {
            throw new RenException("设备名称为空");
        }
  /*      if (StringUtils.isEmpty(sc.getSlDevicesnName())) {
            throw new RenException("智能照明设备名称为空");
        }
        if (StringUtils.isEmpty(sc.getPMDevicesnName())) {
            throw new RenException("PM2.5设备名称为空");
        }
        if (null == sc.getPirGbdAddr()) {
            throw new RenException("通讯地址编码为空");
        }*/
        if (null == sc.getPirGbdAddr()
                && null == sc.getPmGbdAddr()
                && null == sc.getSlGbdAddr()) {
            throw new RenException("485通讯地址编码为空");
        }

        if (null != sc.getPirGbdAddr()
                && !Constant.PATTERNNUM.matcher(String.valueOf(sc.getPirGbdAddr())).matches()) {
            gbdAddrList.add(sc.getPirGbdAddr());
//            throw new RenException("通讯地址编码不是数字:" + sc.getPirGbdAddr());
        }
      /*  else {
            nullList.add("485通讯地址编码为空");
        }*/
        if (null != sc.getPmGbdAddr()
                && !Constant.PATTERNNUM.matcher(String.valueOf(sc.getPmGbdAddr())).matches()) {
            gbdAddrList.add(sc.getPmGbdAddr());
//            throw new RenException("通讯地址编码不是数字:" + sc.getPmGbdAddr());
        }
       /* else {
            nullList.add("485通讯地址编码为空");
        }*/
        if (null != sc.getSlGbdAddr()
                && !Constant.PATTERNNUM.matcher(String.valueOf(sc.getSlGbdAddr())).matches()) {
            gbdAddrList.add(sc.getSlGbdAddr());
//            throw new RenException("通讯地址编码不是数字:" + sc.getSlGbdAddr());
        }
      /*  else {
            nullList.add("485通讯地址编码为空");
        }*/

        if (null == sc.getGwExpirydate()) {
            throw new RenException("有效期为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = sdf.parse(sc.getGwExpirydate());
        if (System.currentTimeMillis() > parse.getTime()) {
            throw new RenException("己过期" + sc.getGwExpirydate());
        }
        /**
         * 安装位置处理
         */
        if (StringUtils.isEmpty(sc.getDfFloorname())
                || StringUtils.isEmpty(sc.getDfName())) {
            throw new RenException("安装位置为空");
        }
        //楼栋信息
        ScDormitoryfloorEntity scDormitoryfloor = new ScDormitoryfloorEntity();
        ScDormitoryfloorEntity scDormitoryfloorEntity = new ScDormitoryfloorEntity();
        ScDormitoryEntity scDormitoryEntity = new ScDormitoryEntity();
        if (StringUtils.isNotEmpty(sc.getDfName())) {
            scDormitoryfloor = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                    .eq("df_floorname", sc.getDfName())
                    .eq("df_type", Constant.FAIL));
            if (null == scDormitoryfloor) {
                throw new RenException("楼栋:" + sc.getDfName() + "不存在");
            }
            //楼层信息

            if (StringUtils.isNotEmpty(sc.getDfFloorname())) {
                scDormitoryfloorEntity = scDormitoryfloorDao.selectOne(new QueryWrapper<ScDormitoryfloorEntity>()
                        .eq("df_floorname", sc.getDfFloorname())
                        .eq("df_type", Constant.SUCCESS)
                        .eq("df_parentid", scDormitoryfloor.getDfFloorid()));
                if (null == scDormitoryfloorEntity) {
                    throw new RenException("楼层:" + sc.getDfFloorname() + "不存在");
                }
                //房间信息
                if (StringUtils.isNotEmpty(sc.getChNum())) {
                    scDormitoryEntity = scDormitoryDao.selectOne(new QueryWrapper<ScDormitoryEntity>()
                            .eq("dr_num", sc.getChNum())
                            .eq("df_floorid", scDormitoryfloorEntity.getDfFloorid()));
                    if (null == scDormitoryEntity) {
                        throw new RenException("房间:" + sc.getChNum() + "不存在");
                    }
                }
            }
        } else {
            throw new RenException("楼栋:" + sc.getDfName() + "空");
        }


        /**
         * 通讯地址编码
         * sc_transformerdc
         *  1.302设备不为空时，302id与设备类型确定唯 一
         *  2. 不存在时只做插入数据重复性验证
         */

        //PM
        if (CollectionUtils.isNotEmpty(scgatewayList)) {
            ScGatewaydcEntity scGatewaydcEntity = scgatewayList.get(0);
            List<ScGatebusdeviceEntity> pMNumList = scGatebusdeviceDao.selectList(new QueryWrapper<ScGatebusdeviceEntity>()
                    .eq("gbd_id", scGatewaydcEntity.getGwId()));
            //组号简单验证
            List<String> groupList = pMNumList.stream().map(x -> x.getGbdGroup()).distinct().collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(pMNumList)) {
                List<Integer> sLNums = pMNumList.stream().map(x -> x.getGbdAddr()).collect(Collectors.toList());
                if (sLNums.contains(sc.getPirGbdAddr())) {
                    existList.add("485通讯地址编码:" + sc.getPirGbdAddr());
                }
                if (sLNums.contains(sc.getSlGbdAddr())) {
                    existList.add("485通讯地址编码:" + sc.getSlGbdAddr());
                }
                if (sLNums.contains(sc.getPmGbdAddr())) {
                    existList.add("485通讯地址编码:" + sc.getPmGbdAddr());
                }
            }
            //导入数据重复验证
            if (addrList.contains(sc.getPirGbdAddr())) {
                repeatList.add(sc.getPirGbdAddr());
//                throw new RenException("重复485通讯地址编码为:" + sc.getPirGbdAddr());
            } else {
                addrList.add(sc.getPirGbdAddr());
            }
            if (addrList.contains(sc.getPmGbdAddr())) {
                repeatList.add(sc.getPmGbdAddr());
            } else {
                addrList.add(sc.getPmGbdAddr());
            }
            if (addrList.contains(sc.getPmGbdAddr())) {
                repeatList.add(sc.getPmGbdAddr());
            } else {
                addrList.add(sc.getPmGbdAddr());
            }
        }
        List<UwbRegionDTO> uwbRegionList = scRegionConfigService.getUwbRegionList();
        if (StringUtils.isNotEmpty(sc.getGwUwbnum())) {
            List<String> fenceNames = uwbRegionList.stream().map(x -> x.getFenceName()).collect(Collectors.toList());

            if (!fenceNames.contains(sc.getGwUwbnum())) {
                nExistList.add("uwb区域编号:" + sc.getGwUwbnum());
            }
        } else {
            throw new RenException("uwb区域编号为空");
        }
        ScGatebusdeviceImportExcel sb = new ScGatebusdeviceImportExcel();

        BeanUtils.copyProperties(sc, sentity);
        if (CollectionUtils.isNotEmpty(scgatewayList)) {
            ScGatewaydcEntity scGatewaydcEntity = scgatewayList.get(0);
            if (null != scGatewaydcEntity) {
                sentity.setGwId(scGatewaydcEntity.getGwId());
                sb.setGbdId(String.valueOf(scGatewaydcEntity.getGwId()));
            }
        }

        sb.setGbdLineNum(sc.getGbdLineNum());

        sentity.setGwState(Constant.FAIL);
        String scDormitoryfloorName = null != scDormitoryfloor.getDfFloorid() ? scDormitoryfloor.getDfFloorid().toString() : "";
        String scEntityName = null != scDormitoryfloorEntity.getDfFloorid() ? "," + scDormitoryfloorEntity.getDfFloorid().toString() : "";
        String scDormitoryName = null != scDormitoryEntity.getDrId() ? "," + scDormitoryEntity.getDrId().toString() : "";

        sentity.setGwSetupaddr(scDormitoryfloorName + scEntityName + scDormitoryName);
        sentity.setDfFloorid(scDormitoryfloorEntity.getDfFloorid());
        sentity.setGwSetupdate(new Date());
        sentity.setGwdate(parse);

        sb.setGbdGroup(sc.getGbdGroup());
        List<UwbRegionDTO> idList = uwbRegionList.stream()
                .filter(x -> x.getFenceName().equals(sc.getGwUwbnum()))
                .collect(Collectors.toList());
        sb.setGbdFenceId(idList.get(0).getId());
        sb.setFenceName(idList.get(0).getFenceName());
        if (CollectionUtils.isNotEmpty(pirEntities)
                && null != sc.getPirGbdAddr()
                && StringUtils.isNotEmpty(sc.getPIRDevicesnName())) {
            sb.setMbdId(String.valueOf(pirEntities.get(0).getMbdId()));
            sb.setGbdDevicesn(pirEntities.get(0).getMbdDevicesn());
            sb.setGbdDevicetype(String.valueOf(Constant.FAIL));
            sb.setGbdAddr(sc.getPirGbdAddr());
            ScGatebusdeviceImportExcel newSb = new ScGatebusdeviceImportExcel();
            BeanUtils.copyProperties(sb, newSb);
            list.add(newSb);
        }
        if (CollectionUtils.isNotEmpty(pmEntities)
                && null != sc.getPmGbdAddr()
                && StringUtils.isNotEmpty(sc.getPMDevicesnName())) {
            sb.setMbdId(String.valueOf(pmEntities.get(0).getMbdId()));
            sb.setGbdDevicesn(pmEntities.get(0).getMbdDevicesn());
            sb.setGbdDevicetype("2");
            sb.setGbdAddr(sc.getPmGbdAddr());
            ScGatebusdeviceImportExcel newSb = new ScGatebusdeviceImportExcel();
            BeanUtils.copyProperties(sb, newSb);
            list.add(newSb);
        }
        String s = String.valueOf(sc.getSlGbdAddr());
        if (CollectionUtils.isNotEmpty(slEntities)
                && null != sc.getSlGbdAddr()
                && StringUtils.isNotEmpty(sc.getSlDevicesnName())) {
            sb.setMbdId(String.valueOf(slEntities.get(0).getMbdId()));
            sb.setGbdDevicesn(slEntities.get(0).getMbdDevicesn());
            sb.setGbdDevicetype(String.valueOf(Constant.SUCCESS));
            sb.setGbdAddr(sc.getSlGbdAddr());
            ScGatebusdeviceImportExcel newSb = new ScGatebusdeviceImportExcel();
            BeanUtils.copyProperties(sb, newSb);
            list.add(newSb);
        }
        gbdLineNum++;
        gwSnList.add(sc.getGwSn());
        sentity.setSblist(list);
        /**
         * 合并单元格重复数据处理
         *   合并单元格必填数据不同时清理
         */


        if (CollectionUtils.isEmpty(mergeDataList)) {
            mergeDataList.add(sc.getGwSn());
        } else {
            if (!mergeDataList.contains(sc.getGwSn())) {
                mergeDataList.clear();
                gwSnList.clear();
                gbdLineNum = 0;
                addrList.clear();
            }
        }
        return sentity;
    }
}
