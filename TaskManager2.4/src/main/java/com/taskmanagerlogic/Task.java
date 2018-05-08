package com.taskmanagerlogic;

import com.InterAction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.reaction.MailSender;
import com.repositories.ErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * JavaBean Task , describes a  task
 *
 * @version 1.0
 */
@Component
@ComponentScan(basePackages = {"com"})
@Document(collection = "tasks")
public class Task implements Serializable, Runnable {

    private static final long serialVersionUID = 12321312324L;
    @Id
    private UUID id;
    private String name;
    private String describe;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long[] getNotificationInterval() {
        return notificationInterval;
    }

    public void setNotificationInterval(long notificationInterval[]) {
        this.notificationInterval = notificationInterval;
    }

    private String email;
    private long[] notificationInterval;


    private MailSender mailSender;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    private Date targetTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    private Date completedTime;

    private List<String> contacts;

    private ErrorRepository errorRecordRepository;

    private Journal journal;

    public UUID getId() {
        return id;
    }

    public Action getStatus() {
        return status;
    }

    public void setStatus(Action status) {
        this.status = status;
    }

    private Action status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(Date dateTime) {
        this.targetTime = dateTime;
    }

    public Date getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Date completedTime) {
        this.completedTime = completedTime;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public List<String> getContacts() {
        return contacts;
    }


    public Task(String name, String describe, Date dateTime, List<String> contacts, long[] notificationInterval) {
        this.status = Action.SCHEDULED;
        this.name = name;
        this.describe = describe;
        this.targetTime = dateTime;
        this.contacts = contacts;
        id = UUID.randomUUID();
        this.notificationInterval = notificationInterval;
    }

    public Task() {
        id = UUID.randomUUID();
    }

    @Override
    public String toString() {

        String taskString = "";
        taskString = "ID : " + id + "\nStatus : " + status.name()
                + "\nName : " + name + "\nDescribe : " + describe + "\nTask time : " + targetTime;

        if (!contacts.isEmpty()) {
            taskString += "\nContacts : ";
            for (String contact : contacts) {
                taskString += contact + " , ";
            }

            taskString = taskString.substring(0, taskString.lastIndexOf(" ,"));
        }
        return taskString + '\n';
    }


    @Override
    public void run() {
        errorRecordRepository = (ErrorRepository) InterAction.applicationContext.getBean("error_record");
        journal = (Journal) InterAction.applicationContext.getBean("journal");
        mailSender = (MailSender) InterAction.applicationContext.getBean("mailSender");
        System.out.println(mailSender);
        journal.updateStatus(this.id, Action.RUNNING);
        System.out.println("You`ve got a task!!!!");
        this.setCompletedTime(new Date());
        System.out.println("Name : " + this.getName() + "\nDescribe : " + this.getDescribe() + "\nComplete at " + this.getCompletedTime());
        try {
            mailSender.perform(this);
        } catch (Exception e) {
            System.out.println("ERROR while perform");
            ErrorRecord errorRecord = new ErrorRecord(
                    this.id,
                    e.getClass().getName(),
                    "Some problem while executing",
                    e.getMessage(),
                    new Date()
            );
            errorRecordRepository.insert(errorRecord);
        }
        journal.updateStatus(this.id, Action.COMPLETED);
    }

}
