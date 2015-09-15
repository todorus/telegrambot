package com.todorus.telegrambot.control;

import com.google.gson.Gson;
import com.todorus.telegrambot.model.Message;
import hudson.model.TaskListener;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by todorus on 15/09/15.
 */
public class BotControllerTest {

    private String token;

    @Mock
    private BotClient botClient;

    @Mock
    private TaskListener taskListener;

    @Mock
    private PrintStream logger;

    private BotController subject;
    private Message message;
    private Message.Response response;
    private String logMessage;
    private boolean result;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reset(botClient);
        reset(logger);

        stub(taskListener.getLogger()).toReturn(logger);

        token = UUID.randomUUID().toString();
        message = new Message(11, "some body text");

        subject = new BotController(botClient, taskListener);
    }

    @Test
    public void given_it_gets_a_OK_response() throws Exception {

        // Given it gets a SUCCESS response
        response = new Message.Response(message);
        stub(botClient.sendMessage(anyString(), any(Message.class))).toReturn(response);

        // expect
        logMessage = "TelegramBot: successfully sent message";
        result = subject.sendMessage(token, message);

        verify(botClient, only()).sendMessage(token, message); // It sends the message
        verify(logger, times(1)).println(logMessage); // It logs the success
        assertTrue(result); // It returns true as it is successful

    }

    @Test
    public void given_it_gets_an_OK_response_with_an_error_code() throws Exception {

        // Given it gets a OK response with an error code
        response = new Message.Response(504, "Unauthorized");
        stub(botClient.sendMessage(anyString(), any(Message.class))).toReturn(response);

        // expect
        logMessage = "TelegramBot: failed to send message \"504 Unauthorized\"";
        result = subject.sendMessage(token, message);

        verify(botClient, only()).sendMessage(token, message); // It sends the message
        verify(logger, times(1)).println(logMessage); // It logs the success
        assertFalse(result); // It returns false as it is unsuccessful

    }

    @Test
    public void given_it_gets_an_ERROR_response() throws Exception {

        // Given it gets a ERROR response
        Response response = new Response("http://some.url",404,"Not Found",new ArrayList<Header>(), null);
        stub(botClient.sendMessage(anyString(), any(Message.class))).toThrow(RetrofitError.httpError("http://some.url", response, RetroAdapter.getGsonConverter(), Message.Response.class));

        // expect
        logMessage = "TelegramBot: " + "failed to send message \"404 Not Found\"";
        result = subject.sendMessage(token, message);

        verify(botClient, only()).sendMessage(token, message); // It sends the message
        verify(logger, times(1)).println(logMessage); // It logs the success
        assertFalse(result); // It returns false as it is unsuccessful

    }
}