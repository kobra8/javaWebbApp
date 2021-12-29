package pl.javasurvival.HelloServer;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageBoardService {
    public static final String FILE_NAME = "messages.jsons";
    private Map<String, Topic> topics;

//    private  final  BoardMessageWriter writer = new BoardMessageWriter(FILE_NAME);
    private  final  MongoRepository mongoRepository;

    public MessageBoardService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringIni.class);

        this.mongoRepository = context.getBean(MongoRepository.class);
        this.topics = List.ofAll(mongoRepository.findAll())
                .foldLeft(HashMap.<String, Topic>empty(),
                        (existingMap, newElement) ->
                                existingMap.put(newElement.topic,
                                        existingMap.get(newElement.topic)
                                                .getOrElse(Topic.create((newElement.topic))
                                                        .addMessage(newElement.message)))
                                );
       ensureIfTopicExist("java");
       ensureIfTopicExist("general");
       ensureIfTopicExist("test");

//        this.topics = new BoardMessageReader().readAllTopics(FILE_NAME);
//        this.topics = List.of("java", "ogolny", "dziwne")
//                .map(name -> Topic.create(name))
//                .toMap(topic -> topic.name, topic -> topic);
    }

    private void ensureIfTopicExist(String topicName) {
        this.topics = this.topics.computeIfAbsent(topicName, key -> Topic.create(key))._2;
    }

    synchronized Option<Topic> getTopic(String topicName) {

        return this.topics.get(topicName);
    }

    Option<Topic> addMessageToTopic(String topicName, Message newMsg) {
      Option<Topic>  existingTopic = getTopic(topicName);
      existingTopic.forEach(topic -> mongoRepository.save(new BoardMessage(newMsg, topicName)));
      Option<Topic> newTopic = updateTopic(topicName, newMsg, existingTopic);
      return newTopic;
    }

    private synchronized Option<Topic> updateTopic(String topicName, Message newMsg, Option<Topic> existingTopic) {
        Option<Topic>  newTopic = existingTopic.map(topic -> topic.addMessage(newMsg));
        Option<Map<String, Topic>> newTopics = newTopic.map(topic -> this.topics.put(topicName, topic));
        newTopics.forEach(topics -> this.topics = topics);
        return newTopic;
    }
}
