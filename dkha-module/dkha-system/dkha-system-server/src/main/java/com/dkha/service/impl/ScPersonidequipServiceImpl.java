

package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScPersonidequipDao;
import com.dkha.dto.ScModbusdevicedcDTO;
import com.dkha.dto.ScPersonidequipDTO;
import com.dkha.entity.ScModbusdevicedcEntity;
import com.dkha.entity.ScPersonidequipEntity;
import com.dkha.entity.ScTransformerdcEntity;
import com.dkha.service.ScPersonidequipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人证识别设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Service
public class ScPersonidequipServiceImpl extends BaseServiceImpl<ScPersonidequipDao, ScPersonidequipEntity> implements ScPersonidequipService {

    @Autowired
    private ScDormitoryfloorServiceImpl scDormitoryfloorService;

    @Override
    public PageData<ScPersonidequipDTO> page(Map<String, Object> params) {
        params.put("pie_id", "desc");
        params.put("pie_setupdate", "desc");
        IPage<ScPersonidequipEntity> page = baseDao.selectPage(
                getPage(params, "pie_id", false),
                getWrapper(params)
        );

        PageData<ScPersonidequipDTO> pageData = getPageData(page, ScPersonidequipDTO.class);
        List<ScPersonidequipDTO> list = pageData.getList();
        int i = 0;
        for (ScPersonidequipDTO scPersonidequipDTO : list) {
            int j = 0;
            String[] split = page.getRecords().get(i).getPieSetupaddr().split(",");
            for (String s : split) {
                String name = scDormitoryfloorService.findName(s);
                Arrays.fill(split, j, j + 1, name);
                j++;
            }
            scPersonidequipDTO.setPieSetupaddr(split);
            i++;
        }

        return pageData;
    }

    @Override
    public List<ScPersonidequipDTO> list(Map<String, Object> params) {
        List<ScPersonidequipEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScPersonidequipDTO.class);
    }

    private QueryWrapper<ScPersonidequipEntity> getWrapper(Map<String, Object> params) {
        String pieDevicename = (String) params.get("pieDevicename");
        String pieStatus = (String) params.get("pieStatus");

        QueryWrapper<ScPersonidequipEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(pieDevicename), "pie_devicename", pieDevicename);
        wrapper.like(StringUtils.isNotBlank(pieStatus), "pie_status", pieStatus);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScPersonidequipDTO get(String id) {
        ScPersonidequipEntity entity = baseDao.selectById(id);

        ScPersonidequipDTO scPersonidequipDTO = ConvertUtils.sourceToTarget(entity, ScPersonidequipDTO.class);
        if (scPersonidequipDTO != null) {
            scPersonidequipDTO.setPieSetupaddr(entity.getPieSetupaddr().split(","));
        }
        return scPersonidequipDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScPersonidequipDTO dto) {
        ScPersonidequipEntity entity = ConvertUtils.sourceToTarget(dto, ScPersonidequipEntity.class);
        entity.setPieSetupdate(new Date());
        entity.setPieIsinitial(0);

        ScPersonidequipEntity devicesn = baseDao.selectOne(new QueryWrapper<ScPersonidequipEntity>()
                .eq("pie_equipsn", dto.getPieEquipsn()));

        if (devicesn != null) {
            throw new RenException("该设备序列号已存在，请重新输入");
        }
        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getPieSetupaddr();
        String join = String.join(",", tfSetupaddr);
        entity.setPieSetupaddr(join);
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScPersonidequipDTO dto) {
        ScPersonidequipEntity entity = ConvertUtils.sourceToTarget(dto, ScPersonidequipEntity.class);

        ScPersonidequipEntity devicesn = baseDao.selectOne(new QueryWrapper<ScPersonidequipEntity>()
                .eq("pie_equipsn", dto.getPieEquipsn()));
        if (devicesn != null) {
            if (!devicesn.getPieId().equals(dto.getPieId()) && devicesn.getPieEquipsn().equals(dto.getPieEquipsn())) {
                throw new RenException("该设备序列号已存在，请重新输入");
            }
        }
        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getPieSetupaddr();
        String join = String.join(",", tfSetupaddr);
        entity.setPieSetupaddr(join);
        entity.setPieIsinitial(0);
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        //逻辑删除
        //logicDelete(ids, ScPersonidequipEntity.class);

        //物理删除
        baseDao.deleteById(id);
    }

    @Override
    public List<ScPersonidequipDTO> getAll() {
        List<ScPersonidequipEntity> scPersonidequipEntities = baseDao.selectList(new QueryWrapper<>());
       return ConvertUtils.sourceToTarget(scPersonidequipEntities,ScPersonidequipDTO.class);
    }


}
