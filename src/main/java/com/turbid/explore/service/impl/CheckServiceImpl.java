package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Case;
import com.turbid.explore.repository.MessageRepository;
import com.turbid.explore.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Pageable pageable = new PageRequest(0,1, Sort.Direction.DESC,"create_time");
        Page<String> pages=  messageRepository.findCodeByPhone(pageable,username,new Date(new Date().getTime()-1000*60*10));
        return pages.getContent().get(0);
    }

}
