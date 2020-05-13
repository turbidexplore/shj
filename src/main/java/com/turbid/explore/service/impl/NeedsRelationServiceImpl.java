package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.NeedsRelation;
import com.turbid.explore.repository.NeedsRelationRepositroy;
import com.turbid.explore.service.NeedsRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NeedsRelationServiceImpl implements NeedsRelationService {

    @Autowired
    private NeedsRelationRepositroy needsRelationRepositroy;

    @Override
    public NeedsRelation save(NeedsRelation needsRelation) {
        return needsRelationRepositroy.saveAndFlush(needsRelation);
    }

    @Override
    @Transactional
    public void updateSEE(String orderno) {
        needsRelationRepositroy.updateSEE( orderno);
    }
}
