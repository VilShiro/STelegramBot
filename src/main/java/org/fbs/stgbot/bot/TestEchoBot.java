package org.fbs.stgbot.bot;

import org.fbs.stgbot.bot.base.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public final class TestEchoBot extends Bot {
    public TestEchoBot() throws TelegramApiException {
        super("5792337403:AAFxy4ksALgHyCw-2pNzcG1jFPaHHmbABMI");
    }

    @Override
    public String getBotUsername() {
        return "Vil_Console_bot";
    }

    @Override
    protected void onStartCommand(Message message) throws TelegramApiException {
        // Set the chat id of the message
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Start command is received");

        execute(sendMessage);
    }

    @Override
    protected void updateParse(Update update) throws TelegramApiException {

    }

    @Override
    protected void callbackQueryParse(Update update) throws TelegramApiException {

    }

    @Override
    protected void messageParse(Update update) throws TelegramApiException {

    }

    @Override
    protected void inlineQueryParse(Update update) throws TelegramApiException {

    }

}
