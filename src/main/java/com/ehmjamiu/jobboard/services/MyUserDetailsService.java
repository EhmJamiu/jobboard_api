package com.ehmjamiu.jobboard.services;


import com.ehmjamiu.jobboard.models.User;
import com.ehmjamiu.jobboard.models.UserPrincipal;
import com.ehmjamiu.jobboard.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User email = userRepository.findUserByEmail(username).orElseThrow(()
        -> new UsernameNotFoundException("This credential you entered is not found"));
        return new UserPrincipal(email);
    }
}