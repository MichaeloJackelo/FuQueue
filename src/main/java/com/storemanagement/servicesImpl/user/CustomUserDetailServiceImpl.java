package com.storemanagement.servicesImpl.user;

import com.storemanagement.dao.user.UserRepository;
import com.storemanagement.daoImpl.user.CustomUserDetails;
import com.storemanagement.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> optionalUser=userRepository.findByName(userName);
        optionalUser
                .orElseThrow(()->new UsernameNotFoundException("Username not found!"));

        return optionalUser
                .map(CustomUserDetails::new).get();
    }
}
