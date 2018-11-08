package io.crawlerbot.crawler.service;

import com.crawler.config.domain.CrawlLine;
import com.google.gson.Gson;


public class DataTransformer {
    public static CrawlLine readFromString(String crawlLineContent) {
        Gson gson = new Gson();
        CrawlLine crawlLine = gson.fromJson(crawlLineContent, CrawlLine.class);
        return crawlLine;
    }
    public static String convertCrawlLineToString(CrawlLine crawlLine) {
        Gson gson = new Gson();
        String content  = gson.toJson(crawlLine);
        return content;
    }

}

