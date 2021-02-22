package com.dkha.service.impl;

import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.dao.ScStatisticsDao;
import com.dkha.dao.ScStudentsInandoutDao;
import com.dkha.dao.ScVisitorrecordDao;
import com.dkha.dto.ScRoomCountDTO;
import com.dkha.dto.VisitorStatisticsDTO;
import com.dkha.entity.ScStatisticsEntity;
import com.dkha.service.ReportFormService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class ReportFormServiceImpl implements ReportFormService {

    @Autowired
    private ScVisitorrecordDao scVisitorrecordDao;
    @Autowired
    private ScStudentsInandoutDao scStudentsInandoutDao;

    @Autowired
    private ScStatisticsDao scStatisticsDao;

    @Override
    public List<VisitorStatisticsDTO> visitorWeek() {

        return scVisitorrecordDao.visitorWeek(new HashMap<>());

    }

    @Override
    public List<VisitorStatisticsDTO> visitorMonth() {

        return scVisitorrecordDao.visitorMonth(new HashMap<>());
    }


    @Override
    public List<VisitorStatisticsDTO> visitorWeekMonth(Map<String, Object> params) {
        //时间类型(1 == 周 , 2 == 月)
        List<VisitorStatisticsDTO> visitorStatisticsDTOS = new ArrayList<>();
        String dateType = (String)params.get("dateType");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
            if("1".equals(dateType)){
                Map<String, Object> stringListMap = DateUtils.weekDate(startDate, endDate);
                List<String> weeks = (List<String>) stringListMap.get("week");
                visitorStatisticsDTOS = scVisitorrecordDao.visitorWeek(params);
                for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                    weeks.remove(visitorStatisticsDTO.getDateStr());
                 }
                if(weeks.size() > 0){
                    for (String week : weeks) {
                        VisitorStatisticsDTO visitorStatisticsDTO = new VisitorStatisticsDTO();
                        visitorStatisticsDTO.setDateStr(week);
                        visitorStatisticsDTO.setCount("0");
                        visitorStatisticsDTO.setFloorname("");
                        visitorStatisticsDTOS.add(visitorStatisticsDTO);
                    }
                }
                for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                    //2020年第39周
                    String dateStr = visitorStatisticsDTO.getDateStr();
                    Map<String,Object> dates = (Map<String,Object>)stringListMap.get("dates");
                    visitorStatisticsDTO.setDateStr(dateStr + " ( " + dates.get(dateStr) + " )");
                }
            }else{
                visitorStatisticsDTOS = scVisitorrecordDao.visitorMonth(params);
                List<String> rangeMonths = DateUtils.getRangeMonths(startDate, endDate);
                for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                    rangeMonths.remove(visitorStatisticsDTO.getDateStr());
                }
                if(rangeMonths.size() > 0){
                    for (String rangeMonth : rangeMonths) {
                        VisitorStatisticsDTO visitorStatisticsDTO = new VisitorStatisticsDTO();
                        visitorStatisticsDTO.setDateStr(rangeMonth);
                        visitorStatisticsDTO.setCount("0");
                        visitorStatisticsDTO.setFloorname("");
                        visitorStatisticsDTOS.add(visitorStatisticsDTO);
                    }
                }
            }
        }

        Collections.sort(visitorStatisticsDTOS, new Comparator<VisitorStatisticsDTO>() {
            public int compare(VisitorStatisticsDTO o1, VisitorStatisticsDTO o2) {
                String name1 =o1.getDateStr();//name1是从你list里面拿出来的一个
                String name2= o2.getDateStr(); //name1是从你list里面拿出来的第二个name
                return name1.compareTo(name2);
            }
        });
        return visitorStatisticsDTOS;
    }

    @Override
    public List<VisitorStatisticsDTO> visitorWorkersWeekMonth(Map<String, Object> params) {
        //时间类型(1 == 周 , 2 == 月)
        List<VisitorStatisticsDTO> visitorStatisticsDTOS = new ArrayList<>();
        String dateType = (String)params.get("dateType");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            if ("1".equals(dateType)) {
                visitorStatisticsDTOS = scVisitorrecordDao.visitorWorkersWeek(params);

                Map<String, Object> stringListMap = DateUtils.weekDate(startDate, endDate);
                List<String> weeks = (List<String>) stringListMap.get("week");
                for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                    weeks.remove(visitorStatisticsDTO.getDateStr());
                }
                if(weeks.size() > 0){
                    for (String week : weeks) {
                        VisitorStatisticsDTO visitorStatisticsDTO = new VisitorStatisticsDTO();
                        visitorStatisticsDTO.setDateStr(week);
                        visitorStatisticsDTO.setCount("0");
                        visitorStatisticsDTO.setName("");
                        visitorStatisticsDTOS.add(visitorStatisticsDTO);
                    }
                }
                for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                    //2020年第39周
                    String dateStr = visitorStatisticsDTO.getDateStr();
                    Map<String,Object> dates = (Map<String,Object>)stringListMap.get("dates");
                    visitorStatisticsDTO.setDateStr(dateStr + " ( " + dates.get(dateStr) + " )");
                }
            } else {
                visitorStatisticsDTOS = scVisitorrecordDao.visitorWorkersMonth(params);
                List<String> rangeMonths = DateUtils.getRangeMonths(startDate, endDate);
                for (VisitorStatisticsDTO visitorStatisticsDTO : visitorStatisticsDTOS) {
                    rangeMonths.remove(visitorStatisticsDTO.getDateStr());
                }
                if(rangeMonths.size() > 0){
                    for (String rangeMonth : rangeMonths) {
                        VisitorStatisticsDTO visitorStatisticsDTO = new VisitorStatisticsDTO();
                        visitorStatisticsDTO.setDateStr(rangeMonth);
                        visitorStatisticsDTO.setCount("0");
                        visitorStatisticsDTO.setName("");
                        visitorStatisticsDTOS.add(visitorStatisticsDTO);
                    }
                }
            }
            Collections.sort(visitorStatisticsDTOS, new Comparator<VisitorStatisticsDTO>() {
                public int compare(VisitorStatisticsDTO o1, VisitorStatisticsDTO o2) {
                    String name1 =o1.getDateStr();//name1是从你list里面拿出来的一个
                    String name2= o2.getDateStr(); //name1是从你list里面拿出来的第二个name
                    return name1.compareTo(name2);
                }
            });
        }
        return visitorStatisticsDTOS;
    }

    @Override
    public List<ScRoomCountDTO> roomWeek() {
        return scStudentsInandoutDao.getByRoomCountWeek(new HashMap<>());
    }

    @Override
    public List<ScRoomCountDTO> getByRoomCountMoth() {
        return scStudentsInandoutDao.getByRoomCountMoth(new HashMap<>());
    }

    @Override
    public List<ScRoomCountDTO>  getByabnormalCountWeek() {
        return scStudentsInandoutDao.getByabnormalCountWeek(new HashMap<>());
    }

    @Override
    public List<ScRoomCountDTO>  getByabnormalCountMoth() {
        return scStudentsInandoutDao.getByabnormalCountMoth(new HashMap<>());
    }

    @Override
    public List<VisitorStatisticsDTO> visitorWorkersWeek() {
        return scVisitorrecordDao.visitorWorkersWeek(new HashMap<>());

    }

    @Override
    public List<VisitorStatisticsDTO> visitorWorkersMonth() {

        return scVisitorrecordDao.visitorWorkersMonth(new HashMap<>());
    }

    @Override
    public  List<ScStatisticsEntity>  roomStatistics(Map<String, Object> params) {
        List<ScStatisticsEntity> scRoomCountDTOS = new ArrayList<>();
        String numType = (String)params.get("numType");
        String dateType = (String)params.get("dateType");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        //时间类型(1 == 周 , 2 == 月)
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            if ("1".equals(dateType)) {
                //次数类型(1 == 次数 , 2 == 人数)
                if ("1".equals(numType)) {
                    scRoomCountDTOS = scStatisticsDao.getByCountWeek(params);

                } else {
                    scRoomCountDTOS = scStatisticsDao.getByCountWeek(params);
                }
                Map<String, Object> stringListMap = DateUtils.weekDate(startDate, endDate);
                List<String> weeks = (List<String>) stringListMap.get("week");
                for (ScStatisticsEntity visitorStatisticsDTO : scRoomCountDTOS) {
                    weeks.remove(visitorStatisticsDTO.getWeeks());
                }
                if (weeks.size() > 0) {
                    for (String week : weeks) {
                        scStatisticsEntityWeek(scRoomCountDTOS,week);
                    }
                }
                for (ScStatisticsEntity visitorStatisticsDTO : scRoomCountDTOS) {
                    //2020年第39周
                    String dateStr = visitorStatisticsDTO.getWeeks();
                    Map<String,Object> dates = (Map<String,Object>)stringListMap.get("dates");
                    visitorStatisticsDTO.setWeeks(dateStr + " ( " + dates.get(dateStr) + " )");
                }
                Collections.sort(scRoomCountDTOS, new Comparator<ScStatisticsEntity>() {
                    public int compare(ScStatisticsEntity o1, ScStatisticsEntity o2) {
                        String name1 =o1.getWeeks();//name1是从你list里面拿出来的一个
                        String name2= o2.getWeeks(); //name1是从你list里面拿出来的第二个name
                        return name1.compareTo(name2);
                    }
                });
            } else {
                //次数类型(1 == 次数 , 2 == 人数)
                if ("1".equals(numType)) {
                    scRoomCountDTOS = scStatisticsDao.getByCountMonths(params);
                } else {
                    scRoomCountDTOS = scStatisticsDao.getByCountMonths(params);
                }
                List<String> rangeMonths = DateUtils.getRangeMonths(startDate, endDate);
                for (ScStatisticsEntity scStatisticsEntity : scRoomCountDTOS) {
                    rangeMonths.remove(scStatisticsEntity.getMonths());
                }
                if(rangeMonths.size() > 0){
                    for (String rangeMonth : rangeMonths) {
                        scStatisticsEntityMonth(scRoomCountDTOS,rangeMonth);
                    }
                }
                Collections.sort(scRoomCountDTOS, new Comparator<ScStatisticsEntity>() {
                    public int compare(ScStatisticsEntity o1, ScStatisticsEntity o2) {
                        String name1 =o1.getMonths();//name1是从你list里面拿出来的一个
                        String name2= o2.getMonths(); //name1是从你list里面拿出来的第二个name
                        return name1.compareTo(name2);
                    }
                });
            }
        }

        return  scRoomCountDTOS;
    }


    @Override
    public List<ScStatisticsEntity> academicStatistics(Map<String, Object> params) {
        List<ScStatisticsEntity> scRoomCountDTOS = new ArrayList<>();
        String numType = (String)params.get("numType");
        String dateType = (String)params.get("dateType");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            //时间类型(1 == 周 , 2 == 月)
            if ("1".equals(dateType)) {
                //次数类型(1 == 次数 , 2 == 人数)
                //if("1".equals(numType)){
                scRoomCountDTOS = scStatisticsDao.getByAcademicWeek(params);
                Map<String, Object> stringListMap = DateUtils.weekDate(startDate, endDate);
                List<String> weeks = (List<String>) stringListMap.get("week");
                for (ScStatisticsEntity visitorStatisticsDTO : scRoomCountDTOS) {
                    weeks.remove(visitorStatisticsDTO.getWeeks());
                }
                if (weeks.size() > 0) {
                    for (String week : weeks) {
                        scStatisticsEntityWeek(scRoomCountDTOS,week);
                    }
                }
                for (ScStatisticsEntity visitorStatisticsDTO : scRoomCountDTOS) {
                    //2020年第39周
                    String dateStr = visitorStatisticsDTO.getWeeks();
                    Map<String,Object> dates = (Map<String,Object>)stringListMap.get("dates");
                    visitorStatisticsDTO.setWeeks(dateStr + " ( " + dates.get(dateStr) + " )");
                }
                Collections.sort(scRoomCountDTOS, new Comparator<ScStatisticsEntity>() {
                    public int compare(ScStatisticsEntity o1, ScStatisticsEntity o2) {
                        String name1 =o1.getWeeks();//name1是从你list里面拿出来的一个
                        String name2= o2.getWeeks(); //name1是从你list里面拿出来的第二个name
                        return name1.compareTo(name2);
                    }
                });
            } else {
                //次数类型(1 == 次数 , 2 == 人数)
                //if("1".equals(numType)){
                scRoomCountDTOS = scStatisticsDao.getByAcademicMonths(params);
                List<String> rangeMonths = DateUtils.getRangeMonths(startDate, endDate);
                for (ScStatisticsEntity scStatisticsEntity : scRoomCountDTOS) {
                    rangeMonths.remove(scStatisticsEntity.getMonths());
                }
                if(rangeMonths.size() > 0){
                    for (String rangeMonth : rangeMonths) {
                        scStatisticsEntityMonth(scRoomCountDTOS,rangeMonth);
                    }
                }
                Collections.sort(scRoomCountDTOS, new Comparator<ScStatisticsEntity>() {
                    public int compare(ScStatisticsEntity o1, ScStatisticsEntity o2) {
                        String name1 =o1.getMonths();//name1是从你list里面拿出来的一个
                        String name2= o2.getMonths(); //name1是从你list里面拿出来的第二个name
                        return name1.compareTo(name2);
                    }
                });
            }
        }
        return  scRoomCountDTOS;
    }

    @Override
    public List<ScStatisticsEntity> getByCountWeek(Map<String, Object> params) {
        return scStatisticsDao.getByCountWeek(params);
    }

    @Override
    public List<ScStatisticsEntity> getByCountMonths(Map<String, Object> params) {
        return scStatisticsDao.getByCountMonths(params);
    }

    @Override
    public List<ScStatisticsEntity> getByAcademicWeek(Map<String, Object> params) {
        return scStatisticsDao.getByAcademicWeek(params);
    }

    @Override
    public List<ScStatisticsEntity> getByAcademicMonths(Map<String, Object> params) {
        return scStatisticsDao.getByAcademicMonths(params);
    }

    @Override
    public List<ScRoomCountDTO> getByAcademicBuildingWeek() {
        return scStudentsInandoutDao.getByAcademicBuildingWeek();
    }

    @Override
    public List<ScRoomCountDTO> getByAcademicBuildingMonths() {
        return scStudentsInandoutDao.getByAcademicBuildingMonths();
    }

    @Override
    public List<ScRoomCountDTO> getByAcademicBuildingPersonneMonths() {
        return scStudentsInandoutDao.getByAcademicBuildingPersonneMonths();
    }

    @Override
    public List<ScRoomCountDTO> getByAcademicBuildingPersonneWeeks() {
        return scStudentsInandoutDao.getByAcademicBuildingPersonneWeeks();
    }
    /**保存周统计表 填充时间段内的周 */
    public  void scStatisticsEntityWeek(List<ScStatisticsEntity> scRoomCountDTOS,String week){
        ScStatisticsEntity scStatisticsEntity = new ScStatisticsEntity();
        scStatisticsEntity.setWeeks(week);
        scStatisticsEntity.setMonths(null);
        scStatisticsEntity.setType1(0);
        scStatisticsEntity.setType2(0);
        scStatisticsEntity.setType3(0);
        scStatisticsEntity.setTypestudent1(0);
        scStatisticsEntity.setTypestudent2(0);
        scStatisticsEntity.setTypestudent3(0);
        scRoomCountDTOS.add(scStatisticsEntity);
    }
    /**保存周统计表 填充时间段内的月份*/
    public  void scStatisticsEntityMonth(List<ScStatisticsEntity> scRoomCountDTOS,String moths){
        ScStatisticsEntity scStatisticsEntity = new ScStatisticsEntity();
        scStatisticsEntity.setMonths(moths);
        scStatisticsEntity.setType1(0);
        scStatisticsEntity.setType2(0);
        scStatisticsEntity.setType3(0);
        scStatisticsEntity.setTypestudent1(0);
        scStatisticsEntity.setTypestudent2(0);
        scStatisticsEntity.setTypestudent3(0);
        scRoomCountDTOS.add(scStatisticsEntity);
    }
}
