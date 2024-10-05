package org.camunda.bpm.extension.pubsub.service;

import org.camunda.bpm.extension.pubsub.dto.PubSubMessage;

public interface PubSubService {

  String publish(PubSubMessage pubSubMessage);

}
