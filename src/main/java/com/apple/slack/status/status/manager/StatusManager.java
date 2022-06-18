package com.apple.slack.status.status.manager;

import com.apple.slack.status.auth.manager.AuthManager;
import com.apple.slack.status.database.manager.DatabaseManager;
import com.apple.slack.status.dnd.manager.DNDManager;
import com.apple.slack.status.status.UpdateStatusFacadeRequest;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.profile.UsersProfileSetResponse;
import com.slack.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
public class StatusManager {

    private final Logger logger = LoggerFactory.getLogger(StatusManager.class);

    private final App slackApp;

    private final AuthManager authManager;

    private final DatabaseManager databaseManager;

    private final DNDManager dndManager;

    @Autowired
    public StatusManager(final App app, final AuthManager authManager, final DatabaseManager databaseManager, final DNDManager dndManager) {
        this.slackApp = app;
        this.authManager = authManager;
        this.databaseManager = databaseManager;
        this.dndManager = dndManager;
    }

    public ResponseEntity setStatus(final UpdateStatusFacadeRequest updateStatusFacadeRequest) {
        final String dsId = updateStatusFacadeRequest.getDsId();
        final ResponseEntity check = authManager.check(dsId);
        if (!check.getStatusCode().is2xxSuccessful()) {
            return check;
        }
        final String xoxpToken = databaseManager.getXoxpToken(dsId);
        final String text = updateStatusFacadeRequest.getText();
        final String emoji = updateStatusFacadeRequest.getEmoji();
        final Integer expirationMin = updateStatusFacadeRequest.getExpirationMin();
        final long expirationSec = Instant.now().getEpochSecond() + (expirationMin * 60);
        final User.Profile profile = new User.Profile();
        profile.setStatusText(text);
        profile.setStatusEmoji(emoji);
        profile.setStatusExpiration(expirationSec);
        try {
            UsersProfileSetResponse usersProfileSetResponse = slackApp.client().usersProfileSet(request -> request
                    .token(xoxpToken)
                    .profile(profile));
            if (usersProfileSetResponse.isOk()) {
                if (updateStatusFacadeRequest.isDnd()) {
                    dndManager.pauseNotifications(xoxpToken, expirationMin);
                }
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(usersProfileSetResponse.getError());
            }
        } catch (IOException | SlackApiException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exception.getMessage());
        }
    }
}
