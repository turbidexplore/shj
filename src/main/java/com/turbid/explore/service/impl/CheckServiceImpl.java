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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(new Date().getTime()-1000*60*10));
        return messageRepository.findCountByMailAndAuthode(mebile,authcode,calendar);
    }

    public int findMessagesByMailAndAuthode(String mail,String authcode){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(new Date().getTime()-1000*60*10));
        return messageRepository.findMessagesByMailAndAuthode(mail,authcode,calendar);
    }

    @Override
    public String findCodeByPhone(String username) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(new Date().getTime()-1000*60*10));
        return messageRepository.findCodeByPhone(username,calendar).get(0).getAuthcode();
    }

}
