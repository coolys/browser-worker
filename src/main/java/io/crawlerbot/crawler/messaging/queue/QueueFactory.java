package io.crawlerbot.crawler.messaging.queue;

import com.rabbitmq.client.*;
import io.crawlerbot.crawler.exceptions.OperationException;
import io.crawlerbot.crawler.logger.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

public class QueueFactory {

    private final LoggerFactory logger = new LoggerFactory(QueueFactory.class);
    private ConnectionFactory factory;
    private Connection producerConnection;
    private Channel proceducerChannel;
    private Connection consumerConnection;
    private Channel consumerChannel;
    private QueueConfig config;

    public QueueFactory(QueueConfig config) throws OperationException {
        if (config == null) {
            logger.info("Queue system config can not be null !");
            throw new OperationException("Queue system config can not be null !");
        }
        this.config = config;
        factory = new ConnectionFactory();
        factory.setHost(config.getHost());
        factory.setUsername(config.getUserName());
        factory.setPassword(config.getPassword());
        factory.setPort(config.getPort());
        logger.info("start crawler engine.....");
    }

    /**
     * @return
     * @throws IOException
     * @throws TimeoutException
     */

    private Connection getProducerConnection() throws IOException, TimeoutException {
        if (producerConnection == null || !producerConnection.isOpen()) {
            producerConnection = factory.newConnection();
        }
        return producerConnection;
    }

    /**
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    private Connection getConsumerConnection() throws IOException, TimeoutException {
        if (consumerConnection == null || !consumerConnection.isOpen()) {
            consumerConnection = factory.newConnection();
        }
        return consumerConnection;
    }

    /**
     * @return proceducerChannel
     * @throws IOException
     * @throws TimeoutException
     */
    private Channel getProducerChannel() throws IOException, TimeoutException {
        if (proceducerChannel != null && proceducerChannel.isOpen()) {
            proceducerChannel.close();
        }
        proceducerChannel = getProducerConnection().createChannel();
        /*proceducerChannel.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException sse) {
                logger.info("ShutdownSignalException: {}", sse);
            }
        });*/
        return proceducerChannel;
    }

    /**
     * @param queueName
     * @param customKey
     * @param body
     * @param headers
     * @param propBuilder
     * @param override
     */
    public void produce(String queueName, String customKey, final byte[] body, final Map<String, Object> headers, AMQP.BasicProperties.Builder propBuilder, Map<String, Object> override) {
        propBuilder = (propBuilder == null) ? new AMQP.BasicProperties.Builder() : propBuilder;
        try {
            Channel channel = getProducerChannel();
            if (channel == null || !channel.isOpen()) {
                throw new OperationException("Channel is null or has been closed");
            }
            channel.queueDeclare(config.getQueueName(), config.isDurable(), config.isExclusive(), config.isAutoDelete(), null);
            //channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, propBuilder.build(), body);
            logger.info("produce success with queueName:", queueName);
        } catch (IOException exception) {
            logger.info("produce exception: ", exception);
            throw new OperationException(exception);
        } catch (TimeoutException exception) {
            logger.info("produce exception: ", exception);
            throw new OperationException(exception);
        }
    }

    /**
     * @param forceCreateConnection
     * @param forceCreatedChannel
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    private Channel getConsumerChannel(boolean forceCreateConnection, boolean forceCreatedChannel) throws IOException, TimeoutException {
        if (forceCreateConnection) {
            logger.info("forceCreateConnection created");
            return getConsumerConnection().createChannel();
        }
        if (forceCreatedChannel) {
            logger.info("forceCreatedChannel created");
            return getConsumerConnection().createChannel();
        }
        if (consumerChannel == null || !consumerChannel.isOpen()) {
            consumerChannel = getConsumerConnection().createChannel();
            logger.info("sharedConsumingChannelCreated consumer channel created");
        }
        return consumerChannel;
    }

    /**
     * @param listener
     * @return
     */
    public QueueWorker consumeMultiThread(final QueueWorkerListener listener) {

        try {
            final boolean _forceNewConnection = Boolean.TRUE.equals(config.getForceCreateConnection());
            final Boolean _forceNewChannel = Boolean.TRUE.equals(config.getForceCreateChannel());
            final Channel _channel = getConsumerChannel(_forceNewConnection, _forceNewChannel);
            final Connection _connection = _channel.getConnection();
            Integer _prefetch = config.getPrefetch();
            if (_prefetch != null && _prefetch > 0) {
                _channel.basicQos(_prefetch);
            }
            final String _queueName;
            final boolean _fixedQueue;
            String opts_queueName = config.getQueueName();
            final boolean opts_durable = !Boolean.FALSE.equals(config.isDurable());
            final boolean opts_exclusive = Boolean.TRUE.equals(config.isExclusive());
            final boolean opts_autoDelete = Boolean.TRUE.equals(config.isAutoDelete());

            final ExecutorService threadPool = new ThreadPoolExecutor(config.getNumberThread(), config.getNumberThread(),
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());


            AMQP.Queue.DeclareOk _declareOk;
            if (opts_queueName != null) {
                _declareOk = _channel.queueDeclare(opts_queueName, opts_durable, opts_exclusive, opts_autoDelete, null);
                _fixedQueue = true;
            } else {
                _declareOk = _channel.queueDeclare();
                _fixedQueue = false;
            }
            _queueName = _declareOk.getQueue();

            logger.info("[*] waiting for messange coming to : {}", opts_queueName);
            final Boolean _autoAck;
            if (config.isAutoAck()) {
                _autoAck = config.isAutoAck();
            } else {
                _autoAck = Boolean.TRUE;
            }

            final Boolean _requeueFailure;
            if (config.isReQueueFailure()) {
                _requeueFailure = config.isReQueueFailure();
            } else {
                _requeueFailure = Boolean.FALSE;
            }

            final Consumer _consumer = new DefaultConsumer(_channel) {
                private void invokeAck(Envelope envelope, boolean success) throws IOException {
                    if (!_autoAck) {
                        if (success) {
                            _channel.basicAck(envelope.getDeliveryTag(), false);
                        } else {
                            if (!_requeueFailure) {
                                _channel.basicAck(envelope.getDeliveryTag(), false);
                            } else {
                                _channel.basicNack(envelope.getDeliveryTag(), false, true);
                            }
                        }
                    }
                }

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        logger.info("invoke listener.processMessage()");
                        threadPool.submit(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    boolean result = listener.processMessage(body, properties, _queueName, _channel, consumerTag);
                                    if (result)
                                        logger.info("process has finished successully !");
                                    else
                                        logger.info("process has not successully !");
                                    logger.info("invoke ACK");
                                    invokeAck(envelope, true);

                                    //
                                } catch (InterruptedException e) {
                                    logger.warn(String.format("Interrupted %s", new String(body)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception ex) {
                        logger.info(
                                "deliveryTag: {}, " +
                                        "consumerTag: {}, " +
                                        "exceptionClass:{}, " +
                                        "exceptionMessage: {}, " +
                                        "requeueFailure: {}", envelope.getDeliveryTag(), consumerTag, ex.getClass().getName(), ex.getMessage(), _autoAck, _requeueFailure);
                        invokeAck(envelope, false);
                    }
                }

                @Override
                public void handleCancelOk(String consumerTag) {
                    logger.info("consumerTag: {}, Consumer[${consumerId}].consume() - handle CancelOk event", consumerTag);
                }

                @Override
                public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                    logger.info("consumerTag: {}, Consumer[${consumerId}].consume() - handle ShutdownSignal event", consumerTag);
                }
            };

            final String _consumerTag = _channel.basicConsume(_queueName, _autoAck, _consumer);
            logger.info("queueName:{}, consumerTag: {},channelNumber: {},Consumer[${consumerId}].consume() create consumer[${consumerTag}]/queue[${queueName}]", _queueName, _consumerTag, _channel.getChannelNumber());
            QueueWorker info = new QueueWorker(_connection, !_forceNewConnection,
                    _channel, !_forceNewChannel, _queueName, _fixedQueue, null, _consumerTag);
            return info;
        } catch (IOException exception) {
            logger.info("exceptionClass:{}," +
                    "exceptionMessage: {}, " +
                    "Consumer[${consumerId}].consume() - has failed", exception.getClass().getName(), exception.getMessage());
            throw new OperationException(exception);
        } catch (TimeoutException exception) {
            logger.info("exceptionClass:{}," +
                    "exceptionMessage: {}," +
                    "Consumer[${consumerId}].consume() - is timeout", exception.getClass().getName(), exception.getMessage());
            throw new OperationException(exception);
        }
    }

    /**
     * @param listener
     * @return
     */
    public QueueWorker consumeSingleThread(final QueueWorkerListener listener) {

        try {
            logger.info("start consumeSingleThread....");
            final boolean _forceNewConnection = Boolean.TRUE.equals(config.getForceCreateConnection());
            final Boolean _forceNewChannel = Boolean.TRUE.equals(config.getForceCreateChannel());
            final Channel _channel = getConsumerChannel(_forceNewConnection, _forceNewChannel);
            final Connection _connection = _channel.getConnection();
            Integer _prefetch = config.getPrefetch();
            if (_prefetch != null && _prefetch > 0) {
                _channel.basicQos(_prefetch);
            }
            final String _queueName;
            final boolean _fixedQueue;
            String opts_queueName = config.getQueueName();
            final boolean opts_durable = !Boolean.FALSE.equals(config.isDurable());
            final boolean opts_exclusive = Boolean.TRUE.equals(config.isExclusive());
            final boolean opts_autoDelete = Boolean.TRUE.equals(config.isAutoDelete());
            AMQP.Queue.DeclareOk _declareOk;
            if (opts_queueName != null) {
                _declareOk = _channel.queueDeclare(opts_queueName, opts_durable, opts_exclusive, opts_autoDelete, null);
                _fixedQueue = true;
            } else {
                _declareOk = _channel.queueDeclare();
                _fixedQueue = false;
            }
            _queueName = _declareOk.getQueue();

            logger.info("[*] waiting for messange coming to : {}", opts_queueName);

            final Boolean _autoAck;
            if (config.isAutoAck()) {
                _autoAck = config.isAutoAck();
            } else {
                _autoAck = Boolean.TRUE;
            }

            final Boolean _requeueFailure;
            if (config.isReQueueFailure()) {
                _requeueFailure = config.isReQueueFailure();
            } else {
                _requeueFailure = Boolean.FALSE;
            }

            final Consumer _consumer = new DefaultConsumer(_channel) {
                private void invokeAck(Envelope envelope, boolean success) throws IOException {
                    if (!_autoAck) {
                        if (success) {
                            _channel.basicAck(envelope.getDeliveryTag(), false);
                        } else {
                            if (!_requeueFailure) {
                                _channel.basicAck(envelope.getDeliveryTag(), false);
                            } else {
                                _channel.basicNack(envelope.getDeliveryTag(), false, true);
                            }
                        }
                    }
                }

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        logger.info("invoke listener.processMessage()");
                        boolean result = listener.processMessage(body, properties, _queueName, _channel, consumerTag);
                        if (result)
                            logger.info("process has finished successully at queueName:{}!", _queueName);
                        else
                            logger.info("process has not successully at queueName: {}!", _queueName);
                        logger.info("invoke ACK");
                        invokeAck(envelope, true);
                    } catch (Exception ex) {
                        logger.info(
                                "deliveryTag: {}, " +
                                        "consumerTag: {}, " +
                                        "exceptionClass:{}, " +
                                        "exceptionMessage: {}, " +
                                        "requeueFailure: {}", envelope.getDeliveryTag(), consumerTag, ex.getClass().getName(), ex.getMessage(), _autoAck, _requeueFailure);
                        invokeAck(envelope, false);
                    }
                }

                @Override
                public void handleCancelOk(String consumerTag) {
                    logger.info("consumerTag: {}, Consumer[${consumerId}].consume() - handle CancelOk event", consumerTag);
                }

                @Override
                public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                    logger.info("consumerTag: {}, Consumer[${consumerId}].consume() - handle ShutdownSignal event", consumerTag);
                }
            };

            final String _consumerTag = _channel.basicConsume(_queueName, _autoAck, _consumer);
            logger.info("queueName:{}, consumerTag: {},channelNumber: {},Consumer[${consumerId}].consume() create consumer[${consumerTag}]/queue[${queueName}]", _queueName, _consumerTag, _channel.getChannelNumber());
            QueueWorker info = new QueueWorker(_connection, !_forceNewConnection,
                    _channel, !_forceNewChannel, _queueName, _fixedQueue, null, _consumerTag);
            return info;
        } catch (IOException exception) {
            logger.info("exceptionClass:{}," +
                    "exceptionMessage: {}, " +
                    "Consumer[${consumerId}].consume() - has failed", exception.getClass().getName(), exception.getMessage());
            throw new OperationException(exception);
        } catch (TimeoutException exception) {
            logger.info("exceptionClass:{}," +
                    "exceptionMessage: {}," +
                    "Consumer[${consumerId}].consume() - is timeout", exception.getClass().getName(), exception.getMessage());
            throw new OperationException(exception);
        }
    }


}
