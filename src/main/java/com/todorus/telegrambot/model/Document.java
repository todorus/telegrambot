package com.todorus.telegrambot.model;

import hudson.model.AbstractBuild;
import hudson.model.Result;
import retrofit.mime.TypedFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by todorus on 16/09/15.
 */
public class Document {

    private long chatId;

    private TypedFile file;

    public Document(long chatId, TypedFile file) {
        this.chatId = chatId;
        this.file = file;
    }

    public long getChatId() {
        return chatId;
    }

    public TypedFile getFile() {
        return file;
    }

    public static Document getLogDocument(Chat chat, AbstractBuild build) {
        if (build.getLogFile() == null) {
            return null;
        }

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            // Generate filename
            String fileName = build.getFullDisplayName()+" log.txt";
            fileName =  fileName.replace(" ", "_");

            // Create a temporary logfile with the desired filename
            File logFile = new File(fileName);
            logFile.deleteOnExit();
            fileInputStream = new FileInputStream(build.getLogFile());
            fileOutputStream = new FileOutputStream(logFile);

            int read = 0;
            byte[] buffer = new byte[1024];

            while ((read = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }

            // add the logfile to a Document
            Document document = new Document(chat.getChatId(), new TypedFile("text/plain", logFile));

            return document;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
