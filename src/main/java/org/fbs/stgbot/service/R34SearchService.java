package org.fbs.stgbot.service;

import com.pengrad.telegrambot.model.request.InlineQueryResultPhoto;
import org.apache.logging.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class R34SearchService {

    private R34SearchService(){}

    public static InlineQueryResultPhoto[] getParsed(String query, Logger LOGGER) throws IOException {
        String query0 = cutSpaces(query);
        int pageStr = 0;
        String pageTag;
        int separatorIndex = query0.indexOf("-");
        Document r34Link;

        if (separatorIndex != -1) {
            try {
                pageStr = 42 * (Integer.parseInt(query0.substring((separatorIndex + 1)).trim()) - 1);
                if (pageStr <= 0) {
                    pageStr = 0;
                }
            } catch (NumberFormatException e) {
                LOGGER.error(e);
            }
            pageTag = query0.substring(0, separatorIndex).trim();
        } else {
            pageTag = query0.trim();
        }

        if (!pageTag.isEmpty()) {
            pageTag = pageTag.replace(' ', '+');
            LOGGER.trace("Search for rule34 images by tag: {}", pageTag);
            r34Link = Jsoup
                    .connect("https://rule34.xxx/index.php?page=post&s=list&tags=" + pageTag + "+&pid=" + pageStr)
                    .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:101.0) Gecko/20100101 Firefox/101.0")
                    .get();

            Elements r34LinkImagesBoard = r34Link.getElementsByClass("image-list");
            Elements r34RedirectLinks = Jsoup.parse(r34LinkImagesBoard.html()).getElementsByTag("a");
            ArrayList<String> imgUrls = new ArrayList<>();
            ArrayList<InlineQueryResultPhoto> photosArray = new ArrayList<>();

            for (Element img : r34RedirectLinks) {
                imgUrls.add("https://rule34.xxx/" + img.attr("href"));
            }

            int i = 0;
            for (String url : imgUrls) {
                LOGGER.trace("Found image: {}", url);
                try {
                    Elements flexi = Jsoup
                            .connect(url)
                            .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:101.0) Gecko/20100101 Firefox/101.0")
                            .get()
                            .getElementsByClass("flexi");
                    Elements img = Jsoup.parse(flexi.html()).getElementsByTag("img");
                    if (!img.attr("src").isEmpty()) {
                        photosArray.add(buildInlinePhoto("photo" + i, img.attr("src")));
                        i++;
                    }
                } catch (HttpStatusException e) {
                    LOGGER.error(e.getMessage());
                }
            }
            InlineQueryResultPhoto[] photos = new InlineQueryResultPhoto[photosArray.size()];
            for (int j = 0; j < photosArray.size(); j++) {
                photos[j] = photosArray.get(j);
            }

            return photos;
        }

        return null;
    }

    private static InlineQueryResultPhoto buildInlinePhoto(String id, String url) {
        return new InlineQueryResultPhoto(id, url, url);
    }

    private static String cutSpaces(String string){
        char[] chars = string.toCharArray();
        ArrayList<Character> charList = new ArrayList<>();
        for (char c: chars){
            charList.add(c);
        }

        int sStart = -1, sEnd = -1;

        for (int i = 0; i < charList.size(); i++) {
            if (charList.get(i) == ' '){
                if (sStart == -1){
                    sStart = i;
                }
                sEnd = i;
            }
            else {
                if (sStart != -1) {
                    for (int j = sStart; j < sEnd; j++) {
                        charList.set(j, null);
                    }
                    sStart = -1;
                    sEnd = -1;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Character c: charList){
            if (c != null) sb.append(c);
        }
        return sb.toString();
    }

}
