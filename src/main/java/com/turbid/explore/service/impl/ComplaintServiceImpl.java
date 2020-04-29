package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Complaint;
import com.turbid.explore.repository.ComplaintRepository;
import com.turbid.explore.service.ComplaintSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintServiceImpl implements ComplaintSerivce {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public Complaint save(Complaint complaint) {
        return complaintRepository.saveAndFlush(complaint);
    }
}
