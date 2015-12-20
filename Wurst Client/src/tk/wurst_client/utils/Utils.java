package tk.wurst_client.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Project: EvenWurse
 * Created: 19/12/15
 * Author: nulldev
 */
public class Utils {
    private static ScheduledExecutorService EXECUTOR_SERVICE = null;
    public static void schedule(Runnable runnable, int time, TimeUnit tu) {
        if(EXECUTOR_SERVICE == null) EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);
        EXECUTOR_SERVICE.schedule(runnable, time, tu);
    }
}
