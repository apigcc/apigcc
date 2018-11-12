package com.apigcc.common.loging;

public interface Logger {

    void setLevel(Level level);

    void setName(String name);

    boolean isDebug();

    void out(Level level, String message, Object... args);

    default void error(String message, Throwable throwable) {
        out(Level.ERROR, "{} {}", throwable, message);
        throwable.printStackTrace();
    }

    default void warning(String message, Object... args) {
        out(Level.WARNING, message, args);
    }

    default void info(String message, Object... args) {
        out(Level.INFO, message, args);
    }

    default void debug(String message, Object... args) {
        out(Level.DEBUG, message, args);
    }

}