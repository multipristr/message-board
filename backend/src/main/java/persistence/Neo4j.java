package persistence;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Neo4j {
    private static final GraphDatabaseService DATABASE_SERVICE = new GraphDatabaseFactory().newEmbeddedDatabase(new File("build/neo4j-db"));

    public GraphDatabaseService getDatabaseService() {
        return DATABASE_SERVICE;
    }
}
