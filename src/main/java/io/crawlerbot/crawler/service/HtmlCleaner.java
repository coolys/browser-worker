package io.crawlerbot.crawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlCleaner {
    public static Document removeElements(Document detailDocument, String selector) {
        try {
            Elements elements = detailDocument.select(selector);
            if (elements == null || elements.size() == 0) return detailDocument;
            for (Element element : elements) {
                System.out.println("remove element:" + element.tag());
                if (element.select("img") == null) {
                    element.remove();
                }
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }
    }

    public static Document removeElementsByListSelectors(Document detailDocument, String selectors) {
        if (selectors == null || selectors == "") return detailDocument;
        try {
            List<String> removeTags = Arrays.asList(selectors.split(","));
            System.out.println("removeElementsByListSelectors:" + removeTags.size());
            for (String tag : removeTags) {
                System.out.println("remove cridical tags:" + tag);
                removeElements(detailDocument, tag);
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }
    }

    public static Document removePredefineTags(Document detailDocument, String tags) {
        if (tags == null || tags == "") return detailDocument;
        try {
            List<String> removeTags = Arrays.asList(tags.split(","));
            System.out.println("removeTags:" + removeTags.size());
            for (String tag : removeTags) {
                detailDocument.select(tag).remove();
            }
            return detailDocument;
        } catch (Exception ex) {
            return detailDocument;
        }

    }

    public static Document removeNotUsedAttr(Document detailDocument) {
        List<String> attToRemove = new ArrayList<>();
        for (Element e : detailDocument.getAllElements()) {
            Attributes at = e.attributes();
            for (Attribute a : at) {
                if (!a.getKey().equalsIgnoreCase("src")) {
                    attToRemove.add(a.getKey());
                }
            }
            for (String att : attToRemove) {
                e.removeAttr(att);
            }
        }
        return detailDocument;
    }


    public static Document cleanDetailHtmlDocument(Document document,String baseUri, String selector, String removeTags) {
        try {
            System.out.println("current_document:" + document.select(selector).html());
            String detailHtml = document.select(selector).html();
            Document detailDocument = Jsoup.parse(detailHtml, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, "a,iframe,script,em,div,style");
            detailDocument = removeNotUsedAttr(detailDocument);
            System.out.println("====================done====================");
            System.out.print(detailDocument);
            return detailDocument;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static String cleanDetailHtmlString(String html, String baseUri, String selector, String removeTags) {
        try {
            System.out.println("current_document:" + html);
            Document detailDocument = Jsoup.parse(html, baseUri, Parser.xmlParser());
            detailDocument = removePredefineTags(detailDocument, removeTags);
            detailDocument = removeElementsByListSelectors(detailDocument, "a,iframe,script,em,div,style");
            detailDocument = removeNotUsedAttr(detailDocument);
            System.out.println("====================done====================");
            System.out.print(detailDocument.html());
            return detailDocument.html();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
