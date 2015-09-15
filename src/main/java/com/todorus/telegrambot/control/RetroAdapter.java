package com.todorus.telegrambot.control;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.todorus.telegrambot.TelegramBotPublisher;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import java.util.concurrent.TimeUnit;

/**
 * Created by todorus on 14/09/15.
 */
public class RetroAdapter {

    public static Client client;

    public static RestAdapter getAdapter(){
        return getAdapter(TelegramBotPublisher.ENDPOINT);
    }

    public static RestAdapter getAdapter(String baseUrl){
        // explicitly set the timeout values, so that this can be used in the background
        if(client == null) {
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(60, TimeUnit.SECONDS);
            httpClient.setReadTimeout(60, TimeUnit.SECONDS);
            client = new OkClient(new OkHttpClient());
        }

        RestAdapter.Builder  builder =  new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setConverter(getGsonConverter())
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setClient(client);

        return builder.build();
    }

    public static GsonConverter getGsonConverter(){
        return new GsonConverter(new Gson());
    }

}
