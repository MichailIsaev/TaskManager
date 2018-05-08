package com.controller;

import com.taskmanagerlogic.History;
import com.taskmanagerlogic.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/history")
@CrossOrigin
public class HistoryRecordsController {
    private Journal journal;
    @Autowired
    public void setJournal(Journal journal){
        this.journal = journal;
    }
    /**
     * GET method for represent the history.
     *
     * @return History.
     */
    @PostAuthorize("hasAuthority('SUPER_USER')")
    @RequestMapping(method = RequestMethod.GET , params = {"page" , "size"})
    public @ResponseBody
    List<History> getHistory(@RequestParam("page") int page , @RequestParam("size") int size) {
        return journal.getHistory(page , size);
    }
}
