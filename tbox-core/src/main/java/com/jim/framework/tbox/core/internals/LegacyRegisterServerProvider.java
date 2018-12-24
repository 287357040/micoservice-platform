package com.jim.framework.tbox.core.internals;

import com.google.common.base.Strings;
import com.jim.framework.tbox.core.enums.Env;
import com.jim.framework.tbox.core.spi.RegisterServerProvider;
import com.jim.framework.tbox.core.util.ResourceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * For legacy meta server configuration use, i.e. apollo-env.properties
 */
public class LegacyRegisterServerProvider implements RegisterServerProvider {

  // make it as lowest as possible, yet not the lowest
  public static final int ORDER = RegisterServerProvider.LOWEST_PRECEDENCE - 1;
  private static final Map<Env, String> domains = new HashMap<>();

  public LegacyRegisterServerProvider() {
    initialize();
  }

  private void initialize() {
    Properties prop = new Properties();
    prop = ResourceUtils.readConfigFile("tbox-env.properties", prop);

    domains.put(Env.LOCAL, getRegisterServerAddress(prop, "local_register", "local.register"));
    domains.put(Env.DEV, getRegisterServerAddress(prop, "dev_register", "dev.register"));
    domains.put(Env.FAT, getRegisterServerAddress(prop, "fat_register", "fat.register"));
    domains.put(Env.UAT, getRegisterServerAddress(prop, "uat_register", "uat.register"));
    domains.put(Env.LPT, getRegisterServerAddress(prop, "lpt_register", "lpt.register"));
    domains.put(Env.PRO, getRegisterServerAddress(prop, "pro_register", "pro.register"));
  }

  private String getRegisterServerAddress(Properties prop, String sourceName, String propName) {
    // 1. Get from System Property.
    String metaAddress = System.getProperty(sourceName);
    if (Strings.isNullOrEmpty(metaAddress)) {
      // 2. Get from OS environment variable, which could not contain dot and is normally in UPPER case,like DEV_META.
      metaAddress = System.getenv(sourceName.toUpperCase());
    }
    if (Strings.isNullOrEmpty(metaAddress)) {
      // 3. Get from properties file.
      metaAddress = prop.getProperty(propName);
    }
    return metaAddress;
  }

  @Override
  public String getRegisterServerAddress(Env targetEnv) {
    String metaServerAddress = domains.get(targetEnv);
    return metaServerAddress == null ? null : metaServerAddress.trim();
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
