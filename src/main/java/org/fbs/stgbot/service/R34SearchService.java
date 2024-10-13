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
        int pageStr = 0;
        String pageTag;
        int separatorIndex = query.indexOf("-");
        Document r34Link;

        if (separatorIndex != -1) {
            try {
                pageStr = 42 * (Integer.parseInt(query.substring((separatorIndex + 1)).trim()) - 1);
                if (pageStr <= 0) {
                    pageStr = 0;
                }
            } catch (NumberFormatException e) {
                LOGGER.error(e);
            }
            pageTag = query.substring(0, separatorIndex).trim();
        } else {
            pageTag = query.trim();
        }

        if (!pageTag.isEmpty()) {
            LOGGER.trace("Search for rule34 images by tag: {}", pageTag);
            r34Link = Jsoup.connect("https://rule34.xxx/index.php?page=post&s=list&tags=" + pageTag + "+&pid=" + pageStr).get();

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
                    Elements flexi = Jsoup.connect(url).get().getElementsByClass("flexi");
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

}
