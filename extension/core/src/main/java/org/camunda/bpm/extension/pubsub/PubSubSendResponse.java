package org.camunda.bpm.extension.pubsub;

import java.util.Map;

import org.camunda.connect.impl.AbstractConnectorResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PubSubSendResponse extends AbstractConnectorResponse {

  public static final String PARAM_MESSAGE_ID = "messageId";

  private final String messageId;

  @Override
  protected void collectResponseParameters(Map<String, Object> responseParameters) {
    responseParameters.put(PARAM_MESSAGE_ID, messageId);
  }

}
