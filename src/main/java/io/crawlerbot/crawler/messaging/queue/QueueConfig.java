package io.crawlerbot.crawler.messaging.queue;


public class QueueConfig {

    private int numberThread;

    private String queueName;

    private Boolean forceCreateChannel;

    private Boolean forceCreateConnection;

    private String host;

    private Integer port;

    private String userName;

    private String password;

    private String addresses;

    private Integer prefetch;

    private Boolean durable;

    private Boolean exclusive;

    private Boolean autoDelete;

    private Boolean autoAck;

    private Boolean reQueueFailure;

    public QueueConfig() {
    }

    public Boolean isReQueueFailure() {
        return reQueueFailure;
    }

    public void setReQueueFailure(Boolean reQueueFailure) {
        this.reQueueFailure = reQueueFailure;
    }

    public Boolean isAutoAck() {
        return autoAck;
    }

    public void setAutoAck(Boolean autoAck) {
        this.autoAck = autoAck;
    }

    public Boolean isDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public Boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public Integer getPrefetch() {
        return prefetch;
    }

    public void setPrefetch(Integer prefetch) {
        this.prefetch = prefetch;
    }

    public int getNumberThread() {
        return numberThread;
    }

    public void setNumberThread(int numberThread) {
        this.numberThread = numberThread;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Boolean getForceCreateChannel() {
        return forceCreateChannel;
    }

    public void setForceCreateChannel(Boolean forceCreateChannel) {
        this.forceCreateChannel = forceCreateChannel;
    }

    public Boolean getForceCreateConnection() {
        return forceCreateConnection;
    }

    public void setForceCreateConnection(Boolean forceCreateConnection) {
        this.forceCreateConnection = forceCreateConnection;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "QueueConfig{" +
                "numberThread=" + numberThread +
                ", queueName='" + queueName + '\'' +
                ", forceCreateChannel=" + forceCreateChannel +
                ", forceCreateConnection=" + forceCreateConnection +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", addresses='" + addresses + '\'' +
                ", prefetch=" + prefetch +
                ", durable=" + durable +
                ", exclusive=" + exclusive +
                ", autoDelete=" + autoDelete +
                ", autoAck=" + autoAck +
                ", reQueueFailure=" + reQueueFailure +
                '}';
    }
}
