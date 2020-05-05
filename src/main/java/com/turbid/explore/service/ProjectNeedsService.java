package com.turbid.explore.service;

import com.turbid.explore.pojo.ProjectNeeds;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectNeedsService {

     ProjectNeeds save(ProjectNeeds projectNeeds);

    List<ProjectNeeds> listByPage(Integer page, String style, String category, String type, String search);

    ProjectNeeds getNeedsByCode(String code);

    List<ProjectNeeds> getMyNeeds(String name,  Integer page,Integer status);

    int countByStatus(String name, int status);
}
