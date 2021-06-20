package repository;

import model.Message;
import org.neo4j.graphdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import persistence.Neo4j;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Repository
public class Neo4jMessageRepository implements IMessageRepository {
    private static final Label MESSAGE_LABEL = Label.label("Message");
    private static final RelationshipType PARENT_RELATIONSHIP = RelationshipType.withName("PARENT_OF");
    private final GraphDatabaseService neo4j;

    @Autowired
    public Neo4jMessageRepository(Neo4j neo4j) {
        this.neo4j = neo4j.getDatabaseService();
    }

    @Override
    public UUID saveMessage(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID());
        }
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(ZonedDateTime.now());
        }
        message.setLastModifiedAt(ZonedDateTime.now());

        try (Transaction transaction = neo4j.beginTx()) {
            Node node = neo4j.createNode(MESSAGE_LABEL);
            node.setProperty("id", message.getId().toString());
            node.setProperty("createdAt", message.getCreatedAt());
            node.setProperty("author", message.getAuthor());
            node.setProperty("lastModifiedAt", message.getLastModifiedAt());
            node.setProperty("content", message.getContent());

            if (message.getParentId() != null) {
                Node parent = neo4j.findNode(MESSAGE_LABEL, "id", message.getParentId().toString());
                parent.createRelationshipTo(node, PARENT_RELATIONSHIP);
            }

            transaction.success();
        }

        return message.getId();
    }

    @Override
    public ZonedDateTime updateMessage(Message message) {
        message.setLastModifiedAt(ZonedDateTime.now());

        try (Transaction transaction = neo4j.beginTx()) {
            Node node = neo4j.findNode(MESSAGE_LABEL, "id", message.getId().toString());
            node.setProperty("content", message.getContent());
            node.setProperty("lastModifiedAt", message.getLastModifiedAt());

            transaction.success();
        }

        return message.getLastModifiedAt();
    }

    @Override
    public void deleteMessage(UUID id) {
        try (Transaction transaction = neo4j.beginTx()) {
            neo4j.execute("MATCH (parent:Message {id:$id})-[*0..]->(child) DETACH DELETE parent, child", Collections.singletonMap("id", id.toString()));
            transaction.success();
        }
    }

    private Message toMessage(Node node) {
        return new Message()
                .setContent((String) node.getProperty("content"))
                .setId(UUID.fromString((String) node.getProperty("id")))
                .setAuthor((String) node.getProperty("author"))
                .setLastModifiedAt((ZonedDateTime) node.getProperty("lastModifiedAt"))
                .setCreatedAt((ZonedDateTime) node.getProperty("createdAt"));
    }

    @Override
    public Optional<Message> selectOneMessage(UUID id) {
        Optional<Message> message;
        try (Transaction transaction = neo4j.beginTx()) {
            message = Optional.ofNullable(neo4j.findNode(MESSAGE_LABEL, "id", id.toString())).map(this::toMessage);
            transaction.success();
        }
        return message;
    }

    @Override
    public List<Message> selectTopLevelMessages() {
        List<Message> topLevelMessages;
        try (Transaction transaction = neo4j.beginTx()) {
            topLevelMessages = neo4j.execute("MATCH (p:Message) WHERE NOT (p)<-[:PARENT_OF]-(:Message) RETURN p ORDER BY p.createdAt").columnAs("p")
                    .stream()
                    .map(node -> toMessage((Node) node))
                    .collect(Collectors.toList());
            transaction.success();
        }
        return topLevelMessages;
    }

    @Override
    public List<Message> selectChildMessages(UUID parentId) {
        List<Message> children;
        try (Transaction transaction = neo4j.beginTx()) {
            children = neo4j.execute("MATCH (parent:Message)-[:PARENT_OF]->(child:Message) WHERE parent.id=$parentId RETURN child ORDER BY child.createdAt", Collections.singletonMap("parentId", parentId.toString()))
                    .columnAs("child")
                    .stream()
                    .map(node -> toMessage((Node) node))
                    .collect(Collectors.toList());
            transaction.success();
        }
        return children;
    }
}
