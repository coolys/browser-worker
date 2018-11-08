package io.crawlerbot.crawler.service;


public interface CrawlerEngine {
    Crawler fetch();

    Crawler parse(boolean splitData);

    Crawler index();

    Crawler upload();

    Crawler nextLinks();

    Crawler report();

    Crawler start();

}
