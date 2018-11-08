/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.crawlerbot.crawler.messaging.workers;

import com.crawler.config.domain.MessagePayLoad;
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


public class SourceWorker {
    private final QueueFactory engine;
    private final Logger logger = LoggerFactory.getLogger(SourceWorker.class);
    private final CrawlerService crawlerService;
    private final ApplicationProperties applicationProperties;

    public SourceWorker(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        QueueConfig config = new QueueConfig();
        config.setAutoAck(false);
        config.setAutoDelete(false);
        config.setExclusive(false);
        config.setDurable(true);
        config.setQueueName(ChannelQueue.CRAWLER_INPUT);
        config.setForceCreateChannel(false);
        config.setForceCreateChannel(false);
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
                    //logger.info("message comming ....");
                    String message = new String(content, "UTF-8");
                    process(message);
                    return true;
                }
            });
        } else {
            QueueWorker worker = engine.consumeSingleThread(new QueueWorkerListener() {
                @Override
                public boolean processMessage(byte[] content, AMQP.BasicProperties properties, String queueName, Channel channel, String workerTag) throws IOException {
                    //logger.info("message comming ....");
                    String message = new String(content, "UTF-8");
                    process(message);
                    return true;
                }
            });
        }

    }

    public void process(String message) {
        //logger.info(" [x] SourceWorker Received '" + message + "'");
        try {
            MessagePayLoad messagePayLoad = MessengerConverter.convertStringToMessagePayload(message);
            if (messagePayLoad != null) {
                //logger.info("Received crawling channel: {}", messagePayLoad);
                Crawler crawler = null;
                try {
                    crawler = new Crawler(messagePayLoad.getInputLink(), crawlerService, applicationProperties,messagePayLoad.getBrowserName(), messagePayLoad.getBrowserHost(), messagePayLoad.getBrowserOS());
                } catch (NotSupportBrowserException e) {
                    e.printStackTrace();
                }
                crawler.start();
            }

        } catch (Exception e) {
            //logger.info("Receive process crawl exception: {}", e.toString());
            e.printStackTrace();

        }

    }
}
