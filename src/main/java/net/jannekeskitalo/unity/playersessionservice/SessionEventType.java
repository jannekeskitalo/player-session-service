package net.jannekeskitalo.unity.playersessionservice;

public enum SessionEventType {
    SESSION_START(1, "start"),
    SESSION_END(2, "end");

    private final int eventTypeId;
    private final String eventTypeCode;

    private SessionEventType(int eventTypeId, String eventTypeCode) {
        this.eventTypeId = eventTypeId;
        this.eventTypeCode = eventTypeCode;
    }
}
