package com.sgg.demo.event;

public abstract class AbstractEvent<T> {

   private final T source;
    protected AbstractEvent(T source) {
        this.source = source;
    }
    public T getSource() {
        return source;
    }
}
