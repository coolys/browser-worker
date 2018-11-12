package io.crawlerbot.crawler.service;

import com.crawler.config.domain.*;
import com.crawler.config.domain.FetchEngine;
import com.crawler.config.domain.enumeration.*;
import com.news.service.Scraper;
import com.news.service.client.SeleniumEngine;
import com.news.service.utils.MappingUtils;
import com.news.service.utils.ParseLinkUtils;
import io.crawlerbot.crawler.config.ApplicationProperties;
import io.crawlerbot.crawler.exceptions.NotSupportBrowserException;
import io.crawlerbot.crawler.logger.LoggerFactory;
import io.crawlerbot.crawler.messaging.CrawlerService;
import io.crawlerbot.crawler.utils.IDConverter;
import org.jsoup.nodes.Document;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Crawler implements CrawlerEngine {

    private final LoggerFactory logger = new LoggerFactory(Crawler.class);
    private WebData input;
    private Channel inputLink;
    private BrowserName browserName;
    private BrowserHost browserHost;
    private BrowserOS browserOS;
    private String remoteURL;
    private CrawlerResult crawlerResult;
    private final CrawlerService crawlerService;
    private final ApplicationProperties applicationProperties;
    Scraper scraper = new Scraper();

    public Crawler(Channel inputLink, CrawlerService crawlerService, ApplicationProperties applicationProperties, BrowserName browserName, BrowserHost browserHost, BrowserOS browserOS) throws MalformedURLException, NotSupportBrowserException {
        this.inputLink = inputLink;
        WebData crawlLine = new WebData();
        crawlLine.setChannel(inputLink);
        crawlLine.setUrl(inputLink.getUrl());
        crawlLine.setLevel(0);
        this.setCrawline(crawlLine);
        this.crawlerService = crawlerService;
        this.applicationProperties = applicationProperties;
        FetchEngine fetchEngine = getFetchEngineByLevel(0, inputLink);

        if (fetchEngine.getEngine().equals(Engine.SELENIUM)) {
            config();
        }

    }

    public Crawler(WebData crawlLine, CrawlerService crawlerService, ApplicationProperties applicationProperties, BrowserName browserName, BrowserHost browserHost, BrowserOS browserOS) throws MalformedURLException, NotSupportBrowserException {
        this.input = crawlLine;
        this.setCrawline(crawlLine);
        this.inputLink = crawlLine.getChannel();
        this.crawlerService = crawlerService;
        this.applicationProperties = applicationProperties;
        this.setCrawline(crawlLine);
        FetchEngine fetchEngine = getFetchEngineByLevel(crawlLine.getLevel(), crawlLine.getChannel());
        if (fetchEngine.getEngine().equals(Engine.SELENIUM)) {
            config();
        }
    }

    public void config() throws NotSupportBrowserException, MalformedURLException {

        this.browserName = BrowserName.valueOf(applicationProperties.systemConfig.getBrowserName());
        this.browserHost = BrowserHost.valueOf(applicationProperties.systemConfig.getBrowserHost());
        this.browserOS = BrowserOS.valueOf(applicationProperties.systemConfig.getBrowserOS());
        this.remoteURL = this.applicationProperties.getSelenium().getUrl();

    }

    public CrawlerResult getCrawlerResult() {
        return crawlerResult;
    }

    public void setCrawlerResult(CrawlerResult crawlerResult) {
        this.crawlerResult = crawlerResult;
    }

    public WebData getCrawline() {
        return this.input;
    }

    public void setCrawline(WebData input) {
        this.input = input;
    }

    public Channel getInputLink() {
        return inputLink;
    }

    public void setInputLink(Channel inputLink) {
        this.inputLink = inputLink;
    }

    @Override
    public String toString() {
        return "Crawler{"
                + "input=" + input
                + ", inputlink=" + inputLink
                + ", browserName=" + browserName
                + ", browserHost=" + browserHost
                + ", crawlerResult=" + crawlerResult
                + ", crawlerService=" + crawlerService                
                + '}';
    }

    public class CrawlerDocument {

        private Document document;

        public CrawlerDocument(Document document) {
            this.document = document;
        }

        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
        }
    }
    private Crawler bindingFetchResult(Document document, String url) {
        CrawlerResult result = new CrawlerResult();
        result.setRequestUrl(url);
        result.setCurrentUrl(url);
        result.setDocument(document);
        this.setCrawlerResult(result);
        return this;

    }

    private FetchEngine getDefaultFetchEngine(Integer level) {
        FetchEngine fetchEngine = new FetchEngine();
        fetchEngine.setDocType(DocType.HTML);
        fetchEngine.setEngine(Engine.SELENIUM);
        fetchEngine.setLevel(level);
        return fetchEngine;
    }

    public FetchEngine getFetchEngineByLevel(Integer level, Channel channel) {
        if (channel == null || channel.getConfigFetchEngines() == null || channel.getConfigFetchEngines().isEmpty()) {
            return getDefaultFetchEngine(level);
        }
        Set<FetchEngine> fetchEngines = channel.getConfigFetchEngines();
        if (fetchEngines == null || fetchEngines.isEmpty()) {
            return getDefaultFetchEngine(level);
        }
        for (FetchEngine fetchEngine : fetchEngines) {
            if (Objects.equals(fetchEngine.getLevel(), level)) {
                return fetchEngine;
            }
        }
        return getDefaultFetchEngine(level);
    }

    public Crawler fetchByHttpClient(String url) {
        HttpEngine httpEngine = new HttpEngine(null);
        CrawlerResult currentResult = httpEngine.doFetch(url);
        this.setCrawlerResult(currentResult);
        return this;
    }

    @Override
    public Crawler fetch() {
        try {
            logger.info("start fetch channel: {}, with url:", this.getCrawline().getChannel().getName(), this.getCrawline().getUrl());

            WebData crawlLine = this.getCrawline();
            String url = crawlLine.getUrl();
            Channel currentChannel = crawlLine.getChannel();
            Integer currentLevel = crawlLine.getLevel();
            FetchEngine fetchEngine = getFetchEngineByLevel(currentLevel, currentChannel);
            if (fetchEngine.getEngine().equals(Engine.SELENIUM)) {
                Document document = new SeleniumEngine()
                        .config(this.browserName, this.browserHost, this.browserOS, this.remoteURL)
                        .fetch(url, currentLevel, null)
                        .getCurrentDocument();
                bindingFetchResult(document, url);
                this.parse(false).index().nextLinks().report();
                return this;
            } else {
                fetchByHttpClient(url);
                this.parse(false).index().nextLinks().report();
                return this;
            }
        } catch (MalformedURLException ex) {
            return this;
        }
    }

    private WebData getScrapeData(WebData currentCrawlerInput, Document htmlDoc, boolean splitData) {
        List<Mapping> mappings = MappingUtils.getMappingByLevel(currentCrawlerInput.getChannel(), currentCrawlerInput.getLevel());  
        WebData currentWebData = scraper.extractHtml(htmlDoc, currentCrawlerInput.getUrl(), currentCrawlerInput.getLevel(), currentCrawlerInput.getChannel(), mappings);       
        currentCrawlerInput.updateData(currentWebData.getData());
        currentCrawlerInput.setMeta(currentWebData.getMeta());
        currentCrawlerInput.setSemantic(currentWebData.getSemantic());
        return currentCrawlerInput;
    }

    @Override
    public Crawler parse(boolean splitData) {
        try {
            WebData crawlLine = this.getCrawline();
            CrawlerResult result = this.getCrawlerResult();
            Document document = result.getDocument();
            crawlLine = getScrapeData(crawlLine, document, splitData);
            this.setCrawline(crawlLine);
            logger.info("parse url:", this.getCrawline().getUrl());
            return this;
        } catch (Exception ex) {
            return this;
        }
    }

    private String getCrawlineDomain(WebData crawlLine) {
        if (crawlLine == null || crawlLine.getUrl() == null || crawlLine.getChannel().getMetas() == null || crawlLine.getChannel().getMetas().isEmpty()) {
            return "";
        }
        Meta meta = crawlLine.getChannel().getMetas().iterator().next();
        return meta.getSiteDomain();
    }

    private String getCrawlineId(WebData crawlLine) {
        return IDConverter.decode(crawlLine.getUrl()) + "";

    }

    @Override
    public Crawler index() {
        try {
            WebData crawlLine = this.getCrawline();
            String id = getCrawlineId(crawlLine);
            String channelName = crawlLine.getChannel().getName();
            String domain = getCrawlineDomain(crawlLine);
            String fileName = domain + "_" + channelName + "_result_level_" + crawlLine.getLevel() + "_link_" + id;
            String jsonFile = fileName + ".json";
            String htmlFile = fileName + ".html";
            Meta meta = crawlLine.getChannel().getMetas().iterator().next();
            FileStoreService.writeJsonFile(jsonFile, crawlLine);
            FileStoreService.writeHtmlToFile(this.getCrawlerResult().getDocument().html(), htmlFile, domain, meta.getLanguageCode());
            logger.info("index jsonFile: {}, html file:", jsonFile, htmlFile);
            Integer currentLevel = crawlLine.getLevel();
            if (currentLevel + 1 == crawlLine.getChannel().getArchiveLevel()) {
                logger.info("send to index:", this.getCrawline().getUrl());
                crawlerService.sendToIndexing(crawlLine);
            }

            return this;
        } catch (Exception ex) {
            logger.info("indexing exception ex: {}", ex);
            return this;
        }
    }

    @Override
    public Crawler upload() {
        return null;
    }

    @Override
    public Crawler nextLinks() {
        try {

            WebData crawlLine = this.getCrawline();
            Channel currentChannel = crawlLine.getChannel();
            if (crawlLine.getLevel() + 1 >= currentChannel.getTotalLevel()) {
                return this;
            }
            List<WebData> nextLinks = ParseLinkUtils.parseNextLink(crawlLine, crawlLine.getLevel(), crawlLine.getChannel());

            logger.info("total next link from url: {} is:", this.getCrawline().getUrl(), nextLinks.size());
            int total = 0;
            total = nextLinks.stream().map((nextInput) -> {
                String domain = getCrawlineDomain(crawlLine);
                String id = getCrawlineId(crawlLine);
                String channelName = currentChannel.getName();
                String resultFileName = domain + "_" + channelName + "_next_link_" + id + ".json";
                FileStoreService.writeLocalFile(resultFileName, nextInput);
                return nextInput;
            }).filter((nextInput) -> (crawlerService != null)).map((nextInput) -> {
                logger.info("crawl next link:", nextInput.getUrl());
                return nextInput;
            }).map((nextInput) -> {
                crawlerService.startCrawlNextLink(nextInput);
                return nextInput;
            }).map((_item) -> 1).reduce(total, Integer::sum);
            logger.info("send total to queue from channel: {}, url: {}, total send: ", this.getCrawline().getChannel().getName(), this.getCrawline().getUrl(), total);
            return this;
        } catch (Exception ex) {
            logger.info("send next link exception:", ex.toString());
            return this;
        }
    }

    @Override
    public Crawler report() {
        return null;
    }

    @Override
    public Crawler start() {
        try {
            this.fetch();
            return this;
        } catch (Exception ex) {
            logger.info("start exception :%s", ex.toString());
            return this;
        }
    }
}
