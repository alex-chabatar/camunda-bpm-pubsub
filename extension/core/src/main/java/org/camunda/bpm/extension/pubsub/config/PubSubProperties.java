package org.camunda.bpm.extension.pubsub.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PubSubProperties {

  private static final String ENV_PROPERTIES_PATH = "PUBSUB_CONFIG";
  private static final String PROPERTIES_CLASSPATH_PREFIX = "classpath:";
  private static final String DEFAULT_PROPERTIES_PATH = PROPERTIES_CLASSPATH_PREFIX + "pubsub-config.properties";

  private static Properties properties;

  private PubSubProperties() {}

  public static void set(Properties properties) {
    PubSubProperties.properties = properties;
  }

  public static Properties get() {
    if (properties != null) {
      return properties;
    }
    Properties properties = new Properties();
    String path = getPropertiesPath();

    try (InputStream inputStream = getPropertiesAsStream(path)) {
      if (inputStream != null) {
        properties.load(inputStream);
        PubSubProperties.properties = properties;
        return PubSubProperties.properties;
      } else {
        throw new IllegalStateException("Cannot find mail configuration at: " + path);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Unable to load mail configuration from: " + path, e);
    }
  }

  private static String getPropertiesPath() {
    return Optional.ofNullable(System.getenv(ENV_PROPERTIES_PATH))
        .orElse(DEFAULT_PROPERTIES_PATH);
  }

  protected static InputStream getPropertiesAsStream(String path) throws IOException {

    if (path.startsWith(PROPERTIES_CLASSPATH_PREFIX)) {
      String pathWithoutPrefix = path.substring(PROPERTIES_CLASSPATH_PREFIX.length());
      log.debug("load mail properties from classpath '{}'", pathWithoutPrefix);
      return PubSubProperties.class.getClassLoader().getResourceAsStream(pathWithoutPrefix);
    } else {
      Path config = Paths.get(path);
      log.debug("load mail properties from path '{}'", config.toAbsolutePath());
      File file = config.toFile();
      if (file.exists()) {
        return Files.newInputStream(file.toPath());
      } else {
        return null;
      }
    }
  }

}
