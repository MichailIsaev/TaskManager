package com.reaction;

import com.InterAction;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mailservice.EmailService;
import com.taskmanagerlogic.Task;
import org.springframework.stereotype.Component;

//@JsonDeserialize(as = MailSender.class)
@Component
public class MailSender{

    private static final long serialVersionUID = 12321312324L;

    private EmailService emailService;

    public MailSender() {
    }

    private String msg;


    public void perform(Task task) {
        emailService = (EmailService) InterAction.applicationContext.getBean("emailService");
        for(String contact : task.getContacts()){
            emailService.sendSimpleMessage(contact, task.getDescribe() , "executed");
        }
    }


    public Object getValue() {
        return msg;
    }


    public void setValue(Object data) {
        msg = (String) data;
    }

}
