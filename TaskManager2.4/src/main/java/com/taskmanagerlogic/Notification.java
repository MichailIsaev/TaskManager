package com.taskmanagerlogic;


import com.mailservice.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@ComponentScan(basePackages = {"com"})

public class Notification implements Runnable {

    private EmailService emailService;
    private Journal journal;

    public void setPresentlyTask(Task presentlyTask) {
        this.presentlyTask = presentlyTask;
    }

    public Task getPresentlyTask() {
        return presentlyTask;
    }

    private Task presentlyTask;

    @Autowired
    public void setEmailService(EmailService emailService, Journal journal) {
        this.emailService = emailService;
        this.journal = journal;
    }


    @Override
    public void run() {
        emailService.sendSimpleMessage(presentlyTask.getEmail(), "SOON", presentlyTask.getDescribe());
    }
}
