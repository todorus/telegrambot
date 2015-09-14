package com.todorus.telegrambot.control;

import com.todorus.telegrambot.model.Message;
import com.todorus.telegrambot.model.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by todorus on 14/09/15.
 */
public interface BotClient {

    @POST("/bot{token}/sendMessage")
    public Message.Response sendMessage(@Path("token") String token, @Body Message message);

}
