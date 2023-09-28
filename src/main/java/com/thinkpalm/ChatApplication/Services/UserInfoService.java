package com.thinkpalm.ChatApplication.Services;

import com.thinkpalm.ChatApplication.Model.UserInfoDetails;
import com.thinkpalm.ChatApplication.Model.UserModel;
import com.thinkpalm.ChatApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoService implements UserDetailsService {
    @Autowired
    private UserRepository urep;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> userInfo = urep.findByName(username);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found!"));
    }
}
