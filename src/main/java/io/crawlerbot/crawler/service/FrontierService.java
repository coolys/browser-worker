package io.crawlerbot.crawler.service;

import com.crawler.config.domain.Channel;
import com.crawler.config.domain.MessagePayLoad;
import com.crawler.config.domain.enumeration.MessageAction;
import com.news.service.utils.StringUtils;
import io.crawlerbot.crawler.config.ApplicationProperties;
import io.crawlerbot.crawler.config.ChannelQueue;
import io.crawlerbot.crawler.messaging.CrawlerService;
import io.crawlerbot.crawler.messaging.MessengerConverter;
import io.crawlerbot.crawler.messaging.queue.QueueConfig;
import io.crawlerbot.crawler.messaging.queue.QueueFactory;
import io.crawlerbot.crawler.messaging.workers.SeedWorker;
import io.crawlerbot.crawler.utils.FileConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FrontierService {

    private final Logger logger = LoggerFactory.getLogger(SeedWorker.class);
    private final CrawlerService crawlerService;
    private final ApplicationProperties applicationProperties;
    private final QueueFactory engine;

    public FrontierService(ApplicationProperties applicationProperties) {
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
        config.setNumberThread(50);
        engine = new QueueFactory(config);
        crawlerService = new CrawlerService(engine);
        startCrawl();

    }


    private void startCrawl() {
           // List<Channel> configs = FileConfigUtils.getAllChannels("en-us", "msn-com_us.json");
            List<Channel> configs = FileConfigUtils.getAllChannels("vi-vn");
            //logger.info("configs size: {}", configs.size());
            for (Channel channel : configs) {
                //logger.info("START CRAWL channel: {}", StringUtils.prettyJsonString(channel));
                    MessagePayLoad messagePayLoad = new MessagePayLoad();
                    messagePayLoad.setMessageAction(MessageAction.INNIT_CRAWL);
                    messagePayLoad.setInputLink(channel);
                    crawlerService.startCrawl(messagePayLoad);
            }
    }

}


