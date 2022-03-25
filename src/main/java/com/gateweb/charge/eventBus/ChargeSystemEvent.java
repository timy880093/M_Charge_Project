package com.gateweb.charge.eventBus;

import java.util.UUID;

public class ChargeSystemEvent {
    final String eventId = UUID.randomUUID().toString();
    EventSource eventSource;
    EventAction eventAction;
    Long sourceId;
    String previousEventId;
    Long callerId;

    public ChargeSystemEvent(
            EventSource eventSource,
            EventAction eventAction, Long sourceId, Long callerId) {
        this.eventSource = eventSource;
        this.eventAction = eventAction;
        this.sourceId = sourceId;
        this.callerId = callerId;
    }

    public ChargeSystemEvent(
            EventSource eventSource,
            EventAction eventAction, Long sourceId, String previousEventId, Long callerId) {
        this.eventSource = eventSource;
        this.eventAction = eventAction;
        this.sourceId = sourceId;
        this.previousEventId = previousEventId;
        this.callerId = callerId;
    }

    public EventSource getEventSource() {
        return eventSource;
    }

    public void setEventSource(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    public EventAction getEventAction() {
        return eventAction;
    }

    public void setEventAction(EventAction eventAction) {
        this.eventAction = eventAction;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getPreviousEventId() {
        return previousEventId;
    }

    public void setPreviousEventId(String previousEventId) {
        this.previousEventId = previousEventId;
    }

    public String getEventId() {
        return eventId;
    }

    public Long getCallerId() {
        return callerId;
    }

    public void setCallerId(Long callerId) {
        this.callerId = callerId;
    }

    @Override
    public String toString() {
        return "ChargeSystemEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventSource=" + eventSource +
                ", eventAction=" + eventAction +
                ", sourceId=" + sourceId +
                ", previousEventId='" + previousEventId + '\'' +
                ", callerId=" + callerId +
                '}';
    }
}
