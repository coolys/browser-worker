package io.crawlerbot.crawler.service;

import com.crawler.config.domain.Channel;
import com.crawler.config.domain.CrawlLine;
import com.crawler.config.domain.FetchEngine;
import com.crawler.config.domain.enumeration.BrowserHost;
import com.crawler.config.domain.enumeration.BrowserName;
import com.crawler.config.domain.enumeration.BrowserOS;
import com.crawler.config.domain.enumeration.Engine;
import io.crawlerbot.crawler.config.ApplicationProperties;
import io.crawlerbot.crawler.config.ChannelQueue;
import io.crawlerbot.crawler.exceptions.NotSupportBrowserException;
import io.crawlerbot.crawler.messaging.CrawlerService;
import io.crawlerbot.crawler.messaging.MessengerConverter;
import io.crawlerbot.crawler.messaging.queue.QueueConfig;
import io.crawlerbot.crawler.messaging.queue.QueueFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CrawlerTest {

    private static Logger logger = LoggerFactory.getLogger(CrawlerTest.class);
    private Crawler crawler;
    private Channel channel;
    private CrawlerService crawlerService;
    private QueueFactory engine;
    private ApplicationProperties applicationProperties;
    @Before
    public void setup() {
        applicationProperties = new ApplicationProperties();
        QueueConfig config = new QueueConfig();
        config.setAutoAck(false);
        config.setAutoDelete(false);
        config.setExclusive(false);
        config.setDurable(true);
        config.setQueueName(ChannelQueue.CRAWLER_INPUT);
        config.setForceCreateChannel(false);
        config.setForceCreateConnection(false);
        config.setReQueueFailure(true);
        config.setHost(applicationProperties.rabbitMQConfig.getHost());
        config.setUserName(applicationProperties.rabbitMQConfig.getUsername());
        config.setPassword(applicationProperties.rabbitMQConfig.getPassword());
        config.setPort(applicationProperties.rabbitMQConfig.getPort());
        config.setNumberThread(10);

        engine = new QueueFactory(config);
        crawlerService = new CrawlerService(engine);

    }
    private void setupCrawler() {

    }
    @Test
    public void config() {
    }

    @Test
    public void getFetchEngineByLevel() throws MalformedURLException, NotSupportBrowserException {

       /* QueueConfig queueConfig = new QueueConfig();
        queueConfig.setPort(15672);
        engine = new QueueFactory(queueConfig);

        channel = new Channel();
        Set<FetchEngine> fetchEngineSet = new HashSet<>();
        FetchEngine fetchEngine0 = new FetchEngine();
        fetchEngine0.setEngine(Engine.SELENIUM);
        fetchEngine0.setLevel(0);

        fetchEngineSet.add(fetchEngine0);


        FetchEngine fetchEngine2 = new FetchEngine();
        fetchEngine2.setEngine(Engine.HTTP_CLIENT);
        fetchEngine2.setLevel(1);

        fetchEngineSet.add(fetchEngine2);

        channel.setConfigFetchEngines(fetchEngineSet);


        crawlerService = new CrawlerService(engine);
        applicationProperties = new ApplicationProperties();
        crawler = new Crawler(channel,crawlerService,applicationProperties, BrowserName.CHROME, BrowserHost.LOCAL, BrowserOS.MAC);


        System.out.println("Total fetchEgnine:" + channel.getConfigFetchEngines().size());
        System.out.println("channel.getConfigFetchEngines():" + channel.getConfigFetchEngines().iterator().next().getEngine());
        System.out.println("channel.getConfigFetchEngines():" + channel.getConfigFetchEngines().iterator().next().getLevel());

        FetchEngine fetchEngine01 = crawler.getFetchEngineByLevel(0, channel);
        assertNotNull(fetchEngine01);
        assertEquals(fetchEngine01.getEngine(), Engine.SELENIUM);
        FetchEngine fetchEngine02 = crawler.getFetchEngineByLevel(1, channel);
        assertNotNull(fetchEngine02);
        assertEquals(fetchEngine02.getEngine(), Engine.HTTP_CLIENT);*/

        try {
            File file = new File("src/test/resources/baotintuc-vn_suc-khoe.json");
            String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            Channel channel = MessengerConverter.convertStringToChannel(fileContent);
            Crawler crawler = new Crawler(channel, crawlerService, applicationProperties, BrowserName.CHROME, BrowserHost.LOCAL, BrowserOS.MAC);
            crawler.start();

        }catch (Exception ex) {
            logger.info("e.ex: {}", ex);

        } catch (NotSupportBrowserException e) {
            e.printStackTrace();
            logger.info("e.printStackTrace: {}", e);
        }

    }

    @Test
    public void getCrawline() {
    }

    @Test
    public void setCrawline() {
    }

    @Test
    public void getLink() {
    }

    @Test
    public void setLink() {
    }

    @Test
    public void getInputLink() {
    }

    @Test
    public void setInputLink() {
    }


    @Test
    public void getNextLink() throws IOException, NotSupportBrowserException {
        /*
        {"link":{"id":"1539228082703","url":"http://cafebiz.vn/cong-nghe.chn","currentLevel":0},"inputLink":{"allowExternalUrl":false,"archiveLevel":2,"totalLevel":2,"destination":"NEWS_ARTICLE_INDEXER","url":"http://cafebiz.vn/cong-nghe.chn","name":"Công nghệ","postType":"ARTICLE","configFetchEngines":[{"engine":"HTTP_CLIENT","domain":"cafebiz.vn","level":0,"name":"cafebiz.vn fetch engine"},{"engine":"HTTP_CLIENT","domain":"cafebiz.vn","level":1,"name":"cafebiz.vn fetch engine"}],"metas":[{"category":"Công nghệ","channelType":"CRAWL_FEED","countryCode":"vn","languageCode":"vi-vn","logo":"https://4-stc-laban.zdn.vn/largeicons/cafebiz.vn-1414747378.png","name":"Công nghệ","publisherName":"CafeBiz","postType":"ARTICLE","rankingCountry":1,"siteDomain":"cafebiz.vn","siteUrl":"http://cafebiz.vn/","url":"http://cafebiz.vn/cong-nghe.chn","destination":"NEWS_ARTICLE_INDEXER","label":"Công nghệ_cafebiz.vn"}],"siteActionConfigs":[],"configMappings":[{"domain":"cafebiz.vn","label":"cafebiz.vn mapping config","configGroups":[{"currentLevel":0,"name":"cafebiz.vn config group level 0","host":"cafebiz.vn","label":"cafebiz.vn config group level 0","expectResultType":"ARRAY","mappings":[{"name":"url","selector":".contentleft a","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 0","attr":"abs:href","dataType":"STRING"}]},{"currentLevel":1,"name":"cafebiz.vn config group level 1","host":"cafebiz.vn","label":"cafebiz.vn config group level 1","expectResultType":"OBJECT","mappings":[{"name":"thumbnailUrl","selector":"meta[property\u003dimage]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"images","selector":"#mainDetail img","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"abs:src","dataType":"STRING"},{"name":"name","selector":".totalcontentdetail h1","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"text","dataType":"STRING"},{"name":"description","selector":"meta[property\u003ddescription]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"description","selector":"meta[name\u003ddescription]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"keywords","selector":"meta[name\u003dog:keywords]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"author","selector":"meta[property\u003dauthor]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"author","selector":"meta[property\u003dog:author]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"texts","selector":"#mainDetail p","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"text","dataType":"STRING"},{"name":"description","selector":"meta[property\u003dog:description]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"keywords","selector":"meta[property\u003dkeywords]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"author","selector":"meta[name\u003dauthor]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"author","selector":"meta[name\u003dog:author]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"thumbnailUrl","selector":"meta[property\u003dog:image]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[property\u003dog:pubdate]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"thumbnailUrl","selector":"meta[name\u003dog:image]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[property\u003dpubdate]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"thumbnailUrl","selector":"meta[name\u003dimage]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[property\u003darticle:published_time]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[name\u003dpubdate]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[name\u003dog:pubdate]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"keywords","selector":"meta[name\u003dkeywords]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"description","selector":"meta[name\u003dog:description]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[property\u003dpublished_time]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"keywords","selector":"meta[property\u003dog:keywords]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[name\u003darticle:published_time]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"},{"name":"datePublished","selector":"meta[name\u003dpublished_time]","host":"cafebiz.vn","configName":"cafebiz.vn getfeed level 1","attr":"content","dataType":"STRING"}]}]}],"destinationFetchEngines":[{"engine":"HTTP_CLIENT","domain":"cafebiz.vn","level":0,"name":"cafebiz.vn fetch engine"},{"engine":"HTTP_CLIENT","domain":"cafebiz.vn","level":1,"name":"cafebiz.vn fetch engine"}],"destinationMetas":[],"destinationSiteConfigs":[],"destinationConfigMappings":[]},"data":[{"url":["http://cafebiz.vn/ngoai-hang-anh.chn","http://cafebiz.vn/cong-nghe.chn","http://cafebiz.vn/doanh-nghiep-cong-nghe.chn","http://cafebiz.vn/startup.chn","http://cafebiz.vn/khoa-hoc.chn","http://cafebiz.vn/khong-con-chiu-lep-ve-truoc-grab-taxi-truyen-thong-da-lien-ket-lai-de-phan-cong-20181009170524618.chn","http://cafebiz.vn/tren-tay-danh-gia-nhanh-bphone-3-gia-tu-699-trieu-cuoi-cung-nguoi-viet-da-co-mot-chiec-smartphone-dang-de-tu-hao-20181010104914009.chn","http://cafebiz.vn/cuoc-chien-sieu-ung-dung-tai-viet-nam-ram-ro-nhu-grab-lang-le-nhu-now-zalo-va-hoang-so-nhu-go-viet-20181008232501499.chn","http://cafebiz.vn/vi-sao-dan-van-phong-dac-biet-ua-thich-galaxy-note9-20181005165631806.chn","http://cafebiz.vn/dung-thu-yahoo-together-vi-vua-ngay-tro-lai-20181007073541286.chn","http://cafebiz.vn/dat-gia-bphone-3-699-trieu-day-se-la-nhung-doi-thu-ma-bkav-phai-cham-tran-o-phan-khuc-tam-trung-20181005153318712.chn","http://cafebiz.vn/dung-1-tu-chi-ra-su-khac-biet-giua-con-nguoi-va-may-moc-neu-ban-tra-loi-tinh-yeu-thi-sai-bet-20181008172920999.chn","http://cafebiz.vn/nguoi-dan-ong-bat-may-bay-vuot-1200-km-di-lam-moi-ngay-20181007195145663.chn","http://cafebiz.vn/yahoo-bat-ngo-quay-tro-lai-voi-ung-dung-chat-yahoo-together-2018100515145104.chn","http://cafebiz.vn/nhung-thu-dan-bien-mat-vi-su-xuat-hien-cua-dien-thoai-di-dong-20181005184617634.chn","http://cafebiz.vn/thien-nhien-ki-dieu-day-la-nhung-sinh-vat-khien-ban-tin-vao-su-bat-tu-20181005100918948.chn","http://cafebiz.vn/bphone-3-la-chiec-dien-thoai-khong-virus-va-khong-the-bi-danh-cap-20181010103745165.chn","http://cafebiz.vn/bi-thuat-duong-nhan-sac-cua-tu-hy-thai-hau-20181007193822273.chn","http://cafebiz.vn/youtuber-viet-tiet-lo-ve-bphone-3-gia-699-trieu-man-hinh-6-inch-tran-day-snapdragon-636-camera-don-12mp-f-18-chong-nuoc-20181004162624682.chn","http://cafebiz.vn/da-co-dich-vu-giao-hang-tap-hoa-den-tantu-lanh-khi-ban-vang-nha-20181006215359083.chn","http://cafebiz.vn/duoc-dinh-gia-60-ty-usd-samsung-cung-co-vi-tri-thuong-hieu-tv-hang-dau-the-gioi-trong-danh-sach-interbrands-20181011095442612.chn","http://cafebiz.vn/man-ra-mat-bphone-3-se-tron-ven-hon-neu-ceo-nguyen-tu-quang-khong-qua-no-20181011093616565.chn","http://cafebiz.vn/ly-do-bphone-3-khong-xuat-hien-tren-he-thong-the-gioi-di-dong-nhu-truoc-day-nhan-vien-ban-hang-toan-huong-nguoi-dung-den-san-pham-cua-apple-samsung-hay-xiaomi-20181010192948346.chn","http://cafebiz.vn/8k-tv-moi-thu-ban-can-biet-ve-tuong-lai-cua-tv-20181010152045768.chn","http://cafebiz.vn/nong-trai-dau-tien-o-my-hoan-toan-thay-the-con-nguoi-bang-robot-20181010162214612.chn","http://cafebiz.vn/gian-thuong-trung-quoc-loi-dung-chinh-sach-sua-chua-iphone-de-truc-loi-va-khien-apple-thiet-hai-hang-ty-usd-moi-nam-nhu-the-nao-2018101016164119.chn","http://cafebiz.vn/ung-dung-ban-rau-trung-quoc-co-the-duoc-dinh-gia-7-ty-usd-20181010140208705.chn","http://cafebiz.vn/bkav-tuyen-bo-la-nha-san-xuat-dau-tien-tren-the-gioi-dua-ai-vao-camera-tren-bphone-truoc-ca-apple-20181010140239377.chn","http://cafebiz.vn/bat-dong-san-tren-sao-hoa-ra-mat-mau-nha-mini-dep-lung-linh-giua-hoa-tinh-bao-hieu-ngay-dinh-cu-khong-xa-cua-loai-nguoi-20181010110906337.chn","http://cafebiz.vn/kho-hang-amazon-tien-dau-vut-day-khong-can-sap-xep-theo-thu-tu-nhung-lai-hieu-qua-nhat-the-gioi-la-sao-20181010105237478.chn","http://cafebiz.vn/chu-xe-kia-morning-danh-gia-toyota-wigo-pho-ngon-nhung-com-moi-phu-hop-de-an-hang-ngay-20181010100303837.chn","http://cafebiz.vn/day-la-bphone-3-voi-man-hinh-tran-day-chiec-smartphone-khong-cam-nhung-co-tran-that-la-cao-2018101010032954.chn","http://cafebiz.vn/bphone-3-va-bphone-3-pro-lo-dien-truoc-gio-ra-mat-gia-699-va-999-trieu-dong-20181010084114884.chn","http://cafebiz.vn/vietjet-air-nang-tam-tien-ich-cho-khach-hang-bang-thanh-toan-qua-qr-code-20181009172924853.chn","http://cafebiz.vn/vi-sao-google-can-rang-bo-qua-hop-dong-10-ty-usd-voi-lau-nam-goc-20181009160451134.chn","http://cafebiz.vn/93-dan-van-phong-dung-smartphone-cho-cong-viec-lam-sao-de-toi-uu-hieu-qua-2018100911300039.chn","http://cafebiz.vn/so-bi-trung-phat-google-che-giau-mot-loi-bao-mat-nghiem-trong-trong-google-co-tu-3-nam-nay-20181009110156421.chn","http://cafebiz.vn/bat-dong-voi-cong-ty-cu-ky-su-bo-viec-thanh-lap-hang-moi-canh-tranh-truc-tiep-thu-ve-loi-nhuan-toi-hang-chuc-trieu-usd-2018100910410764.chn","http://cafebiz.vn/tong-thu-ky-via-tranh-chap-giua-fpt-va-cmc-chi-la-phan-noi-cua-tang-bang-chim-20181009100913124.chn","http://cafebiz.vn/tu-viec-the-coffee-house-nhan-vai-nghin-don-hang-online-moi-ngay-den-cuoc-chien-dam-mau-tren-thi-truong-giao-do-an-cua-grabfood-now-va-lala-2018100817135664.chn","http://cafebiz.vn/brand/1-cau-chuyen-kinh-doanh.chn","http://cafebiz.vn/giai-phap-cho-nhung-bat-cap-thanh-toan-tien-mat-the-tai-viet-nam-20181001145709582.chn","http://cafebiz.vn/bi-quyet-quan-tri-doanh-nghiep-cua-cac-ong-lon-trong-ky-nguyen-40-20180925170850772.chn","http://cafebiz.vn/cuc-hang-khong-viet-nam-dung-dien-thoai-khi-may-bay-cat-ha-canh-se-bi-phat-3-5-trieu-dong-20181006173011661.chn","http://cafebiz.vn/bloomberg-chip-gian-diep-trung-quoc-duoc-tim-thay-trong-phan-cung-cua-apple-amazon-20181005083547291.chn","http://cafebiz.vn/samsung-vo-dich-khoan-lam-quang-cao-bua-de-dim-hang-doi-thu-ho-vua-chung-minh-dieu-do-20180929081720776.chn","http://cafebiz.vn/bphone-3-se-chinh-thuc-trinh-lang-vao-10-10-toi-20180927101325909.chn"]}]}

         */

        File file = new File("src/test/resources/baotintuc-vn_suc-khoe.json");
        String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        Channel channel = MessengerConverter.convertStringToChannel(fileContent);

        String crawlLineContent = "{\"link\":{\"id\":\"1539228082703\",\"url\":\"http://cafebiz.vn/cong-nghe.chn\",\"currentLevel\":0},\"inputLink\":{\"allowExternalUrl\":false,\"archiveLevel\":2,\"totalLevel\":2,\"destination\":\"NEWS_ARTICLE_INDEXER\",\"url\":\"http://cafebiz.vn/cong-nghe.chn\",\"name\":\"Công nghệ\",\"postType\":\"ARTICLE\",\"configFetchEngines\":[{\"engine\":\"HTTP_CLIENT\",\"domain\":\"cafebiz.vn\",\"level\":0,\"name\":\"cafebiz.vn fetch engine\"},{\"engine\":\"HTTP_CLIENT\",\"domain\":\"cafebiz.vn\",\"level\":1,\"name\":\"cafebiz.vn fetch engine\"}],\"metas\":[{\"category\":\"Công nghệ\",\"channelType\":\"CRAWL_FEED\",\"countryCode\":\"vn\",\"languageCode\":\"vi-vn\",\"logo\":\"https://4-stc-laban.zdn.vn/largeicons/cafebiz.vn-1414747378.png\",\"name\":\"Công nghệ\",\"publisherName\":\"CafeBiz\",\"postType\":\"ARTICLE\",\"rankingCountry\":1,\"siteDomain\":\"cafebiz.vn\",\"siteUrl\":\"http://cafebiz.vn/\",\"url\":\"http://cafebiz.vn/cong-nghe.chn\",\"destination\":\"NEWS_ARTICLE_INDEXER\",\"label\":\"Công nghệ_cafebiz.vn\"}],\"siteActionConfigs\":[],\"configMappings\":[{\"domain\":\"cafebiz.vn\",\"label\":\"cafebiz.vn mapping config\",\"configGroups\":[{\"currentLevel\":0,\"name\":\"cafebiz.vn config group level 0\",\"host\":\"cafebiz.vn\",\"label\":\"cafebiz.vn config group level 0\",\"expectResultType\":\"ARRAY\",\"mappings\":[{\"name\":\"url\",\"selector\":\".contentleft a\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 0\",\"attr\":\"abs:href\",\"dataType\":\"STRING\"}]},{\"currentLevel\":1,\"name\":\"cafebiz.vn config group level 1\",\"host\":\"cafebiz.vn\",\"label\":\"cafebiz.vn config group level 1\",\"expectResultType\":\"OBJECT\",\"mappings\":[{\"name\":\"thumbnailUrl\",\"selector\":\"meta[property\\u003dimage]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"images\",\"selector\":\"#mainDetail img\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"abs:src\",\"dataType\":\"STRING\"},{\"name\":\"name\",\"selector\":\".totalcontentdetail h1\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"text\",\"dataType\":\"STRING\"},{\"name\":\"description\",\"selector\":\"meta[property\\u003ddescription]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"description\",\"selector\":\"meta[name\\u003ddescription]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"keywords\",\"selector\":\"meta[name\\u003dog:keywords]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"author\",\"selector\":\"meta[property\\u003dauthor]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"author\",\"selector\":\"meta[property\\u003dog:author]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"texts\",\"selector\":\"#mainDetail p\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"text\",\"dataType\":\"STRING\"},{\"name\":\"description\",\"selector\":\"meta[property\\u003dog:description]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"keywords\",\"selector\":\"meta[property\\u003dkeywords]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"author\",\"selector\":\"meta[name\\u003dauthor]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"author\",\"selector\":\"meta[name\\u003dog:author]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"thumbnailUrl\",\"selector\":\"meta[property\\u003dog:image]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[property\\u003dog:pubdate]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"thumbnailUrl\",\"selector\":\"meta[name\\u003dog:image]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[property\\u003dpubdate]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"thumbnailUrl\",\"selector\":\"meta[name\\u003dimage]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[property\\u003darticle:published_time]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[name\\u003dpubdate]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[name\\u003dog:pubdate]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"keywords\",\"selector\":\"meta[name\\u003dkeywords]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"description\",\"selector\":\"meta[name\\u003dog:description]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[property\\u003dpublished_time]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"keywords\",\"selector\":\"meta[property\\u003dog:keywords]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[name\\u003darticle:published_time]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"},{\"name\":\"datePublished\",\"selector\":\"meta[name\\u003dpublished_time]\",\"host\":\"cafebiz.vn\",\"configName\":\"cafebiz.vn getfeed level 1\",\"attr\":\"content\",\"dataType\":\"STRING\"}]}]}],\"destinationFetchEngines\":[{\"engine\":\"HTTP_CLIENT\",\"domain\":\"cafebiz.vn\",\"level\":0,\"name\":\"cafebiz.vn fetch engine\"},{\"engine\":\"HTTP_CLIENT\",\"domain\":\"cafebiz.vn\",\"level\":1,\"name\":\"cafebiz.vn fetch engine\"}],\"destinationMetas\":[],\"destinationSiteConfigs\":[],\"destinationConfigMappings\":[]},\"data\":[{\"url\":[\"http://cafebiz.vn/ngoai-hang-anh.chn\",\"http://cafebiz.vn/cong-nghe.chn\",\"http://cafebiz.vn/doanh-nghiep-cong-nghe.chn\",\"http://cafebiz.vn/startup.chn\",\"http://cafebiz.vn/khoa-hoc.chn\",\"http://cafebiz.vn/khong-con-chiu-lep-ve-truoc-grab-taxi-truyen-thong-da-lien-ket-lai-de-phan-cong-20181009170524618.chn\",\"http://cafebiz.vn/tren-tay-danh-gia-nhanh-bphone-3-gia-tu-699-trieu-cuoi-cung-nguoi-viet-da-co-mot-chiec-smartphone-dang-de-tu-hao-20181010104914009.chn\",\"http://cafebiz.vn/cuoc-chien-sieu-ung-dung-tai-viet-nam-ram-ro-nhu-grab-lang-le-nhu-now-zalo-va-hoang-so-nhu-go-viet-20181008232501499.chn\",\"http://cafebiz.vn/vi-sao-dan-van-phong-dac-biet-ua-thich-galaxy-note9-20181005165631806.chn\",\"http://cafebiz.vn/dung-thu-yahoo-together-vi-vua-ngay-tro-lai-20181007073541286.chn\",\"http://cafebiz.vn/dat-gia-bphone-3-699-trieu-day-se-la-nhung-doi-thu-ma-bkav-phai-cham-tran-o-phan-khuc-tam-trung-20181005153318712.chn\",\"http://cafebiz.vn/dung-1-tu-chi-ra-su-khac-biet-giua-con-nguoi-va-may-moc-neu-ban-tra-loi-tinh-yeu-thi-sai-bet-20181008172920999.chn\",\"http://cafebiz.vn/nguoi-dan-ong-bat-may-bay-vuot-1200-km-di-lam-moi-ngay-20181007195145663.chn\",\"http://cafebiz.vn/yahoo-bat-ngo-quay-tro-lai-voi-ung-dung-chat-yahoo-together-2018100515145104.chn\",\"http://cafebiz.vn/nhung-thu-dan-bien-mat-vi-su-xuat-hien-cua-dien-thoai-di-dong-20181005184617634.chn\",\"http://cafebiz.vn/thien-nhien-ki-dieu-day-la-nhung-sinh-vat-khien-ban-tin-vao-su-bat-tu-20181005100918948.chn\",\"http://cafebiz.vn/bphone-3-la-chiec-dien-thoai-khong-virus-va-khong-the-bi-danh-cap-20181010103745165.chn\",\"http://cafebiz.vn/bi-thuat-duong-nhan-sac-cua-tu-hy-thai-hau-20181007193822273.chn\",\"http://cafebiz.vn/youtuber-viet-tiet-lo-ve-bphone-3-gia-699-trieu-man-hinh-6-inch-tran-day-snapdragon-636-camera-don-12mp-f-18-chong-nuoc-20181004162624682.chn\",\"http://cafebiz.vn/da-co-dich-vu-giao-hang-tap-hoa-den-tantu-lanh-khi-ban-vang-nha-20181006215359083.chn\",\"http://cafebiz.vn/duoc-dinh-gia-60-ty-usd-samsung-cung-co-vi-tri-thuong-hieu-tv-hang-dau-the-gioi-trong-danh-sach-interbrands-20181011095442612.chn\",\"http://cafebiz.vn/man-ra-mat-bphone-3-se-tron-ven-hon-neu-ceo-nguyen-tu-quang-khong-qua-no-20181011093616565.chn\",\"http://cafebiz.vn/ly-do-bphone-3-khong-xuat-hien-tren-he-thong-the-gioi-di-dong-nhu-truoc-day-nhan-vien-ban-hang-toan-huong-nguoi-dung-den-san-pham-cua-apple-samsung-hay-xiaomi-20181010192948346.chn\",\"http://cafebiz.vn/8k-tv-moi-thu-ban-can-biet-ve-tuong-lai-cua-tv-20181010152045768.chn\",\"http://cafebiz.vn/nong-trai-dau-tien-o-my-hoan-toan-thay-the-con-nguoi-bang-robot-20181010162214612.chn\",\"http://cafebiz.vn/gian-thuong-trung-quoc-loi-dung-chinh-sach-sua-chua-iphone-de-truc-loi-va-khien-apple-thiet-hai-hang-ty-usd-moi-nam-nhu-the-nao-2018101016164119.chn\",\"http://cafebiz.vn/ung-dung-ban-rau-trung-quoc-co-the-duoc-dinh-gia-7-ty-usd-20181010140208705.chn\",\"http://cafebiz.vn/bkav-tuyen-bo-la-nha-san-xuat-dau-tien-tren-the-gioi-dua-ai-vao-camera-tren-bphone-truoc-ca-apple-20181010140239377.chn\",\"http://cafebiz.vn/bat-dong-san-tren-sao-hoa-ra-mat-mau-nha-mini-dep-lung-linh-giua-hoa-tinh-bao-hieu-ngay-dinh-cu-khong-xa-cua-loai-nguoi-20181010110906337.chn\",\"http://cafebiz.vn/kho-hang-amazon-tien-dau-vut-day-khong-can-sap-xep-theo-thu-tu-nhung-lai-hieu-qua-nhat-the-gioi-la-sao-20181010105237478.chn\",\"http://cafebiz.vn/chu-xe-kia-morning-danh-gia-toyota-wigo-pho-ngon-nhung-com-moi-phu-hop-de-an-hang-ngay-20181010100303837.chn\",\"http://cafebiz.vn/day-la-bphone-3-voi-man-hinh-tran-day-chiec-smartphone-khong-cam-nhung-co-tran-that-la-cao-2018101010032954.chn\",\"http://cafebiz.vn/bphone-3-va-bphone-3-pro-lo-dien-truoc-gio-ra-mat-gia-699-va-999-trieu-dong-20181010084114884.chn\",\"http://cafebiz.vn/vietjet-air-nang-tam-tien-ich-cho-khach-hang-bang-thanh-toan-qua-qr-code-20181009172924853.chn\",\"http://cafebiz.vn/vi-sao-google-can-rang-bo-qua-hop-dong-10-ty-usd-voi-lau-nam-goc-20181009160451134.chn\",\"http://cafebiz.vn/93-dan-van-phong-dung-smartphone-cho-cong-viec-lam-sao-de-toi-uu-hieu-qua-2018100911300039.chn\",\"http://cafebiz.vn/so-bi-trung-phat-google-che-giau-mot-loi-bao-mat-nghiem-trong-trong-google-co-tu-3-nam-nay-20181009110156421.chn\",\"http://cafebiz.vn/bat-dong-voi-cong-ty-cu-ky-su-bo-viec-thanh-lap-hang-moi-canh-tranh-truc-tiep-thu-ve-loi-nhuan-toi-hang-chuc-trieu-usd-2018100910410764.chn\",\"http://cafebiz.vn/tong-thu-ky-via-tranh-chap-giua-fpt-va-cmc-chi-la-phan-noi-cua-tang-bang-chim-20181009100913124.chn\",\"http://cafebiz.vn/tu-viec-the-coffee-house-nhan-vai-nghin-don-hang-online-moi-ngay-den-cuoc-chien-dam-mau-tren-thi-truong-giao-do-an-cua-grabfood-now-va-lala-2018100817135664.chn\",\"http://cafebiz.vn/brand/1-cau-chuyen-kinh-doanh.chn\",\"http://cafebiz.vn/giai-phap-cho-nhung-bat-cap-thanh-toan-tien-mat-the-tai-viet-nam-20181001145709582.chn\",\"http://cafebiz.vn/bi-quyet-quan-tri-doanh-nghiep-cua-cac-ong-lon-trong-ky-nguyen-40-20180925170850772.chn\",\"http://cafebiz.vn/cuc-hang-khong-viet-nam-dung-dien-thoai-khi-may-bay-cat-ha-canh-se-bi-phat-3-5-trieu-dong-20181006173011661.chn\",\"http://cafebiz.vn/bloomberg-chip-gian-diep-trung-quoc-duoc-tim-thay-trong-phan-cung-cua-apple-amazon-20181005083547291.chn\",\"http://cafebiz.vn/samsung-vo-dich-khoan-lam-quang-cao-bua-de-dim-hang-doi-thu-ho-vua-chung-minh-dieu-do-20180929081720776.chn\",\"http://cafebiz.vn/bphone-3-se-chinh-thuc-trinh-lang-vao-10-10-toi-20180927101325909.chn\"]}]}";
        CrawlLine crawlLine = MessengerConverter.convertStringToCrawlLine(crawlLineContent);
        Crawler crawler = new Crawler(channel, crawlerService, applicationProperties, BrowserName.CHROME, BrowserHost.LOCAL, BrowserOS.MAC);

        logger.info("crawlLine: {}", crawlLine.getData());
        //List<CrawlLine> crawlLineList = crawler.getNextLinks(crawlLine);

        //logger.info("crawlLineList: {}", crawlLineList.size());


    }
    @Test
    public void fetch() {
    }

    @Test
    public void parse() {
    }

    @Test
    public void index() {
    }

    @Test
    public void upload() {
    }

    @Test
    public void nextLinks() {
    }

    @Test
    public void report() {
    }

    @Test
    public void start() {
    }

    @Test
    public void fetchByHttpClient() throws MalformedURLException, NotSupportBrowserException {
        QueueConfig queueConfig = new QueueConfig();
        queueConfig.setPort(15672);
        engine = new QueueFactory(queueConfig);

        channel = new Channel();
        Set<FetchEngine> fetchEngineSet = new HashSet<>();
        FetchEngine fetchEngine0 = new FetchEngine();
        fetchEngine0.setEngine(Engine.HTTP_CLIENT);
        fetchEngine0.setLevel(0);

        fetchEngineSet.add(fetchEngine0);


        FetchEngine fetchEngine2 = new FetchEngine();
        fetchEngine2.setEngine(Engine.HTTP_CLIENT);
        fetchEngine2.setLevel(1);

        fetchEngineSet.add(fetchEngine2);

        channel.setConfigFetchEngines(fetchEngineSet);
        crawlerService = new CrawlerService(engine);
        crawler = new Crawler(channel,crawlerService,applicationProperties, BrowserName.CHROME, BrowserHost.LOCAL, BrowserOS.MAC);

        crawler = crawler.fetchByHttpClient("https://techcrunch.com");
        assertNotNull(crawler.getCrawlerResult().getDocument());
        System.out.println("title:" + crawler.getCrawlerResult().getDocument().title());
        assertEquals(crawler.getCrawlerResult().getDocument().title(), "TechCrunch – Startup and Technology News");

    }
}