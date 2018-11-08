package io.crawlerbot.crawler.utils;

public class ThreadUtils {
    public static void sleep(Long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        }catch (Exception ex) {
        }
    }
}
