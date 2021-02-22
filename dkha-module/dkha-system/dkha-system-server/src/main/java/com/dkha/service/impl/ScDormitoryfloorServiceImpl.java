package com.dkha.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.ErrorCode;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.*;
import com.dkha.dto.ScDormitoryDTO;
import com.dkha.dto.ScDormitoryfloorDTO;
import com.dkha.dto.UwbFloorDTO;
import com.dkha.entity.*;
import com.dkha.enums.ScDormitoryfloorLeafEnum;
import com.dkha.enums.ScDormitoryfloorTypeEnum;
import com.dkha.service.ScDormitoryfloorService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 楼栋，楼层信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScDormitoryfloorServiceImpl extends BaseServiceImpl<ScDormitoryfloorDao, ScDormitoryfloorEntity> implements ScDormitoryfloorService {

    @Autowired
    private ScDormitoryDao scDormitoryDao;

    /**
     *  房间人员信息
     */
    @Autowired
    private ScDormitorypersonDao scDormitorypersonDao;
    /**
     *  设备信息 PRI PM2.5 智能灯控
     */
    @Autowired
    private ScModbusdevicedcDao scModbusdevicedcDao;
    /**
     *  电流设备信息
     */
    @Autowired
    private ScTransformerdcDao scTransformerdcDao;

    /**
     *  网关设备信息
     */
    @Autowired
    private ScGatewaydcDao scGatewaydcDao;

    /**
     *  认证设备信息
     */
    @Autowired
    private ScPersonidequipDao scPersonidequipDaoDao;

    /**
     *  互感器宿舍关联关系
     */
    @Autowired
    private ScTransformerroomDao scTransformerroomDao;

    /**
     * 楼层与星网地图绑定表
     */
    @Autowired
    private ScDfmapbindingDao scDfmapbindingDao;

    @Override
    public PageData<ScDormitoryfloorDTO> page(Map<String, Object> params) {
        IPage<ScDormitoryfloorEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScDormitoryfloorDTO.class);
    }

    @Override
    public List<ScDormitoryfloorDTO> list(Map<String, Object> params) {
        String pid = (String) params.get("pid");
        String type = (String) params.get("type");
        if(StringUtils.isBlank(type)){
            throw new RenException("类型传入为空");
        }
        List<ScDormitoryfloorDTO> dtoList = new ArrayList<>();
        if(type.equals(String.valueOf(ScDormitoryfloorTypeEnum.FLOOR.code()))){
            QueryWrapper<ScDormitoryEntity> entityQueryWrapper = new QueryWrapper<>();
            entityQueryWrapper.eq(StringUtils.isNotBlank(pid),"df_floorid",pid);
            entityQueryWrapper.orderByAsc("cast(dr_num as signed)");
            List<ScDormitoryEntity> scDormitoryEntities = scDormitoryDao.selectList(entityQueryWrapper);
            for (ScDormitoryEntity scDormitoryEntity : scDormitoryEntities) {
                ScDormitoryfloorDTO dto = new ScDormitoryfloorDTO();
                dto.setDfFloorid(scDormitoryEntity.getDrId());
                dto.setDfFloorname(scDormitoryEntity.getDrNum());
                dto.setDfParentid(scDormitoryEntity.getDfFloorid());
                dto.setDfType(2);
                dto.setHasChildren(false);
                dto.setDrCapacity(scDormitoryEntity.getDrCapacity());
                dto.setDfPurpose(scDormitoryEntity.getDfPurpose());
                dtoList.add(dto);
            }

        }else{
            if(StringUtils.isBlank(pid)){
                //查询一级
                params.put("pId", 0);
            }
            String roleNames = SecurityUser.getUser().getRoleNames();
            List<Integer> dfPurposes = new ArrayList<>();
            if(roleNames.contains("学生宿舍")){
                dfPurposes.add(0);
            }
            if(roleNames.contains("教师宿舍")){
                dfPurposes.add(1);
            }
            if(roleNames.contains("教学楼")){
                dfPurposes.add(2);
            }
            params.put("dfPurposes",dfPurposes);
            List<ScDormitoryfloorEntity> entities = baseDao.getList(params);

            for (ScDormitoryfloorEntity entity : entities) {
                ScDormitoryfloorDTO dto = new ScDormitoryfloorDTO();
                BeanUtils.copyProperties(entity, dto);
                dto.setHasChildren(true);
                dtoList.add(dto);
            }
        }


        return dtoList;
    }

    private QueryWrapper<ScDormitoryfloorEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScDormitoryfloorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScDormitoryfloorDTO get(Long id,Integer type) {
        ScDormitoryfloorDTO dormitoryfloorDTO = null;
        if(type == ScDormitoryfloorTypeEnum.ROOM.code()){
            ScDormitoryEntity scDormitoryEntity = scDormitoryDao.getById(id);
            dormitoryfloorDTO = new ScDormitoryfloorDTO();
            dormitoryfloorDTO.setDfFloorid(scDormitoryEntity.getDrId());
            dormitoryfloorDTO.setDfFloorname(scDormitoryEntity.getDrNum());
            dormitoryfloorDTO.setDfParentid(scDormitoryEntity.getDfFloorid());
            dormitoryfloorDTO.setDfType(2);
            dormitoryfloorDTO.setDfFloorid(scDormitoryEntity.getDrId());
            dormitoryfloorDTO.setHasChildren(false);
            dormitoryfloorDTO.setDrCapacity(scDormitoryEntity.getDrCapacity());
            dormitoryfloorDTO.setDfPurpose(scDormitoryEntity.getDfPurpose());
            dormitoryfloorDTO.setParentName(scDormitoryEntity.getParentName());

        }else {
            ScDormitoryfloorEntity entity = baseDao.getByPId(id);
            dormitoryfloorDTO = ConvertUtils.sourceToTarget(entity, ScDormitoryfloorDTO.class);
            if(type == ScDormitoryfloorTypeEnum.FLOOR.code()){
                QueryWrapper<ScDfmapbindingEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("df_floorid",entity.getDfFloorid());
                ScDfmapbindingEntity scDfmapbindingEntity = scDfmapbindingDao.selectOne(queryWrapper);
                if(scDfmapbindingEntity != null){
                    String[] uwbFloors = {scDfmapbindingEntity.getMapId().toString(),scDfmapbindingEntity.getFloor()};
                    dormitoryfloorDTO.setUwbFloors(uwbFloors);
                }
            }
        }

        return dormitoryfloorDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScDormitoryfloorDTO dto) {
        //0 学生宿舍  1 教师宿舍 2 教学楼
//        String roleNames = SecurityUser.getUser().getRoleNames();
//
//        if(dto.getDfPurpose() == 0 && !roleNames.contains("学生宿舍")){
//            throw new RenException("无权限");
//        }
//        if(dto.getDfPurpose() == 1 && !roleNames.contains("教师宿舍")){
//            throw new RenException("无权限");
//        }
//        if(dto.getDfPurpose() == 2 && !roleNames.contains("教学楼")){
//            throw new RenException("无权限");
//        }


        if(dto.getDfType() == ScDormitoryfloorTypeEnum.ROOM.code()){
            ScDormitoryEntity scDormitoryEntity = new ScDormitoryEntity();
            scDormitoryEntity.setDfFloorid(dto.getDfParentid());
            scDormitoryEntity.setDrNum(dto.getDfFloorname());
            scDormitoryEntity.setDrCapacity(dto.getDrCapacity());
            scDormitoryEntity.setDrState(1);
            scDormitoryEntity.setDfPurpose(dto.getDfPurpose());
            scDormitoryEntity.setDfIsfull(0);
            scDormitoryDao.insert(scDormitoryEntity);
        }else{
            ScDormitoryfloorEntity entity = ConvertUtils.sourceToTarget(dto, ScDormitoryfloorEntity.class);
            ScDormitoryfloorEntity parentEntity = baseDao.getByPId(entity.getDfParentid());
            if(parentEntity != null && parentEntity.getLeaf() == ScDormitoryfloorLeafEnum.YES.value()) {
                parentEntity.setLeaf(ScDormitoryfloorLeafEnum.NO.value());
                baseDao.updateById(parentEntity);
            }
            entity.setLeaf(ScDormitoryfloorLeafEnum.YES.value());

            insert(entity);

            //楼层与星网地图绑定表
            String[] uwbFloors = dto.getUwbFloors();
            if(uwbFloors != null && uwbFloors.length >1){
                ScDfmapbindingEntity scDfmapbindingEntity = new ScDfmapbindingEntity();
                scDfmapbindingEntity.setDfFloorid(entity.getDfFloorid());
                scDfmapbindingEntity.setMapId(Long.valueOf(uwbFloors[0]));
                scDfmapbindingEntity.setFloor(uwbFloors[1]);
                scDfmapbindingDao.insert(scDfmapbindingEntity);
            }

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScDormitoryfloorDTO dto) {
        //0 学生宿舍  1 教师宿舍 2 教学楼
//        String roleNames = SecurityUser.getUser().getRoleNames();
//
//        if(dto.getDfPurpose() == 0 && !roleNames.contains("学生宿舍")){
//            throw new RenException("无权限");
//        }
//        if(dto.getDfPurpose() == 1 && !roleNames.contains("教师宿舍")){
//            throw new RenException("无权限");
//        }
//        if(dto.getDfPurpose() == 2 && !roleNames.contains("教学楼")){
//            throw new RenException("无权限");
//        }
        if(dto.getDfType() == ScDormitoryfloorTypeEnum.ROOM.code()){
            ScDormitoryEntity scDormitoryEntity = new ScDormitoryEntity();
            scDormitoryEntity.setDrId(dto.getDfFloorid());
            scDormitoryEntity.setDfFloorid(dto.getDfParentid());
            scDormitoryEntity.setDrNum(dto.getDfFloorname());
            scDormitoryEntity.setDrCapacity(dto.getDrCapacity());
            scDormitoryEntity.setDfPurpose(dto.getDfPurpose());
            scDormitoryDao.updateById(scDormitoryEntity);
        }else {

            ScDormitoryfloorEntity entity = ConvertUtils.sourceToTarget(dto, ScDormitoryfloorEntity.class);
            //上级不能为自身
            if (entity.getDfFloorid().equals(entity.getDfParentid())) {
                throw new RenException(ErrorCode.SUPERIOR_REGION_ERROR);
            }
            ScDormitoryfloorEntity parentEntity = baseDao.getByPId(entity.getDfParentid());
            if (parentEntity != null && parentEntity.getLeaf() == ScDormitoryfloorLeafEnum.YES.value()) {
                parentEntity.setLeaf(ScDormitoryfloorLeafEnum.NO.value());
                baseDao.updateById(parentEntity);
            }
            //查询下级
            int subCount = baseDao.getCountByPid(dto.getDfFloorid());
            if (subCount == 0) {
                entity.setLeaf(ScDormitoryfloorLeafEnum.YES.value());
            } else {
                entity.setLeaf(ScDormitoryfloorLeafEnum.NO.value());
            }
            updateById(entity);

            //楼层与星网地图绑定表
            String[] uwbFloors = dto.getUwbFloors();
            QueryWrapper<ScDfmapbindingEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("df_floorid",entity.getDfFloorid());
            ScDfmapbindingEntity scDfmapbindingEntity = scDfmapbindingDao.selectOne(queryWrapper);
            if(uwbFloors != null && uwbFloors.length >1) {
                if (scDfmapbindingEntity != null) {
                    scDfmapbindingEntity.setMapId(Long.valueOf(uwbFloors[0]));
                    scDfmapbindingEntity.setFloor(uwbFloors[1]);
                    scDfmapbindingDao.updateById(scDfmapbindingEntity);
                } else {
                    scDfmapbindingEntity = new ScDfmapbindingEntity();
                    scDfmapbindingEntity.setDfFloorid(entity.getDfFloorid());
                    scDfmapbindingEntity.setMapId(Long.valueOf(uwbFloors[0]));
                    scDfmapbindingEntity.setFloor(uwbFloors[1]);
                    scDfmapbindingDao.insert(scDfmapbindingEntity);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id,Integer type) {
        //逻辑删除
        //logicDelete(ids, ScDormitoryfloorEntity.class);
        QueryWrapper<ScDormitorypersonEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dr_id",id);
        List<ScDormitorypersonEntity> scDormitorypersonEntities = scDormitorypersonDao.selectList(queryWrapper);
        if(scDormitorypersonEntities != null && scDormitorypersonEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.ROOM.code()){
            throw new RenException("该房间关联有设备，不允许删除");
        }
        if(scDormitorypersonEntities != null && scDormitorypersonEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.FLOOR.code()){
            throw new RenException("该楼层关联有设备，不允许删除");
        }
        if(scDormitorypersonEntities != null && scDormitorypersonEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.BUILDING.code()){
            throw new RenException("该楼栋关联有设备，不允许删除");
        }

        //485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备';
        QueryWrapper<ScModbusdevicedcEntity> scModbusdevicedcEntityQueryWrapper = new QueryWrapper<>();
        scModbusdevicedcEntityQueryWrapper.apply("FIND_IN_SET ("+id+",mbd_uwbaddr)");
        List<ScModbusdevicedcEntity> scModbusdevicedcEntities = scModbusdevicedcDao.selectList(scModbusdevicedcEntityQueryWrapper);
        if(scModbusdevicedcEntities != null && scModbusdevicedcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.ROOM.code()){
            throw new RenException("该房间关联有设备，不允许删除");
        }
        if(scModbusdevicedcEntities != null && scModbusdevicedcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.FLOOR.code()){
            throw new RenException("该楼层关联有设备，不允许删除");
        }
        if(scModbusdevicedcEntities != null && scModbusdevicedcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.BUILDING.code()){
            throw new RenException("该楼栋关联有设备，不允许删除");
        }
        //互感器设备信息
        QueryWrapper<ScTransformerdcEntity> scTransformerdcEntityQueryWrapper = new QueryWrapper<>();
        scTransformerdcEntityQueryWrapper.apply("FIND_IN_SET ("+id+",tf_setupaddr)");
        List<ScTransformerdcEntity> scTransformerdcEntities = scTransformerdcDao.selectList(scTransformerdcEntityQueryWrapper);
        if(scTransformerdcEntities != null && scTransformerdcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.ROOM.code()){
            throw new RenException("该房间关联有设备，不允许删除");
        }
        if(scTransformerdcEntities != null && scTransformerdcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.FLOOR.code()){
            throw new RenException("该楼层关联有设备，不允许删除");
        }
        if(scTransformerdcEntities != null && scTransformerdcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.BUILDING.code()){
            throw new RenException("该楼栋关联有设备，不允许删除");
        }

        //互感器宿舍关联关系
        QueryWrapper<ScTransformerroomEntity> scTransformerroomEntityQueryWrapper = new QueryWrapper<>();
        scTransformerroomEntityQueryWrapper.eq("dr_id",id);
        List<ScTransformerroomEntity> scTransformerroomEntities = scTransformerroomDao.selectList(scTransformerroomEntityQueryWrapper);
        if(scTransformerroomEntities != null && scTransformerroomEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.ROOM.code()){
            throw new RenException("该房间关联有设备，不允许删除");
        }
        if(scTransformerroomEntities != null && scTransformerroomEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.FLOOR.code()){
            throw new RenException("该楼层关联有设备，不允许删除");
        }
        if(scTransformerroomEntities != null && scTransformerroomEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.BUILDING.code()){
            throw new RenException("该楼栋关联有设备，不允许删除");
        }

        //网关设备
        QueryWrapper<ScGatewaydcEntity> scGatewaydcEntityQueryWrapper = new QueryWrapper<>();
        scGatewaydcEntityQueryWrapper.apply("FIND_IN_SET ("+id+",gw_setupaddr)");
        List<ScGatewaydcEntity> scGatewaydcEntities = scGatewaydcDao.selectList(scGatewaydcEntityQueryWrapper);
        if(scGatewaydcEntities != null && scGatewaydcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.ROOM.code()){
            throw new RenException("该房间关联有设备，不允许删除");
        }
        if(scGatewaydcEntities != null && scGatewaydcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.FLOOR.code()){
            throw new RenException("该楼层关联有设备，不允许删除");
        }
        if(scGatewaydcEntities != null && scGatewaydcEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.BUILDING.code()){
            throw new RenException("该楼栋关联有设备，不允许删除");
        }

        //人证设备
        QueryWrapper<ScPersonidequipEntity> scPersonidequipEntityQueryWrapper = new QueryWrapper<>();
        scPersonidequipEntityQueryWrapper.apply("FIND_IN_SET ("+id+",pie_setupaddr)");
        List<ScPersonidequipEntity> scPersonidequipEntities = scPersonidequipDaoDao.selectList(scPersonidequipEntityQueryWrapper);
        if(scPersonidequipEntities != null && scPersonidequipEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.ROOM.code()){
            throw new RenException("该房间关联有设备，不允许删除");
        }
        if(scPersonidequipEntities != null && scPersonidequipEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.FLOOR.code()){
            throw new RenException("该楼层关联有设备，不允许删除");
        }
        if(scPersonidequipEntities != null && scPersonidequipEntities.size() > 0 && type == ScDormitoryfloorTypeEnum.BUILDING.code()){
            throw new RenException("该楼栋关联有设备，不允许删除");
        }

        //物理删除
        if(type == ScDormitoryfloorTypeEnum.ROOM.code()){

            scDormitoryDao.deleteById(id);
        }else {
            if(type == ScDormitoryfloorTypeEnum.BUILDING.code() ){
                QueryWrapper<ScDormitoryfloorEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("df_parentid",id);
                List<ScDormitoryfloorEntity> entities = baseDao.selectList(wrapper);
                if(entities != null && entities.size()> 0 ){
                    throw new RenException("下级有关联，不允许删除");
                }

            }


            if(type == ScDormitoryfloorTypeEnum.FLOOR.code() ) {
                QueryWrapper<ScDormitoryEntity> dormitoryEntityQueryWrapper = new QueryWrapper<>();
                dormitoryEntityQueryWrapper.eq("df_floorid", id);
                List<ScDormitoryEntity> scDormitoryEntities = scDormitoryDao.selectList(dormitoryEntityQueryWrapper);
                if (scDormitoryEntities != null && scDormitoryEntities.size() > 0) {
                    throw new RenException("下级有关联，不允许删除");
                }
            }

            baseDao.deleteById(id);
        }
    }

    @Override
    public List<Map<String, Object>> getTreeList(Integer dfPurpose) {
        return baseDao.getTreeList(dfPurpose);
    }

    @Override
    public List<Map<String, Object>> roomTree(Integer dfPurpose) {
        List<Map<String, Object>> treeList = baseDao.getTreeList(dfPurpose);
        QueryWrapper<ScDormitoryEntity> queryWrapper = new QueryWrapper<>();
        if(dfPurpose != -1){
            queryWrapper.eq("df_purpose",dfPurpose);
        }
        queryWrapper.orderByAsc("cast(dr_num as signed)");
        List<ScDormitoryEntity> scDormitoryEntities = scDormitoryDao.selectList(queryWrapper);
        for (ScDormitoryEntity scDormitoryEntity : scDormitoryEntities) {
            Map<String, Object> map = new HashMap<>();
            map.put("df_floorid",scDormitoryEntity.getDrId());
            map.put("df_parentid",scDormitoryEntity.getDfFloorid());
            map.put("df_floorname",scDormitoryEntity.getDrNum());
            map.put("df_type",2);
            treeList.add(map);
        }
        return treeList;
    }

    @Override
    public List<ScDormitoryDTO> getByFloorId(Long floorId) {
        QueryWrapper<ScDormitoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("df_floorid",floorId);
        List<ScDormitoryEntity> scDormitoryEntities = scDormitoryDao.selectList(queryWrapper);
        List<ScDormitoryDTO> scDormitoryDTOS = ConvertUtils.sourceToTarget(scDormitoryEntities, ScDormitoryDTO.class);
        return scDormitoryDTOS;
    }

    @Override
    public String findName(String s) {
        return baseDao.findName(s);
    }

    @Override
    public List<UwbFloorDTO> uwbbuildingtree() {


        //TODO: 调用uwb系统的接口获取楼栋数据json数据
        String str="{\n" +
                "\"status\":200,\n" +
                "\"result\":[\n" +
                "{\n" +
                "\"id\":10,\n" +
                "\"parkId\":4,\n" +
                "\"buildingId\":0,\n" +
                "\"buildingName\":\"0\",\n" +
                "\"mapType\":0,\n" +
                "\"map\":\"/group1/default/20200829/01/40/3/nkt.acmap\",\n" +
                "\"mapName\":\"nkt\"\n" +
                "},\n" +
                "{\n" +
                "\"id\":11,\n" +
                "\"parkId\":4,\n" +
                "\"buildingId\":2,\n" +
                "\"buildingName\":\"人工智能专业化众创空间\",\n" +
                "\"mapType\":1,\n" +
                "\"map\":\"/group1/default/20200829/01/40/3/xw2.acmap\",\n" +
                "\"mapName\":\"xw2\",\n" +
                "\"floorList\":\"[\\\"F1\\\",\\\"F2\\\",\\\"F3\\\",\\\"F4\\\",\\\"F5\\\",\\\"F6\\\",\\\"F7\\\",\\\"F8\\\",\\\"F9\\\",\\\"F10\\\",\\\"F11\\\",\\\"F12\\\",\\\"F13\\\",\\\"F14\\\",\\\"F15\\\",\\\"F16\\\",\\\"F17\\\",\\\"F18\\\",\\\"F19\\\",\\\"F20\\\"]\"\n" +
                "},\n" +
                "{\n" +
                "\"id\":12,\n" +
                "\"parkId\":4,\n" +
                "\"buildingId\":1,\n" +
                "\"buildingName\":\"国家级无线通信众创空间\",\n" +
                "\"mapType\":1,\n" +
                "\"map\":\"/group1/default/20200829/01/40/3/xw.acmap\",\n" +
                "\"mapName\":\"xw\",\n" +
                "\"floorList\":\"[\\\"F1\\\",\\\"F2\\\",\\\"F3\\\",\\\"F4\\\",\\\"F5\\\",\\\"F6\\\",\\\"F7\\\",\\\"F8\\\",\\\"F9\\\",\\\"F10\\\",\\\"F11\\\",\\\"F12\\\",\\\"F13\\\",\\\"F14\\\",\\\"F15\\\",\\\"F16\\\",\\\"F17\\\",\\\"F18\\\",\\\"F19\\\",\\\"F20\\\",\\\"F21\\\",\\\"F22\\\",\\\"F23\\\",\\\"F24\\\",\\\"F25\\\",\\\"F26\\\"]\"\n" +
                "}\n" +
                "]\n" +
                "}";


        List<UwbFloorDTO> treeBuild=new ArrayList<>();
        JSONObject jb = JSONObject.parseObject(str);
        JSONArray jsonArray = jb.getJSONArray("result");
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            List<Map<String,Object>> floorList=new ArrayList<>();
            int mapType= jsonObject.getInteger("mapType");
            if(mapType==1) {
                UwbFloorDTO build= new UwbFloorDTO();

                JSONArray jsonsub= jsonObject.getJSONArray("floorList");
                List<UwbFloorDTO> listInnerfloor=new ArrayList<>();
                for (Object subobj : jsonsub) {
                    UwbFloorDTO innerfloor=new UwbFloorDTO();
                    innerfloor.setValue(subobj.toString());
                    innerfloor.setLabel(subobj.toString());
                    innerfloor.setChildren(new ArrayList<>());
                    listInnerfloor.add(innerfloor);
                }
                build.setLabel(jsonObject.getString("buildingName"));
                build.setValue(jsonObject.getString("id") );
                build.setChildren(listInnerfloor);
                treeBuild.add(build);
            }
        }
        return treeBuild;
    }

    @Override
    public List<Map<String, Object>> roleTree() {
        String roleNames = SecurityUser.getUser().getRoleNames();
        List<Integer> dfPurposes = new ArrayList<>();
        if(roleNames.contains("学生宿舍")){
            dfPurposes.add(0);
        }
        if(roleNames.contains("教师宿舍")){
            dfPurposes.add(1);
        }
        if(roleNames.contains("教学楼")){
            dfPurposes.add(2);
        }
        return baseDao.roleTree(dfPurposes);
    }

}
