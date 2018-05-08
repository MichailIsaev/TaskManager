package com.taskmanagerlogic;

import com.repositories.ErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * This is a record of a task error.
 *
 * @version 1.1
 */

@ComponentScan(basePackages = {"com.repositories", "com.configuration"})
@Document(collection = "error_record")
public class ErrorRecord {

    public UUID getId() {
        return id;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public Date getDate() {
        return date;
    }

    private UUID id;
    private String exceptionClass;
    private String message;
    private String stackTrace;
    private Date date;


    public ErrorRecord(UUID id, String exceptionClass, String message, String stackTrace, Date date) {
        this.id = id;
        this.exceptionClass = exceptionClass;
        this.message = message;
        this.date = date;
        this.stackTrace = stackTrace;
    }


}