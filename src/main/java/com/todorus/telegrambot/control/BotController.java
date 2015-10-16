package com.todorus.telegrambot.control;

import com.todorus.telegrambot.model.BotToken;
import com.todorus.telegrambot.model.Document;
import com.todorus.telegrambot.model.Message;
import hudson.model.TaskListener;
import retrofit.RetrofitError;

import java.io.PrintStream;

/**
 * Created by todorus on 15/09/15.
 */
public class BotController {

    private BotClient botClient;
    private TaskListener taskListener;

    public BotController(BotClient botClient, TaskListener taskListener){
        this.botClient = botClient;
        this.taskListener = taskListener;
    }

    public boolean sendMessage(BotToken token, Message message){
        PrintStream logger = taskListener.getLogger();

        logger.println("TelegramBot: Sending message to chat " + message.getChatId());

        if(message != null) {
            try {
                Message.Response response = botClient.sendMessage(token.toString(), message);

                if (response.isOk()) {
                    logger.println("TelegramBot: successfully sent message");
                    return true;
                } else {
                    String logMessage = "TelegramBot: " + "failed to send message \"" + response.getErrorCode() + " " + response.getDescription() + "\"";
                    logger.println(logMessage);
                }

            } catch (RetrofitError retrofitError){
                String logMessage = "TelegramBot: " + "failed to send message \"" + retrofitError.getMessage() + "\"";
                logger.println(logMessage);
            }
        }

        return false;
    }

    // TODO write a test for this method
    public boolean sendDocument(BotToken token, Document document){
        PrintStream logger = taskListener.getLogger();

        try {
            Message.Response response = botClient.sendDocument(token.toString(), document.getChatId(), document.getFile());

            if (response.isOk()) {
                logger.println("TelegramBot: successfully sent document");
                return true;
            } else {
                String logMessage = "TelegramBot: " + "failed to send document \"" + response.getErrorCode() + " " + response.getDescription() + "\"";
                logger.println(logMessage);
            }

        } catch (RetrofitError retrofitError){
            String logMessage = "TelegramBot: " + "failed to send document \"" + retrofitError.getMessage() + "\"";
            logger.println(logMessage);
        }

        return false;
    }

}
