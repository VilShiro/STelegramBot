package org.fbs.stgbot.bot;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
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
    String queryS = query.query().trim().toLowerCase();
    List<String> answerList = new LinkedList<String>();
    answerList.add("first_answer");
    answerList.add("second_answer");
    InlineQueryResult[] resArray = new InlineQueryResult[answerList.size()];
    for (int j = 0; j < resArray.length; j++) {
        resArray[j] = new InlineQueryResultArticle(query.id(), answerList.get(j), queryS);
    }

    // TODO: BaseRequest request =  new  .answerInlineQuery(query.id(), resArray);
    }
}
