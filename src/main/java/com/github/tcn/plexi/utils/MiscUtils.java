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

    public static String StringMask(String toObfuscate, int numberShown){
        return toObfuscate.replaceAll(".(?=.{"+numberShown+"})", "X");
    }

    public static String OverseerrPermIntDecypherer(int permInt){
        switch (permInt){
            case 0:
                return "No Permissions Given to User";
            case 2:
                return "Administrator";
            case 8:
                return "User Manager";
            case 16:
                return "Request Manager";
            case 32:
                return "Able to Request All Media";
            case 64:
                return "Able to Vote on Media";
            case 128:
                return "Auto Approve All Requests";
            case 256:
                return "Auto Approve Movie Requests";
            case 512:
                return "Auto Approve TV Requests";
            case 1024:
                return "Request 4K Media";
            case 2048:
                return "Request 4K Movies";
            case 4096:
                return "Request 4K Television";
            case 8192:
                return "Request Advanced Media Options";
            case 16384:
                return "View Requested Media";
            case 32768:
                return "Auto Approve 4K Requests";
            case 65536:
                return "Auto Approve 4K Movie Requests";
            case 131072:
                return "Auto Approve 4K TV Requests";
            case 262144:
                return "Request Movies";
            case 524288:
                return "Request TV";
            case 1048576:
                return "Manage Issues";
            case 2097152:
                return "View Issues";
            case 4194304:
                return "Create Issues";
            case 8388608:
                return "Auto Request Media";
            case 16777216:
                return "Auto Request Movies";
            case 33554432:
                return "Auto Request TV";
            case 67108864:
                return "View Recently Watched Media";
            case 134217728:
                return "View Watchlists";
        }
        return "Unable to decipher permission integer.";
    }
}
