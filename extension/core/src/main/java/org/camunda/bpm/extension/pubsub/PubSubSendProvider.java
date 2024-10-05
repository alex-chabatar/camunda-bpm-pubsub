package org.camunda.bpm.extension.pubsub;

import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class PubSubSendProvider implements ConnectorProvider {

  @Override
  public String getConnectorId() {
    return PubSubSendConnector.CONNECTOR_ID;
  }

  @Override
  public Connector<?> createConnectorInstance() {
    return new PubSubSendConnector();
  }

}
