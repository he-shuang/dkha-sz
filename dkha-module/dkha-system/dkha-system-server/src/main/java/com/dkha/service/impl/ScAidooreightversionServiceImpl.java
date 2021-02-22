
package com.dkha.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dkha.commons.fileupload.minio.MinioUtil;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScAidooreightversionDao;
import com.dkha.dto.doorcontrol.ScAidooreightversionDTO;
import com.dkha.entity.ScAidooreightversionEntity;
import com.dkha.entity.ScStudentsEntity;
import com.dkha.service.ScAidooreightversionService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-21
 */
@Service
public class ScAidooreightversionServiceImpl extends BaseServiceImpl<ScAidooreightversionDao, ScAidooreightversionEntity> implements ScAidooreightversionService {

    @Autowired
    private MinioUtil minioUtil;

    @Override
    public PageData<ScAidooreightversionDTO> page(Map<String, Object> params) {
        IPage<ScAidooreightversionEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScAidooreightversionDTO.class);
    }

    @Override
    public List<ScAidooreightversionDTO> list(Map<String, Object> params) {
        List<ScAidooreightversionEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScAidooreightversionDTO.class);
    }

    private QueryWrapper<ScAidooreightversionEntity> getWrapper(Map<String, Object> params){
        String aevPackname = (String)params.get("aevPackname");
        String aeMainboard = (String)params.get("aeMainboard");

        QueryWrapper<ScAidooreightversionEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(aevPackname), "aev_packname", aevPackname);
        wrapper.eq(StringUtils.isNotBlank(aeMainboard), "ae_mainboard", aeMainboard);

        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScAidooreightversionDTO get(String id) {
        ScAidooreightversionEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScAidooreightversionDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScAidooreightversionDTO dto) {
        ScAidooreightversionEntity entity = ConvertUtils.sourceToTarget(dto, ScAidooreightversionEntity.class);
        entity.setAevId(IdWorker.getId());
        entity.setCreateDate(new Date());
        insert(entity);
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScAidooreightversionDTO dto) {
        ScAidooreightversionEntity entity = ConvertUtils.sourceToTarget(dto, ScAidooreightversionEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScAidooreightversionEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public String uploadNewVersionAPKfile(MultipartFile file) {
        if(file!=null) {
            //获取后缀
            String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
            JSONObject jsonObject = null;
            String originalFilename = file.getOriginalFilename();
            Long nowdate=System.currentTimeMillis();
            try {
                jsonObject = minioUtil.uploadFile(file.getBytes(),"eightdoorapk", nowdate+originalFilename.substring(0,originalFilename.indexOf(".")), suffix);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RenException("图片上传失败");
            }
             return   jsonObject.getString("path");
        }else
        {
            throw new RenException("图片上传信息错误，文件信息不能为空！" );
        }
    }
    @Override
    public    ScAidooreightversionEntity getLastVersionByType(Integer mainboard){
        return  baseDao.getLastVersionByType(mainboard);
    }
}