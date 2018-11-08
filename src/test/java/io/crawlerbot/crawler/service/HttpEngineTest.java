package io.crawlerbot.crawler.service;

import com.crawler.config.domain.CrawlerResult;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import sun.net.www.http.HttpClient;

import static org.junit.Assert.*;

public class HttpEngineTest {

    private HttpEngine httpEngine;
   @Before
   public void setup() {
       httpEngine = new HttpEngine(null);
   }


    @Test
    public void doFetch() {
        CrawlerResult crawlerResult = httpEngine.doFetch("https://edition.cnn.com/");
        assertNotNull(crawlerResult);
        Document document = crawlerResult.getDocument();
        assertNotNull(document);
        assertEquals(document.title(), "CNN International - Breaking News, US News, World News and Video");
        System.out.println(document.title());


        CrawlerResult crawlerResult2 = httpEngine.doFetch("http://t.co/I5YYd9tddw");
        assertNotNull(crawlerResult2);
        Document document2 = crawlerResult2.getDocument();
        assertNotNull(document2);
        assertEquals(document2.title(), "Manning | Home");
        System.out.println(document2.title());



    }

    @Test
    public void getRedirectUrl() {
       String url = httpEngine.getRedirectUrl("http://t.co/I5YYd9tddw");
       System.out.println(url);
       assertEquals(url, "https://www.manning.com/");
    }
}