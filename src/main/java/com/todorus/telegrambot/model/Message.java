package com.todorus.telegrambot.model;

import com.google.gson.annotations.SerializedName;
import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by todorus on 14/09/15.
 */
public class Message {

    /**
     * Unique identifier for the message recipient — User or GroupChat id
     */
    @SerializedName("chat_id")
    private long chatId;

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
     * @param chatId Unique identifier for the message recipient — User or GroupChat id
     * @param text   Text of the message to be sent
     */
    public Message(long chatId, String text) {
        this(chatId, text, null, false, null, null);
    }


    /**
     * @param chatId                Unique identifier for the message recipient — User or GroupChat id
     * @param text                  Text of the message to be sent
     * @param parseMode             Send Markdown, if you want Telegram apps to show bold, italic and inline URLs in your bot's message. For the moment, only Telegram for Android supports this.
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyToMessageId      If the message is a reply, ID of the original message
     * @param replyMarkup           Additional interface options. A JSON-serialized object for a custom reply keyboard, instructions to hide keyboard or to force a reply from the user.
     */
    public Message(long chatId, String text, String parseMode, boolean disableWebPagePreview, Integer replyToMessageId, ReplyMarkup replyMarkup) {
        this.chatId = chatId;
        this.text = text;
        this.parseMode = parseMode;
        this.disableWebPagePreview = disableWebPagePreview;
        this.replyToMessageId = replyToMessageId;
        this.replyMarkup = replyMarkup;
    }

    public long getChatId() {
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

        /**
         * (for unittesting only)
         * Simulate an Error response from the Telegram Bot API
         *
         * @param errorCode
         * @param description
         */
        public Response(int errorCode, String description) {
            super(errorCode, description);
        }

        /**
         * (for unittesting only)
         * Simulate an Error response from the Telegram Bot API
         *
         * @param result
         */
        public Response(Message result) {
            this.ok = true;
            this.result = result;
        }

        public Message getResult() {
            return result;
        }
    }


    public static class Builder {

        List<ChangeLogSet.Entry> entries = new ArrayList<ChangeLogSet.Entry>();

        private AbstractBuild build;
        private long chatId;

        public Builder setBuild(AbstractBuild build) {
            this.build = build;

            // We can't get the size without using up the only iterator of the changeset
            Iterator<ChangeLogSet.Entry> iterator = build.getChangeSet().iterator();
            while(iterator.hasNext()){
                entries.add(iterator.next());
            }

            return this;
        }

        public Builder setChatId(long chatId) {
            this.chatId = chatId;
            return this;
        }


        public Message build() {
            final int MAX_MESSAGES = 3;

            String fullDisplayName = build.getFullDisplayName();
            String result = build.getResult().toString();

            String commit = "";

            ChangeLogSet.Entry entry;
            int size = entries.size();
            int startIndex = Math.max(0, size - MAX_MESSAGES);
            for(int i = startIndex; i < size; i++){
                entry = entries.get(i);
                commit += "\n" + entry.getAuthor().getDisplayName() + ": " + entry.getMsg();
            }

            if(startIndex > 0){
                // It has at least one commit that isn't shown, show dots
                commit += "\n...";
            }

            Message message = new Message(chatId, fullDisplayName + " " + result + commit);

            return message;
        }

    }

}
