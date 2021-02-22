package com.dkha.commons.security.feign.fallback;

import feign.hystrix.FallbackFactory;
import com.dkha.commons.security.feign.AccountFeignClient;
import com.dkha.commons.tools.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 账号接口 FallbackFactory
 */
@Slf4j
@Component
public class AccountFeignClientFallbackFactory implements FallbackFactory<AccountFeignClient> {
    @Override
    public AccountFeignClient create(Throwable throwable) {
        log.error("{}", throwable);

        return username -> new Result<>();
    }
}
