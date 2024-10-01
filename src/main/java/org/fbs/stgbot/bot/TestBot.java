package org.fbs.stgbot.bot;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import org.fbs.stgbot.bot.base.Bot;

import java.util.LinkedList;
import java.util.List;

public final class TestBot extends Bot {

    private Message lastMessage = new Message();

    public TestBot(String botToken, String startCommandRaw) {
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
        lastMessage = message;
    }

    @Override
    protected void inlineQueryParse(InlineQuery query) {

    }


}
