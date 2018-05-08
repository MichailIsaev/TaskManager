package com.taskmanagerlogic;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is for response. This encapsulates tasks and size.
 *
 * @version 1.0
 */
@Component
public class TaskResponse {

    private long size;
    private List<Task> tasks;

    public void setSize(long size) {
        this.size = size;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public long getSize() {
        return size;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
