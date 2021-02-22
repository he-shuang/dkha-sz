package com.dkha.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.dkha.commons.dynamic.datasource.annotation.DataSource;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.excel.ExcelSheetVO;
import com.dkha.commons.tools.excel.ExcelUtils;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.http.RestTemplateUtils;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.commons.tools.utils.IpUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dao.*;
import com.dkha.dto.*;
import com.dkha.entity.*;


import com.dkha.excel.ScAidooreightDailyExcel;
import com.dkha.excel.ScDormitorypersonExcel;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.service.FvScDeviceService;
import com.dkha.service.ScDormitorypersonService;
import com.dkha.service.ScStudentsService;
import io.micrometer.shaded.org.pcollections.HashPMap;
import io.micrometer.shaded.org.pcollections.PMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 宿舍当前入住人员信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScDormitorypersonServiceImpl extends BaseServiceImpl<ScDormitorypersonDao, ScDormitorypersonEntity> implements ScDormitorypersonService {

    @Autowired
    private FvScDeviceService fvScDeviceService;
    @Autowired
    private ScDormitoryDao scDormitoryDao;
    @Autowired
    private ScStudentsDao scStudentsDao;
    @Autowired
    private SysAdminFeignClient sysAdminFeignClient;
    @Autowired
    private ScDormitorypersonService scDormitorypersonService;
    @Autowired
    private ScAidoorfivePersonlistDao scAidoorfivePersonlistDao;

    @Autowired
    private ScFaceverificationDao faceverificationDao;

    private String SAVEEQTOFACE = "http://" + IpUtils.getIpAddress() + ":8080/system/fvscdevice/saveEqToFace";

    @Override
    public PageData<ScDormitorypersonInfoDTO> page(Map<String, Object> params) {
        String type = (String) params.get("type");
        if (StringUtils.isBlank(type)) {
            throw new RenException("类型传入为空");
        }
        paramsToLike(params, "scStuname", "scNo");
        IPage<ScDormitorypersonInfoDTO> page = baseDao.findPage(
                getPage(params, "cast(dr_num as signed)", true),
                params
        );

        return getPageData(page, ScDormitorypersonInfoDTO.class);
    }

    @Override
    public Map<String, Object> getRoomCheckInInfo(Map<String, Object> params) {
        //0 楼栋，1 楼层 2 房间
        String type = (String) params.get("type");
        if (StringUtils.isBlank(type)) {
            throw new RenException("类型传入为空");
        }
        paramsToLike(params, "scStuname", "scNo");
        List<ScDormitorypersonInfoDTO> dormitorypersonInfoDTOS = baseDao.list(params);
        //当前楼层归寝
        String floorId = (String) params.get("floorId");
        Integer outNum = baseDao.countByFloorId(floorId, "0");
        //当前楼层入住总人数
        Integer backNum = baseDao.countByFloorId(floorId, null);
        Map<String, Object> map = new HashMap<>();
        map.put("data", dormitorypersonInfoDTOS);
        map.put("outNum", outNum);
        map.put("backNum", backNum);
        return map;
    }

    @Override
    public Map<String, Object> getOutNum() {
        Integer outNum = baseDao.selectCount(new QueryWrapper<ScDormitorypersonEntity>().eq("is_out", 1));
        Integer backNum = baseDao.selectCount(new QueryWrapper<ScDormitorypersonEntity>().eq("is_out", 0));
        Map<String, Object> map = new HashMap<>();
        map.put("outNum", outNum);
        map.put("backNum", backNum);
        return map;
    }


    @Override
    public List<ScDormitorypersonDTO> list(Map<String, Object> params) {
        List<ScDormitorypersonEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScDormitorypersonDTO.class);
    }

    private QueryWrapper<ScDormitorypersonEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<ScDormitorypersonEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScDormitorypersonDTO get(String id) {
        ScDormitorypersonEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScDormitorypersonDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<ScDormitorypersonDTO> dto, DoorAndPersonListDTO doorAndPersonListDTO) {

        List<ScDormitorypersonEntity> entities = ConvertUtils.sourceToTarget(dto, ScDormitorypersonEntity.class);
        for (ScDormitorypersonEntity entity : entities) {
            entity.setDrOccupancydate(new Date());
            QueryWrapper<ScDormitorypersonEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sc_stdid", entity.getScStdid());

            List<ScDormitorypersonEntity> scDormitorypersonEntities = baseDao.selectList(queryWrapper);
            if (scDormitorypersonEntities != null && scDormitorypersonEntities.size() > 0) {
                throw new RenException("该学生已被分配房间,请勿重复分配!");
            }
            insert(entity);
        }

        ScDormitoryEntity scDormitoryEntity = scDormitoryDao.selectById(dto.get(0).getDrId());
        scDormitoryEntity.setDrState(2);
        scDormitoryEntity.setDfIsfull(dto.size() == scDormitoryEntity.getDrCapacity() ? 1 : 0);

        if (scDormitoryDao.updateById(scDormitoryEntity) > 0) {

            //自动下发人脸到门禁设备
            try {
                //删除人脸下发记录
                QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();

                String strSerial = doorAndPersonListDTO.getFiveDoors().get(0).getFSerial();
                String strPassword = doorAndPersonListDTO.getFiveDoors().get(0).getFPassword();

                queryWrapper.eq("serial", strSerial);
                queryWrapper.eq("password", strPassword);

                //查询人脸下发记录
                List<ScAidoorfivePersonlistEntity> listfivePerson = scAidoorfivePersonlistDao.selectList(queryWrapper);
                //判斷容量和修改保存的人数是否相等
                if (scDormitoryEntity.getDrCapacity() != dto.size()) {
                     //循环删除权限信息
                    for (ScDormitorypersonDTO person : dto) {
                        queryWrapper.eq("user_id", person.getScStdid());
                        //查询人脸下发记录的人脸文件名
                        List<ScAidoorfivePersonlistEntity> personlist = scAidoorfivePersonlistDao.selectList(queryWrapper);
                        if (personlist.size() > 0) {
                            //删除人脸权限
                            fvScDeviceService.deletePermissionFace(strSerial, strPassword, personlist.get(0).getImgId());
                            //删除人脸下发记录
                            scAidoorfivePersonlistDao.delete(queryWrapper);
                        }
                        //下发权限
                        fvScDeviceService.saveEqToFace(doorAndPersonListDTO, listfivePerson);
                    }

                } else if (scDormitoryEntity.getDrCapacity() == dto.size()) {
                    //删除所有权限
                    boolean deleteFaceAll = fvScDeviceService.deletePermissionFaceAll(strSerial, strPassword, listfivePerson);
                    if (deleteFaceAll) {
                        //删除人脸下发记录
                        scAidoorfivePersonlistDao.delete(queryWrapper);
                        //下发权限
                        fvScDeviceService.saveEqToFace(doorAndPersonListDTO, listfivePerson);
                    } else {
                        throw new RenException("删除门禁设备上的权限失败!");
                    }
                }
            } catch (Exception e) {
                throw new RenException(e.getMessage());
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(List<ScDormitorypersonDTO> dto, DoorAndPersonListDTO doorAndPersonListDTO) {

        List<ScDormitorypersonEntity> entities = ConvertUtils.sourceToTarget(dto, ScDormitorypersonEntity.class);
        updateBatchById(entities);
        ScDormitoryEntity scDormitoryEntity = scDormitoryDao.selectById(dto.get(0).getDrId());
        scDormitoryEntity.setDrState(2);
        scDormitoryEntity.setDfIsfull(dto.size() == scDormitoryEntity.getDrCapacity() ? 1 : 0);
        int result = scDormitoryDao.updateById(scDormitoryEntity);

        if (result > 0) {
            //自动下发人脸
            try {
                QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
                String strSerial = doorAndPersonListDTO.getFiveDoors().get(0).getFSerial();
                String strPassword = doorAndPersonListDTO.getFiveDoors().get(0).getFPassword();
                queryWrapper.eq("serial", strSerial);
                queryWrapper.eq("password", strPassword);

                //查询人脸下发记录
                List<ScAidoorfivePersonlistEntity> listfivePerson = scAidoorfivePersonlistDao.selectList(queryWrapper);
                //判断传入人数是否为两人
                if (scDormitoryEntity.getDrCapacity() != dto.size()) {
                    for (ScDormitorypersonDTO person : dto) {
                        queryWrapper.eq("user_id", person.getScStdid());
                        //查询人脸下发记录的人脸文件名
                        List<ScAidoorfivePersonlistEntity> personlist = scAidoorfivePersonlistDao.selectList(queryWrapper);
                        if (personlist.size() > 0) {
                            //删除人脸权限
                            fvScDeviceService.deletePermissionFace(strSerial, strPassword, personlist.get(0).getImgId());
                            //删除人脸下发记录
                            scAidoorfivePersonlistDao.delete(queryWrapper);
                        }
                        //下发权限
                        fvScDeviceService.saveEqToFace(doorAndPersonListDTO, listfivePerson);
                    }
                } else if (scDormitoryEntity.getDrCapacity() == dto.size()) {
                    //删除所有权限
                    boolean deleteFaceAll = fvScDeviceService.deletePermissionFaceAll(strSerial, strPassword, listfivePerson);
                    if (deleteFaceAll) {
                        //删除人脸下发记录
                        scAidoorfivePersonlistDao.delete(queryWrapper);
                        //下发权限
                        fvScDeviceService.saveEqToFace(doorAndPersonListDTO, listfivePerson);
                    } else {
                        throw new RenException("删除门禁设备上的权限失败!");
                    }
                }
            } catch (Exception e) {
                throw new RenException(e.getMessage());
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {

        //先删除权限
        for (int i = 0; i < ids.length; i++) {
            ScDormitorypersonEntity person = baseDao.selectById(ids[i]);

            QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", person.getScStdid());

            List<ScAidoorfivePersonlistEntity> scAidoorfivePersonlistEntities = scAidoorfivePersonlistDao.selectList(queryWrapper);

            if (scAidoorfivePersonlistEntities != null) {
                scAidoorfivePersonlistDao.delete(queryWrapper);
                try {
                    for (ScAidoorfivePersonlistEntity scAidoorfivePersonlistEntity : scAidoorfivePersonlistEntities) {
                        fvScDeviceService.deletePermissionFace(
                                scAidoorfivePersonlistEntity.getSerial(),
                                scAidoorfivePersonlistEntity.getPassword(),
                                scAidoorfivePersonlistEntity.getImgId());
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        ScDormitorypersonEntity scDormitorypersonEntity = baseDao.selectById(ids[0]);
        Long drId = scDormitorypersonEntity.getDrId();
        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
        //查询当前房间入住人数
        Integer thisCount = baseDao.selectCount(new QueryWrapper<ScDormitorypersonEntity>().eq("dr_id", drId));
        ScDormitoryEntity scDormitoryEntity = scDormitoryDao.selectById(drId);
        scDormitoryEntity.setDfIsfull(0);
        if (thisCount == 0) {
            scDormitoryEntity.setDrState(1);
        }
        scDormitoryDao.updateById(scDormitoryEntity);

    }

    @Override
    public void delete(String drId) {
        Map<String, Object> map = new HashMap();
        map.put("dr_id", drId);
        baseDao.deleteByMap(map);
    }

    @Override
    public void importInfoExcel(MultipartFile file) {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            //模板信息导入解析
            ExcelSheetVO excelSheetVO = ExcelUtils.importExcel(file.getInputStream(), suffix);
            //将数据解析为集合
            List<List<Object>> dataList = excelSheetVO.getDataList();

            for (List<Object> objects : dataList) {

                ScDormitorypersonEntity entity = new ScDormitorypersonEntity();
                //查询学生信息中有无此学生，若无则不导入此数据
                ScStudentsEntity student = scStudentsDao.getByScNo(objects.get(1).toString());
                if (student == null) {
                    continue;
                }
                //查询宿舍房间中有无此房间，若无则不导入此数据
                ScDormitoryEntity dormitory = scDormitoryDao.getByScNum(objects.get(2).toString());
                if (dormitory == null) {
                    continue;
                }
                //查询此学号学生是否被分配房间，若已经分配则不导入此数据
                QueryWrapper<ScDormitorypersonEntity> studentWrapper = new QueryWrapper<>();
                studentWrapper.eq("sc_stdid", student.getScStdid());
                List<ScDormitorypersonEntity> scDormitorypersonEntities = baseDao.selectList(studentWrapper);
                if (scDormitorypersonEntities != null && scDormitorypersonEntities.size() > 0) {
                    continue;
                }
                //判断房间是否已经住满，若以住满则不导入此数据
                if (dormitory.getDfIsfull() == 1) {
                    continue;
                }

                //宿舍入住人员赋值
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                entity.setDrId(dormitory.getDrId());
                entity.setScStdid(student.getScStdid());
                Date parse = sdf.parse(objects.get(3).toString());
                entity.setDrOccupancydate(parse);
                entity.setIsOut(-1);
                //入住人员录入
                insert(entity);
                //查询入住人员录入房间后当前房间是否住满
                QueryWrapper<ScDormitorypersonEntity> personWrapper = new QueryWrapper<>();
                studentWrapper.eq("dr_id", dormitory.getDrId());
                List<ScDormitorypersonEntity> persons = baseDao.selectList(personWrapper);
                //更新房间状态
                ScDormitoryEntity scDormitoryEntity = scDormitoryDao.selectById(entity.getDrId());
                scDormitoryEntity.setDrState(2);
                scDormitoryEntity.setDfIsfull(persons.size() == scDormitoryEntity.getDrCapacity() ? 1 : 0);
                scDormitoryDao.updateById(scDormitoryEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RenException(e.getMessage());
        }
    }

    @Override
    @DataSource("slave2")
    public List<FvScDeviceEntity> getByDrNum(String drNum) {
        List<FvScDeviceEntity> byDrNum = baseDao.getByDrNum(drNum);
        return byDrNum;
    }

    @Override
    public void exportInfoExcel(HttpServletResponse response) throws Exception {
        List<ScDormitorypersonExcel> scDormitorypersonExcels = baseDao.exportInfoExcel();
        //获取当前时间
        String nowTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
        for (ScDormitorypersonExcel scDormitorypersonExcel : scDormitorypersonExcels) {
//            String schoolName = scDormitorypersonService.getDictBySchool(scDormitorypersonExcel.getScSchool());
//            scDormitorypersonExcel.setScSchool(schoolName);
            scDormitorypersonExcel.setUpdateDate(nowTime);
            ScFaceverificationEntity faceverificationEntity = faceverificationDao.getLastData(scDormitorypersonExcel.getScStdid().toString());
            scDormitorypersonExcel.setCheckDate(faceverificationEntity.getCreateDate());
            if(faceverificationEntity.getVerificationType() == 1){
                scDormitorypersonExcel.setIsTeachingBuilding("是");
            }else if(faceverificationEntity.getVerificationType() == 2){
                scDormitorypersonExcel.setIsTeachingBuilding("否");
            }

        }
//        //按照学生宿舍号进行排序
//        scDormitorypersonExcels.sort(new Comparator<ScDormitorypersonExcel>() {
//            @Override
//            public int compare(ScDormitorypersonExcel o1, ScDormitorypersonExcel o2) {
//                return Integer.valueOf(o1.getDrNum()).compareTo(Integer.valueOf(o2.getDrNum()));
//            }
//        });
        ExcelUtils.exportExcelToTarget(response, "未归寝学生统计表", scDormitorypersonExcels, ScDormitorypersonExcel.class);
    }

    @Override
    public String getDictBySchool(String scSchool) {
        return baseDao.getDictBySchool(scSchool);
    }

}
