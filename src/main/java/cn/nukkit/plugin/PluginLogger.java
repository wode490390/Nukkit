package cn.nukkit.plugin;

import cn.nukkit.Server;
import cn.nukkit.utils.LogLevel;
import cn.nukkit.utils.Logger;
import lombok.extern.log4j.Log4j2;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@Log4j2
public class PluginLogger implements Logger {

    private final String pluginName;

    private static final Logger logger = Server.getInstance().getLogger();

    public PluginLogger(Plugin context) {
        String prefix = context.getDescription().getPrefix();
        this.pluginName = prefix != null ? "[" + prefix + "] " : "[" + context.getDescription().getName() + "] ";
    }

    @Override
    public void fatal(String message) {
        this.log(LogLevel.FATAL, message);
    }

    @Override
    public void fatal(String message, Throwable t) {
        this.log(LogLevel.FATAL, message, t);
    }

    @Override
    public void error(String message) {
        this.log(LogLevel.ERROR, message);
    }

    @Override
    public void error(String message, Throwable t) {
        this.log(LogLevel.ERROR, message, t);
    }

    @Override
    public void warn(String message) {
        this.log(LogLevel.WARN, message);
    }

    @Override
    public void warn(String message, Throwable t) {
        this.log(LogLevel.WARN, message, t);
    }

    @Override
    public void info(String message) {
        this.log(LogLevel.INFO, message);
    }

    @Override
    public void info(String message, Throwable t) {
        this.log(LogLevel.INFO, message, t);
    }

    @Override
    public void debug(String message) {
        this.log(LogLevel.DEBUG, message);
    }

    @Override
    public void debug(String message, Throwable t) {
        this.log(LogLevel.DEBUG, message, t);
    }

    @Override
    public void trace(String message) {
        this.log(LogLevel.TRACE, message);
    }

    @Override
    public void trace(String message, Throwable t) {
        this.log(LogLevel.TRACE, message, t);
    }

    @Override
    public void log(LogLevel level, String message) {
        logger.log(level, this.pluginName + message);
    }

    @Override
    public void log(LogLevel level, String message, Throwable t) {
        logger.log(level, this.pluginName + message, t);
    }

    @Override
    public void throwing(Throwable t) {
        log.throwing(t);
    }
}
