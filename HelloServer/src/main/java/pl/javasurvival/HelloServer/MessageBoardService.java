package pl.javasurvival.HelloServer;

import io.vavr.collection.Map;
import io.vavr.control.Option;

public class MessageBoardService {
    private Map<String, Topic> topics;

    Option<Topic> getTopic(String topicName) {
        return this.topics.get(topicName);
    }

    Option<Topic> addMessageToTopic(String topicName, Message newMsg) {
      Option<Topic>  newTopic = getTopic(topicName).map(topic -> topic.addMessage(newMsg));
    }
}
