package com.apple.slack.status.auth.manager;

import com.apple.slack.status.database.manager.DatabaseManager;
import com.apple.slack.status.properties.SlackAppProperties;
import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthManager {

    private final Logger logger = LoggerFactory.getLogger(AuthManager.class);

    private final SlackAppProperties slackAppProperties;

    private final DatabaseManager databaseManager;

    private final App slackApp;

    @Autowired
    public AuthManager(final SlackAppProperties slackAppProperties, final DatabaseManager databaseManager, final App app) {
        this.slackAppProperties = slackAppProperties;
        this.databaseManager = databaseManager;
        slackApp = app;
    }

    public ResponseEntity auth(final String code, final String state) {
        try {
            final OAuthV2AccessResponse oAuthV2AccessResponse = slackApp.client().oauthV2Access(request -> request
                    .clientId(slackAppProperties.getClientId())
                    .clientSecret(slackAppProperties.getClientSecret())
                    .code(code)
            );
            if (oAuthV2AccessResponse.isOk()) {
                final OAuthV2AccessResponse.AuthedUser authedUser = oAuthV2AccessResponse.getAuthedUser();
                final String username = authedUser.getId();
                final String accessToken = authedUser.getAccessToken();
                final String scopes = authedUser.getScope();
                databaseManager.saveSlackUser(state, username, accessToken, scopes);
                return ResponseEntity.ok().build();
            } else {
                final String errorMessage = oAuthV2AccessResponse.getError();
                logger.info(errorMessage);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorMessage);
            }
        } catch (IOException | SlackApiException exception) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exception.getMessage());
        }
    }

    public ResponseEntity check(final String dsId) {
        if (databaseManager.slackUserExists(dsId)) {
            return ResponseEntity.ok().build();
        }
        else {
            String slackAuthURI = slackAppProperties.getAuthURI();
            slackAuthURI = slackAuthURI + "&state=" + dsId;
            return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).body(slackAuthURI);
        }
    }
}
