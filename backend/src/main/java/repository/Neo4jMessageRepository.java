package repository;

import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("neo4jMessageRepository")
public class Neo4jMessageRepository implements IMessageRepository {
    private final Connection neo4j;

    @Lazy
    @Autowired
    public Neo4jMessageRepository(Connection neo4j) {
        this.neo4j = neo4j;
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

        try (PreparedStatement preparedStatement = neo4j.prepareStatement(
                "CREATE (m:Message {id: ?, createdAt: ?, author: ?, lastModifiedAt: ?, content: ?})"
        )) {
            preparedStatement.setString(1, message.getId().toString());
            preparedStatement.setString(2, message.getCreatedAt().toString());
            preparedStatement.setString(3, message.getAuthor());
            preparedStatement.setString(4, message.getLastModifiedAt().toString());
            preparedStatement.setString(5, message.getContent());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        if (message.getParentId() != null) {
            try (PreparedStatement preparedStatement = neo4j.prepareStatement(
                    "MATCH (child:Message), (parent:Message) WHERE child.id = ? AND parent.id = ? CREATE (parent)-[:PARENT_OF]->(child)"
            )) {
                preparedStatement.setString(1, message.getId().toString());
                preparedStatement.setString(2, message.getParentId().toString());
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
        }

        return message.getId();
    }

    @Override
    public ZonedDateTime updateMessage(Message message) {
        message.setLastModifiedAt(ZonedDateTime.now());

        try (PreparedStatement preparedStatement = neo4j.prepareStatement(
                "MATCH (m {id: ?}) SET m.content = ?, m.lastModifiedAt = ?"
        )) {
            preparedStatement.setString(1, message.getId().toString());
            preparedStatement.setString(2, message.getContent());
            preparedStatement.setString(3, message.getLastModifiedAt().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        return message.getLastModifiedAt();
    }

    @Override
    public void deleteMessage(UUID id) {
        try (PreparedStatement preparedStatement = neo4j.prepareStatement(
                "MATCH (parent:Message {id: ?})-[*0..]->(child) DETACH DELETE parent, child"
        )) {
            preparedStatement.setString(1, id.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private Message toMessage(ResultSet result) throws SQLException {
        return new Message()
                .setId(UUID.fromString(result.getString("m.id")))
                .setCreatedAt(ZonedDateTime.parse(result.getString("m.createdAt")))
                .setAuthor(result.getString("m.author"))
                .setLastModifiedAt(ZonedDateTime.parse(result.getString("m.lastModifiedAt")))
                .setContent(result.getString("m.content"));
    }

    @Override
    public Optional<Message> selectOneMessage(UUID id) {
        try (PreparedStatement preparedStatement = neo4j.prepareStatement(
                "MATCH (m:Message {id: ?}) RETURN m.id, m.createdAt, m.author, m.lastModifiedAt, m.content"
        )) {
            preparedStatement.setString(1, id.toString());
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? Optional.of(toMessage(result)) : Optional.empty();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @Override
    public List<Message> selectTopLevelMessages() {
        List<Message> topLevelMessages = new ArrayList<>();

        try (PreparedStatement preparedStatement = neo4j.prepareStatement(
                "MATCH (m:Message) WHERE NOT (m)<-[:PARENT_OF]-(:Message) " +
                        "RETURN m.id, m.createdAt, m.author, m.lastModifiedAt, m.content ORDER BY m.createdAt DESC"
        )) {
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                topLevelMessages.add(toMessage(results));
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        return topLevelMessages;
    }

    @Override
    public List<Message> selectChildMessages(UUID parentId) {
        List<Message> children = new ArrayList<>();

        try (PreparedStatement preparedStatement = neo4j.prepareStatement(
                "MATCH (parent:Message)-[:PARENT_OF]->(m:Message) WHERE parent.id=? " +
                        "RETURN m.id, m.createdAt, m.author, m.lastModifiedAt, m.content ORDER BY m.createdAt"
        )) {
            preparedStatement.setString(1, parentId.toString());
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                children.add(toMessage(results));
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        return children;
    }
}
