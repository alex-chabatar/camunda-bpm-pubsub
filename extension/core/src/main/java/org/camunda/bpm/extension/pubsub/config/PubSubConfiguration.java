package org.camunda.bpm.extension.pubsub.config;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class PubSubConfiguration {

  /**
   * Custom channel provider, for example when using PubSub Simulator
   */
  private TransportChannelProvider channelProvider;

  /**
   * Custom credentials provider, for example when using PubSub Simulator
   */
  private CredentialsProvider credentialsProvider;

  /**
   * Default GCP project name
   */
  private String projectName;

  /**
   * Default GCP topic name
   */
  private String topicName;

}
