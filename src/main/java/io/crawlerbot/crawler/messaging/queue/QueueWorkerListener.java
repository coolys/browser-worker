package io.crawlerbot.crawler.messaging.queue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;

public interface QueueWorkerListener {
    public boolean processMessage(byte[] content, AMQP.BasicProperties properties, String queueName, Channel channel, String workerTag) throws IOException;
}
