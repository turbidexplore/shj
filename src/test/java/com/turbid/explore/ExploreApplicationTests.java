package com.turbid.explore;

import com.turbid.explore.service.NeedsRelationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExploreApplicationTests {

    @Autowired
    private NeedsRelationService needsRelationService;

    @Test
    public void contextLoads() {
        needsRelationService.updateSEE("b7761fbbd199435f89dba35ec81a1dee");
    }

}
