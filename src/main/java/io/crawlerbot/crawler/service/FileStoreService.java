package io.crawlerbot.crawler.service;

import com.crawler.config.domain.*;
import com.github.jsonldjava.utils.JsonUtils;
import com.github.slugify.Slugify;
import com.google.gson.Gson;
import com.news.service.domain.DataFeed;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public class FileStoreService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStoreService.class);
    // read from properties
    private static final String LOGCAL_PATH = "/var/log/data/";

    public String getClassPath(String fileName) {
        //ClassLoader classLoader = getClass().getClassLoader();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        return rootPath + "/" + fileName;
    }
    public CrawlLine readLocalCrawline(String fileName) {
        Gson gson = new Gson();
        CrawlLine result = null;
        BufferedReader br = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            br = new BufferedReader(new FileReader(getClassPath(fileName)));
            result = gson.fromJson(br, CrawlLine.class);
            if (result != null) {
               LOGGER.info("result: {}", result);
            }
        } catch (FileNotFoundException e) {
            return result;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block

                }
            }
        }
        //logger.info("channel is : {}", result);
        return result;
    }
    public Channel readLocalChannel(String fileName) {

        Gson gson = new Gson();
        Channel result = null;
        BufferedReader br = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            br = new BufferedReader(new FileReader(getClassPath(fileName)));
            result = gson.fromJson(br, Channel.class);
            if (result != null) {
                System.out.print(result);
            }
        } catch (FileNotFoundException e) {
            return result;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block

                }
            }
        }
        //logger.info("channel is : {}", result);
        return result;

    }

    public static boolean writeHtmlToFile(String content, String fileName, String domain) {
        try {            
            String domainSlug = new Slugify().slugify(domain);
            File file = new File("/var/log/data/"  +domainSlug +"/" + fileName);
            FileUtils.writeStringToFile(file, content, "utf-8");            
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
     public static boolean writeHtmlToFile(String content, String fileName, String domain, String languageCode) {
        try {
            
            String domainSlug = new Slugify().slugify(domain);
            File file = new File("/var/log/data/"  +domainSlug +"/"  + languageCode +"/" + fileName);
            FileUtils.writeStringToFile(file, content, "utf-8");            
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean writeHtmlToFile(String content, String fileName, String domain, WebData webData) {
        try {            
            Meta meta = webData.getChannel().getMetas().iterator().next();
            String domainSlug = new Slugify().slugify(domain);
            File file = new File("/var/log/data/"  +domainSlug +"/" + meta.getLanguageCode() + "/" + fileName);
            FileUtils.writeStringToFile(file, content, "utf-8");            
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static String writeJsonFile(String fileName, WebData resultData) {
        try {            
            Meta meta = resultData.getChannel().getMetas().iterator().next();
            String domainSlug =  new Slugify().slugify(meta.getSiteDomain());
            FileContent fileContent = new FileContent();
            fileContent.setContent(JsonUtils.toPrettyString(resultData));
            File file = new File(LOGCAL_PATH + domainSlug + "/"+  meta.getLanguageCode() +"/" + fileName);
            FileUtils.writeStringToFile(file, fileContent.getContent(), "utf-8");
            return fileName;
        } catch (IOException ex) {            
            return null;
        }
    }

    public static String writeJsonFile(String fileName, DataFeed resultData) {
        try {            
            String domainSlug =  new Slugify().slugify(resultData.getPublisher().getDomain());
            FileContent fileContent = new FileContent();
            fileContent.setContent(JsonUtils.toPrettyString(resultData));
            File file = new File(LOGCAL_PATH + domainSlug + "/"+  fileName);
            FileUtils.writeStringToFile(file, fileContent.getContent(), "utf-8");            
            return fileName;
        } catch (IOException ex) {            
            return null;
        }
    }

    public static String writeLocalFile(String fileName, WebData resultData) {
        try {            
            Meta meta = resultData.getChannel().getMetas().iterator().next();
            String domainSlug =  new Slugify().slugify(meta.getSiteDomain());
            FileContent fileContent = new FileContent();
            fileContent.setContent(JsonUtils.toPrettyString(resultData));
            File file = new File(LOGCAL_PATH + domainSlug + "/"+  fileName);
            FileUtils.writeStringToFile(file, fileContent.getContent(), "utf-8");            
            return fileName;
        } catch (IOException ex) {            
            return null;
        }
    }

}
