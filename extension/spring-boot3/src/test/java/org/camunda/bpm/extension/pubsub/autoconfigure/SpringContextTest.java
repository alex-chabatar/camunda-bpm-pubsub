package org.camunda.bpm.extension.pubsub.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.camunda.bpm.extension.pubsub.config.PubSubConfigurationFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.google.api.gax.core.NoCredentialsProvider;

@SpringBootTest(classes = SpringContextTest.TestConfig.class)
class SpringContextTest {

  @Value("${PUBSUB_PROJECT_NAME}")
  private String projectName;

  @Value("${PUBSUB_TOPIC_NAME}")
  private String topicName;

  @Test
  void contextLoaded() {

    var factory = PubSubConfigurationFactory.getInstance().get();

    assertThat(factory).isNotNull();

    assertThat(factory.getProjectName()).isEqualTo(projectName);
    assertThat(factory.getTopicName()).isEqualTo(topicName);

    assertThat(factory.getChannelProvider()).isNotNull();
    assertThat(factory.getCredentialsProvider()).isNotNull().isInstanceOf(NoCredentialsProvider.class);

  }

  @SpringBootConfiguration
  @ComponentScan("org.camunda.bpm.extension.pubsub")
  public static class TestConfig {
  }

}
