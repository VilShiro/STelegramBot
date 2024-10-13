package org.fbs.stgbot.bot;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InlineQueryResultPhoto;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.SendDocument;
import org.apache.commons.io.FileUtils;
import org.fbs.stgbot.bot.base.Bot;
import org.fbs.stgbot.bot.base.MultiClientBot;
import org.fbs.stgbot.data.ClientThread;
import org.fbs.stgbot.data.ClientThreads;
import org.fbs.stgbot.service.R34SearchService;
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

public class Rule34SearcherBot extends MultiClientBot {

    public Rule34SearcherBot(String botToken, String startCommandRaw) {
        super(botToken, startCommandRaw);
    }

    @Override
    protected void callbackQueryParse(CallbackQuery query, ArrayList<ClientThreads> threads) throws Exception {

    }

    @Override
    protected void entitiesParse(MessageEntity[] messageEntities, Message message, ArrayList<ClientThreads> threads) throws Exception {
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
    protected void inlineQueryParse(InlineQuery query, ArrayList<ClientThreads> threads) throws Exception {
        ClientThreads clientThreads = getThreadsByUserId(query.from().id(), threads);

        if (clientThreads == null){
            threads.add(new ClientThreads(query.from().id()));
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

    @Override
    protected void messageParse(Message message, ArrayList<ClientThreads> threads) throws Exception {

    }

    @Override
    protected void onStartCommand(Message message, ArrayList<ClientThreads> threads) throws Exception {

    }

    @Override
    protected void updateParse(Update update, ArrayList<ClientThreads> threads) throws Exception {

    }

    @Override
    protected void init() {

    }

    private InlineQueryResultArticle buildInlineArticle(String id, String title, String message) {
        return new InlineQueryResultArticle(id, title, message);
    }

    private AnswerInlineQuery getRequest(InlineQuery query, String chatId) throws IOException {

        InlineQueryResultPhoto[] photos = R34SearchService.getParsed(query.query(), LOGGER);

        LOGGER.trace("Send inline result: {}", (Object) photos);
        try {
            return  (new AnswerInlineQuery(query.id(), photos));
        }catch (RuntimeException e){
            LOGGER.error(e);
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
