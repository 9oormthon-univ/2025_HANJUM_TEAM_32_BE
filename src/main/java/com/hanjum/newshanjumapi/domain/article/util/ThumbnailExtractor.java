package com.hanjum.newshanjumapi.domain.article.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ThumbnailExtractor {
    public static String fetchThumbnail(String articleUrl) {

        try{
            Document doc = Jsoup.connect(articleUrl).get();

            return doc.select("meta[property=og:image]").attr("content");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
