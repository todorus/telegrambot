package com.todorus.telegrambot.model;

import hudson.model.AbstractBuild;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.stub;

/**
 * Created by todorus on 16/09/15.
 */
public class DocumentTest {

    long chatId;
    Chat chat;

    final static String logFileName = "build_#5_log.txt";
    final static String logContents = "the logs contents";
    final static String logMimeType = "text/plain";

    @Mock
    AbstractBuild build;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        chatId = new Random().nextInt();
        chat = new Chat(chatId);

        stub(build.getFullDisplayName()).toReturn("build #5");
    }

    @Test
    public void it_retrieves_the_logfile_and_lets_the_name_reference_the_build() throws Exception {

        // Create a logfile for the build mock
        File logFile = new File("log");
        logFile.deleteOnExit();
        FileOutputStream outputStream = new FileOutputStream(logFile);
        outputStream.write(logContents.getBytes());
        outputStream.close();

        stub(build.getLogFile()).toReturn(logFile);

        Document result = Document.getLogDocument(chat, build);

        assertNotNull(result.getFile());
        assertEquals(logFileName, result.getFile().fileName());
        assertEquals(logMimeType, result.getFile().mimeType());

        // Check if the contents of the new file is the same
        byte[] logData = fileToByteArray(result.getFile().file());
        assertEquals(logContents, new String(logData));
    }

    @Test
    public void it_returns_null_when_it_cannot_create_the_log_document() throws Exception {
        stub(build.getLogFile()).toReturn(null);

        Document result = Document.getLogDocument(chat, build);

        assertNull(result);
    }

    private byte[] fileToByteArray(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int nRead;
        byte[] buffer = new byte[1024];

        while ((nRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            byteArrayOutputStream.write(buffer, 0, nRead);
        }

        byteArrayOutputStream.flush();
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        return data;
    }
}