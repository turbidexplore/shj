package com.turbid.explore.task;

import com.turbid.explore.service.ProjectNeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TaskService {

    @Autowired
    private ProjectNeedsService projectNeedsService;

    @Async
    @Scheduled(cron = "0 0 13 * * ?")
    public void testTasks() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date());
        projectNeedsService.colseOverNeeds(dateStr);
    }
}
