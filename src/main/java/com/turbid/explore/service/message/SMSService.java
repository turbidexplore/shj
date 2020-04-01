package com.turbid.explore.service.message;

import org.springframework.stereotype.Service;

@Service
public interface SMSService {

     int sendSMS(String mobile) throws InterruptedException;

     int sendSMS(String phone, String[] params, int templateId);

     int sendSMS(String[] phoneNumbers, String[] params, int templateId);



}
