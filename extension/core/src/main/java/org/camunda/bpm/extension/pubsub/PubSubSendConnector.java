package org.camunda.bpm.extension.pubsub;

import org.camunda.bpm.extension.pubsub.dto.PubSubMessage;
import org.camunda.bpm.extension.pubsub.service.PubSubService;
import org.camunda.bpm.extension.pubsub.service.PubSubServiceFactory;
import org.camunda.connect.impl.AbstractConnector;
import org.camunda.connect.spi.ConnectorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PubSubSendConnector extends AbstractConnector<PubSubSendRequest, PubSubSendResponse> {

  public static final String CONNECTOR_ID = "pubsub-send";

  public PubSubSendConnector() {
    super(CONNECTOR_ID);
  }

  @Override
  public PubSubSendRequest createRequest() {
    return new PubSubSendRequest(this);
  }

  @Override
  public ConnectorResponse execute(PubSubSendRequest request) {

    PubSubService pubSubService = PubSubServiceFactory.getInstance().get();

    String messageId = pubSubService.publish(
        PubSubMessage.builder()
            .projectName(request.getProjectName())
            .topicName(request.getTopicName())
            .attributes(request.getAttributes())
            .data(request.getData())
            .build());

    log.info("Published message ID: {}", messageId);

    return new PubSubSendResponse(messageId);

  }

}
