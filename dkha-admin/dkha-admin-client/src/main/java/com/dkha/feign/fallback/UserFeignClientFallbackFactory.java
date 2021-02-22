package com.dkha.feign.fallback;

import feign.hystrix.FallbackFactory;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.utils.Result;
import com.dkha.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户接口 FallbackFactory
 */
@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
    @Override
    public UserFeignClient create(Throwable throwable) {
        log.error("{}", throwable);

        return new UserFeignClient() {
            @Override
            public Result<UserDetail> getById(Long id) {
                return new Result<>();
            }

            @Override
            public Result<List<Long>> getRoleIdList(Long userId) {
                return new Result<>();
            }
        };
    }
}
