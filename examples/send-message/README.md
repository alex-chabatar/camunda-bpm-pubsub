# Camunda BPM PubSub example - send PubSub message

This example demonstrate how to

* send a PubSub message with `attributes` map and `data` string/json to specific `projectName`/`topicName`

![Process](../../docs/SendPubSubMessage.png)

## How to run it

1. Build the executable JAR using Maven `mvn clean install`. If you want to adjust the predefined configuration, use `src/main/resources/application.properties` before you build.
2. Run the application using `java -jar target/camunda-bpm-pubsub-example-send-message.jar`.
3. Go to [Camunda Cockpit](http://localhost:8080) and start a process with a list of variables.
4. Complete `Define Payload` user task to optionally change `project/topicName` and set the `data` field (String or JSON)
5. The message will be sent to pubsub `projectName/topicName` using `pubsub-send` connector in `Send PubSub with Connector` service task
   * attributes = ${execution.getVariables()}
   * data = ${data}
6. Check that a `Review Results` user task is created - complete it. In the process there should be a new variable called `messageId`. This is the output parameter for `pubsub-send` connector.
7. Now, check your PubSub messages

### Run it as JUnit test

You can also use the [SendMessageTest](src/test/java/org/camunda/bpm/extension/pubsub/example/SendMessageTest.java) to run an example using a Spring Boot Test and a mocked PubSub service.

### Run it with Docker

See [Docker Compose](../../docker-compose.yml) with preconfigured:
* spring boot application
* connected to PubSub simulator

```shell
docker-compose build
docker-compose up
```

Go to [Camunda Cockpit](http://localhost:8888) and repeat steps 2-7 from the previous section.
