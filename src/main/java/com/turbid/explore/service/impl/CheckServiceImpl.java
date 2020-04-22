package com.turbid.explore.service.impl;

import com.turbid.explore.repository.MessageRepository;
import com.turbid.explore.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private MessageRepository messageRepository;

    public int findMessagesByMebileAndAuthode(String mebile,String authcode){
        return messageRepository.findCountByMailAndAuthode(mebile,authcode,new Date(new Date().getTime()-1000*60*10));
    }

    public int findMessagesByMailAndAuthode(String mail,String authcode){
        return messageRepository.findMessagesByMailAndAuthode(mail,authcode,new Date(new Date().getTime()-1000*60*10));
    }

    @Override
    public String findCodeByPhone(String username) {
        return messageRepository.findCodeByPhone(username,new Date(new Date().getTime()-1000*60*10)).get(0).getAuthcode();
    }

}
