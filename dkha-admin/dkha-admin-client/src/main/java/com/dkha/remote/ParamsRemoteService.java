package com.dkha.remote;

import com.alibaba.fastjson.JSON;
import com.dkha.commons.tools.exception.ErrorCode;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.feign.ParamsFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  参数
 */
@Component
public class ParamsRemoteService {
    @Autowired
    private ParamsFeignClient paramsFeignClient;

    /**
     * 根据参数编码，获取value的Object对象
     * @param paramCode  参数编码
     * @param clazz  Object对象
     */
    public <T> T getValueObject(String paramCode, Class<T> clazz) {
        String paramValue = paramsFeignClient.getValue(paramCode);
        if(StringUtils.isNotBlank(paramValue)){
            return JSON.parseObject(paramValue, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RenException(ErrorCode.PARAMS_GET_ERROR);
        }
    }

    /**
     * 根据参数编码，更新value
     * @param paramCode  参数编码
     * @param paramValue  参数值
     */
    public void updateValueByCode(String paramCode, String paramValue){
        paramsFeignClient.updateValueByCode(paramCode, paramValue);
    }

}
