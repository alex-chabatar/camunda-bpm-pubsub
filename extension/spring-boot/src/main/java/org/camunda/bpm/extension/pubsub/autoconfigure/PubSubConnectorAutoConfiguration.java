package org.camunda.bpm.extension.pubsub.autoconfigure;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.camunda.bpm.extension.pubsub.config.PubSubConfiguration;
import org.camunda.bpm.extension.pubsub.config.PubSubConfigurationFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.camunda.bpm.extension.pubsub")
public class PubSubConnectorAutoConfiguration {

  @Value("${PUBSUB_PROJECT_NAME:#{null}}")
  private String projectName;

  @Value("${PUBSUB_TOPIC_NAME:#{null}}")
  private String topicName;

  @PostConstruct
  public void initialize() {
    PubSubConfiguration factory = PubSubConfigurationFactory.getInstance().get();
    Optional.ofNullable(projectName).ifPresent(factory::setProjectName);
    Optional.ofNullable(topicName).ifPresent(factory::setTopicName);
  }

}
