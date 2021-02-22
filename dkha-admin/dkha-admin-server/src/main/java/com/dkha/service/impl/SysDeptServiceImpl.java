

package com.dkha.service.impl;

import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.enums.SuperAdminEnum;
import com.dkha.commons.tools.exception.ErrorCode;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.TreeUtils;
import com.dkha.dao.SysDeptDao;
import com.dkha.dto.SysDeptDTO;
import com.dkha.entity.SysDeptEntity;
import com.dkha.service.SysDeptService;
import com.dkha.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * @since 1.0.0
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysDeptDTO> list(Map<String, Object> params) {
        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if(user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", getSubDeptIdList(user.getDeptId()));
        }

        //查询部门列表
        List<SysDeptEntity> entityList = baseDao.getList(params);

        List<SysDeptDTO> dtoList = ConvertUtils.sourceToTarget(entityList, SysDeptDTO.class);

        return TreeUtils.build(dtoList);
    }

    @Override
    public SysDeptDTO get(Long id) {
        //超级管理员，部门ID为null
        if(id == null){
            return null;
        }

        SysDeptEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, SysDeptDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysDeptDTO dto) {
        SysDeptEntity entity = ConvertUtils.sourceToTarget(dto, SysDeptEntity.class);

        entity.setPids(getPidList(entity.getPid()));
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDeptDTO dto) {
        SysDeptEntity entity = ConvertUtils.sourceToTarget(dto, SysDeptEntity.class);

        //上级部门不能为自身
        if(entity.getId().equals(entity.getPid())){
            throw new RenException(ErrorCode.SUPERIOR_DEPT_ERROR);
        }

        //上级部门不能为下级部门
        List<Long> subDeptList = getSubDeptIdList(entity.getId());
        if(subDeptList.contains(entity.getPid())){
            throw new RenException(ErrorCode.SUPERIOR_DEPT_ERROR);
        }

        entity.setPids(getPidList(entity.getPid()));
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //判断是否有子部门
        List<Long> subList = getSubDeptIdList(id);
        if(subList.size() > 1){
            throw new RenException(ErrorCode.DEPT_SUB_DELETE_ERROR);
        }

        //判断部门下面是否有用户
        int count = sysUserService.getCountByDeptId(id);
        if(count > 0){
            throw new RenException(ErrorCode.DEPT_USER_DELETE_ERROR);
        }

        //逻辑删除
        logicDelete(new Long[]{id}, SysDeptEntity.class);
    }

    @Override
    public List<Long> getSubDeptIdList(Long id) {
        List<Long> deptIdList = baseDao.getSubDeptIdList("%" + id + "%");
        deptIdList.add(id);

        return deptIdList;
    }

    /**
     * 获取所有上级部门ID
     * @param pid 上级ID
     */
    private String getPidList(Long pid){
        //顶级部门，无上级部门
        if(Constant.DEPT_ROOT.equals(pid)){
            return Constant.DEPT_ROOT + "";
        }

        //所有部门的id、pid列表
        List<SysDeptEntity> deptList = baseDao.getIdAndPidList();

        //list转map
        Map<Long, SysDeptEntity> map = new HashMap<>(deptList.size());
        for(SysDeptEntity entity : deptList){
            map.put(entity.getId(), entity);
        }

        //递归查询所有上级部门ID列表
        List<Long> pidList = new ArrayList<>();
        getPidTree(pid, map, pidList);

        return StringUtils.join(pidList, ",");
    }

    private void getPidTree(Long pid, Map<Long, SysDeptEntity> map, List<Long> pidList) {
        //顶级部门，无上级部门
        if(Constant.DEPT_ROOT.equals(pid)){
            return ;
        }

        //上级部门存在
        SysDeptEntity parent = map.get(pid);
        if(parent != null){
            getPidTree(parent.getPid(), map, pidList);
        }

        pidList.add(pid);
    }
}
