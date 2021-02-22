

package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.NewsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 新闻
 */
@Mapper
public interface NewsDao extends BaseDao<NewsEntity> {

    List<NewsEntity> getList(Map<String, Object> params);

}
