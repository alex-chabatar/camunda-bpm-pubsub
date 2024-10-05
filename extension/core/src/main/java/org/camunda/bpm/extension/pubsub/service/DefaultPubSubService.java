package org.camunda.bpm.extension.pubsub.service;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.camunda.bpm.extension.pubsub.config.PubSubConfiguration;
import org.camunda.bpm.extension.pubsub.config.PubSubConfigurationFactory;
import org.camunda.bpm.extension.pubsub.dto.PubSubMessage;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Topic;
import com.google.pubsub.v1.TopicName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultPubSubService implements PubSubService {

  @Override
  public String publish(PubSubMessage pubSubMessage) {

    Publisher publisher = null;

    try {

      publisher = publisher(pubSubMessage.getProjectName(), pubSubMessage.getTopicName());

      PubsubMessage.Builder builder = PubsubMessage.newBuilder();

      Optional.ofNullable(pubSubMessage.getAttributes())
          .ifPresent(values -> builder.putAllAttributes(
              values.entrySet().stream()
                  .collect(toMap(
                      Map.Entry::getKey,
                      e -> Optional.ofNullable(e.getValue())
                          .map(Objects::toString)
                          .orElse("")))));

      Optional.ofNullable(pubSubMessage.getData())
          .ifPresent(value -> builder.setData(ByteString.copyFrom(value.getBytes(StandardCharsets.UTF_8))));

      PubsubMessage message = builder.build();

      // Once published, returns a server-assigned message id (unique within the topic)
      log.info("Publishing message to PubSub: {}", message);

      ApiFuture<String> messageIdFuture = publisher.publish(message);
      return messageIdFuture.get();

    } catch (Exception ex) {

      log.error(ex.getMessage(), ex);
      throw new RuntimeException(ex);

    } finally {

      if (publisher != null) {

        // When finished with the publisher, shutdown to free up resources.
        publisher.shutdown();

        try {
          publisher.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

      }

    }

  }

  private Publisher publisher(String projectName, String topicName) throws IOException {

    Publisher.Builder publisherBuilder = Publisher.newBuilder(TopicName.of(projectName, topicName));

    PubSubConfiguration configurationFactory = PubSubConfigurationFactory.getInstance().get();
    TransportChannelProvider channelProvider = configurationFactory.getChannelProvider();
    CredentialsProvider credentialsProvider = configurationFactory.getCredentialsProvider();

    Optional.ofNullable(channelProvider)
        .ifPresent(publisherBuilder::setChannelProvider);
    Optional.ofNullable(credentialsProvider)
        .ifPresent(publisherBuilder::setCredentialsProvider);

    // initialize TopicAdminClient
    initializeTopicAdminClient();

    if (!hasTopic(projectName, topicName)) {
      createTopic(projectName, topicName);
    }

    Publisher publisher = publisherBuilder.build();

    log.info("created publisher: {}", publisher);
    log.info("will publish to {}/{}", projectName, topicName);

    return publisher;

  }

  private TopicAdminClient topicAdminClient;

  private void initializeTopicAdminClient() throws IOException {
    if (topicAdminClient == null) {
      PubSubConfiguration configurationFactory = PubSubConfigurationFactory.getInstance().get();
      TransportChannelProvider channelProvider = configurationFactory.getChannelProvider();
      CredentialsProvider credentialsProvider = configurationFactory.getCredentialsProvider();
      if (channelProvider != null && credentialsProvider != null) {
        topicAdminClient = TopicAdminClient.create(
            TopicAdminSettings.newBuilder()
                .setTransportChannelProvider(channelProvider)
                .setCredentialsProvider(credentialsProvider)
                .build());
      } else {
        topicAdminClient = TopicAdminClient.create();
      }
    }
  }

  private boolean hasTopic(String projectId, String topicId) {
    try {
      Topic topic = topicAdminClient.getTopic(TopicName.of(projectId, topicId));
      return topic != null;
    } catch (Exception ex) {
      log.warn(ex.getMessage());
      return false;
    }
  }

  private void createTopic(String projectId, String topicId) {
    try {
      Topic topic = topicAdminClient.createTopic(TopicName.of(projectId, topicId));
      log.info("Created topic: {}", topic);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }

}
