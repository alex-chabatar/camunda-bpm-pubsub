package org.camunda.bpm.extension.pubsub.config;

import java.util.Properties;

import org.camunda.bpm.extension.pubsub.AbstractFactory;

public class PubSubConfigurationFactory extends AbstractFactory<PubSubConfiguration> {

  private static final String PUBSUB_PROJECT_NAME = "PUBSUB_PROJECT_NAME";
  private static final String PUBSUB_TOPIC_NAME = "PUBSUB_TOPIC_NAME";

  private static final PubSubConfigurationFactory INSTANCE = new PubSubConfigurationFactory();

  private PubSubConfigurationFactory() {
    // to avoid instantiation
  }

  public static PubSubConfigurationFactory getInstance() {
    return INSTANCE;
  }

  @Override
  protected PubSubConfiguration createInstance() {

    Properties properties = PubSubProperties.get();

    return PubSubConfiguration.builder()
        .projectName(properties.getProperty(PUBSUB_PROJECT_NAME))
        .topicName(properties.getProperty(PUBSUB_TOPIC_NAME))
        .build();

  }

}
