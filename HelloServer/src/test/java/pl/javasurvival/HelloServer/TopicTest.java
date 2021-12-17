package pl.javasurvival.HelloServer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicTest {

    @Test
    public void createdTopicHasNoMessages(){
        Topic created = Topic.create("testowy");

        assertTrue(created.messages.isEmpty());
    }

    @Test
    public void createdtopicHasCorrectName(){
        Topic created = Topic.create("testowy2");

        assertEquals("testowy2", created.name);
    }

    @Test
    public void createdAfterAddMessageTopicHasOneMessage(){
        Topic created = Topic.create("testowy2");
        Topic newTopic = created.addMessage(new Message("moja", "ja"));

        assertTrue( newTopic.messages.length() == 1);
    }

    @Test
    public void createdAfterAddMessageHasSameTopic(){
        Topic created = Topic.create("testowy2");
        Topic newTopic = created.addMessage(new Message("moja", "ja"));

        assertEquals( "testowy2", newTopic.name );
    }

    @Test
    public void createdAfterAdd2MessageHasTwoMessage() {
        Topic newTopic = Topic.create("testowy2")
                .addMessage(new Message("moja1", "ja"))
                .addMessage(new Message("moja2", "ja"));

        assertEquals(2, newTopic.messages.length());
    }

    @Test
    public void addedMessageIsLast() {
        Topic newTopic = Topic.create("testowy2")
                .addMessage(new Message("moja1", "ja"))
                .addMessage(new Message("moja2", "ja"))
                .addMessage(new Message("moja3", "ja"));

        assertEquals("moja3", newTopic.messages.last().content);
    }
}