package pl.javasurvival.HelloServer;

import io.vavr.collection.Map;
import io.vavr.control.Option;

public class MessageBoardService {
    public static final String FILE_NAME = "messages.jsons";
    private Map<String, Topic> topics;

    private  final  BoardMessageWriter writer = new BoardMessageWriter(FILE_NAME);

    public MessageBoardService() {
        this.topics = new BoardMessageReader().readAllTopics(FILE_NAME);
//        this.topics = List.of("Java", "Ogólny", "Dziwne")
//                .map(name -> Topic.create(name))
//                .toMap(topic -> topic.name, topic -> topic);
    }

    synchronized Option<Topic> getTopic(String topicName) {

        return this.topics.get(topicName);
    }

    synchronized Option<Topic> addMessageToTopic(String topicName, Message newMsg) {
      Option<Topic>  newTopic = getTopic(topicName).map(topic -> topic.addMessage(newMsg));
      newTopic.forEach(topic -> writer.write(topic.name, newMsg));
      Option<Map<String, Topic>> newTopics = newTopic.map(topic -> this.topics.put(topicName, topic));
      newTopics.forEach(topics -> this.topics = topics);
      return newTopic;
    }
}
