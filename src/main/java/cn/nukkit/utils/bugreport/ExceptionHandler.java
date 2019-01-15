package cn.nukkit.utils.bugreport;

import cn.nukkit.Nukkit;
import com.bugsnag.Bugsnag;
import java.util.UUID;

/**
 * Project nukkit
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handle(thread, throwable);
    }

    public void handle(Thread thread, Throwable throwable) {
        throwable.printStackTrace();

        try {
            new BugReportGenerator(throwable).start();
        } catch (Exception exception) {
            // Fail Safe
        }

        //NukkitV auto report
        Bugsnag bugsnag = new Bugsnag("d44e2b1cd60a24020699b6e2662d3814", false);
        bugsnag.addCallback((report) -> {
            report.setAppInfo("NukkitV", Nukkit.CODENAME).setUserName(Nukkit.VERSION).setUserId(UUID.randomUUID().toString()).setDeviceInfo("runtime.processors", Runtime.getRuntime().availableProcessors()).setDeviceInfo("runtime.memory.total", Runtime.getRuntime().totalMemory()).setDeviceInfo("runtime.memory.max", Runtime.getRuntime().maxMemory()).setDeviceInfo("runtime.memory.free", Runtime.getRuntime().freeMemory());
            for (String info : System.getProperties().stringPropertyNames()) {
                report.setDeviceInfo(info, System.getProperties().getProperty(info));
            }
        });
        bugsnag.notify(throwable);
    }
}
