package cn.nukkit.utils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface Logger {

    void fatal(String message);

    void fatal(String message, Throwable t);

    void error(String message);

    void error(String message, Throwable t);

    void warn(String message);

    void warn(String message, Throwable t);

    void info(String message);

    void info(String message, Throwable t);

    void debug(String message);

    void debug(String message, Throwable t);

    void trace(String message);

    void trace(String message, Throwable t);

    void log(LogLevel level, String message);

    void log(LogLevel level, String message, Throwable t);

    void throwing(Throwable t);
}
