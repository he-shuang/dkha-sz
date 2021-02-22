package com.dkha.feign.fallback;

import com.dkha.feign.ParamsFeignClient;
import org.springframework.stereotype.Component;

/**
 * 参数接口 FallbackFactory
 */
@Component
public class ParamsFeignClientFallbackFactory implements ParamsFeignClient {
    @Override
    public String getValue(String paramCode) {
        return null;
    }

    @Override
    public void updateValueByCode(String paramCode, String paramValue) {

    }
}
