package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.enums.DelFlagEnum;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.security.user.SecurityUser;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.enums.SuperAdminEnum;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScFaceverificationDao;
import com.dkha.dto.ScFaceverificationDTO;
import com.dkha.entity.ScFaceverificationEntity;
import com.dkha.exception.ModuleErrorCode;
import com.dkha.service.ScFaceverificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 刷脸或卡记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@Service
public class ScFaceverificationServiceImpl extends BaseServiceImpl<ScFaceverificationDao, ScFaceverificationEntity> implements ScFaceverificationService {

    @Override
    public PageData<ScFaceverificationDTO> page(Map<String, Object> params) {

        //超级管理员数据不过滤
        UserDetail user = SecurityUser.getUser();
        if(user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            params.put("type", null);
        }
        // 转换成like
        paramsToLike(params, "recognitionName");
        // 分页
        int inowpage = Integer.parseInt((String) params.get("page"));
        int ipagesize = Integer.parseInt((String) params.get("limit"));
        int scol = ipagesize * (inowpage - 1);
        params.put("scol", scol);
        params.put("ipagesize", ipagesize);
        params.put("deptId",user.getDeptId());
        // 查询
        List<ScFaceverificationEntity> list = baseDao.getMyList(params);
        long total = baseDao.getMyCount(params);

        return getPageData(list, total, ScFaceverificationDTO.class);
    }

    @Override
    public List<ScFaceverificationDTO> list(Map<String, Object> params) {
        //超级管理员数据不过滤
        UserDetail user = SecurityUser.getUser();
        if(user.getSuperAdmin() == SuperAdminEnum.YES.value()){
            params.put("type",null);
        }
        // 转换成like
        paramsToLike(params, "recognitionName");

        // 分页
        int inowpage = Integer.parseInt((String) params.get("page"));
        int ipagesize = Integer.parseInt((String) params.get("limit"));
        int scol = ipagesize * (inowpage - 1);
        params.put("scol", scol);
        params.put("ipagesize", ipagesize);
        // 查询
        List<ScFaceverificationEntity> list = baseDao.getMyList(params);

        return ConvertUtils.sourceToTarget(list, ScFaceverificationDTO.class);
    }

    private QueryWrapper<ScFaceverificationEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScFaceverificationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScFaceverificationDTO get(String id) {
        ScFaceverificationEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScFaceverificationDTO.class);
    }

}
