

package com.dkha.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.enums.SuperAdminEnum;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.UwbStatusInterface;
import com.dkha.dao.SysUserDao;
import com.dkha.dto.SysUserDTO;
import com.dkha.entity.SysUserEntity;
import com.dkha.service.SysDeptService;
import com.dkha.service.SysRoleUserService;
import com.dkha.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 * @since 1.0.0
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${uwb.url}")
    private String uwbUrl;

    @Override
    public PageData<SysUserDTO> page(Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "username");

        //分页
        IPage<SysUserEntity> page = getPage(params, Constant.CREATE_DATE, false);

        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if(user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
        }

        //查询
        List<SysUserEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), SysUserDTO.class);
    }

    @Override
    public List<SysUserDTO> list(Map<String, Object> params) {
        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if(user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
        }

        List<SysUserEntity> entityList = baseDao.getList(params);

        return ConvertUtils.sourceToTarget(entityList, SysUserDTO.class);
    }

    @Override
    public SysUserDTO get(Long id) {
        SysUserEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    public SysUserDTO getByUsername(String username) {
        SysUserEntity entity = baseDao.getByUsername(username);
        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserDTO dto) {
        // UWB新增用户
        Long uwbUserId = this.uwbAddUser(dto);
        dto.setUwbUserId(uwbUserId);

        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);

        //密码加密
        String password = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(password);

        //保存用户
        entity.setSuperAdmin(SuperAdminEnum.NO.value());
        insert(entity);

        //保存角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserDTO dto) {
        // UWB编辑用户
        this.uwbUpdateUser(dto);
        // UWB密码重置
        if(!"".equals(dto.getPassword())){
            this.uwbRepasswordUser(dto.getUwbUserId(), dto.getPassword(), false);
        }

        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);

        //密码加密
        if(StringUtils.isBlank(dto.getPassword())){
            entity.setPassword(null);
        }else{
            String password = passwordEncoder.encode(entity.getPassword());
            entity.setPassword(password);
        }

        //更新用户
        updateById(entity);

        //更新角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //逻辑删除
        //logicDelete(ids, SysUserEntity.class);
        //物理删除
        deleteBatchIds(Arrays.asList(ids));

        //角色用户关系，需要保留，不然逻辑删除就变成物理删除了
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long id, String newPassword) {
        // UWB密码重置
        this.uwbRepasswordUser(id, newPassword, true);

        newPassword = passwordEncoder.encode(newPassword);

        baseDao.updatePassword(id, newPassword);
    }

    @Override
    public int getCountByDeptId(Long deptId) {
        return baseDao.getCountByDeptId(deptId);
    }

    @Override
    public List<Long> getUserIdListByDeptId(List<Long> deptIdList) {
        return baseDao.getUserIdListByDeptId(deptIdList);
    }

    /**
     * UWB新增用户
     */
    private Long uwbAddUser(SysUserDTO dto){
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("email", dto.getEmail());
        jsonParam.put("loginName", dto.getUsername());
        jsonParam.put("nickname", dto.getRealName());
        jsonParam.put("phone", dto.getMobile());
        jsonParam.put("roleIds", new int[]{2});
        jsonParam.put("password", dto.getPassword());
        jsonParam.put("status", dto.getStatus());
        String result = CloseableHttpClientToInterface.uwbAddUser(uwbUrl, jsonParam.toString(), "1");

        String curStatus = UwbStatusInterface.UwbStatus(result);
        JSONObject statusObject = JSONObject.parseObject(curStatus);
        Integer statusCode = statusObject.getInteger("code");
        // 异常
        if(statusCode.intValue() != 0){
            throw new RenException(statusObject.getString("msg"));
        }

        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getLong("result");
    }

    /**
     * UWB编辑用户
     */
    private void uwbUpdateUser(SysUserDTO dto){
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("email", dto.getEmail());
        jsonParam.put("nickname", dto.getRealName());
        jsonParam.put("phone", dto.getMobile());
        jsonParam.put("roleIds", new int[]{2});
        jsonParam.put("status", dto.getStatus());
        String result = CloseableHttpClientToInterface.uwbUpdateUser(uwbUrl, dto.getUwbUserId(), jsonParam.toString(), "1");

        String curStatus = UwbStatusInterface.UwbStatus(result);
        JSONObject statusObject = JSONObject.parseObject(curStatus);
        Integer statusCode = statusObject.getInteger("code");
        // 异常
        if(statusCode.intValue() != 0){
            throw new RenException(statusObject.getString("msg"));
        }
    }

    /**
     * UWB密码重置
     * @param flag 传值我们系统用户ID：true 传星网用户ID false
     */
    private void uwbRepasswordUser(Long id, String newPassword, Boolean flag){
        Long uwbUserId = id;
        if(flag){
            SysUserDTO dto = this.get(id);
            uwbUserId = dto.getUwbUserId();
        }
        String result = CloseableHttpClientToInterface.uwbRepasswordUser(uwbUrl, uwbUserId, newPassword, "1");

        String curStatus = UwbStatusInterface.UwbStatus(result);
        JSONObject statusObject = JSONObject.parseObject(curStatus);
        Integer statusCode = statusObject.getInteger("code");
        // 异常
        if(statusCode.intValue() != 0){
            throw new RenException(statusObject.getString("msg"));
        }
    }

    /**
     * UWB设置用户状态
     */
    private void uwbStatusUser(SysUserDTO dto){
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("status", dto.getStatus());
        String result = CloseableHttpClientToInterface.uwbStatusUser(uwbUrl, dto.getUwbUserId(), jsonParam.toString(), "1");

        String curStatus = UwbStatusInterface.UwbStatus(result);
        JSONObject statusObject = JSONObject.parseObject(curStatus);
        Integer statusCode = statusObject.getInteger("code");
        // 异常
        if(statusCode.intValue() != 0){
            throw new RenException(statusObject.getString("msg"));
        }
    }
}
