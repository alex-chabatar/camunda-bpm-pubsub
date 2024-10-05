package org.camunda.bpm.extension.pubsub.autoconfigure;

import javax.annotation.PostConstruct;

import org.camunda.bpm.extension.pubsub.config.PubSubConfigurationFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Configuration("pubSubConnectorEmulatorConfig")
@ConditionalOnProperty(name = PubSubEmulatorConfig.PUBSUB_EMULATOR_HOST)
@Slf4j
public class PubSubEmulatorConfig {

  public static final String PUBSUB_EMULATOR_HOST = "PUBSUB_EMULATOR_HOST";

  @Value("${PUBSUB_EMULATOR_HOST}")
  private String host;

  @PostConstruct
  public void initialize() {

    ManagedChannel channel = ManagedChannelBuilder.forTarget(host).usePlaintext().build();
    log.info("managedChannel: {} for host: {}", channel, host);

    TransportChannelProvider transportChannelProvider = FixedTransportChannelProvider
        .create(GrpcTransportChannel.create(channel));
    PubSubConfigurationFactory.getInstance().get().setChannelProvider(transportChannelProvider);
    log.info("transportChannelProvider: {} for managedChannel: {}", transportChannelProvider, channel);

    CredentialsProvider credentialsProvider = NoCredentialsProvider.create();
    PubSubConfigurationFactory.getInstance().get().setCredentialsProvider(credentialsProvider);
    log.info("credentialsProvider: {}", credentialsProvider);

  }

}