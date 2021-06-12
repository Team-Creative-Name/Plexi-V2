package com.github.tcn.plexi.utils;

import com.github.tcn.plexi.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;


//a class full of random static methods for things
public class Misc {

    public static ThreadFactory newThreadFactory(String threadName, Logger logger, boolean isDaemon){
        return (r) ->{
          Thread thread = new Thread(r, threadName);
            thread.setDaemon(isDaemon);
            thread.setUncaughtExceptionHandler((Thread errorThread, Throwable throwable) ->
                    logger.error("There was an uncaught exception in the {} thread pool! ", thread.getName(), throwable)
                    );
            return thread;
        };
    }

    public static ThreadFactory newThreadFactory(String threadName){
        return newThreadFactory(threadName, LoggerFactory.getLogger("Plexi: Threading"),true );
    }

    public static ThreadFactory newThreadFactory(String threadName, Logger logger){
        return newThreadFactory(threadName, logger,true );
    }

    public static ThreadFactory newThreadFactory(String threadName, boolean isDaemon){
        return newThreadFactory(threadName, LoggerFactory.getLogger("Plexi: Threading"),isDaemon );
    }



}
