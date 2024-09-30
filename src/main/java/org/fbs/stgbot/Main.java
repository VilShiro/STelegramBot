package org.fbs.stgbot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fbs.stgbot.bot.TestEchoBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    public static final Logger LOGGER = LogManager.getLogger(Main.class);


    public static void main(String[] args) throws TelegramApiException {
        TestEchoBot echoBot = new TestEchoBot();


    }

}