

package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.dto.SysRegionDTO;
import com.dkha.dto.region.RegionProvince;
import com.dkha.entity.SysRegionEntity;

import java.util.List;
import java.util.Map;

/**
 * 行政区域
 */
public interface SysRegionService extends BaseService<SysRegionEntity> {

	List<SysRegionDTO> list(Map<String, Object> params);

	List<Map<String, Object>> getTreeList();

	SysRegionDTO get(Long id);

	void save(SysRegionDTO dto);

	void update(SysRegionDTO dto);

	void delete(Long id);

	int getCountByPid(Long pid);

	List<RegionProvince> getRegion(boolean threeLevel);
}
