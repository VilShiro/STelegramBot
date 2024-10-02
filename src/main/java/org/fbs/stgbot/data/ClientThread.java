package org.fbs.stgbot.data;

import static org.fbs.stgbot.bot.base.Bot.LOGGER;

public class ClientThread extends Thread{

    private final String userId;

    public ClientThread(String userId){
        this.userId = userId;
        LOGGER.trace("ClientThread obj was created with chat id: {}", userId);
        start();
    }

    public String getUserId() {
        return userId;
    }
}
