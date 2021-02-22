package com.dkha.service;


import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.DoorAndPersonListDTO;
import com.dkha.dto.ScDormitorypersonDTO;
import com.dkha.dto.ScDormitorypersonInfoDTO;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.excel.ScDormitorypersonExcel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 宿舍当前入住人员信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScDormitorypersonService extends BaseService<ScDormitorypersonEntity> {

    PageData<ScDormitorypersonInfoDTO> page(Map<String, Object> params);

    List<ScDormitorypersonDTO> list(Map<String, Object> params);

    ScDormitorypersonDTO get(String id);

    void save(List<ScDormitorypersonDTO> dto,DoorAndPersonListDTO doorAndPersonListDTO);

    void update(List<ScDormitorypersonDTO> dto,DoorAndPersonListDTO doorAndPersonListDTO);

    void delete(String[] ids);

    void delete(String drId);

    Map<String, Object> getRoomCheckInInfo(Map<String, Object> params);

    Map<String,Object> getOutNum();

    void importInfoExcel(MultipartFile file);

    List<FvScDeviceEntity> getByDrNum(String drNum);

    /**
     * 宿舍未归人员导出
     */
    void exportInfoExcel(HttpServletResponse response) throws Exception;

    /**
     * 获取字典中的学院名称
     * @param scSchool
     * @return
     */
    String getDictBySchool(String scSchool);
}
