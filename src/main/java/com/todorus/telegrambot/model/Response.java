package com.todorus.telegrambot.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by todorus on 14/09/15.
 *
 * Class which defines the shared fields of the responses that the Telegram Bot API defines. Should be
 * implemented by other Models, as the "result" field differs in type across the methods in the API.
 *
 *
 * From: https://core.telegram.org/bots/api#making-requests
 * ------------------------------------------------------
 * The response contains a JSON object, which always has a Boolean field ‘ok’ and may have an optional
 * String field ‘description’ with a human-readable description of the result. If ‘ok’ equals true,
 * the request was successful and the result of the query can be found in the ‘result’ field.
 *
 * In case of an unsuccessful request, ‘ok’ equals false and the error is explained in the ‘description’.
 * An Integer ‘error_code’ field is also returned, but its contents are subject to change in the future.
 * ------------------------------------------------------
 *
 */
public abstract class Response {

    protected boolean ok;

    protected String description;

    // Contents are subject to change
    @SerializedName("error_code")
    protected int errorCode;

    protected Response(){}

    /**
     * (for unittesting only)
     * Simulate an Error response from the Telegram Bot API
     * @param errorCode
     * @param description
     */
    protected Response(int errorCode, String description){
        this.errorCode = errorCode;
        this.description = description;
        this.ok = false;
    }

    public boolean isOk() {
        return ok;
    }

    public String getDescription() {
        return description;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
