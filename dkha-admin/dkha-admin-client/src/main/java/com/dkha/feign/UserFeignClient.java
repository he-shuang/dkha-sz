

package com.dkha.feign;

import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.constant.ServiceConstant;
import com.dkha.commons.tools.utils.Result;
import com.dkha.feign.fallback.UserFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户接口
 */
@FeignClient(name = ServiceConstant.AIRPORTPAC_ADMIN_SERVER, contextId = "UserFeignClient", fallbackFactory = UserFeignClientFallbackFactory.class)
public interface UserFeignClient {

    /**
     * 根据用户ID，获取用户信息
     */
    @GetMapping("sys/user/getById")
    Result<UserDetail> getById(@RequestParam("id") Long id);

    /**
     * 根据用户ID，获取角色Id列表
     */
    @GetMapping("sys/role/getRoleIdList")
    Result<List<Long>> getRoleIdList(@RequestParam("userId") Long userId);

}
