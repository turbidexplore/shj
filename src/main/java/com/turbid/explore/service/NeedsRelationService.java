package com.turbid.explore.service;

import com.turbid.explore.pojo.NeedsRelation;
import org.springframework.stereotype.Service;

@Service
public interface NeedsRelationService {
    NeedsRelation save(NeedsRelation needsRelation);

    void updateSEE(String orderno);
}
