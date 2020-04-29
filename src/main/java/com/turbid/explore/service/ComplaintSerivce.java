package com.turbid.explore.service;

import com.turbid.explore.pojo.Complaint;
import org.springframework.stereotype.Service;

@Service
public interface ComplaintSerivce {
    Complaint save(Complaint complaint);
}
