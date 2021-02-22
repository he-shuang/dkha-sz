package com.dkha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dkha.entity.ScDeptDoorEntity;

import java.util.List;

public interface ScDeptDoorService extends IService<ScDeptDoorEntity> {

    boolean addOrUpdate(ScDeptDoorEntity deptDoorEntity);

    List<ScDeptDoorEntity> findDeptDoorByDoorId(Long doorId);
}
