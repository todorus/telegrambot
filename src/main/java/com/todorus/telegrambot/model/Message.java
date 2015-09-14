package com.todorus.telegrambot.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by todorus on 14/09/15.
 */
public class Message {

    /**
     * Unique identifier for the message recipient — User or GroupChat id
     */
    @SerializedName("chat_id")
    private int chatId;

    /**
     * Text of the message to be sent
     */
    private String text;

    /**
     * Send Markdown, if you want Telegram apps to show bold, italic and inline URLs in your bot's message. For the moment, only Telegram for Android supports this.
     */
    @SerializedName("parse_mode")
    private String parseMode;

    /**
     * Disables link previews for links in this message
     */
    @SerializedName("disable_web_page_preview")
    private boolean disableWebPagePreview;

    /**
     * If the message is a reply, ID of the original message
     * This field is an Integer instead of an int so it can be null and ignored by the JSON serializer
     */
    @SerializedName("reply_to_message_id")
    private Integer replyToMessageId;

    /**
     * ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply  Optional  Additional interface options.
     * A JSON-serialized object for a custom reply keyboard, instructions to hide keyboard or to force a reply from the user.
     */
    @SerializedName("reply_markup")
    private ReplyMarkup replyMarkup;

    /**
     *
     * @param chatId Unique identifier for the message recipient — User or GroupChat id
     * @param text Text of the message to be sent
     */
    public Message(int chatId, String text){
        this(chatId, text, null, false, null, null);
    }


    /**
     *
     * @param chatId Unique identifier for the message recipient — User or GroupChat id
     * @param text Text of the message to be sent
     * @param parseMode Send Markdown, if you want Telegram apps to show bold, italic and inline URLs in your bot's message. For the moment, only Telegram for Android supports this.
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyToMessageId If the message is a reply, ID of the original message
     * @param replyMarkup Additional interface options. A JSON-serialized object for a custom reply keyboard, instructions to hide keyboard or to force a reply from the user.
     */
    public Message(int chatId, String text, String parseMode, boolean disableWebPagePreview, Integer replyToMessageId, ReplyMarkup replyMarkup){
        this.chatId = chatId;
        this.text = text;
        this.parseMode = parseMode;
        this.disableWebPagePreview = disableWebPagePreview;
        this.replyToMessageId = replyToMessageId;
        this.replyMarkup = replyMarkup;
    }

    public int getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }

    public String getParseMode() {
        return parseMode;
    }

    public boolean isDisableWebPagePreview() {
        return disableWebPagePreview;
    }

    public Integer getReplyToMessageId() {
        return replyToMessageId;
    }

    public ReplyMarkup getReplyMarkup() {
        return replyMarkup;
    }

    /**
     * Implementation of the Response class for methods that return a Message object
     */
    public static class Response extends com.todorus.telegrambot.model.Response {

        private Message result;

        public Message getResult() {
            return result;
        }
    }

}
