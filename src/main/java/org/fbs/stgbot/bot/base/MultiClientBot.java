package org.fbs.stgbot.bot.base;

import com.pengrad.telegrambot.model.*;
import org.fbs.stgbot.data.ClientThreads;

import java.util.ArrayList;

public abstract class MultiClientBot extends Bot {

    private final ArrayList<ClientThreads> parsingThreadsUpdates = new ArrayList<>();
    private final ArrayList<ClientThreads> parsingThreadsMessages = new ArrayList<>();
    private final ArrayList<ClientThreads> parsingThreadsEntities = new ArrayList<>();
    private final ArrayList<ClientThreads> parsingThreadsCallbackBack = new ArrayList<>();
    private final ArrayList<ClientThreads> parsingThreadsInline = new ArrayList<>();
    private final ArrayList<ClientThreads> parsingThreadsStart = new ArrayList<>();

    protected MultiClientBot(String botToken, String startCommandRaw) {
        super(botToken, startCommandRaw);
    }

    protected ClientThreads getThreadsByUserId(long userId, ArrayList<ClientThreads> threadsArrayList){
        for (ClientThreads threads : threadsArrayList) {
            if (threads.getUserId() == userId) {
                return threads;
            }
        }
        return null;
    }

    protected abstract void callbackQueryParse(CallbackQuery query, ArrayList<ClientThreads> threads) throws Exception;

    protected abstract void entitiesParse(MessageEntity[] messageEntities, Message message, ArrayList<ClientThreads> threads) throws Exception;

    protected abstract void inlineQueryParse(InlineQuery query, ArrayList<ClientThreads> threads) throws Exception;

    protected abstract void messageParse(Message message, ArrayList<ClientThreads> threads) throws Exception;

    protected abstract void onStartCommand(Message message, ArrayList<ClientThreads> threads) throws Exception;

    protected abstract void updateParse(Update update, ArrayList<ClientThreads> threads) throws Exception;

    @Deprecated
    public ArrayList<ClientThreads> getParsingThreadsCallbackBack() {
        return parsingThreadsCallbackBack;
    }

    @Deprecated
    public ArrayList<ClientThreads> getParsingThreadsEntities() {
        return parsingThreadsEntities;
    }

    @Deprecated
    public ArrayList<ClientThreads> getParsingThreadsInline() {
        return parsingThreadsInline;
    }

    @Deprecated
    public ArrayList<ClientThreads> getParsingThreadsMessages() {
        return parsingThreadsMessages;
    }

    @Deprecated
    public ArrayList<ClientThreads> getParsingThreadsStart() {
        return parsingThreadsStart;
    }

    @Deprecated
    public ArrayList<ClientThreads> getParsingThreadsUpdates() {
        return parsingThreadsUpdates;
    }

    @Deprecated
    @Override
    protected void callbackQueryParse(CallbackQuery query) throws Exception {
        callbackQueryParse(query, parsingThreadsCallbackBack);
    }

    @Deprecated
    @Override
    protected void entitiesParse(MessageEntity[] messageEntities, Message message) throws Exception {
        entitiesParse(messageEntities, message, parsingThreadsEntities);
    }

    @Deprecated
    @Override
    protected void inlineQueryParse(InlineQuery query) throws Exception {
        inlineQueryParse(query, parsingThreadsInline);
    }

    @Deprecated
    @Override
    protected void messageParse(Message message) throws Exception {
        messageParse(message, parsingThreadsMessages);
    }

    @Deprecated
    @Override
    protected void onStartCommand(Message message) throws Exception {
        onStartCommand(message, parsingThreadsStart);
    }

    @Deprecated
    @Override
    protected void updateParse(Update update) throws Exception {
        updateParse(update, parsingThreadsUpdates);
    }
}
