package com.dkha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkha.dao.ScDeptDoorDao;
import com.dkha.entity.ScDeptDoorEntity;
import com.dkha.service.ScDeptDoorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ScDeptDoorServiceImpl extends ServiceImpl<ScDeptDoorDao, ScDeptDoorEntity> implements ScDeptDoorService {

    @Resource
    private ScDeptDoorDao scDeptDoorDao;

    @Override
    public boolean addOrUpdate(ScDeptDoorEntity deptDoorEntity) {
        return scDeptDoorDao.insertOrUpdate(deptDoorEntity) > 0;
    }

    @Override
    public List<ScDeptDoorEntity> findDeptDoorByDoorId(Long doorId) {
        return scDeptDoorDao.selectDeptDoorByDoorId(doorId);
    }
}
