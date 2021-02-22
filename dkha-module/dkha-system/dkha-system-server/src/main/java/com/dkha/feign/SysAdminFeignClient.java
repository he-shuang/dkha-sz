

package com.dkha.feign;

import com.dkha.commons.tools.utils.Result;
import com.dkha.feign.fallback.SysAdminFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * admin Feign Client
 */
@FeignClient(name = "dkha-admin-server", fallbackFactory = SysAdminFeignClientFallbackFactory.class)
public interface SysAdminFeignClient {


	@GetMapping("sys/dict/data/getByType")
	Result getByType(@RequestParam("type") String type);

}
