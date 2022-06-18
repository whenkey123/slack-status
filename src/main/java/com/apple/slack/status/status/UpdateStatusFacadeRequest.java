package com.apple.slack.status.status;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateStatusFacadeRequest {

    @NotEmpty
    private String dsId;
    @NotEmpty
    private String text;
    @NotEmpty
    private String emoji;
    @NotNull
    private Integer expirationMin;
    private boolean dnd;

    public String getDsId() {
        return dsId;
    }

    public void setDsId(final String dsId) {
        this.dsId = dsId;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(final String emoji) {
        this.emoji = emoji;
    }

    public Integer getExpirationMin() {
        return expirationMin;
    }

    public void setExpirationMin(final Integer expirationMin) {
        this.expirationMin = expirationMin;
    }

    public boolean isDnd() {
        return dnd;
    }

    public void setDnd(final boolean dnd) {
        this.dnd = dnd;
    }
}
