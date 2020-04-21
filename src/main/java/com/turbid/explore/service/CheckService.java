package com.turbid.explore.service;

import org.springframework.stereotype.Service;

@Service
public interface CheckService {

     int findMessagesByMebileAndAuthode(String mebile, String authcode);

    int findMessagesByMailAndAuthode(String mail, String authcode);

    String findCodeByPhone(String username);
}
