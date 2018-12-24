package com.jim.framework.tbox.foundation.internals.provider;

import com.jim.framework.tbox.foundation.internals.Utils;
import com.jim.framework.tbox.foundation.internals.io.BOMInputStream;
import com.jim.framework.tbox.foundation.spi.ApplicationProvider;
import com.jim.framework.tbox.foundation.spi.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DefaultApplicationProvider implements ApplicationProvider {
  private static final Logger logger = LoggerFactory.getLogger(DefaultApplicationProvider.class);
  public static final String APP_PROPERTIES_CLASSPATH = "/META-INF/app.properties";
  private Properties appProperties = new Properties();

  private String appId;

  @Override
  public void initialize() {
    try {
      InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_PROPERTIES_CLASSPATH);
      if (in == null) {
        in = DefaultApplicationProvider.class.getResourceAsStream(APP_PROPERTIES_CLASSPATH);
      }

      initialize(in);
    } catch (Throwable ex) {
      logger.error("Initialize DefaultApplicationProvider failed.", ex);
    }
  }

  @Override
  public void initialize(InputStream in) {
    try {
      if (in != null) {
        try {
          appProperties.load(new InputStreamReader(new BOMInputStream(in), StandardCharsets.UTF_8));
        } finally {
          in.close();
        }
      }

      initAppId();
    } catch (Throwable ex) {
      logger.error("Initialize DefaultApplicationProvider failed.", ex);
    }
  }

  @Override
  public String getAppId() {
    return appId;
  }

  @Override
  public boolean isAppIdSet() {
    return !Utils.isBlank(appId);
  }

  @Override
  public String getProperty(String name, String defaultValue) {
    if ("app.id".equals(name)) {
      String val = getAppId();
      return val == null ? defaultValue : val;
    } else {
      String val = appProperties.getProperty(name, defaultValue);
      return val == null ? defaultValue : val;
    }
  }

  @Override
  public Class<? extends Provider> getType() {
    return ApplicationProvider.class;
  }

  private void initAppId() {
    // 1. Get app.id from System Property
    appId = System.getProperty("app.id");
    if (!Utils.isBlank(appId)) {
      appId = appId.trim();
      logger.info("App ID is set to {} by app.id property from System Property", appId);
      return;
    }

    // 2. Try to get app id from app.properties.
    appId = appProperties.getProperty("app.id");
    if (!Utils.isBlank(appId)) {
      appId = appId.trim();
      logger.info("App ID is set to {} by app.id property from {}", appId, APP_PROPERTIES_CLASSPATH);
      return;
    }

    appId = null;
    logger.warn("app.id is not available from System Property and {}. It is set to null", APP_PROPERTIES_CLASSPATH);
  }

  @Override
  public String toString() {
    return "appId [" + getAppId() + "] properties: " + appProperties + " (DefaultApplicationProvider)";
  }
}
