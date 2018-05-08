package com.taskmanagerlogic;

import com.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This class is the history of completed tasks.
 *
 * @version 1.0
 */
@Component
@Document(collection = "history")
public class History {

    private Date eventDate;

    public Date getEventDate() {
        return eventDate;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public String getEvent() {
        return event;
    }

    private Task currentTask;

    private String event;

    public History() {

    }

    public History(Task task, String operation, Date eventDate) {
        this.event = operation;
        this.eventDate = eventDate;
        this.currentTask = task;
    }

    @Autowired
    private HistoryRepository historyRepository;


    @Override
    public String toString() {
        String result;
        result = "Count of deleted tasks : " + countOfCleanedTask();
        if (event != null)
            result += "\nLast call : " + event;
        else
            result += "\nLast call : unknown";

        return result;
    }


    public void addCleanedTask(Task task) {
        if (countOfCleanedTask() == 20) {
            historyRepository.deleteAll();
        }
        this.eventDate = new Date();
        this.event = "clean";
        this.currentTask = task;
        historyRepository.insert(new History(this.currentTask, this.event, this.eventDate));

    }


    private long countOfCleanedTask() {
        return historyRepository.count();
    }

}
