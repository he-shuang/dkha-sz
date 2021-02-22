package com.dkha.service;

import com.dkha.commons.dynamic.datasource.annotation.DataSource;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.dao.SysUserDao;
import com.dkha.entity.SysUserEntity;

/**
 * 测试多数据源
 * @since 1.1.0
 */
@DataSource("slave2")
public class DynamicService extends BaseServiceImpl<SysUserDao, SysUserEntity> {
}
