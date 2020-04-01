package com.turbid.explore.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    @Scheduled(cron = "0 00 01 ? * *")
    public void testTasks() {

    }
}
