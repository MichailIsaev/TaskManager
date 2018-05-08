package com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.taskmanagerlogic.*;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Controller for rest api.
 * There are all requests which you can use from web.
 * TODO Check users' authorities. Now there is a simple checking using switch - case only in getByName and getAllTasks. Only for test.
 *
 * @version 1.1
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin
@PreAuthorize("hasAnyAuthority('SUPER_USER') or hasAuthority('OTHER')")
public class TaskController {

    private TaskResponse response;

    private Journal journal;

    private History history;

    private Notification notification;

    private String email;

    private String authority;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @Autowired
    public void setResponse(TaskResponse response) {
        this.response = response;
    }

    @Autowired
    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    @Autowired
    public void setHistory(History history) {
        this.history = history;
    }

    @Autowired
    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    /**
     * GET method for represent by name and page.
     * Now there is a simple checking for 2 roles.
     *
     * @param name task name
     * @param page current page
     * @param size size of the page
     * @return List of tasks.
     */
    @RequestMapping(method = RequestMethod.GET, params = {"name", "page", "size"})
    public @ResponseBody
    TaskResponse getByName(@AuthenticationPrincipal Authentication authentication,
                           @RequestParam("name") String name,
                           @RequestParam("page") int page,
                           @RequestParam("size") int size) {

        List<Task> tasks;

        if (authentication.getAuthorities().toString().equals("[SUPER_USER]")) {
            tasks = journal.findByName(name, page, size);
            response.setSize(journal.countByName(name));
        } else {
            String email = authentication.getPrincipal().toString();
            tasks = journal.findByNameAndEmail(page, size, name, email);
            response.setSize(journal.countByNameAndEmail(name, email));
        }

        response.setTasks(tasks);

        return response;
    }


    @RequestMapping(method = RequestMethod.GET, params = {"name", "from", "to", "page", "size"})
    public @ResponseBody
    TaskResponse getByName(@AuthenticationPrincipal Authentication authentication,
                           @RequestParam("name") String name,
                           @RequestParam("from") String from,
                           @RequestParam("to") String to,
                           @RequestParam("page") int page,
                           @RequestParam("size") int size) {


        Date fromDate = Date.from(Instant.ofEpochMilli(Long.valueOf(from)));
        Date toDate = Date.from(Instant.ofEpochMilli(Long.valueOf(to)));

        List<Task> tasks;

        if (authentication.getAuthorities().toString().equals("[SUPER_USER]")) {
            tasks = journal.findByPeriodOfTimeAndName(fromDate, toDate, page, size, name);
            response.setSize(journal.countByPeriodAndName(fromDate, toDate, name));
        } else {
            String email = authentication.getPrincipal().toString();
            tasks = journal.findByPeriodOfTimeAndNameAndEmail(fromDate, toDate, page, size, name, email);
            response.setSize(journal.countByPeriodAndNameAndEmail(fromDate, toDate, name, email));
        }

        response.setTasks(tasks);

        return response;
    }


    /**
     * GET method for represent all tasks.
     * Now there is a simple checking for 2 roles.
     *
     * @param page current page
     * @param size page size
     * @return All tasks
     */
    @RequestMapping(method = RequestMethod.GET, params = {"page", "size"})
    public @ResponseBody
    TaskResponse getAllTasks(@AuthenticationPrincipal Authentication authentication, @RequestParam("page") int page, @RequestParam("size") int size) {

        List<Task> tasks;

        if (authentication.getAuthorities().toString().equals("[SUPER_USER]")) {
            tasks = journal.getTasks(page, size);
            response.setSize(journal.length());
        } else {
            String email = authentication.getPrincipal().toString();
            tasks = journal.findAllByEmail(page, size, email);
            response.setSize(journal.countByEmail(email));
        }

        response.setTasks(tasks);


        return response;
    }

    /**
     * GET method for represent by period of time
     *
     * @param from - left border of time
     * @param to   - right border of time
     * @param page - content page
     * @param size - size of page content
     */
    @RequestMapping(method = RequestMethod.GET, params = {"from", "to", "page", "size"})
    public @ResponseBody
    TaskResponse getByPeriodOfTime(@AuthenticationPrincipal Authentication authentication,
                                   @RequestParam("from") String from,
                                   @RequestParam("to") String to,
                                   @RequestParam("page") int page,
                                   @RequestParam("size") int size) {

        List<Task> tasks = null;

        Date fromDate = Date.from(Instant.ofEpochMilli(Long.valueOf(from)));
        Date toDate = Date.from(Instant.ofEpochMilli(Long.valueOf(to)));

        if (authentication.getAuthorities().toString().equals("[SUPER_USER]")) {
            tasks = journal.findByPeriodOfTime(fromDate, toDate, page, size);
            response.setSize(journal.countByDate(fromDate, toDate));
        } else {
            tasks = journal.findByPeriodOfTimeAndEmail(fromDate, toDate, page, size, authentication.getPrincipal().toString());
            response.setSize(journal.countByDate(fromDate, toDate));
        }

        response.setTasks(tasks);

        return response;
    }

    /**
     * GET method for represent by id
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Task getTaskById(@PathVariable("id") String id) {
        return journal.findById(UUID.fromString(id));
    }


    /**
     * GET method that cleans the journal. Why is this GET?
     */
    @PostAuthorize("hasAuthority('SUPER_USER')")
    @RequestMapping(value = "/*", method = RequestMethod.GET)
    public @ResponseBody
    void cleanJournal() {
        journal.clean();
    }

    /**
     * DELETE method for delete by name
     *
     * @param name
     */
    @RequestMapping(method = RequestMethod.DELETE, params = "name")
    public void deleteByName(@RequestParam(value = "name") String name) {
        journal.delete(name);
    }

    /**
     * DELETE method for delete by id
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable("id") String id) {
        journal.delete(UUID.fromString(id));
    }

    /**
     * DELETE method for delete by status
     *
     * @param status
     */
    @RequestMapping(method = RequestMethod.DELETE, params = "status")
    public void deleteByStatus(@RequestParam(value = "status") String status) {
        journal.delete(Action.valueOf(status.toUpperCase()));
    }

    @RequestMapping(method = RequestMethod.DELETE, params = "email")
    public void deleteTaskWithOwner(@RequestParam(value = "email") String email) {
        journal.deleteAllByEmail(email);
    }

    /**
     * DELETE method for delete by period of time
     *
     * @param from
     * @param to
     * @throws ParseException
     */
    @RequestMapping(method = RequestMethod.DELETE, params = {"from", "to"})
    public void deleteByPeriodOfTime(@RequestParam(value = "from") String from,
                                     @RequestParam(value = "to") String to) throws ParseException {
        Date fromDate = dateFormat.parse(from);
        Date toDate = dateFormat.parse(to);
        journal.delete(fromDate, toDate);
    }


    /**
     * POST method for create the Task
     *
     * @return Task
     * @throws ParseException
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public @ResponseBody
    Task addTasks(@RequestBody Task task) throws ParseException {
        System.out.println(task);

        journal.add(task);
        journal.schedule(task);
        notification.setPresentlyTask(task);
        journal.scheduleNotification(notification);
        return task;
    }

    /**
     * PUT method for putting contacts.
     *
     * @param id       Task id
     * @param contacts contacts
     * @return Task
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    Task putContacts(@PathVariable("id") String id, @RequestBody String contacts) {
        String[] communications = contacts.split(",");
        Task task = journal.findById(UUID.fromString(id));
        journal.setContacts(task, Arrays.asList(communications));
        return task;
    }

    /**
     * It works when bad request.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Check arguments!")
    @ExceptionHandler({IllegalArgumentException.class, ParseException.class,
            NumberFormatException.class, NullPointerException.class})
    public void handleExceptions() {

    }
}
