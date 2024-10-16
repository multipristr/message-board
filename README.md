# message-board

![build](https://github.com/multipristr/message-board/actions/workflows/gradle.yml/badge.svg)
![build](https://github.com/multipristr/message-board/actions/workflows/gatsby.yml/badge.svg)

## A RESTful API to serve as the backend for a public message board
It should support the following features: 
- A client can create a message in the service
- A client can modify their own messages
- A client can delete their own messages
- A client can view all messages in the service 

## Running the solution
Clone and run the solution in Docker compose by executing
```shell
git clone https://github.com/hamsatom-psql/message-board.git && cd message-board && docker compose up
```
UI is running on [localhost:8000](http://localhost:8000)  
There's Swagger running at [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
API  base url is [localhost:8080](http://localhost:8080)

### UI locally 
1. down Docker compose service `message-board-frontend`
2. run
```shell
cd frontend && npm install && npm run develop
```

### API locally
1. down Docker compose service `message-board-backend`
2. have running Docker compose service `neo4j`
3. build by executing
```shell
cd backend && ./gradlew build
```
4. run Java application with main class [MessageBoardApplication](backend/src/main/java/org/MessageBoardApplication.java)

## Disabling Neo4J
You can change Neo4J repository to Java map backed repository by changing `@Bean Neo4jMessageRepository` to `@Bean InMemoryMessageRepository` in [SpringConfiguration.java](backend/src/main/java/org/config/SpringConfiguration.java)

## Database choice
I chose Neo4J because it can easily operate with message hierarchy.
If I didn't introduce replies I would opt for an SQL database.
With user login as foreign key in message table I could easily find user's messages and privileges would be more secure and flexible. 
