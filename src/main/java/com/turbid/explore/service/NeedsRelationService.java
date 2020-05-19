package com.turbid.explore.service;

import com.turbid.explore.pojo.NeedsRelation;
import com.turbid.explore.pojo.ProjectNeeds;
import org.springframework.stereotype.Service;

@Service
public interface NeedsRelationService {
    NeedsRelation save(NeedsRelation needsRelation);

    void updateSEE(String orderno);

    ProjectNeeds getByOrder(String orderno);
}
