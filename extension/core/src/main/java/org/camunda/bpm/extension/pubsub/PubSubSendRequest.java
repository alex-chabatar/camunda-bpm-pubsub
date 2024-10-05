package org.camunda.bpm.extension.pubsub;

import static java.util.Collections.emptyMap;

import java.util.Map;
import java.util.Optional;

import org.camunda.bpm.extension.pubsub.config.PubSubConfiguration;
import org.camunda.bpm.extension.pubsub.config.PubSubConfigurationFactory;
import org.camunda.connect.impl.AbstractConnectorRequest;
import org.camunda.connect.spi.Connector;

public class PubSubSendRequest extends AbstractConnectorRequest<PubSubSendResponse> {

  public static final String PARAM_PROJECT_NAME = "projectName";
  public static final String PARAM_TOPIC_NAME = "topicName";
  public static final String PARAM_ATTRIBUTES = "attributes"; // Map<String, String>
  public static final String PARAM_DATA = "data"; // String

  private final PubSubConfiguration configuration;

  public PubSubSendRequest(Connector connector) {
    super(connector);
    this.configuration = PubSubConfigurationFactory.getInstance().get();
  }

  public String getProjectName() {
    return Optional.ofNullable(getRequestParameter(PARAM_PROJECT_NAME))
        .map(String::valueOf)
        .orElse(configuration.getProjectName());
  }

  public String getTopicName() {
    return Optional.ofNullable(getRequestParameter(PARAM_TOPIC_NAME))
        .map(String::valueOf)
        .orElse(configuration.getTopicName());
  }

  public Map<String, Object> getAttributes() {
    Map<String, Object> attributes = getRequestParameter(PARAM_ATTRIBUTES);
    return Optional.ofNullable(attributes).orElse(emptyMap());
  }

  public String getData() {
    return Optional.ofNullable(getRequestParameter(PARAM_DATA))
        .map(String::valueOf)
        .orElse("");
  }

}
