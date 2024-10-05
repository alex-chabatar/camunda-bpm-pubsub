# Camunda BPM PubSub Starter for Spring Boot 3

This starter wraps the `camunda-bpm-pubsub-core` community connector and is intended to be used with camunda spring-boot.
The plugin configures the connectors for sending [Google PubSub messages](https://cloud.google.com/pubsub) based on the YAML/Properties file used for configuring your spring boot app, and registers the connectors upon startup.

## Install

This plugin can be used with Camunda Spring Boot Starter with version >= 7.20

1. Add the dependency:
   ```xml
   <dependency>
     <groupId>camunda-bpm-examples</groupId>
     <artifactId>camunda-bpm-pubsub-spring-boot3-starter</artifactId>
     <version>${version.camunda-bpm-pubsub}</version>
   </dependency>
   ```

2. Configure the connector.

## How to Use it?

For instructions on how to use the connectors from a service task,
see [the root project's readme](../../README.md).

## How to Configure it?

### General Configuration

Configure the plugin via a YAML/Properties file (i.e., the `application.yml`).

Please activate one of `GOOGLE_APPLICATION_CREDENTIALS` or `PUBSUB_EMULATOR_HOST`

### Example

An Example configuration can look like this

```properties
# Activate one of GOOGLE_APPLICATION_CREDENTIALS or PUBSUB_EMULATOR_HOST

# Google PubSub
# GOOGLE_APPLICATION_CREDENTIALS=classpath:your-pubsub-credentials.json

# PubSub Emulator
# PUBSUB_EMULATOR_HOST=0.0.0.0:8085

# PubSub Common
PUBSUB_PROJECT_NAME=your-project-id
PUBSUB_TOPIC_NAME=your-topic-id
```