package cn.nukkit.utils;

public class CommandException extends RuntimeException {

    public CommandException(String message) {
        super(message);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
