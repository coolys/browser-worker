package io.crawlerbot.crawler.messaging;


import com.crawler.config.domain.Channel;
import com.crawler.config.domain.CrawlLine;
import com.crawler.config.domain.MessagePayLoad;
import com.crawler.config.domain.WebData;
import com.crawler.config.domain.enumeration.DestinationSystem;
import com.crawler.config.domain.enumeration.MessageAction;
import io.crawlerbot.crawler.config.ChannelQueue;
import io.crawlerbot.crawler.logger.LoggerFactory;
import io.crawlerbot.crawler.messaging.queue.QueueFactory;

import java.io.UnsupportedEncodingException;


public class CrawlerService {
    private static final LoggerFactory logger = new LoggerFactory(CrawlerService.class);
    private final QueueFactory engine;

    public CrawlerService(QueueFactory engine) {
        this.engine = engine;
    }

    private byte[] getBytes(String message) {
        try {
            return message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startCrawl(MessagePayLoad messagePayLoad) {
        //logger.info("[+] start crawl: {}", messagePayLoad);
        messagePayLoad.setMessageAction(MessageAction.INNIT_CRAWL);
        String messageContent = MessengerConverter.convertMessagePayLoadToString(messagePayLoad);
        if (messageContent != null) {
            byte[] data = getBytes(messageContent);
            if (data != null) {
                engine.produce(ChannelQueue.CRAWLER_INPUT, ChannelQueue.CRAWLER_INPUT, data, null, null, null);
            }
        }
    }

    public void startCrawlNextLink(WebData crawlLine) {
        //logger.info("[+] start next crawl link: {}", crawlLine);
        String content = MessengerConverter.convertWebDataToString(crawlLine);
        if (content != null) {
            //this.template.convertAndSend(RabbitmqConfig.CRAWLER_NEXT, content);\
            byte[] data = getBytes(content);
            if (data != null) {
                engine.produce(ChannelQueue.CRAWLER_NEXT, ChannelQueue.CRAWLER_NEXT, data, null, null, null);
            }
        }
    }

    public void sendToIndexing(WebData crawlLine) {
        //logger.info("[+] send to indexing: {}", crawlLine);
        Channel channel = crawlLine.getChannel();
        MessagePayLoad messagePayLoad = new MessagePayLoad();
       // messagePayLoad.setCrawlLine(crawlLine);
        messagePayLoad.setWebData(crawlLine);
        messagePayLoad.setInputLink(crawlLine.getChannel());
        messagePayLoad.setMessageAction(MessageAction.INDEX);
        String content = MessengerConverter.convertMessagePayLoadToString(messagePayLoad);

        // Toi nay nho set tiep cai destination de parser
        if (crawlLine.getChannel().getDestination().equals(DestinationSystem.CATEGORY_INDEXER)) {
            byte[] data = getBytes(content);
            if (data != null) {
                engine.produce(ChannelQueue.INDEXING_CATEGORY, ChannelQueue.INDEXING_CATEGORY, data, null, null, null);
                engine.produce(ChannelQueue.INDEXING_CHANNEL, ChannelQueue.INDEXING_CHANNEL, data, null, null, null);
            }

        } else {

            byte[] data = getBytes(content);
            if (data != null) {
                engine.produce(channel.getDestination().toString(), channel.getDestination().toString(), data, null, null, null);
            }
        }

    }

}
