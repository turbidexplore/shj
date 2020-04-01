package com.turbid.explore.service;

import com.turbid.explore.pojo.Needs;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NeedsService {

    public void save(Needs needs);

    List<Needs> listByPage(Integer page);
}
