package com.apple.slack.status.dnd.manager;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.dnd.DndSetSnoozeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DNDManager {

    private final Logger logger = LoggerFactory.getLogger(DNDManager.class);

    private final App slackApp;

    @Autowired
    public DNDManager(final App slackApp) {
        this.slackApp = slackApp;
    }

    public boolean pauseNotifications(final String xoxpToken, final Integer minutes) {
        try {
            final DndSetSnoozeResponse dndSetSnoozeResponse = slackApp.client().dndSetSnooze(request -> request
                    .token(xoxpToken)
                    .numMinutes(minutes)
            );
            return dndSetSnoozeResponse.isOk();
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
        return false;
    }
}
