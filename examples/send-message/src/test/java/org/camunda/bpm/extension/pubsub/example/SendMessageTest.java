package org.camunda.bpm.extension.pubsub.example;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.camunda.bpm.extension.pubsub.PubSubSendRequest.*;
import static org.camunda.bpm.extension.pubsub.PubSubSendResponse.PARAM_MESSAGE_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.validateMockitoUsage;

import java.util.Map;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.test.TestHelper;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.pubsub.config.PubSubConfiguration;
import org.camunda.bpm.extension.pubsub.config.PubSubConfigurationFactory;
import org.camunda.bpm.extension.pubsub.dto.PubSubMessage;
import org.camunda.bpm.extension.pubsub.service.PubSubService;
import org.camunda.bpm.extension.pubsub.service.PubSubServiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = SendMessageTest.TestConfig.class)
@ExtendWith(MockitoExtension.class)
class SendMessageTest {

  @SpringBootConfiguration
  @ComponentScan("org.camunda.bpm.extension.pubsub")
  public static class TestConfig {
  }

  // --- PubSub

  @Mock
  protected TransportChannelProvider transportChannelProvider;

  @Mock
  protected CredentialsProvider credentialsProvider;

  @Mock
  protected PubSubService pubSubService;

  @BeforeEach
  public void setUp() {

    PubSubConfiguration pubSubConfiguration = PubSubConfigurationFactory.getInstance().get();
    pubSubConfiguration.setChannelProvider(transportChannelProvider); // Mock
    pubSubConfiguration.setCredentialsProvider(credentialsProvider); // Mock

    PubSubServiceFactory.getInstance().set(pubSubService); // Mock

  }

  @AfterEach
  public void tearDown() {
    validateMockitoUsage();
  }

  @Test
  void ensureProcess() {

    // create process

    Map<String, Object> model = ImmutableMap.of(
        "name", "John Doe",
        "age", 50);

    ProcessInstance processInstance = runtimeService()
        .startProcessInstanceByKey("SendPubSubMessage", model);

    assertThat(processInstance)
        .isStarted()
        .isWaitingAt("Task_DefinePayload")
        .hasVariables("name", "age", PARAM_PROJECT_NAME, PARAM_TOPIC_NAME);

    // Task_DefinePayload

    String projectName = "test-project-new";
    String topicName = "test-topic-new";
    String data = "{ \"message\": \"Hello PubSub\" }";

    Map<String, Object> definePayloadResults = ImmutableMap.of(
        PARAM_PROJECT_NAME, projectName,
        PARAM_TOPIC_NAME, topicName,
        PARAM_DATA, data);

    String messageId = "1";

    Mockito.when(pubSubService.publish(any(PubSubMessage.class)))
        .thenReturn(messageId);

    complete(task("Task_DefinePayload"), definePayloadResults);

    // Task_SendPubSubMessage

    waitForJobExecutorToProcessAllJobs();

    // Task_ReviewResults

    assertThat(processInstance)
        .isWaitingAt("Task_ReviewResults")
        .hasVariables("name", "age", PARAM_PROJECT_NAME, PARAM_TOPIC_NAME, PARAM_DATA, PARAM_MESSAGE_ID);

    Map<String, Object> attributes = Maps.newHashMap(model);
    attributes.putAll(definePayloadResults);

    PubSubMessage expectedPubSubMessage = PubSubMessage.builder()
        .projectName(projectName)
        .topicName(topicName)
        .attributes(attributes)
        .data(data)
        .build();

    Mockito.verify(pubSubService, times(1))
        .publish(eq(expectedPubSubMessage));

    complete(task("Task_ReviewResults"));

    assertThat(processInstance).isEnded();

  }

  private void waitForJobExecutorToProcessAllJobs() {
    waitForJobExecutorToProcessAllJobs(60 * 1000L, 25L);
  }

  private void waitForJobExecutorToProcessAllJobs(long maxMillisToWait, long intervalMillis) {
    TestHelper.waitForJobExecutorToProcessAllJobs(
        (ProcessEngineConfigurationImpl) processEngine().getProcessEngineConfiguration(),
        maxMillisToWait,
        intervalMillis);
  }

}
