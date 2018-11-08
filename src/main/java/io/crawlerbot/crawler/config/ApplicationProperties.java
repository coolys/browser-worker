package io.crawlerbot.crawler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

public class ApplicationProperties {

    private final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
    public final SeleniumConfig selenium;

    public SeleniumConfig getSelenium() {
        return selenium;
    }

    public final RabbitMQConfig rabbitMQConfig;

    public RabbitMQConfig getRabbitMQConfig() {
        return rabbitMQConfig;
    }

    public final SystemConfig systemConfig;

    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    private Properties appProps;

    public ApplicationProperties() {

        try {
            Properties appProps = new Properties();
            appProps.load(ApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties"));
            this.appProps = appProps;
        } catch (Exception ex) {
            logger.info("[-] exception:{}", ex.toString());
            Properties appProps = new Properties();
            this.appProps = appProps;
        }
        this.selenium = new SeleniumConfig(this.appProps);
        this.rabbitMQConfig = new RabbitMQConfig(this.appProps);
        this.systemConfig = new SystemConfig(this.appProps);
        logger.info("RabbitMQ Configuration:{}," +
                "Selenium Configuration:{}," +
                "System Configuration:{}", this.rabbitMQConfig,this.selenium,this.systemConfig);
    }


    @Override
    public String toString() {
        return "ApplicationProperties{" +
                ", selenium=" + selenium +
                ", rabbitMQConfig=" + rabbitMQConfig +
                ", systemConfig=" + systemConfig +
                '}';
    }

    public static class SystemConfig {
        private String browserName;
        private String browserHost;
        private String browserOS;

        private void getSystemConfigEnv() {
            if (System.getenv("BROWSER_OS") != null) {
                this.browserOS = System.getenv("BROWSER_OS");
            }
            if (System.getenv("BROWSER_HOST") != null) {
                this.browserHost = System.getenv("BROWSER_HOST");
            }
            if (System.getenv("BROWSER_NAME") != null) {
                this.browserName = System.getenv("BROWSER_NAME");
            }
        }

        private void getSystemConfigProperties(Properties appProps) {

            if (appProps.getProperty("browser.os") != null) {
                this.browserOS = appProps.getProperty("browser.os");
            }
            if (appProps.getProperty("browser.host") != null) {
                this.browserHost = appProps.getProperty("browser.host");
            }
            if (appProps.getProperty("browser.name") != null) {
                this.browserName = appProps.getProperty("browser.name");
            }
        }

        public SystemConfig(Properties appProps) {
            getSystemConfigEnv();
            if (this.browserOS == null || this.browserOS.isEmpty()) {
                getSystemConfigProperties(appProps);
            }
        }

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getBrowserHost() {
            return browserHost;
        }

        public void setBrowserHost(String browserHost) {
            this.browserHost = browserHost;
        }

        public String getBrowserOS() {
            return browserOS;
        }

        public void setBrowserOS(String browserOS) {
            this.browserOS = browserOS;
        }

        @Override
        public String toString() {
            return "SystemConfig{" +
                    "browserName='" + browserName + '\'' +
                    ", browserHost='" + browserHost + '\'' +
                    ", browserOS='" + browserOS + '\'' +
                    '}';
        }
    }

    public static class SeleniumConfig {
        private String url;

        public void setUrl(String url) {
            this.url = url;
        }

        public SeleniumConfig(Properties appProps) {
            if (System.getenv("APPLICATION_SELENIUM_URL") != null) {
                this.url = System.getenv("APPLICATION_SELENIUM_URL");
            }
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "SeleniumConfig{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }

    public static class RabbitMQConfig {
        private String host;
        private Integer port;
        private String username;
        private String password;

        private void getRabbitSystemEnv() {
            if (System.getenv("QUEUE_CONFIG_HOST") != null) {
                this.host = System.getenv("QUEUE_CONFIG_HOST");
            }
            if (System.getenv("QUEUE_CONFIG_PORT") != null) {
                this.port = Integer.parseInt(System.getenv("QUEUE_CONFIG_PORT"));
            }
            if (System.getenv("QUEUE_CONFIG_USERNAME") != null) {
                this.username = System.getenv("QUEUE_CONFIG_USERNAME");
            }
            if (System.getenv("QUEUE_CONFIG_PASSWORD") != null) {
                this.password = System.getenv("QUEUE_CONFIG_PASSWORD");
            }
        }

        @Override
        public String toString() {
            return "RabbitMQConfig{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }

        private void getRabbitSystemProperties(Properties appProps) {

            if (appProps.getProperty("queue.config.host") != null) {
                this.host = appProps.getProperty("queue.config.host");
            }
            if (appProps.getProperty("queue.config.port") != null) {
                this.port = Integer.parseInt(appProps.getProperty("queue.config.port"));
            }
            if (appProps.getProperty("queue.config.username") != null) {
                this.username = appProps.getProperty("queue.config.username");
            }
            if (appProps.getProperty("queue.config.password") != null) {
                this.password = appProps.getProperty("queue.config.password");
            }


        }

        public RabbitMQConfig(Properties appProps) {
            this.getRabbitSystemEnv();
            if (this.host == null || this.host.isEmpty()) {
                this.getRabbitSystemProperties(appProps);
            }
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
