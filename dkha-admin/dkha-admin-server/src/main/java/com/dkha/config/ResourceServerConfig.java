

package com.dkha.config;

import com.dkha.commons.security.constant.ResourceConstant;
import com.dkha.commons.security.exception.SecurityAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 资源服务器配置
 */
@Configuration
@AllArgsConstructor
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore);

        //token不存在或错误时，异常处理
        resources.authenticationEntryPoint(new SecurityAuthenticationEntryPoint());
   }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(ResourceConstant.IGNORING_URLS).permitAll()
            .antMatchers("/user/getByUsername").permitAll()
            .antMatchers(HttpMethod.GET, "/params/code/{paramCode}").permitAll()
            .anyRequest().authenticated()
        ;
    }

}
