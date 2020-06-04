package com.turbid.explore.configuration;

import com.turbid.explore.service.CheckService;
import com.turbid.explore.service.impl.UserSecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserSecurityServiceImpl userSecurityService;

    @Autowired
    private CheckService checkService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest =  requestAttributes.getRequest();
        String login_type= servletRequest.getParameter("login_type").trim();
        String password="";
        if(0<userSecurityService.findByPhoneCount(username)){
            if(login_type=="password"||login_type.equals("password")){
                password=userSecurityService.findByPhone(username).getPassword();
            }else if(login_type=="sms"||login_type.equals("sms")){
                password=checkService.findCodeByPhone(username);
            }else if(login_type=="open"||login_type.equals("open")){
                password="123456";
            }
        }
        return new org.springframework.security.core.userdetails.User(username,passwordEncoder.encode(password),true,true,true,true, AuthorityUtils.NO_AUTHORITIES);
    }
}
