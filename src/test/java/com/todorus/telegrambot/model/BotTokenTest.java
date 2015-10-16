package com.todorus.telegrambot.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by todorus on 16/10/15.
 */
public class BotTokenTest {

    @Test
    public void testParseTokens() throws Exception {
        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        String singleTokenInput = token1;
        List<BotToken> singleTokenExpected = new ArrayList<BotToken>();
        singleTokenExpected.add(new BotToken(token1));

        List<BotToken> singleTokenResult = BotToken.parseTokens(singleTokenInput);
        assertEquals(singleTokenExpected, singleTokenResult);


        String multipleTokenInput = token1+";"+token2+";"+token3;
        List<BotToken> multipleTokenExpected = new ArrayList<BotToken>();
        multipleTokenExpected.add(new BotToken(token1));
        multipleTokenExpected.add(new BotToken(token2));
        multipleTokenExpected.add(new BotToken(token3));

        assertEquals(multipleTokenExpected, BotToken.parseTokens(multipleTokenInput));

    }
}