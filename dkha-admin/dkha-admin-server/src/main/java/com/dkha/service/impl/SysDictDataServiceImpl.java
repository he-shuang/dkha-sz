

package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.config.ElectricMQConfig;
import com.dkha.dao.SysDictDataDao;
import com.dkha.dao.SysDictTypeDao;
import com.dkha.dto.SysDictDataDTO;
import com.dkha.entity.SysDictDataEntity;
import com.dkha.entity.SysDictTypeEntity;
import com.dkha.enums.ChangeDataEventEnum;
import com.dkha.service.SysDictDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 字典类型
 */
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataDao, SysDictDataEntity> implements SysDictDataService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SysDictTypeDao sysDictTypeDao;
    @Override
    public PageData<SysDictDataDTO> page(Map<String, Object> params) {
        IPage<SysDictDataEntity> page = baseDao.selectPage(
            getPage(params, "sort", true),
            getWrapper(params)
        );

        return getPageData(page, SysDictDataDTO.class);
    }

    private QueryWrapper<SysDictDataEntity> getWrapper(Map<String, Object> params){
        String dictTypeId = (String) params.get("dictTypeId");
        String dictLabel = (String) params.get("dictLabel");
        String dictValue = (String) params.get("dictValue");

        QueryWrapper<SysDictDataEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_type_id", dictTypeId);
        wrapper.like(StringUtils.isNotBlank(dictLabel), "dict_label", dictLabel);
        wrapper.like(StringUtils.isNotBlank(dictValue), "dict_value", dictValue);

        return wrapper;
    }

    @Override
    public SysDictDataDTO get(Long id) {
        SysDictDataEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysDictDataDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysDictDataDTO dto) {
        SysDictDataEntity entity = ConvertUtils.sourceToTarget(dto, SysDictDataEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDictDataDTO dto) {
        SysDictDataEntity entity = ConvertUtils.sourceToTarget(dto, SysDictDataEntity.class);

        updateById(entity);
        SysDictTypeEntity sysDictTypeEntity = sysDictTypeDao.selectById(dto.getDictTypeId());
        if(ChangeDataEventEnum.ELECTRICVALUE.getMsg().equals(sysDictTypeEntity.getDictType())){
            rabbitTemplate.convertAndSend(ElectricMQConfig.MQ_UPDATE_CURRENTVALUE_EXCHANGE,null,ChangeDataEventEnum.ELECTRICVALUE_WRING.getCode());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //删除
        deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<SysDictDataDTO> getByType(String type) {
        return baseDao.getByType(type);
    }

}
