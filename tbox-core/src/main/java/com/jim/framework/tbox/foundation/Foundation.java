package com.jim.framework.tbox.foundation;

import com.jim.framework.tbox.foundation.internals.NullProviderManager;
import com.jim.framework.tbox.foundation.internals.ServiceBootstrap;
import com.jim.framework.tbox.foundation.spi.ApplicationProvider;
import com.jim.framework.tbox.foundation.spi.NetworkProvider;
import com.jim.framework.tbox.foundation.spi.ProviderManager;
import com.jim.framework.tbox.foundation.spi.ServerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by celiang.hu on 2018-12-18.
 */
public class Foundation {
    private static final Logger logger = LoggerFactory.getLogger(Foundation.class);
    private static Object lock = new Object();

    private static volatile ProviderManager manager;

    // Encourage early initialization and fail early if it happens.
    static {
        getManager();
    }

    // create provider manager has concurent,neet to add lock
    private static ProviderManager getManager() {
        try {
            if (manager == null) {
                // Double locking to make sure only one thread initializes ProviderManager.
                synchronized (lock) {
                    if (manager == null) {
                        manager = ServiceBootstrap.loadFirst(ProviderManager.class);
                    }
                }
            }

            return manager;
        } catch (Throwable ex) {
            manager = new NullProviderManager();
            logger.error("Initialize ProviderManager failed.", ex);
            return manager;
        }
    }

    public static String getProperty(String name, String defaultValue) {
        try {
            return getManager().getProperty(name, defaultValue);
        } catch (Throwable ex) {
            logger.error("getProperty for {} failed.", name, ex);
            return defaultValue;
        }
    }

    public static NetworkProvider net() {
        try {
            return getManager().provider(NetworkProvider.class);
        } catch (Exception ex) {
            logger.error("Initialize NetworkProvider failed.", ex);
            return NullProviderManager.provider;
        }
    }

    public static ServerProvider server() {
        try {
            return getManager().provider(ServerProvider.class);
        } catch (Exception ex) {
            logger.error("Initialize ServerProvider failed.", ex);
            return NullProviderManager.provider;
        }
    }

    public static ApplicationProvider app() {
        try {
            return getManager().provider(ApplicationProvider.class);
        } catch (Exception ex) {
            logger.error("Initialize ApplicationProvider failed.", ex);
            return NullProviderManager.provider;
        }
    }
}
