package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.CrudServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.SysNoticeDao;
import com.dkha.dto.SysNoticeDTO;
import com.dkha.entity.SysNoticeEntity;
import com.dkha.entity.SysNoticeUserEntity;
import com.dkha.enums.NoticeReadStatusEnum;
import com.dkha.enums.NoticeStatusEnum;
import com.dkha.enums.ReceiverTypeEnum;
import com.dkha.service.SysNoticeService;
import com.dkha.service.SysNoticeUserService;
import com.dkha.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通知管理
 */
@Service
public class SysNoticeServiceImpl extends CrudServiceImpl<SysNoticeDao, SysNoticeEntity, SysNoticeDTO> implements SysNoticeService {
    @Autowired
    private SysNoticeUserService sysNoticeUserService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public QueryWrapper<SysNoticeEntity> getWrapper(Map<String, Object> params){
        String type = (String)params.get("type");

        QueryWrapper<SysNoticeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(type), "type", type);
        wrapper.orderByDesc(Constant.CREATE_DATE);
        return wrapper;
    }

    @Override
    public PageData<SysNoticeDTO> getNoticeUserPage(Map<String, Object> params) {
        //分页
        IPage<SysNoticeEntity> page = getPage(params, null, false);

        //查询
        List<SysNoticeEntity> list = baseDao.getNoticeUserList(params);

        return getPageData(list, page.getTotal(), SysNoticeDTO.class);
    }

    @Override
    public PageData<SysNoticeDTO> getMyNoticePage(Map<String, Object> params) {
        //分页
        IPage<SysNoticeEntity> page = getPage(params, null, false);

        //查询
        params.put("receiverId", SecurityUser.getUserId());
        List<SysNoticeEntity> list = baseDao.getMyNoticeList(params);

        return getPageData(list, page.getTotal(), SysNoticeDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysNoticeDTO dto) {
        SysNoticeEntity entity = ConvertUtils.sourceToTarget(dto, SysNoticeEntity.class);

        //更新发送者信息
        if(dto.getStatus() == NoticeStatusEnum.SEND.value()){
            entity.setSenderName(SecurityUser.getUser().getRealName());
            entity.setSenderDate(new Date());
        }

        baseDao.insert(entity);

        //发送通知
        dto.setId(entity.getId());
        sendNotice(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysNoticeDTO dto) {
        SysNoticeEntity entity = ConvertUtils.sourceToTarget(dto, SysNoticeEntity.class);

        //更新发送者信息
        if(dto.getStatus() == NoticeStatusEnum.SEND.value()){
            entity.setSenderName(SecurityUser.getUser().getRealName());
            entity.setSenderDate(new Date());
        }

        this.updateById(entity);

        //发送通知
        sendNotice(dto);
    }

    /**
     * 发送通知
     */
    public void sendNotice(SysNoticeDTO notice){
        //如果是草稿，在不发送通知
        if(notice.getStatus() == NoticeStatusEnum.DRAFT.value()){
            return;
        }

        //全部用户
        if(notice.getReceiverType() == ReceiverTypeEnum.ALL.value()){
            //发送给全部用户
            sendAllUser(notice);
        }else {  //选中用户
            List<Long> userIdList = sysUserService.getUserIdListByDeptId(notice.getReceiverTypeList());
            if(userIdList.size() == 0){
                return;
            }

            //发送给选中用户
            sendUser(notice, userIdList);
        }
    }

    /**
     * 发送给全部用户
     */
    public void sendAllUser(SysNoticeDTO notice){
        SysNoticeUserEntity noticeUser = new SysNoticeUserEntity()
            .setNoticeId(notice.getId())
            .setReadStatus(NoticeReadStatusEnum.UNREAD.value());
        sysNoticeUserService.insertAllUser(noticeUser);
    }

    /**
     * 发送给选中用户
     */
    public void sendUser(SysNoticeDTO notice, List<Long> userIdList){
        userIdList.forEach(userId -> {
            SysNoticeUserEntity noticeUser = new SysNoticeUserEntity()
                .setNoticeId(notice.getId())
                .setReceiverId(userId)
                .setReadStatus(NoticeReadStatusEnum.UNREAD.value());

            sysNoticeUserService.save(noticeUser);
        });
    }

}
