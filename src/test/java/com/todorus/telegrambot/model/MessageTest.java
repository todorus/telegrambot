package com.todorus.telegrambot.model;

import hudson.model.AbstractBuild;
import hudson.model.Result;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by todorus on 15/09/15.
 */
public class MessageTest {

    @Mock
    private AbstractBuild build;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void it_generates_the_correct_message_content() throws Exception {
        Message.Builder builder = new Message.Builder();
        Result result;

        String fullName = "the full name";
        stub(build.getFullDisplayName()).toReturn(fullName);

        // Given it fails
        result = Result.FAILURE;
        stub(build.getResult()).toReturn(result);
        assertEquals("the full name FAILURE", builder.setBuild(build).build().getText());

        // Given it is unstable
        result = Result.UNSTABLE;
        stub(build.getResult()).toReturn(result);
        assertEquals("the full name UNSTABLE", builder.setBuild(build).build().getText());

        // Given it is succesful
        result = Result.SUCCESS;
        stub(build.getResult()).toReturn(result);
        assertEquals("the full name SUCCESS", builder.setBuild(build).build().getText());
    }

}