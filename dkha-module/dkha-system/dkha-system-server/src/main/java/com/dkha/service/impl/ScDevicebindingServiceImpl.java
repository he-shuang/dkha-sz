package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.mybatis.service.impl.CrudServiceImpl;
import com.dkha.dao.ScDevicebindingDao;
import com.dkha.dao.ScPmalarmDao;
import com.dkha.dao.ScTransformalarmDao;
import com.dkha.dto.AlarmMassge;
import com.dkha.dto.ScDevicebindingDTO;
import com.dkha.entity.ScDevicebindingEntity;
import com.dkha.entity.ScPmalarmEntity;
import com.dkha.entity.ScTransformalarmEntity;
import com.dkha.enums.TypeEnum;
import com.dkha.service.ScDevicebindingService;
import com.dkha.service.ScPmalarmService;
import com.dkha.service.ScTransformalarmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 星网云联设备位置绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Service
public class ScDevicebindingServiceImpl extends CrudServiceImpl<ScDevicebindingDao, ScDevicebindingEntity, ScDevicebindingDTO> implements ScDevicebindingService {
    @Autowired
    private ScTransformalarmDao scTransformalarmDao;

    @Autowired
    private ScPmalarmDao scPmalarmDao;
    @Override
    public QueryWrapper<ScDevicebindingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScDevicebindingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public List<ScDevicebindingEntity> getBadingDevice() {
        return this.baseDao.getBadingDevice();
    }

    @Override
    public List<ScDevicebindingEntity> getAllDevice() {

        return this.baseDao.getAllDevice();
    }

    @Override
    public List<ScDevicebindingEntity> getNoBangding() {
        return this.baseDao.getNoBangding();
    }

    @Override
    public List<AlarmMassge> getIsAlarm() {
        return this.baseDao.getIsAlarm();
    }

    @Override
    public List<AlarmMassge> getAlarmHistory(String startTime, String endTime) {
        return this.baseDao.getAlarmHistory(startTime,endTime);
    }

    @Override
    public void handleAlarm(AlarmMassge alarmMassge) {
        /**电流预警*/
        if(TypeEnum.TYPE_ELECTRIC.getCode().equals(alarmMassge.getAlarmType())){
            ScTransformalarmEntity scTransformalarmEntity = new ScTransformalarmEntity();
            //报警信息id
            scTransformalarmEntity.setTfaId(Long.valueOf(alarmMassge.getId()));
            //是否处理  1处理
            scTransformalarmEntity.setTfaIshandle(1);
            //处理时间
            scTransformalarmEntity.setTfaHandletime(new Date());
            scTransformalarmDao.updateById(scTransformalarmEntity);
            /**PM2.5*/
        }else {
            ScPmalarmEntity scPmalarmEntity = new ScPmalarmEntity();
            scPmalarmEntity.setPmaId(Long.valueOf(alarmMassge.getId()));
            //是否处理  1处理
            scPmalarmEntity.setPmaIshandle(1);
            //处理时间
            scPmalarmEntity.setPmaHandletime(new Date());
            scPmalarmDao.updateById(scPmalarmEntity);
        }
    }
}