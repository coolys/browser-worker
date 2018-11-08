package io.crawlerbot.crawler.messaging.workers;

import com.crawler.config.domain.CrawlLine;

import com.crawler.config.domain.WebData;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import io.crawlerbot.crawler.config.ApplicationProperties;
import io.crawlerbot.crawler.config.ChannelQueue;
import io.crawlerbot.crawler.exceptions.NotSupportBrowserException;
import io.crawlerbot.crawler.messaging.CrawlerService;
import io.crawlerbot.crawler.messaging.MessengerConverter;
import io.crawlerbot.crawler.messaging.queue.QueueConfig;
import io.crawlerbot.crawler.messaging.queue.QueueFactory;
import io.crawlerbot.crawler.messaging.queue.QueueWorker;
import io.crawlerbot.crawler.messaging.queue.QueueWorkerListener;
import io.crawlerbot.crawler.service.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.MalformedURLException;


public class SeedWorker {
    private final Logger logger = LoggerFactory.getLogger(SeedWorker.class);
    private final CrawlerService crawlerService;
    private final ApplicationProperties applicationProperties;
    private final QueueFactory engine;

    public SeedWorker(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        QueueConfig config = new QueueConfig();
        config.setAutoAck(false);
        config.setAutoDelete(false);
        config.setExclusive(false);
        config.setDurable(true);
        config.setQueueName(ChannelQueue.CRAWLER_NEXT);
        config.setForceCreateChannel(false);
        config.setForceCreateConnection(false);
        config.setReQueueFailure(true);
        config.setHost(applicationProperties.rabbitMQConfig.getHost());
        config.setUserName(applicationProperties.rabbitMQConfig.getUsername());
        config.setPassword(applicationProperties.rabbitMQConfig.getPassword());
        config.setPort(applicationProperties.rabbitMQConfig.getPort());
        config.setNumberThread(1);
        engine = new QueueFactory(config);

        crawlerService = new CrawlerService(engine);

        if (config.getNumberThread() > 1) {
            QueueWorker worker = engine.consumeMultiThread(new QueueWorkerListener() {
                @Override
                public boolean processMessage(byte[] content, AMQP.BasicProperties properties, String queueName, Channel channel, String workerTag) throws IOException {
                    //logger.info("processMessage...");
                    String body = new String(content, "UTF-8");
                    process(body);
                    return true;
                }
            });
        } else {
            QueueWorker worker = engine.consumeSingleThread(new QueueWorkerListener() {
                @Override
                public boolean processMessage(byte[] content, AMQP.BasicProperties properties, String queueName, Channel channel, String workerTag) throws IOException {
                    //logger.info("processMessage...");
                    String body = new String(content, "UTF-8");
                    process(body);
                    return true;
                }
            });
        }
    }

    public void process(String message) {
        //logger.info(" [x] SourceWorker Received '" + message + "'");
        try {
            WebData crawlLine = MessengerConverter.convertStringToWebData(message);
            //logger.info("crawlLine......:{}", crawlLine);
            if (crawlLine != null && crawlLine.getUrl() != null) {
                Crawler crawler = null;
                try {
                    crawler = new Crawler(crawlLine, crawlerService, applicationProperties,null, null, null);
                } catch (NotSupportBrowserException e) {
                    e.printStackTrace();
                }
                crawler.start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.info("[-] crawlLine exception: {}", ex.toString());
        }

    }


}
