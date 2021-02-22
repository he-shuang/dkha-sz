

package com.dkha.config;

import com.dkha.constant.OauthConstant;
import com.dkha.token.RenTokenEnhancer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

/**
 * 认证服务器配置
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private AuthenticationManager authenticationManager;
    private DataSource dataSource;
    private UserDetailsService userDetailsService;
    private TokenStore tokenStore;
    private WebResponseExceptionTranslator<OAuth2Exception> renWebResponseExceptionTranslator;

    /**
     * 配置客户端信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(OauthConstant.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(OauthConstant.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 授权码管理
     */
    @Bean
    public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE);
        //密码模式
        endpoints.authenticationManager(authenticationManager);
        //支持刷新令牌
        endpoints.userDetailsService(userDetailsService);
        //令牌管理
        endpoints.tokenStore(tokenStore);
        //令牌增强
        endpoints.tokenEnhancer(tokenEnhancer());
        //登录或者鉴权失败时的返回信息
        endpoints.exceptionTranslator(renWebResponseExceptionTranslator);
        //授权码管理，授权码存放在oauth_code表中
        endpoints.authorizationCodeServices(jdbcAuthorizationCodeServices());
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new RenTokenEnhancer();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
            .allowFormAuthenticationForClients()
            .tokenKeyAccess("permitAll()")   //匿名可访问/oauth/token_key
            .checkTokenAccess("isAuthenticated()") //认证后可访问/oauth/check_token
        ;
    }
}
