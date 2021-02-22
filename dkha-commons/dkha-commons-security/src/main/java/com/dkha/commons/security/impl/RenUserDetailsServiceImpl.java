package com.dkha.commons.security.impl;

import com.dkha.commons.security.enums.UserStatusEnum;
import com.dkha.commons.security.feign.AccountFeignClient;
import com.dkha.commons.security.user.UserDetail;
import com.dkha.commons.tools.exception.ErrorCode;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService
 */
@Service
public class RenUserDetailsServiceImpl implements UserDetailsService {
    @Autowired(required=false)
    private AccountFeignClient accountFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<UserDetail> result = accountFeignClient.getByUsername(username);
        UserDetail userDetail = result.getData();
        if(userDetail == null){
            throw new RenException(ErrorCode.ACCOUNT_NOT_EXIST);
        }

        //账号不可用
        if(userDetail.getStatus() == UserStatusEnum.DISABLE.value()){
            userDetail.setEnabled(false);
        }

        return userDetail;
    }
}
