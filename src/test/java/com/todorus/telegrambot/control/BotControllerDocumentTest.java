package com.todorus.telegrambot.control;

import com.todorus.telegrambot.model.BotToken;
import com.todorus.telegrambot.model.Document;
import com.todorus.telegrambot.model.Message;
import hudson.model.TaskListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import javax.print.Doc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by todorus on 15/09/15.
 */
public class BotControllerDocumentTest {

    private BotToken token;

    @Mock
    private BotClient botClient;

    @Mock
    private TaskListener taskListener;

    @Mock
    private PrintStream logger;

    final static int chatId = 11;
    final static String logFileName = "build_#5_log.txt";
    final static String logContents = "the logs contents";
    final static String logMimeType = "text/plain";

    private BotController subject;
    private Document document;
    private Message message;
    private Message.Response response;
    private String logMessage;
    private boolean result;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reset(botClient);
        reset(logger);

        // Create a logfile to send
        File logFile = new File(logFileName);
        logFile.deleteOnExit();
        FileOutputStream outputStream = new FileOutputStream(logFile);
        outputStream.write(logContents.getBytes());
        outputStream.close();
        document = new Document(chatId, new TypedFile(logMimeType, logFile));

        // Generate a random token
        token = new BotToken(UUID.randomUUID().toString());

        message = new Message(chatId, null);

        stub(taskListener.getLogger()).toReturn(logger);
        subject = new BotController(botClient, taskListener);
    }

    @Test
    public void given_it_gets_a_OK_response() throws Exception {

        // Given it gets a SUCCESS response
        response = new Message.Response(message);
        stub(botClient.sendDocument(anyString(), anyInt(), any(TypedFile.class))).toReturn(response);

        // expect
        logMessage = "TelegramBot: successfully sent document";
        result = subject.sendDocument(token, document);

        verify(botClient, only()).sendDocument(token.toString(), chatId, document.getFile()); // It sends the document
        verify(logger, times(1)).println(logMessage); // It logs the success
        assertTrue(result); // It returns true as it is successful

    }

    @Test
    public void given_it_gets_an_OK_response_with_an_error_code() throws Exception {

        // Given it gets a OK response with an error code
        response = new Message.Response(504, "Unauthorized");
        stub(botClient.sendDocument(anyString(), anyInt(), any(TypedFile.class))).toReturn(response);

        // expect
        logMessage = "TelegramBot: failed to send document \"504 Unauthorized\"";
        result = subject.sendDocument(token, document);

        verify(botClient, only()).sendDocument(token.toString(), chatId, document.getFile()); // It sends the document
        verify(logger, times(1)).println(logMessage); // It logs the success
        assertFalse(result); // It returns false as it is unsuccessful

    }

    @Test
    public void given_it_gets_an_ERROR_response() throws Exception {

        // Given it gets a ERROR response
        Response response = new Response("http://some.url",404,"Not Found",new ArrayList<Header>(), null);
        stub(botClient.sendDocument(anyString(), anyInt(), any(TypedFile.class))).toThrow(RetrofitError.httpError("http://some.url", response, RetroAdapter.getGsonConverter(), Message.Response.class));

        // expect
        logMessage = "TelegramBot: " + "failed to send document \"404 Not Found\"";
        result = subject.sendDocument(token, document);

        verify(botClient, only()).sendDocument(token.toString(), chatId, document.getFile()); // It sends the message
        verify(logger, times(1)).println(logMessage); // It logs the success
        assertFalse(result); // It returns false as it is unsuccessful

    }
}