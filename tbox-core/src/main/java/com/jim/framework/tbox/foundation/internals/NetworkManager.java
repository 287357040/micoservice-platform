package com.jim.framework.tbox.foundation.internals;

import java.net.*;
import java.util.*;


/**
 * Created by celiang.hu on 2018-12-18.
 */
public class NetworkManager {

    private static NetworkManager instance = new NetworkManager();
    private InetAddress local;
    private InetAddress localHost;

    public static NetworkManager getInstance() {
        return instance;
    }

    private NetworkManager() {
        load();
    }

    public String getLocalHostAddress() {
        return local.getHostAddress();
    }

    public String getLocalHostName() {
        try {
            if (null == localHost) {
                localHost = InetAddress.getLocalHost();
            }
            return localHost.getHostName();
        } catch (UnknownHostException e) {
            return local.getHostName();
        }
    }
    private void load() {
        String ip = getProperty("host.ip");


        if (ip != null) {
            try {
                local = InetAddress.getByName(ip);
                return;
            } catch (Exception e) {
                System.err.println(e);
                // ignore
            }
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            List<NetworkInterface> nis = interfaces == null ? Collections.<NetworkInterface>emptyList() : Collections.list(interfaces);
            List<InetAddress> addresses = new ArrayList<InetAddress>();
            InetAddress local = null;

            try {
                for (NetworkInterface ni : nis) {
                    if (ni.isUp() && !ni.isLoopback()) {
                        addresses.addAll(Collections.list(ni.getInetAddresses()));
                    }
                }
                local = findValidateIp(addresses);
            } catch (Exception e) {
                // ignore
            }
            if (local != null) {
                this.local = local;
                return;
            }
        } catch (SocketException e) {
            // ignore it
        }

        local = InetAddress.getLoopbackAddress();
    }

    private InetAddress findValidateIp(List<InetAddress> addresses) {
        InetAddress local = null;
        int maxWeight = -1;
        for (InetAddress address : addresses) {
            if (address instanceof Inet4Address) {
                int weight = 0;

                if (address.isSiteLocalAddress()) {
                    weight += 8;
                }

                if (address.isLinkLocalAddress()) {
                    weight += 4;
                }

                if (address.isLoopbackAddress()) {
                    weight += 2;
                }

                // has host name
                // TODO fix performance issue when calling getHostName
                if (!Objects.equals(address.getHostName(), address.getHostAddress())) {
                    weight += 1;
                }

                if (weight > maxWeight) {
                    maxWeight = weight;
                    local = address;
                }
            }
        }
        return local;
    }

    private String getProperty(String name) {
        String value = System.getProperty(name);
        if (value == null) {
            value = System.getenv(name);
        }
        return value;
    }
}
