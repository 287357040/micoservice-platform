package com.jim.framework.tbox.foundation.internals.provider;

import com.jim.framework.tbox.foundation.internals.NetworkManager;
import com.jim.framework.tbox.foundation.spi.NetworkProvider;
import com.jim.framework.tbox.foundation.spi.Provider;

/**
 * Created by celiang.hu on 2018-12-18.
 */
public class DefaultNetworkProvider implements NetworkProvider {
    @Override
    public Class<? extends Provider> getType() {
        return DefaultNetworkProvider.class;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if ("host.address".equalsIgnoreCase(key)) {
            String val = getHostAddress();
            return val == null ? defaultValue : val;
        } else if ("host.name".equalsIgnoreCase(key)) {
            String val = getHostName();
            return val == null ? defaultValue : val;
        } else {
            return defaultValue;
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public String getHostAddress() {
        return  NetworkManager.getInstance().getLocalHostAddress();
    }

    @Override
    public String getHostName() {
        return NetworkManager.getInstance().getLocalHostName();
    }

    @Override
    public String toString() {
        return "hostName [" + getHostName() + "] hostIP [" + getHostAddress() + "] (DefaultNetworkProvider)";

    }
}
