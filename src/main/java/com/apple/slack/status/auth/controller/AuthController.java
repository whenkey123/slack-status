package com.apple.slack.status.auth.controller;

import com.apple.slack.status.auth.manager.AuthManager;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthManager authManager;

    @Autowired
    public AuthController(final AuthManager authManager) {
        this.authManager = authManager;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity authAndCheck(
            @RequestParam(value = "dsId", required = false) String dsId,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) final String state) {
        if (!StringUtil.isNullOrEmpty(dsId)) {
            return authManager.check(dsId);
        }
        else if (!StringUtil.isNullOrEmpty(code) && !StringUtil.isNullOrEmpty(state)) {
            return authManager.auth(code, state);
        }
        return ResponseEntity.badRequest().build();
    }
}
