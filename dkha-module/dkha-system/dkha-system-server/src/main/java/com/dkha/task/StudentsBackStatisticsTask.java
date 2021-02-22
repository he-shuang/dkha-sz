package com.dkha.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.dao.ScDormitorypersonDao;
import com.dkha.dao.ScStudentsOutEverydayDetailsDao;
import com.dkha.dao.ScStudentsOutHistoryDao;
import com.dkha.dto.ScAidooreightDTO;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.entity.ScStudentsOutEverydayDetailsEntity;
import com.dkha.entity.ScStudentsOutHistoryEntity;
import com.dkha.service.ScAidooreightService;
import com.dkha.service.ScDormitorypersonService;
import com.dkha.service.ScStudentsOutEverydayDetailsService;
import com.dkha.service.ScStudentsService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * All rights 成都电科慧安
 *
 * @ClassName: AidooreightTask
 * @description: 学生归寝统计
 * @create: 2020/9/16 11:02
 **/
@Slf4j
//@Component
//@EnableScheduling
public class StudentsBackStatisticsTask {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ScStudentsOutHistoryDao scStudentsOutHistoryDao;
    @Autowired
    private ScDormitorypersonDao scDormitorypersonDao;
    @Autowired
    private ScStudentsOutEverydayDetailsService scStudentsOutEverydayDetailsService;

    /*
     *  0 0 1 * * ?       每天凌晨1点执行一次
     *  0/5 * * * * ?     每隔5秒执行一次
     *  0 0/30 * * * ?    每隔30分钟执行一次
     *  0 0 0/1 * * ?     每隔1小时执行一次
     *  fixedDelay = 1000 * 60 * 5 间隔5分钟
     * */
//    @Scheduled(cron = "0 0 22 * * ? ")
    public void configureTasks() {
        // 执行任务
        logger.error("学生归寝统计定时任务执行开始***************: " + System.currentTimeMillis());
        QueryWrapper<ScDormitorypersonEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_out",1);
        List<ScDormitorypersonEntity> scDormitorypersonEntities = scDormitorypersonDao.selectList(queryWrapper);
        ScStudentsOutHistoryEntity scStudentsOutHistoryEntity = new ScStudentsOutHistoryEntity();
        scStudentsOutHistoryEntity.setDate(new Date());
        scStudentsOutHistoryEntity.setNum(scDormitorypersonEntities.size());
        scStudentsOutHistoryDao.insert(scStudentsOutHistoryEntity);
        ArrayList<ScStudentsOutEverydayDetailsEntity> everydayDetailsEntities = new ArrayList<>();
        for (ScDormitorypersonEntity entity : scDormitorypersonEntities) {
            ScStudentsOutEverydayDetailsEntity scStudentsOutEverydayDetailsEntity = new ScStudentsOutEverydayDetailsEntity();
            scStudentsOutEverydayDetailsEntity.setCreateDate(new Date());
            scStudentsOutEverydayDetailsEntity.setScStdid(entity.getScStdid());
            everydayDetailsEntities.add(scStudentsOutEverydayDetailsEntity);
        }
        if(everydayDetailsEntities.size() > 0){
            scStudentsOutEverydayDetailsService.insertBatch(everydayDetailsEntities);
        }
        logger.error("学生归寝统计定时任务执行结束***************: " + System.currentTimeMillis());
    }
}
