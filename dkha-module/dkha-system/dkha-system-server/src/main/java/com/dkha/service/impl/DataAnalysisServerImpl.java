package com.dkha.service.impl;

import com.dkha.dao.ScStudentsInandoutDao;
import com.dkha.dto.DataAnalysisDTO;
import com.dkha.service.DataAnalysisServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DataAnalysisServerImpl implements DataAnalysisServer {

    @Autowired
    private ScStudentsInandoutDao scStudentsInandoutDao;

    @Override
    public List<DataAnalysisDTO> notInOrOut(Map<String, Object> params) {
        List<DataAnalysisDTO> dataAnalysisDTOS =  new ArrayList<>();
        String type = (String)params.get("type");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        int num = Integer.parseInt(params.get("num").toString());
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && num > 0){

//            params.put("vType",type.split(","));
//            List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.notInOrOut(params);
//            dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            //类型(2 == 教学楼闸机 , 1 == 宿舍闸机, 3 == 宿舍房间, 4 == UWB)
            if(type.contains("2")){
                params.put("vType",2);
                List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.notInOrOut(params);
                dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            }
            if(type.contains("1")){
                params.put("vType",1);
                List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.notInOrOut(params);
                dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            }
            if(type.contains("3")){
                params.put("vType",3);
                List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.notInOrOut(params);
                dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            }

            Collections.sort(dataAnalysisDTOS, new Comparator<DataAnalysisDTO>() {
                public int compare(DataAnalysisDTO o1, DataAnalysisDTO o2) {
                    String name1 =o1.getScStuname();//name1是从你list里面拿出来的一个
                    String name2= o2.getScStuname(); //name1是从你list里面拿出来的第二个name
                    return name1.compareTo(name2);
                }
            });
        }
        return dataAnalysisDTOS;
    }

    @Override
    public List<DataAnalysisDTO> onlyInNotOut(Map<String, Object> params) {
        List<DataAnalysisDTO> dataAnalysisDTOS =  new ArrayList<>();
        String type = (String)params.get("type");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        int num = Integer.parseInt(params.get("num").toString());
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && num > 0){

//            params.put("vType",type.split(","));
//            List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.onlyInNotOut(params);
//            dataAnalysisDTOS.addAll(dataAnalysisDTOS1);

            //类型(2 == 教学楼闸机 , 1 == 宿舍闸机, 3 == 宿舍房间, 4 == UWB)
            if(type.contains("2")){
                params.put("vType",2);
                List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.onlyInNotOut(params);
                dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            }
            if(type.contains("1")){
                params.put("vType",1);
                List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.onlyInNotOut(params);
                dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            }


            Collections.sort(dataAnalysisDTOS, new Comparator<DataAnalysisDTO>() {
                public int compare(DataAnalysisDTO o1, DataAnalysisDTO o2) {
                    String name1 =o1.getScStuname();//name1是从你list里面拿出来的一个
                    String name2= o2.getScStuname(); //name1是从你list里面拿出来的第二个name
                    return name1.compareTo(name2);
                }
            });
        }
        return dataAnalysisDTOS;
    }

    @Override
    public List<DataAnalysisDTO> onlyOutNotIn(Map<String, Object> params) {
        List<DataAnalysisDTO> dataAnalysisDTOS =  new ArrayList<>();
        String type = (String)params.get("type");
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        int num = Integer.parseInt(params.get("num").toString());
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate) && num > 0){

//            params.put("vType",type.split(","));
//            List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.onlyOutNotIn(params);
//            dataAnalysisDTOS.addAll(dataAnalysisDTOS1);

            //类型(2 == 教学楼闸机 , 1 == 宿舍闸机, 3 == 宿舍房间, 4 == UWB)
            if(type.contains("2")){
                params.put("vType",2);
                List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.onlyOutNotIn(params);
                dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            }
            if(type.contains("1")){
                params.put("vType",1);
                List<DataAnalysisDTO> dataAnalysisDTOS1 = scStudentsInandoutDao.onlyOutNotIn(params);
                dataAnalysisDTOS.addAll(dataAnalysisDTOS1);
            }


            Collections.sort(dataAnalysisDTOS, new Comparator<DataAnalysisDTO>() {
                public int compare(DataAnalysisDTO o1, DataAnalysisDTO o2) {
                    String name1 =o1.getScStuname();//name1是从你list里面拿出来的一个
                    String name2= o2.getScStuname(); //name1是从你list里面拿出来的第二个name
                    return name1.compareTo(name2);
                }
            });
        }
        return dataAnalysisDTOS;
    }
}
