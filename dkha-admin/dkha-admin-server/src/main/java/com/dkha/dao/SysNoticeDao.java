package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.SysNoticeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* 通知管理
*/
@Mapper
public interface SysNoticeDao extends BaseDao<SysNoticeEntity> {
    /**
     * 获取被通知的用户列表
     */
    List<SysNoticeEntity> getNoticeUserList(Map<String, Object> params);

    /**
     * 获取我的通知列表
     */
    List<SysNoticeEntity> getMyNoticeList(Map<String, Object> params);
}
