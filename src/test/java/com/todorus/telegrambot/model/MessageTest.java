package com.todorus.telegrambot.model;

import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.model.User;
import hudson.scm.ChangeLogSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by todorus on 15/09/15.
 */
public class MessageTest {

    @Mock
    private AbstractBuild build;

    @Mock
    private ChangeLogSet<TestEntry> changeLogSet;

    @Mock
    private User user;
    private String userDisplayName;

    private List<TestEntry> changes;
    private String changesString;

    private Message.Builder builder;
    private String fullName;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        builder = new Message.Builder();

        userDisplayName = "User"+new Random().nextInt();
        stub(user.getDisplayName()).toReturn(userDisplayName);

        changes = new ArrayList<TestEntry>();
        changes.add(new TestEntry(UUID.randomUUID().toString(), user, null));
        changes.add(new TestEntry(UUID.randomUUID().toString(), user, null));
        changes.add(new TestEntry(UUID.randomUUID().toString(), user, null));
        changes.add(new TestEntry(UUID.randomUUID().toString(), user, null));

        Iterator<TestEntry> iterator = changes.iterator();
        stub(build.getChangeSet()).toReturn(changeLogSet);
        stub(changeLogSet.iterator()).toReturn(iterator);

        changesString = "\n" + userDisplayName + ": " + changes.get(0).getMsg() +
                "\n" + userDisplayName + ": " + changes.get(1).getMsg() +
                "\n" + userDisplayName + ": " + changes.get(2).getMsg() +
                "\n...";

        fullName = "the full name";
        stub(build.getFullDisplayName()).toReturn(fullName);
    }

    @Test
    public void it_generates_the_correct_message_content_FAILURE() throws Exception {

        Result result = Result.FAILURE;
        stub(build.getResult()).toReturn(result);
        String message = builder.setBuild(build).build().getText();
        assertEquals(fullName+" FAILURE" + changesString, message);
    }

    @Test
    public void it_generates_the_correct_message_content_UNSTABLE() throws Exception {
        Result result = Result.UNSTABLE;
        stub(build.getResult()).toReturn(result);
        String message = builder.setBuild(build).build().getText();
        assertEquals(fullName+" UNSTABLE" + changesString, message);
    }

    @Test
    public void it_generates_the_correct_message_content_SUCCESS() throws Exception {
        Result result = Result.SUCCESS;
        stub(build.getResult()).toReturn(result);
        String message = builder.setBuild(build).build().getText();
        assertEquals(fullName+" SUCCESS" + changesString, message);
    }

    private static class TestEntry extends ChangeLogSet.Entry {

        private String message;
        private User author;
        private Collection<String> affectedPaths;

        public TestEntry(String message, User author, Collection<String> affectedPaths) {
            this.message = message;
            this.author = author;
            this.affectedPaths = affectedPaths;
        }

        @Override
        public String getMsg() {
            return message;
        }

        @Override
        public User getAuthor() {
            return author;
        }

        @Override
        public Collection<String> getAffectedPaths() {
            return affectedPaths;
        }
    }

}