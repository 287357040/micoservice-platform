package com.jim.framework.tbox.foundation.internals.provider;

import com.jim.framework.tbox.foundation.internals.Utils;
import com.jim.framework.tbox.foundation.internals.io.BOMInputStream;
import com.jim.framework.tbox.foundation.spi.Provider;
import com.jim.framework.tbox.foundation.spi.ServerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by celiang.hu on 2018-12-18.
 */
public class DefaultServerProvider implements ServerProvider {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServerProvider.class);
    private static final String SERVER_PROPERTIES_LINUX = "/opt/settings/server.properties";
    private static final String SERVER_PROPERTIES_WINDOWS = "C:/opt/settings/server.properties";

    private Properties serverProperties = new Properties();
    private String env;
    private String dc;

    @Override
    public String getEnvType() {
        return env;
    }

    @Override
    public boolean isEnvTypeSet() {
        return false;
    }

    @Override
    public String getDataCenter() {
        return dc;
    }

    @Override
    public boolean isDataCenterSet() {
        return false;
    }

    @Override
    public void initialize(InputStream in) throws IOException {
        try {
            if (in != null) {
                try {
                    serverProperties.load(new InputStreamReader(new BOMInputStream(in), StandardCharsets.UTF_8));
                } finally {
                    in.close();
                }
            }

            initEnvType();
            initDataCenter();
        } catch (Throwable ex) {
            logger.error("Initialize DefaultServerProvider failed.", ex);
        }
    }

    private void initDataCenter() {
        // 1. Try to get environment from JVM system property
        dc = System.getProperty("idc");
        if (!Utils.isBlank(dc)) {
            dc = dc.trim();
            logger.info("Data Center is set to [{}] by JVM system property 'idc'.", dc);
            return;
        }

        // 2. Try to get idc from OS environment variable
        dc = System.getenv("IDC");
        if (!Utils.isBlank(dc)) {
            dc = dc.trim();
            logger.info("Data Center is set to [{}] by OS env variable 'IDC'.", dc);
            return;
        }

        // 3. Try to get idc from from file "server.properties"
        dc = serverProperties.getProperty("idc");
        if (!Utils.isBlank(dc)) {
            dc = dc.trim();
            logger.info("Data Center is set to [{}] by property 'idc' in server.properties.", dc);
            return;
        }

        // 4. Set Data Center to null.
        dc = null;
        logger.debug("Data Center is set to null. Because it is not available in either (1) JVM system property 'idc', (2) OS env variable 'IDC' nor (3) property 'idc' from the properties InputStream.");

    }

    private void initEnvType() {
        // 1. Try to get environment from JVM system property
        env = System.getProperty("env");
        if (!Utils.isBlank(env)) {
            env = env.trim();
            logger.info("Environment is set to [{}] by JVM system property 'env'.", env);
            return;
        }

        // 2. Try to get environment from OS environment variable
        env = System.getenv("ENV");
        if (!Utils.isBlank(env)) {
            env = env.trim();
            logger.info("Environment is set to [{}] by OS env variable 'ENV'.", env);
            return;
        }

        // 3. Try to get environment from file "server.properties"
        env = serverProperties.getProperty("env");
        if (!Utils.isBlank(env)) {
            env = env.trim();
            logger.info("Environment is set to [{}] by property 'env' in server.properties.", env);
            return;
        }

        // 4. Set environment to null.
        env = null;
        logger.info("Environment is set to null. Because it is not available in either (1) JVM system property 'env', (2) OS env variable 'ENV' nor (3) property 'env' from the properties InputStream.");

    }

    @Override
    public Class<? extends Provider> getType() {
        return ServerProvider.class;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if ("env".equalsIgnoreCase(key)) {
            String val = getEnvType();
            return val == null ? defaultValue : val;
        } else if ("dc".equalsIgnoreCase(key)) {
            String val = getDataCenter();
            return val == null ? defaultValue : val;
        } else {
            String val = serverProperties.getProperty(key, defaultValue);
            return val == null ? defaultValue : val.trim();
        }
    }

    @Override
    public void initialize() {
        try {
            String path = Utils.isOSWindows() ? SERVER_PROPERTIES_WINDOWS : SERVER_PROPERTIES_LINUX;

            File file = new File(path);
            if (file.exists() && file.canRead()) {
                logger.info("Loading {}", file.getAbsolutePath());
                FileInputStream fis = new FileInputStream(file);
                initialize(fis);
                return;
            }

            initialize(null);
        } catch (Throwable ex) {
            logger.error("Initialize DefaultServerProvider failed.", ex);
        }
    }

    @Override
    public String toString() {
        return "environment [" + getEnvType() + "] data center [" + getDataCenter() + "] properties: " + serverProperties
                + " (DefaultServerProvider)";
    }
}
