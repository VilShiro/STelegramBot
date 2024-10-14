# Telegram Bot in Java

**Description:**
This Telegram bot is developed in Java using the `java-telegram-bot-api` library. It is designed to [describe the main functionality of the bot here].

**Installation:**
1. **Install Java 17+:** Download and install the latest version of Java from the official website: [https://www.java.com/en/download/](https://www.java.com/en/download/)
2. **Download the bot JAR file:**
   ```bash
   wget https://github.com/VilShiro/STelegramBot/releases/latest/download/stb.jar
   ```
3. **Run the bot:**
   ```bash
   java -jar stb.jar
   ```

**Debugging:**
* **Logging:** The log4j library is used for logging.
* **Log level:** For more detailed information about the bot's operation, add the `--info` argument when starting.

**Development:**
* **Library:** The library [java-telegram-bot-api](https://github.com/pengrad/java-telegram-bot-api)([Maven Central](https://mvnrepository.com/artifact/com.github.pengrad/java-telegram-bot-api)) is used for interacting with the Telegram API.
* **Working principle:**
    * When receiving an update from a user (message, callback, inline, etc.), the data about the user, update, and chat is extracted.
    * All active threads associated with this user and update type are deleted.
    * A new thread is created to process the update.
    * In the new thread, the received information is parsed.
    * The parsed data is sent back to the user's chat.
  ```
  bot_update -> 
    : 
      user, info, chat -> 
  
  deleteAll(
    threads(user, info).getAll
  ) ->  
  
  threads(user).createNew(
    run{
      parse(info)
    }
  ) ->
  
  push(chat, parsedInfo);
  ```

**Tools:**
* [List the tools used here, such as IDE, version control systems, databases]