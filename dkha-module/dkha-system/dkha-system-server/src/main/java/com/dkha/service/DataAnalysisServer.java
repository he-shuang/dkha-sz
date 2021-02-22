package com.dkha.service;

import com.dkha.dto.DataAnalysisDTO;

import java.util.List;
import java.util.Map;

public interface DataAnalysisServer {
    List<DataAnalysisDTO> notInOrOut(Map<String, Object> params);

    List<DataAnalysisDTO> onlyInNotOut(Map<String, Object> params);

    List<DataAnalysisDTO> onlyOutNotIn(Map<String, Object> params);
}
