package io.crawlerbot.crawler;

import io.crawlerbot.crawler.config.ApplicationProperties;
import io.crawlerbot.crawler.logger.LoggerFactory;
import io.crawlerbot.crawler.messaging.workers.SourceWorker;
import io.crawlerbot.crawler.messaging.workers.SeedWorker;
import io.crawlerbot.crawler.service.FrontierService;

public class Application {

    private final LoggerFactory logger = new LoggerFactory(Application.class);
    private void start() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        logger.info("applicationProperties: {}", applicationProperties);
        SeedWorker seedWorker = new SeedWorker(applicationProperties);
        SourceWorker sourceWorker = new SourceWorker(applicationProperties);
    }
    public static void main(String[] argv) throws Exception {
        Long id = System.currentTimeMillis();
        System.setProperty("log.name", "news-crawler-worker-" + id + ".log");
        System.setProperty("workerId", id.toString());
        System.setProperty("appName", "news-crawler-worker");
        new Application().start();
    }


}