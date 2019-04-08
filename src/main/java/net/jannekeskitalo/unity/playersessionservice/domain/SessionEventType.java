package net.jannekeskitalo.unity.playersessionservice.domain;

public enum SessionEventType {
    START("start"),
    END("end");

    private String code;
    SessionEventType(String code) {
        this.code = code;
    }
}
