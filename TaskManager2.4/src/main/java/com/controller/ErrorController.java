package com.controller;

import com.taskmanagerlogic.ErrorRecord;
import com.taskmanagerlogic.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/error_records")
@CrossOrigin
public class ErrorController {
    private Journal journal;

    @Autowired
    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    /**
     * GET method for represent the error record.
     *
     * @return Error record.
     */
    @PostAuthorize("hasAuthority('SUPER_USER')")
    @RequestMapping(method = RequestMethod.GET, params = {"page", "size"})
    public @ResponseBody
    List<ErrorRecord> getErrorRecords(@RequestParam("page") int page, @RequestParam("size") int size) {
        return journal.getErrorRecords(page, size);
    }
}
