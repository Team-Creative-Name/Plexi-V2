package com.github.tcn.plexi.utils;

import com.github.tcn.plexi.overseerr.templates.request.allRequests.MediaRequests;
import com.github.tcn.plexi.overseerr.templates.search.MediaSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadFactory;


//a class full of random static methods for things
public class MiscUtils {

    public static ThreadFactory newThreadFactory(String threadName, Logger logger, boolean isDaemon) {
        return (r) -> {
            Thread thread = new Thread(r, threadName);
            thread.setDaemon(isDaemon);
            thread.setUncaughtExceptionHandler((Thread errorThread, Throwable throwable) ->
                    logger.error("There was an uncaught exception in the {} thread pool! ", thread.getName(), throwable)
            );
            return thread;
        };
    }

    public static ThreadFactory newThreadFactory(String threadName) {
        return newThreadFactory(threadName, LoggerFactory.getLogger("Plexi: Threading"), true);
    }

    public static ThreadFactory newThreadFactory(String threadName, Logger logger) {
        return newThreadFactory(threadName, logger, true);
    }

    public static ThreadFactory newThreadFactory(String threadName, boolean isDaemon) {
        return newThreadFactory(threadName, LoggerFactory.getLogger("Plexi: Threading"), isDaemon);
    }

    public static MediaSearch stripActors(MediaSearch toStrip) {
        toStrip.getResults().removeIf(result -> result.getMediaType().equals("person"));
        return toStrip;
    }

    public static MediaSearch filterByType(MediaSearch search, String filter) {
        search.getResults().removeIf(result -> !result.getMediaType().equals(filter));
        return search;
    }

    public static MediaRequests filterByType(MediaRequests requests, String filter) {
        requests.getResults().removeIf(request -> !request.getType().equals(filter));
        return requests;
    }

    public static String formatStackTraceAsString(StackTraceElement[] stackTrace) {
        StringBuilder stackTraceString = new StringBuilder();
        for (StackTraceElement stackElement : stackTrace) {
            stackTraceString.append(stackElement.toString()).append("\n");
        }
        return stackTraceString.toString();
    }

    public static String urlEncode(String toEncode) {
        //This is a bit of a hack, but it seems to cover everything that I need. (I think...)
        return URLEncoder.encode(toEncode, StandardCharsets.UTF_8).replace("+", "%20").replace("*", "%2A");
    }
}
