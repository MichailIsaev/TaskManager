package com;

import com.mailservice.EmailService;
import com.mailservice.EmailServiceImpl;
import com.repositories.ErrorRepository;
import com.repositories.HistoryRepository;
import com.taskmanagerlogic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Interacts with ending users
 *
 * @version 1.0
 */
@SpringBootApplication
@Component("InterAction")
public class InterAction {

    private Journal journal;

    private Cleaner cleaner;

    @Autowired
    private HistoryRepository historyRepository;


    public static ApplicationContext applicationContext;

    private ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();


    @Autowired
    public InterAction(Cleaner cleaner, Journal journal) {
        this.cleaner = cleaner;
        this.journal = journal;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        applicationContext = SpringApplication.run(InterAction.class);
        InterAction interAction = (InterAction) (applicationContext.getBean("InterAction"));
        interAction.communicate();
    }


    /**
     * Realize interacts with user
     * Recognizes the user input com.commands
     *
     * @throws IOException
     */
    public void communicate() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String in, commandPart[];
        if (!journal.getTasks().isEmpty()) {
            journal.schedule();
        }
        executor.setMaxPoolSize(10);
        executor.initialize();
        System.out.println("Hey , i'm your task manager right now.");
        /*Task super_task = new Task();
        super_task.setStatus(Action.SCHEDULED);
        super_task.setDescribe("SUPER_USER task");
        super_task.setName("Super task");
        super_task.setEmail("mail");
        super_task.setNotificationInterval(1000000);
        super_task.setTargetTime(Date.from(Instant.now().plusMillis(10000000 * 100)));
        Task other_task = new Task();
        other_task.setStatus(Action.SCHEDULED);
        other_task.setDescribe("OTHER task");
        other_task.setName("Other task");
        other_task.setEmail("other_mail");
        other_task.setTargetTime(Date.from(Instant.now().plusMillis(10000000 * 100)));
        other_task.setNotificationInterval(1000000);
        journal.add(other_task);
        journal.add(super_task);*/
        //create task , desc , 20:42 26.02.2019 , output - 1000 , archac3@gmail.com
       /* while (true) {
            in = input.readLine().trim();
            commandPart = in.split(",");
            try {
                command = CommandResolver.createCommand(commandPart);
                executor.execute(command, 1000);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }*/


    }

}



