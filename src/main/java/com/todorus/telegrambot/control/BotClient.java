package com.todorus.telegrambot.control;

import com.todorus.telegrambot.model.Message;
import retrofit.http.*;
import retrofit.mime.TypedFile;

/**
 * Created by todorus on 14/09/15.
 */
public interface BotClient {

    @POST("/bot{token}/sendMessage")
    public Message.Response sendMessage(@Path("token") String token, @Body Message message);

    @Multipart
    @POST("/bot{token}/sendDocument")
    public Message.Response sendDocument(@Path("token") String token, @Query("chat_id") int chatId, @Part("document") TypedFile document);

}
