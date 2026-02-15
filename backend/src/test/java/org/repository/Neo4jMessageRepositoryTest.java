package org.repository;

import org.config.SpringConfiguration;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.ContextConfiguration;

@Disabled("Requires Neo4j instance")
@DataNeo4jTest
@ContextConfiguration(classes = SpringConfiguration.class)
class Neo4jMessageRepositoryTest extends IMessageRepositoryTest {
    @Autowired
    private Neo4jMessageRepository repository;

    @Override
    IMessageRepository getRepository() {
        return repository;
    }
}