package com.turbid.explore.service.message;

import org.springframework.stereotype.Service;

@Service
public interface MailService {


    void send(String mail);

    void sendHtmlMail(String mail);

}
