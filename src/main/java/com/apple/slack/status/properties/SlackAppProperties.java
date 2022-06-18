package com.apple.slack.status.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("slack.app")
public class SlackAppProperties {

    private String clientId;
    private String botToken;
    private String clientSecret;
    private String signingSecret;
    private String authURI;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(final String botToken) {
        this.botToken = botToken;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getSigningSecret() {
        return signingSecret;
    }

    public void setSigningSecret(final String signingSecret) {
        this.signingSecret = signingSecret;
    }

    public String getAuthURI() {
        return authURI;
    }

    public void setAuthURI(final String authURI) {
        this.authURI = authURI;
    }
}
