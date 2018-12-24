package com.jim.framework.tbox.foundation.spi;

public interface ProviderManager {
  public String getProperty(String name, String defaultValue);

  public <T extends Provider> T provider(Class<T> clazz);
}
