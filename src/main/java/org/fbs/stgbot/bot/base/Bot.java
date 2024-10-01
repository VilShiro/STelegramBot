package org.fbs.stgbot.bot.base;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.GetUpdates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;


public abstract class Bot{

    public static final Logger LOGGER = LogManager.getLogger(Bot.class);

    private final GetUpdates getUpdates = new GetUpdates().offset(0).timeout(0);

    private final TelegramBot bot;

    protected Bot(String botToken, String startCommandRaw){
        StringBuilder stringBuilder = new StringBuilder();
        String startCommand = "";
        if (startCommandRaw.toCharArray()[0] == '/'){
            for (int i = 1; i < startCommandRaw.length(); i++) {
                stringBuilder.append(startCommandRaw.toCharArray()[i]);
            }
            startCommand = stringBuilder.toString();
        }
        startCommand = startCommandRaw;

        bot = new TelegramBot(botToken);
        String finalStartCommand = startCommand;
        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> list) {

                Update lastUpdate = list.get(list.size()-1);
                LOGGER.trace("New update was found: {}", lastUpdate);
                updateParse(lastUpdate);

                if (lastUpdate.message() != null && !Objects.equals(lastUpdate.message().text(), "")){
                    Message message = lastUpdate.message();
                    LOGGER.trace("Message was found: {}", message);
                    messageParse(message);
                    try {
                        if (message.entities().length > 0) {
                            MessageEntity[] entities = message.entities();
                            LOGGER.trace("Message has entities, parser was called");
                            entitiesParse(entities);
                            if (message.text().contains(finalStartCommand)) {
                                LOGGER.trace("Message: {}", message.text());
                                onStartCommand(message);
                                LOGGER.trace("Start command : {} was called", finalStartCommand);
                            }
                        }
                    }
                    catch (NullPointerException e){
                        LOGGER.error(e.getMessage());
                    }
                }
                else if (lastUpdate.callbackQuery() != null){
                    CallbackQuery query = lastUpdate.callbackQuery();
                    LOGGER.trace("Callback query was found: {}", query);
                    callbackQueryParse(query);
                }
                else if (lastUpdate.inlineQuery() != null) {
                    InlineQuery query = lastUpdate.inlineQuery();
                    LOGGER.trace("Inline query was found: {}", query);
                    inlineQueryParse(query);
                }
                else {
                    LOGGER.error("Unknown update: {}", lastUpdate);
                }

                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        }, e -> {
            if (e.response() != null) {
                // got bad response from telegram
                e.response().errorCode();
                e.response().description();
            } else {
                // probably network error
                LOGGER.error(e.getMessage());
            }
        }
        );
    }

    protected TelegramBot getBot(){
        return bot;
    }

    protected abstract void onStartCommand(Message message);

    protected abstract void updateParse(Update update);

    protected abstract void callbackQueryParse(CallbackQuery query);

    protected abstract void entitiesParse(MessageEntity[] messageEntities);

    protected abstract void messageParse(Message message);

    protected abstract void inlineQueryParse(InlineQuery query);

}
