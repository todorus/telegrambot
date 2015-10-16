package com.todorus.telegrambot.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by todorus on 16/10/15.
 */
public class ChatTest {

    @Test
    public void testParseChats() throws Exception {
        Random random = new Random();

        long id1 = random.nextLong();
        long id2 = random.nextLong();
        long id3 = random.nextLong();

        String singleChatInput = Long.toString(id1);
        List<Chat> singleTokenExpected = new ArrayList<Chat>();
        singleTokenExpected.add(new Chat(id1));

        assertEquals(singleTokenExpected, Chat.parseChats(singleChatInput));


        String multipleChatInput = Long.toString(id1)+";"+Long.toString(id2)+";"+Long.toString(id3);
        List<Chat> multipleTokenExpected = new ArrayList<Chat>();
        multipleTokenExpected.add(new Chat(id1));
        multipleTokenExpected.add(new Chat(id2));
        multipleTokenExpected.add(new Chat(id3));

        assertEquals(multipleTokenExpected, Chat.parseChats(multipleChatInput));
    }
}