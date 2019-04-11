package cn.nukkit.utils;

import lombok.extern.log4j.Log4j2;

/**
 * author: MagicDroidX
 * Nukkit
 */
/*
We need to keep this class for backwards compatibility
 */
@Log4j2
public class MainLogger extends ThreadedLogger {

    private static final MainLogger logger = new MainLogger();

    private MainLogger() {

    }

    public static MainLogger getLogger() {
        return logger;
    }

    @Override
    public void fatal(String message) {
        log.fatal(message);
    }

    @Override
    public void fatal(String message, Throwable t) {
        log.fatal(message, t);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        log.info(message, t);
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void debug(String message, Throwable t) {
        log.debug(message, t);
    }

    @Override
    public void trace(String message) {
        log.trace(message);
    }

    @Override
    public void trace(String message, Throwable t) {
        log.trace(message, t);
    }

    @Override
    public void log(LogLevel level, String message) {
        level.log(this, message);
    }

    @Override
    public void log(LogLevel level, String message, Throwable t) {
        level.log(this, message, t);
    }

    @Override
    public void throwing(Throwable t) {
        log.throwing(t);
    }
}
