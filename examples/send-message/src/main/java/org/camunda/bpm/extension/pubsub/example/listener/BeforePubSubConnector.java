package org.camunda.bpm.extension.pubsub.example.listener;

import static org.camunda.bpm.extension.pubsub.PubSubSendRequest.PARAM_PROJECT_NAME;
import static org.camunda.bpm.extension.pubsub.PubSubSendRequest.PARAM_TOPIC_NAME;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.pubsub.config.PubSubConfiguration;
import org.camunda.bpm.extension.pubsub.config.PubSubConfigurationFactory;
import org.springframework.stereotype.Service;

@Service
public class BeforePubSubConnector implements ExecutionListener {

  @Override
  public void notify(DelegateExecution execution) {
    PubSubConfiguration factory = PubSubConfigurationFactory.getInstance().get();
    execution.setVariable(PARAM_PROJECT_NAME, factory.getProjectName());
    execution.setVariable(PARAM_TOPIC_NAME, factory.getTopicName());
  }

}