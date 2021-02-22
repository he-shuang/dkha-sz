package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.dao.ScDormitorypersonDao;
import com.dkha.dao.ScRegionConfigDao;
import com.dkha.dao.ScStudentsNotInSchoolDao;
import com.dkha.dto.ScStudentsNotInSchoolDTO;
import com.dkha.dto.ScStudentsNotInSchoolDTO;
import com.dkha.entity.*;
import com.dkha.service.ScDormitorypersonService;
import com.dkha.service.ScStudentsNotInSchoolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生未归寝每日统计 服务实现类
 * </p>
 *
 * @author Spring
 * @since 2020-12-07
 */
@Service
@Slf4j
public class ScStudentsNotInSchoolServiceImpl extends BaseServiceImpl<ScStudentsNotInSchoolDao, ScStudentsNotInSchoolEntity> implements ScStudentsNotInSchoolService {

    @Autowired
    private ScDormitorypersonDao scDormitorypersonDao;

    @Override
    public PageData<ScStudentsNotInSchoolDTO> page(Map<String, Object> params) {

        paramsToLike(params,"scStuname","scNo","drNum");
        IPage<ScStudentsNotInSchoolEntity> page = baseDao.findPage(
                getPage(params, "date", false),params
        );

        return getPageData(page,ScStudentsNotInSchoolDTO.class);
    }

    @Override
    public ScStudentsNotInSchoolDTO get(String id) {
        return null;
    }

    @Override
    public void save(ScStudentsNotInSchoolDTO dto) {

    }

    @Override
    public void update(ScStudentsNotInSchoolDTO dto) {

    }

    @Override
    public void delete(String[] ids) {

    }

    @Override
    public List<ScStudentsNotInSchoolDTO> list(Map<String, Object> params) {
        return null;
    }

    @Override
    public ScStudentsNotInSchoolDTO getTest() {

        //当前日期
        String currentDate = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
        //当日九点
        String startTime = currentDate+" 21:00:00";
        //当日十一点
        String endTime = currentDate+" 23:00:00";

        //获取记录
        List<Map<String, Object>> test = baseDao.getInSchoolRecord(startTime,endTime);
        List<String> sns = new ArrayList<>();
        for (Map<String, Object> map : test) {
            sns.add(map.get("sc_no").toString());
        }
        //获取疑似未归寝学生
        List<ScStudentsEntity> students = baseDao.getStudents(sns);
        for (ScStudentsEntity student : students) {

            Map<String, Object> goOutMsg = baseDao.getGoOutMsg(student.getScNo(),currentDate);
            //查看数据库中是否已存在记录
            QueryWrapper<ScStudentsNotInSchoolEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",Long.valueOf(goOutMsg.get("scStdid").toString()));
            List<ScStudentsNotInSchoolEntity> scStudentsNotInSchoolEntities = baseDao.selectList(queryWrapper);
            if (scStudentsNotInSchoolEntities.size()>0){
                continue;
            }
            //获取宿舍房间号
            String drNum = scDormitorypersonDao.getDrNumBySn(student.getScNo());
            ScStudentsNotInSchoolEntity scStudentsNotInSchoolEntity = new ScStudentsNotInSchoolEntity();
            scStudentsNotInSchoolEntity.setId(Long.valueOf(goOutMsg.get("scStdid").toString()));
            scStudentsNotInSchoolEntity.setScPhotoimg(goOutMsg.get("scPhotoimg").toString());
            scStudentsNotInSchoolEntity.setScStuname(goOutMsg.get("scStuname").toString());
            scStudentsNotInSchoolEntity.setScNo(goOutMsg.get("scNo").toString());
            scStudentsNotInSchoolEntity.setScPhonenum(goOutMsg.get("scPhonenum").toString());
            scStudentsNotInSchoolEntity.setScSchool(goOutMsg.get("scSchool").toString());
            scStudentsNotInSchoolEntity.setDrNum(drNum);

            if (goOutMsg.get("leaveDoorTime")!=null){
                //时间格式转换
                Date leaveDoorTime = DateUtils.parse(goOutMsg.get("leaveDoorTime").toString(), DateUtils.DATE_TIME_PATTERN);
                scStudentsNotInSchoolEntity.setLeaveDoorTime(leaveDoorTime);
            }
            if (goOutMsg.get("leaveSchoolTime")!=null){
                //时间格式转换
                Date leaveSchoolTime = DateUtils.parse(goOutMsg.get("leaveSchoolTime").toString(), DateUtils.DATE_TIME_PATTERN);
                scStudentsNotInSchoolEntity.setLeaveSchoolTime(leaveSchoolTime);
            }

            scStudentsNotInSchoolEntity.setDate(currentDate);
            baseDao.insert(scStudentsNotInSchoolEntity);
        }
        return null;
    }
}
