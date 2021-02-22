package com.dkha.service;

import com.dkha.commons.mybatis.service.CrudService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.SysNoticeDTO;
import com.dkha.entity.SysNoticeEntity;

import java.util.Map;

/**
 * 通知管理
 */
public interface SysNoticeService extends CrudService<SysNoticeEntity, SysNoticeDTO> {

    /**
     * 获取被通知的用户
     */
    PageData<SysNoticeDTO> getNoticeUserPage(Map<String, Object> params);

    /**
     * 获取我的通知列表
     */
    PageData<SysNoticeDTO> getMyNoticePage(Map<String, Object> params);
}
