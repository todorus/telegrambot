package com.todorus.telegrambot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by todorus on 16/10/15.
 * TODO write a validator method
 */
public class BotToken {

    private String token;

    public BotToken(String token){
        this.token = token;
    }

    public static List<BotToken> parseTokens(String tokens){
        String[] tokenStrings = tokens.split(";");

        List<BotToken> botTokens = new ArrayList<BotToken>(tokenStrings.length);
        for(String tokenString : tokenStrings){
            botTokens.add(new BotToken(tokenString));
        }

        return botTokens;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotToken botToken = (BotToken) o;

        return !(token != null ? !token.equals(botToken.token) : botToken.token != null);

    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }
}
