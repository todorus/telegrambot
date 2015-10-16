package com.todorus.telegrambot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by todorus on 16/10/15.
 */
public class Chat {

    private long chatId;

    public Chat(long chatId){
        this.chatId = chatId;
    }

    public static List<Chat> parseChats(String chatIds){
        String[] ids = chatIds.split(";");

        List<Chat> chats = new ArrayList<Chat>(ids.length);
        for(String id : ids){
            chats.add(new Chat(Long.parseLong(id)));
        }
        return chats;
    }

    public long getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        return chatId == chat.chatId;

    }

    @Override
    public int hashCode() {
        return (int) (chatId ^ (chatId >>> 32));
    }
}
