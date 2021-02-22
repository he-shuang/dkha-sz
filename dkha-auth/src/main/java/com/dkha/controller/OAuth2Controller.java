package com.dkha.controller;

import com.dkha.commons.log.SysLogLogin;
import com.dkha.commons.log.enums.LogTypeEnum;
import com.dkha.commons.log.enums.LoginOperationEnum;
import com.dkha.commons.log.producer.LogProducer;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.redis.RedisKeys;
import com.dkha.commons.tools.redis.RedisUtils;
import com.dkha.commons.tools.utils.IpUtils;
import com.dkha.commons.tools.utils.Result;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 认证管理
 */
@RestController
@AllArgsConstructor
@Api(tags="认证管理")
public class OAuth2Controller {
    private TokenStore tokenStore;
    private LogProducer logProducer;
    private RedisUtils redisUtils;

    /**
     * 退出
     */
    @PostMapping("/oauth/logout")
    public Result logout(HttpServletRequest request) {
        String access_token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(access_token);
        if(oAuth2AccessToken != null){
            //用户信息
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
            UserDetail user = (UserDetail) oAuth2Authentication.getPrincipal();

            tokenStore.removeAccessToken(oAuth2AccessToken);
            OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
            tokenStore.removeRefreshToken(oAuth2RefreshToken);
            tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);

            //清空菜单导航、权限标识
            redisUtils.deleteByPattern(RedisKeys.getUserMenuNavKey(user.getId()));
            redisUtils.delete(RedisKeys.getUserPermissionsKey(user.getId()));

            //退出日志
            SysLogLogin log = new SysLogLogin();
            log.setType(LogTypeEnum.LOGIN.value());
            log.setOperation(LoginOperationEnum.LOGOUT.value());
            log.setIp(IpUtils.getIpAddr(request));
            log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
            log.setIp(IpUtils.getIpAddr(request));
            log.setCreator(user.getId());
            log.setCreatorName(user.getUsername());
            log.setCreateDate(new Date());
            logProducer.saveLog(log);
        }
        return new Result();
    }

}
