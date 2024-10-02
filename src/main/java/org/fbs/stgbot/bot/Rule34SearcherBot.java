package org.fbs.stgbot.bot;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InlineQueryResultPhoto;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.SendDocument;
import org.apache.commons.io.FileUtils;
import org.fbs.stgbot.bot.base.Bot;
import org.fbs.stgbot.data.ClientThread;
import org.fbs.stgbot.data.ClientThreads;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class Rule34SearcherBot extends Bot {

    private final ArrayList<ClientThreads> parsingThreadsS = new ArrayList<>();


    public Rule34SearcherBot(String botToken, String startCommandRaw) {
        super(botToken, startCommandRaw);
    }

    @Override
    protected void init() {

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
    protected void entitiesParse(MessageEntity[] messageEntities, Message message) {
        if (message.text().equals("/sendLog")){
            ClassLoader classloader = ClientThread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("local/logs/logs.log");
            assert is != null;
            File doc = new File("logs.log");
            try {
                FileUtils.copyInputStreamToFile(is, doc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SendDocument document = new SendDocument(message.messageId(), doc);
            getBot().execute(document);
        }
    }

    @Override
    protected void messageParse(Message message) {

    }

    @Override
    protected void inlineQueryParse(InlineQuery query) throws IOException {

        ClientThreads clientThreads = null;
        for (ClientThreads threads: parsingThreadsS){
            if (threads.getUserId() == query.from().id()){
                clientThreads = threads;
            }
        }
        if (clientThreads == null){
            clientThreads = new ClientThreads(query.from().id());
            parsingThreadsS.add(clientThreads);
        }

        if (Objects.equals(clientThreads.getUserId(), query.from().id())) {
            for (int i = 0; i < clientThreads.size(); i++) {
                clientThreads.removeClientThread(i);
            }
        }

        clientThreads.addClientThread(() -> {
            try {
                getBot().execute(getRequest(query, query.id()));
            } catch (IOException e) {
                LOGGER.error(e);
            }
        });

    }

    private InlineQueryResultPhoto buildInlinePhoto(String id, String url) {
        return new InlineQueryResultPhoto(id, url, url);
    }

    private InlineQueryResultArticle buildInlineArticle(String id, String title, String message) {
        return new InlineQueryResultArticle(id, title, message);
    }

    private AnswerInlineQuery getRequest(InlineQuery query, String chatId) throws IOException {
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
                return  (new AnswerInlineQuery(query.id(), photos));
            }catch (RuntimeException e){
                LOGGER.error(e);
            }
        }
        try {
            return new AnswerInlineQuery(chatId, buildInlineArticle("0", "Tag not found", "Tag does not found"));
        }
        catch (RuntimeException e){
            LOGGER.error(e);
            return null;
        }
    }

}
