package org.fbs.stgbot.bot.base;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.fbs.stgbot.Main.LOGGER;

public abstract class Bot extends TelegramLongPollingBot {

    protected Bot(String botToken) throws TelegramApiException {
        super(botToken);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    public Class<DefaultBotSession> getBotSession() {
        return DefaultBotSession.class;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Update updateC = update;
        LOGGER.trace("Received update: {}", updateC);

        // Start command variant
        try {
            updateParse(updateC);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        if (updateC!= null) {
            // CallbackQuery variant
            if (updateC.hasCallbackQuery()) {
                try {
                    callbackQueryParse(updateC);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            // Message variant
            else if (updateC.hasMessage()) {
                try{
                messageParse(updateC);
                } catch (TelegramApiException e0) {
                    throw new RuntimeException(e0);
                }
                Message message = updateC.getMessage();
                if (message.isCommand()){
                    LOGGER.info("Received command: {}", message.getText());
                    switch (message.getText()) {
                        case "/start":
                            try {
                                onStartCommand(message);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "stop":
                            LOGGER.info("Received stop command");
                            System.exit(0);
                        default:
                            LOGGER.info("Unknown command: {}", message.getText());
                    }
                }
            }
            // InlineQuery variant
            else if (updateC.hasInlineQuery()) {
                try {
                    inlineQueryParse(updateC);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            // Unknown variant
            else {
                LOGGER.error("Unknown update type, id: {}, message: {}", updateC.getUpdateId(), updateC.getMessage().getText());
            }
        }

    }

    // Override that method to provide bot token
    @Override
    public String getBotUsername() {
        return null;
    }

    protected abstract void onStartCommand(Message message) throws TelegramApiException;

    protected abstract void updateParse(Update update) throws TelegramApiException;

    protected abstract void callbackQueryParse(Update update) throws TelegramApiException;

    protected abstract void messageParse(Update update) throws TelegramApiException;

    protected abstract void inlineQueryParse(Update update) throws TelegramApiException;

}
