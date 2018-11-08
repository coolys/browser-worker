package io.crawlerbot.crawler.service;

import com.crawler.config.domain.*;
import com.crawler.config.domain.Action;
import com.crawler.config.domain.FetchEngine;
import com.crawler.config.domain.enumeration.*;
import com.google.common.base.Function;
import com.news.service.Scraper;
import com.news.service.utils.ParseLinkUtils;
import io.crawlerbot.crawler.config.ApplicationProperties;
import io.crawlerbot.crawler.exceptions.NotSupportBrowserException;
import io.crawlerbot.crawler.logger.LoggerFactory;
import io.crawlerbot.crawler.messaging.CrawlerService;
import io.crawlerbot.crawler.utils.IDConverter;
import io.crawlerbot.crawler.utils.ThreadUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class Crawler implements CrawlerEngine {
    private final LoggerFactory logger = new LoggerFactory(Crawler.class);
    private WebData input;
    private Channel inputLink;
    private BrowserName browserName;
    private BrowserHost browserHost;
    private BrowserOS browserOS;
    private CrawlerResult crawlerResult;
    private CrawlerService crawlerService;
    private ApplicationProperties applicationProperties;
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
        FetchEngine fetchEngine = getFetchEngineByLevel(0,inputLink);

        if (fetchEngine.getEngine().equals(Engine.SELENIUM)) {
            config(browserName, browserHost, browserOS);
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
            config(browserName, browserHost, browserOS);
        }
    }


    public void config(BrowserName browserName, BrowserHost browserHost, BrowserOS browserOS) throws NotSupportBrowserException, MalformedURLException {

        this.browserName = BrowserName.valueOf(applicationProperties.systemConfig.getBrowserName());
        this.browserHost = BrowserHost.valueOf(applicationProperties.systemConfig.getBrowserHost());
        this.browserOS = BrowserOS.valueOf(applicationProperties.systemConfig.getBrowserOS());

        WebDriver browser;
        Capabilities chromeCapabilities;

        if (this.browserName == null) this.browserName = BrowserName.CHROME;
        if (this.browserName.equals(BrowserName.CHROME)) {
            chromeCapabilities = DesiredCapabilities.chrome();
        } else if (this.browserName.equals(BrowserName.FIREFOX)) {
            chromeCapabilities = DesiredCapabilities.firefox();
        } else {
            throw new NotSupportBrowserException("The input browser is not valid, please input chrome or firefox!");
        }

        if (browserHost == null) browserHost = BrowserHost.LOCAL;
        if (browserHost.equals(BrowserHost.LOCAL)) {
            String os = "mac";
            if (browserOS.equals(BrowserOS.UBUNTU)) {
                os = "linux";
            }
            if (browserOS.equals(BrowserOS.MAC)) {
                os = "mac";
            }
            if (browserOS.equals(BrowserOS.WINDOWS)) {
                os = "window";
            }
            if (this.browserName.equals(BrowserName.CHROME)) {
                System.setProperty("webdriver.chrome.driver", "selenium/" + os + "/chromedriver");
                browser = new ChromeDriver();
            } else {
                System.setProperty("webdriver.firefox.driver", "selenium/" + os + "/gekodriver");
                browser = new FirefoxDriver();
            }
        } else {
            //logger.info("start browser with url : {}", this.applicationProperties.getSelenium().getUrl());
            browser = new RemoteWebDriver(new URL(this.applicationProperties.getSelenium().getUrl()), chromeCapabilities);
        }
        this.browser = browser;

        //logger.info("[] config browser: {}", this.toString());

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
        return "Crawler{" +
                "input=" + input +

                ", inputlink=" + inputLink +
                ", browserName=" + browserName +
                ", browserHost=" + browserHost +
                ", crawlerResult=" + crawlerResult +
                ", crawlerService=" + crawlerService +
                ", browser=" + browser +
                '}';
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


    private WebDriver browser;

    private boolean quitBroweser() {
        if (this.browser == null) return true;
        if (this.browser != null) {
            this.browser.quit();
            return true;
        }
        return true;
    }


    private Crawler bindingFetchResult(String url) {
        String currentUrl = this.browser.getCurrentUrl();
        String html = this.browser.getPageSource();
        Document document = Jsoup.parse(html);
        document.setBaseUri(currentUrl);

        CrawlerResult result = new CrawlerResult();
        result.setRequestUrl(url);
        result.setCurrentUrl(currentUrl);
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
        if (channel == null || channel.getConfigFetchEngines() == null || channel.getConfigFetchEngines().size() == 0)
            return getDefaultFetchEngine(level);
        Set<FetchEngine> fetchEngines = channel.getConfigFetchEngines();
        if (fetchEngines == null || fetchEngines.size() == 0)
            return getDefaultFetchEngine(level);
        for (FetchEngine fetchEngine : fetchEngines) {
            if (fetchEngine.getLevel() == level) {
                //logger.info("fetchEngine by level:{} detail:{}", level, fetchEngine);
                return fetchEngine;
            }
        }
        return getDefaultFetchEngine(level);
    }

    private Crawler fetchBySelenium(String url, Integer currentLevel) {
        try {
            this.browser.get(url);
            ThreadUtils.sleep(2L);
            bindingFetchResult(url);
            List<Action> actions = InputLinkHelper.getSiteActions(inputLink, currentLevel);


            if (actions == null || actions.size() == 0) {
                quitBroweser();
                this.parse(false).index().nextLinks().report();
                return this;
            }

            for (Action siteAction : actions) {

                if (siteAction.getAction() != null && siteAction.getAction().equals(ActionType.SCROLL)) {
                    // waiting until done action
                    Integer totalActions = siteAction.getTotalActions();
                    for (Integer from = 0; from < totalActions; from++) {

                        doScroll(url, totalActions);
                    }
                    bindingFetchResult(url);
                    this.parse(false).index().nextLinks().report();
                } else if (siteAction.getAction() != null && siteAction.getAction().equals(ActionType.CLICK)) {
                    if (siteAction.getGetContent().equals(SeleniumActionGetContent.DONE_ACTION)) {
                        Integer totalActions = siteAction.getTotalActions();
                        for (Integer from = 0; from < totalActions; from++) {
                            doClick(url, totalActions, siteAction);
                        }
                        bindingFetchResult(url);
                        boolean splitData = false;
                        this.parse(splitData).index().nextLinks().report();


                    } else if (siteAction.getGetContent().equals(SeleniumActionGetContent.EACH_ACTION)) {

                        Integer totalActions = siteAction.getTotalActions();
                        if (totalActions != -1) {

                            for (Integer from = 0; from < totalActions; from++) {
                                WebElement loadMoreButton = getLoadMoreButton(siteAction);
                                if (loadMoreButton != null && loadMoreButton.isDisplayed()) {
                                    doClick(url, totalActions, siteAction);
                                    bindingFetchResult(url);
                                    boolean splitData = true;
                                    this.parse(splitData).index().nextLinks().report();
                                }
                            }
                        } else {
                            while (true) {
                                WebElement loadMoreButton = getLoadMoreButton(siteAction);
                                if (loadMoreButton != null && loadMoreButton.isDisplayed()) {
                                    doClick(url, totalActions, siteAction);
                                    bindingFetchResult(url);
                                    boolean splitData = true;
                                    this.parse(splitData).index().nextLinks().report();
                                } else {
                                    break;
                                }
                            }
                        }

                    }
                }
            }
            quitBroweser();
            return this;
        } catch (Exception ex) {
            return this;
        }
    }

    public Crawler fetchByHttpClient(String url) {
        HttpEngine httpEngine = new HttpEngine(null);
        CrawlerResult crawlerResult = httpEngine.doFetch(url);
        ////logger.info("fetchByHttpClient: {}", crawlerResult.getDocument().html());
        this.setCrawlerResult(crawlerResult);
        return this;
    }

    @Override
    public Crawler fetch() {
        try {
            logger.info("start fetch channel: {}, with url:", this.getCrawline().getChannel().getName(), this.getCrawline().getUrl());

            WebData crawlLine = this.getCrawline();
            String url = crawlLine.getUrl();
            Channel inputLink =crawlLine.getChannel();
            Integer currentLevel = crawlLine.getLevel();
            FetchEngine fetchEngine = getFetchEngineByLevel(currentLevel, inputLink);
            if (fetchEngine.getEngine().equals(Engine.SELENIUM)) {
                return fetchBySelenium(url, currentLevel);
            } else {
                fetchByHttpClient(url);
                this.parse(false).index().nextLinks().report();
                return this;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return this;
        }
    }

    private WebData getScrapeData(WebData currentCrawlerInput, Document htmlDoc, boolean splitData) {
        WebData currentWebData = scraper.extractHtml(htmlDoc,currentCrawlerInput.getUrl(), currentCrawlerInput.getLevel());
        currentCrawlerInput.updateData(currentWebData.getData());
        currentCrawlerInput.setMeta(currentWebData.getMeta());
        currentCrawlerInput.setSemantic(currentWebData.getSemantic());


        return currentCrawlerInput;
    }


    @Override
    public Crawler parse(boolean splitData) {
        try {
            WebData crawlLine = this.getCrawline();
            CrawlerResult crawlerResult = this.getCrawlerResult();
            Document document = crawlerResult.getDocument();
            crawlLine = getScrapeData(crawlLine, document, splitData);
            this.setCrawline(crawlLine);

            logger.info("parse url:", this.getCrawline().getUrl());

            return this;
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.info("parse ex: {}", ex);
            return this;
        }
    }

    private String getCrawlineDomain(WebData crawlLine) {
        if (crawlLine == null || crawlLine.getUrl() == null || crawlLine.getChannel().getMetas() == null || crawlLine.getChannel().getMetas().size() == 0)
            return "";
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

            FileStoreService.writeJsonFile(jsonFile, crawlLine);
            FileStoreService.writeHtmlToFile(this.getCrawlerResult().getDocument().html(), htmlFile, domain,crawlLine);
            logger.info("index jsonFile: {}, html file:", jsonFile, htmlFile);
            Integer currentLevel = crawlLine.getLevel();
            if (currentLevel + 1 == crawlLine.getChannel().getArchiveLevel()) {
                logger.info("send to index:", this.getCrawline().getUrl());
                crawlerService.sendToIndexing(crawlLine);
            }

            return this;
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.info("indexing exception ex: {}", ex);
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
            Channel inputLink = crawlLine.getChannel();
            if (crawlLine.getLevel() + 1 >= inputLink.getTotalLevel()) return this;
            List<WebData> nextLinks = ParseLinkUtils.parseNextLink(crawlLine, crawlLine.getLevel(), crawlLine.getChannel());

            logger.info("total next link from url: {} is:", this.getCrawline().getUrl(), nextLinks.size());
            int total = 0;
            for (WebData nextInput : nextLinks) {
                String domain = getCrawlineDomain(crawlLine);
                String id = getCrawlineId(crawlLine);
                String channelName = inputLink.getName();
                String resultFileName = domain + "_" + channelName + "_next_link_" + id + ".json";
                FileStoreService.writeLocalFile(resultFileName, nextInput);
                if (crawlerService != null) {
                    logger.info("crawl next link:", nextInput.getUrl());
                    crawlerService.startCrawlNextLink(nextInput);
                    total++;
                }
            }
            logger.info("send total to queue from channel: {}, url: {}, total send: ", this.getCrawline().getChannel().getName(), this.getCrawline().getUrl(), total);

            return this;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("send next link exception:", ex.toString());
            //logger.info("nextLinks Exception ex: {}", ex);
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
            //logger.info("start exception :%s", ex.toString());
            return this;
        }
    }


    private void doScroll(String url, Integer totalActions) {

        ThreadUtils.sleep(2000L);
        int total = 0;
        do {

            ThreadUtils.sleep(3000L);
            ((JavascriptExecutor) this.browser)
                    .executeScript("window.scrollTo(0, 80000)");
            total++;
        }
        while (total < totalActions);
    }

    private WebElement getLoadMoreButton(Action fetchAction) {
        final String cssSelector = fetchAction.getSelector();
        Wait<WebDriver> stubbornWait = new FluentWait<>(this.browser)
                .withTimeout(10, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        WebElement loadMoreButton = stubbornWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.cssSelector(cssSelector));
            }
        });
        return loadMoreButton;
    }

    private void doClick(String url, Integer totalActions, Action fetchAction) {
        try {
            //logger.info("[] start do click next");
            WebElement loadMoreButton = getLoadMoreButton(fetchAction);
            ((JavascriptExecutor) this.browser).executeScript("arguments[0].scrollIntoView(true);", loadMoreButton);
            ThreadUtils.sleep(500L);
            if (loadMoreButton != null) {
                loadMoreButton.click();
                ThreadUtils.sleep(3000L);
            }
        } catch (Exception ex) {
            //logger.info("[] click event exception: {}", ex.toString());
        }
    }

    private void doClicks(String url, Integer totalActions, Action fetchAction) {

        final String cssSelector = fetchAction.getSelector();
        ThreadUtils.sleep(3000L);
        while (true) {
            Wait<WebDriver> stubbornWait = new FluentWait<>(this.browser)
                    .withTimeout(10, TimeUnit.SECONDS)
                    .pollingEvery(5, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);

            WebElement loadMoreButton = stubbornWait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.cssSelector(cssSelector));
                }
            });
            if (loadMoreButton != null) {
                loadMoreButton.click();
                ThreadUtils.sleep(3000L);
            } else {
                break;
            }
        }

    }
}
