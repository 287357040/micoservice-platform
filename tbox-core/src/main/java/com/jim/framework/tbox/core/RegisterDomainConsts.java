package com.jim.framework.tbox.core;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jim.framework.tbox.core.enums.Env;
import com.jim.framework.tbox.core.spi.RegisterServerProvider;
import com.jim.framework.tbox.core.util.NetUtil;
import com.jim.framework.tbox.foundation.internals.ServiceBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by celiang.hu on 2018-12-16.
 */
public class RegisterDomainConsts {

    public static final String DEFAULT_META_URL = "http://tbox.register";

    private static final Logger logger = LoggerFactory.getLogger(RegisterDomainConsts.class);
    private static final Map<Env, String> registerServerAddressCache = Maps.newConcurrentMap();
    private static final Map<String, String> selectedRegisterServerAddressCache = Maps.newConcurrentMap();
    private static final AtomicBoolean periodicRefreshStarted = new AtomicBoolean(false);


    private static List<RegisterServerProvider> registerServerProviders = null;
    private static final Object LOCK = new Object();

    public static String getDomain(Env env) {
        String registerServerAddress = getRegisterServerAddress(env);
        if (registerServerAddress.contains(",")) {
            return selectRegisterServerAddress(registerServerAddress);
        }
        return registerServerAddress;
    }

    private static String selectRegisterServerAddress(String registerServerAddress) {
        String metaAddressSelected = selectedRegisterServerAddressCache.get(registerServerAddress);
        if (metaAddressSelected == null) {
            // initialize
            if (periodicRefreshStarted.compareAndSet(false, true)) {
          //      schedulePeriodicRefresh();
            }
            updateMetaServerAddresses(registerServerAddress);
            metaAddressSelected = selectedRegisterServerAddressCache.get(registerServerAddress);
        }

        return metaAddressSelected;
    }

    private static void updateMetaServerAddresses(String registerServerAddress) {
        logger.debug("Selecting meta server address for: {}", registerServerAddress);

//        Transaction transaction = Tracer.newTransaction("Apollo.MetaService", "refreshMetaServerAddress");
//        transaction.addData("Url", metaServerAddresses);

        try {
            List<String> metaServers = Lists.newArrayList(registerServerAddress.split(","));
            // random load balancing
            Collections.shuffle(metaServers);

            boolean serverAvailable = false;

            for (String address : metaServers) {
                address = address.trim();
                //check whether /services/config is accessible
                if (NetUtil.pingUrl(address + "/services/config")) {
                    // select the first available meta server
                    selectedRegisterServerAddressCache.put(registerServerAddress, address);
                    serverAvailable = true;
                    logger.debug("Selected meta server address {} for {}", address, registerServerAddress);
                    break;
                }
            }

            // we need to make sure the map is not empty, e.g. the first update might be failed
            if (!selectedRegisterServerAddressCache.containsKey(registerServerAddress)) {
                selectedRegisterServerAddressCache.put(registerServerAddress, metaServers.get(0).trim());
            }

            if (!serverAvailable) {
                logger.warn("Could not find available meta server for configured meta server addresses: {}, fallback to: {}",
                        registerServerAddress, selectedRegisterServerAddressCache.get(registerServerAddress));
            }


        } catch (Throwable ex) {
            throw ex;
        }
    }

    private static String getRegisterServerAddress(Env env) {
        if (!registerServerAddressCache.containsKey(env)) {
            initMetaServerAddress(env);
        }

        return registerServerAddressCache.get(env);
    }

    private static void initMetaServerAddress(Env env) {
        if (registerServerProviders == null) {
            synchronized (LOCK) {
                if (registerServerProviders == null) {
                    registerServerProviders = initRegisterServerProviders();
                }
            }
        }
        String metaAddress = null;
        for (RegisterServerProvider provider : registerServerProviders) {
            metaAddress = provider.getRegisterServerAddress(env);
            if (!Strings.isNullOrEmpty(metaAddress)) {
                logger.info("Located meta server address {} for env {} from {}", metaAddress, env,
                        provider.getClass().getName());
                break;
            }
        }

        if (Strings.isNullOrEmpty(metaAddress)) {
            // Fallback to default meta address
            metaAddress = DEFAULT_META_URL;
            logger.warn(
                    "Meta server address fallback to {} for env {}, because it is not available in all MetaServerProviders",
                    metaAddress, env);
        }
        registerServerAddressCache.put(env, metaAddress);
    }

    private static List<RegisterServerProvider> initRegisterServerProviders() {
        Iterator<RegisterServerProvider> registerServerProviderIterator = ServiceBootstrap.loadAll(RegisterServerProvider.class);

        List<RegisterServerProvider> registerServerProviders = Lists.newArrayList(registerServerProviderIterator);

        Collections.sort(registerServerProviders, new Comparator<RegisterServerProvider>() {
            @Override
            public int compare(RegisterServerProvider o1, RegisterServerProvider o2) {
                // the smaller order has higher priority
                return Integer.compare(o1.getOrder(), o2.getOrder());
            }
        });

        return registerServerProviders;
    }
}
