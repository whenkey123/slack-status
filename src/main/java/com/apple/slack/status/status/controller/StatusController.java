package com.apple.slack.status.status.controller;

import com.apple.slack.status.status.UpdateStatusFacadeRequest;
import com.apple.slack.status.status.manager.StatusManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/status")
public class StatusController {

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

    private final StatusManager statusManager;

    @Autowired
    public StatusController(final StatusManager statusManager) {
        this.statusManager = statusManager;
    }

    @PostMapping(value = "/set")
    public ResponseEntity setStatus(@Valid @RequestBody final UpdateStatusFacadeRequest updateStatusFacadeRequest) {
        return statusManager.setStatus(updateStatusFacadeRequest);
    }
}
