package org.camunda.bpm.extension.pubsub.service;

import org.camunda.bpm.extension.pubsub.AbstractFactory;

public class PubSubServiceFactory extends AbstractFactory<PubSubService> {

  private static final PubSubServiceFactory INSTANCE = new PubSubServiceFactory();

  private PubSubServiceFactory() {
    // to avoid instantiation
  }

  public static PubSubServiceFactory getInstance() {
    return INSTANCE;
  }

  @Override
  protected PubSubService createInstance() {
    return new DefaultPubSubService();
  }

}
