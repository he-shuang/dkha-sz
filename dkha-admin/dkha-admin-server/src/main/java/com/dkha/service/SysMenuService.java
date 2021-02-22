

package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.dto.SysMenuDTO;
import com.dkha.entity.SysMenuEntity;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 * @since 1.0.0
 */
public interface SysMenuService extends BaseService<SysMenuEntity> {

    SysMenuDTO get(Long id);

    void save(SysMenuDTO dto);

    void update(SysMenuDTO dto);

    void delete(Long id);

    /**
     * 菜单列表
     *
     * @param type 菜单类型
     */
    List<SysMenuDTO> getMenuList(Integer type);

    /**
     * 用户菜单列表
     *
     * @param userDetail 用户信息
     * @param type 菜单类型
     */
    List<SysMenuDTO> getUserMenuList(UserDetail userDetail, Integer type);

    /**
     * 用户菜单导航
     * @param userDetail 用户信息
     */
    List<SysMenuDTO> getUserMenuNavList(UserDetail userDetail);

    /**
     * 获取用户权限标识
     */
    Set<String> getUserPermissions(UserDetail userDetail);

    /**
     * 根据父菜单，查询子菜单
     * @param pid  父菜单ID
     */
    List<SysMenuDTO> getListPid(Long pid);
}
