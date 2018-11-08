package io.crawlerbot.crawler.messaging.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class QueueWorker {
    private final Connection connection;
    private final boolean sharedConnection;
    private final Channel channel;
    private final boolean sharedChannel;
    private final String queueName;
    private final boolean fixedQueue;
    private final String consumerId;
    private final String consumerTag;

    public QueueWorker(Connection connection, boolean sharedConnection, Channel channel, boolean sharedChannel,
                        String queueName, boolean fixedQueue, String consumerId, String consumerTag) {
        this.connection = connection;
        this.sharedConnection = sharedConnection;
        this.channel = channel;
        this.sharedChannel = sharedChannel;
        this.queueName = queueName;
        this.fixedQueue = fixedQueue;
        this.consumerId = consumerId;
        this.consumerTag = consumerTag;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isSharedConnection() {
        return sharedConnection;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isSharedChannel() {
        return sharedChannel;
    }

    public String getQueueName() {
        return queueName;
    }

    public boolean isFixedQueue() {
        return fixedQueue;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getConsumerTag() {
        return consumerTag;
    }
}
