package com.dkha.feign.fallback;

import com.dkha.commons.tools.utils.Result;
import com.dkha.feign.SysAdminFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 库存 Feign Client FallbackFactory
 */
@Slf4j
@Component
public class SysAdminFeignClientFallbackFactory implements FallbackFactory<SysAdminFeignClient> {
    @Override
    public SysAdminFeignClient create(Throwable throwable) {
        log.error("{}", throwable);

        return (type) -> new Result().error();
    }
}
