package com.chesshouzs.server.config.queue;

public class KafkaMessageWrapper {
    private Object value;

    public KafkaMessageWrapper(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
