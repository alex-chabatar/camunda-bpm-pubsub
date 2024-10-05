package org.camunda.bpm.extension.pubsub.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public class PubSubMessage implements Serializable {

  String projectName;

  String topicName;

  Map<String, Object> attributes;

  String data;

};


