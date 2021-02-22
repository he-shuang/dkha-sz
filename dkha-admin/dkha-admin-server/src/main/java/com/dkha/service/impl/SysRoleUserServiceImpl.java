

package com.dkha.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.dao.SysRoleUserDao;
import com.dkha.entity.SysRoleUserEntity;
import com.dkha.service.SysRoleUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色用户关系
 * @since 1.0.0
 */
@Service
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUserDao, SysRoleUserEntity> implements SysRoleUserService {

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除角色用户关系
        deleteByUserId(userId);

        //用户没有一个角色权限的情况
        if(CollUtil.isEmpty(roleIdList)){
            return ;
        }

        //保存角色用户关系
        for(Long roleId : roleIdList){
            SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
            sysRoleUserEntity.setUserId(userId);
            sysRoleUserEntity.setRoleId(roleId);

            //保存
            insert(sysRoleUserEntity);
        }
    }

    @Override
    public void deleteByRoleIds(Long[] roleIds) {
        baseDao.deleteByRoleIds(roleIds);
    }

    @Override
    public void deleteByUserId(Long userId) {
        baseDao.deleteByUserId(userId);
    }

    @Override
    public List<Long> getRoleIdList(Long userId) {

        return baseDao.getRoleIdList(userId);
    }
}
