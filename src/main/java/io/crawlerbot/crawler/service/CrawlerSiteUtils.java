package io.crawlerbot.crawler.service;

import java.net.URI;

public class CrawlerSiteUtils {

    public static String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception ex) {
            return null;
        }
    }

}
