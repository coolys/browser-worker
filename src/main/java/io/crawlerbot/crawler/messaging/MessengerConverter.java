package io.crawlerbot.crawler.messaging;

import com.crawler.config.domain.Channel;
import com.crawler.config.domain.CrawlLine;
import com.crawler.config.domain.MessagePayLoad;
import com.crawler.config.domain.WebData;
import com.google.gson.Gson;

public class MessengerConverter {

    public static String convertChannelToString(Channel channel) {
        if(channel == null) return null;
        Gson gson = new Gson();
        String content = gson.toJson(channel);
        return content;
    }
    public static String convertCrawlLineToString(CrawlLine crawlLine) {
        if(crawlLine == null) return null;
        Gson gson = new Gson();
        String content = gson.toJson(crawlLine);
        return content;
    }
    public static String convertWebDataToString(WebData crawlLine) {
        if(crawlLine == null) return null;
        Gson gson = new Gson();
        String content = gson.toJson(crawlLine);
        return content;
    }

    public static CrawlLine convertStringToCrawlLine(String message) {
        if(message == null || message.length() ==0) return null;
        Gson gson = new Gson();
        CrawlLine crawlLine = gson.fromJson(message, CrawlLine.class);
        return crawlLine;
    }
    public static WebData convertStringToWebData(String message) {
        if(message == null || message.length() ==0) return null;
        Gson gson = new Gson();
        WebData crawlLine = gson.fromJson(message, WebData.class);
        return crawlLine;
    }
    public static String convertMessagePayLoadToString(MessagePayLoad messagePayLoad) {
        if(messagePayLoad == null) return null;
        Gson gson = new Gson();
        String content = gson.toJson(messagePayLoad);
        return content;
    }
    public static MessagePayLoad convertStringToMessagePayload(String message) {
        if(message == null || message.length() ==0) return null;
        Gson gson = new Gson();
        MessagePayLoad messagePayLoad = gson.fromJson(message, MessagePayLoad.class);
        return messagePayLoad;
    }


    public static Channel convertStringToChannel(String message) {
        if(message == null || message.length() ==0) return null;
        Gson gson = new Gson();
        Channel result = gson.fromJson(message, Channel.class);
        return result;
    }
//    public static Channel convertStringToChannel(String message) {
//        if(message == null || message.length() ==0) return null;
//        Gson gson = new Gson();
//        Channel result = gson.fromJson(message, Channel.class);
//        return result;
//    }



}
