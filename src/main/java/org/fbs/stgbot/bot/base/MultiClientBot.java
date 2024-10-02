package org.fbs.stgbot.bot.base;

import com.pengrad.telegrambot.model.*;
import org.fbs.stgbot.data.ClientThreads;

import java.io.IOException;
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

    protected ClientThreads getThreadsById(long userId, ArrayList<ClientThreads> threadsArrayList){
        for (ClientThreads threads : threadsArrayList) {
            if (threads.getUserId() == userId) {
                return threads;
            }
        }
        return null;
    }

    protected abstract void callbackQueryParse(CallbackQuery query, ArrayList<ClientThreads> threads);

    protected abstract void entitiesParse(MessageEntity[] messageEntities, Message message, ArrayList<ClientThreads> threads);

    protected abstract void inlineQueryParse(InlineQuery query, ArrayList<ClientThreads> threads) throws IOException;

    protected abstract void messageParse(Message message, ArrayList<ClientThreads> threads);

    protected abstract void onStartCommand(Message message, ArrayList<ClientThreads> threads);

    protected abstract void updateParse(Update update, ArrayList<ClientThreads> threads);

    public ArrayList<ClientThreads> getParsingThreadsCallbackBack() {
        return parsingThreadsCallbackBack;
    }

    public ArrayList<ClientThreads> getParsingThreadsEntities() {
        return parsingThreadsEntities;
    }

    public ArrayList<ClientThreads> getParsingThreadsInline() {
        return parsingThreadsInline;
    }

    public ArrayList<ClientThreads> getParsingThreadsMessages() {
        return parsingThreadsMessages;
    }

    public ArrayList<ClientThreads> getParsingThreadsStart() {
        return parsingThreadsStart;
    }

    public ArrayList<ClientThreads> getParsingThreadsUpdates() {
        return parsingThreadsUpdates;
    }

    @Override
    protected void callbackQueryParse(CallbackQuery query) {

    }

    @Override
    protected void entitiesParse(MessageEntity[] messageEntities, Message message) {

    }

    @Override
    protected void inlineQueryParse(InlineQuery query) throws IOException {

    }

    @Override
    protected void messageParse(Message message) {

    }

    @Override
    protected void onStartCommand(Message message) {

    }

    @Override
    protected void updateParse(Update update) {

    }
}
