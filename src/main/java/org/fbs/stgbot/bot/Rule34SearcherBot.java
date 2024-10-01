package org.fbs.stgbot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InlineQueryResultPhoto;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import org.fbs.stgbot.bot.base.Bot;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Rule34SearcherBot extends Bot {

    private final ArrayList<Thread> parsingThreads = new ArrayList<>();

    public Rule34SearcherBot(String botToken, String startCommandRaw) {
        super(botToken, startCommandRaw);
    }

    @Override
    protected void onStartCommand(Message message) {

    }

    @Override
    protected void updateParse(Update update) {

    }

    @Override
    protected void callbackQueryParse(CallbackQuery query) {

    }

    @Override
    protected void entitiesParse(MessageEntity[] messageEntities) {

    }

    @Override
    protected void messageParse(Message message) {

    }

    @Override
    protected void inlineQueryParse(InlineQuery query) throws IOException {

        for (int i = 0; i < parsingThreads.size(); i++) {
            parsingThreads.remove(i);
        }

        parsingThreads.add(new Thread(() -> {
            try {
                getRequest(query, getBot());
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }));
        parsingThreads.get(parsingThreads.size() - 1).start();

    }

    private InlineQueryResultPhoto buildInlinePhoto(String id, String url) {
        return new InlineQueryResultPhoto(id, url, url);
    }

    private InlineQueryResultArticle buildInlineArticle(String id, String title, String message) {
        return new InlineQueryResultArticle(id, title, message);
    }

    private void getRequest(InlineQuery query, TelegramBot bot) throws IOException {
        int pageStr = 0;
        String pageTag = "";
        int separatorIndex = query.query().indexOf("-");
        Document r34Link;

        if (separatorIndex!= -1) {
            try {
                pageStr = 42 * (Integer.parseInt(query.query().substring((separatorIndex + 1)).trim()) - 1);
                if (pageStr <= 0){
                    pageStr = 0;
                }
            }catch (NumberFormatException e){
                LOGGER.error(e);
            }
            pageTag = query.query().substring(0, separatorIndex).trim();
        }
        else {
            pageTag = query.query().trim();
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
            for (String url: imgUrls){
                LOGGER.trace("Found image: {}", url);
                try {
                    Elements flexi = Jsoup.connect(url).get().getElementsByClass("flexi");
                    Elements img = Jsoup.parse(flexi.html()).getElementsByTag("img");
                    if (!img.attr("src").isEmpty()) {
                        photosArray.add(buildInlinePhoto("photo" + i, img.attr("src")));
                        i++;
                    }
                }
                catch (HttpStatusException e){
                    LOGGER.error(e.getMessage());
                }
            }

            InlineQueryResultPhoto[] photos = new InlineQueryResultPhoto[photosArray.size()];
            for (int j = 0; j < photosArray.size(); j++) {
                photos[j] = photosArray.get(j);
            }

            LOGGER.trace("Send inline result: {}", photosArray);
            try {
                bot.execute(new AnswerInlineQuery(query.id(), photos));
            }catch (RuntimeException e){
                LOGGER.error(e);
            }
        }
    }

}
