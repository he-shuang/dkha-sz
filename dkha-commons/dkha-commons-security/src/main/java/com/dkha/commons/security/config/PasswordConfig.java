

package com.dkha.commons.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 加密方式
 */
@Configuration
@AllArgsConstructor
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        PasswordConfig passwordConfig = new PasswordConfig();
        String dkha = passwordConfig.passwordEncoder().encode("dkha");
        System.out.println(dkha);
    }
}
