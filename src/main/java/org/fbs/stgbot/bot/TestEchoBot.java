package org.fbs.stgbot.bot;

import com.pengrad.telegrambot.model.*;
import org.fbs.stgbot.bot.base.Bot;

public final class TestEchoBot extends Bot {


    public TestEchoBot(String botToken, String startCommandRaw) {
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
    protected void inlineQueryParse(InlineQuery query) {

    }
}
