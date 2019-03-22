package cn.nukkit.utils;

import java.util.function.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public enum LogLevel implements Comparable<LogLevel> {
    NONE((l, m) -> {}, (l, m, t) -> {}),
    FATAL(MainLogger::fatal, MainLogger::fatal),
    ERROR(MainLogger::error, MainLogger::error),
    WARN(MainLogger::warn, MainLogger::warn),
    INFO(MainLogger::info, MainLogger::info),
    DEBUG(MainLogger::debug, MainLogger::debug),
    TRACE(MainLogger::trace, MainLogger::trace);

    public static final LogLevel DEFAULT_LEVEL = INFO;

    private final BiConsumer<MainLogger, String> logTo;
    private final TriConsumer<MainLogger, String, Throwable> logThrowableTo;

    LogLevel(BiConsumer<MainLogger, String> logTo, TriConsumer<MainLogger, String, Throwable> logThrowableTo) {
        this.logTo = logTo;
        this.logThrowableTo = logThrowableTo;
    }

    public void log(MainLogger logger, String message) {
        logTo.accept(logger, message);
    }

    public void log(MainLogger logger, String message, Throwable throwable) {
        logThrowableTo.accept(logger, message, throwable);
    }

    public int getLevel() {
        return ordinal();
    }
}
