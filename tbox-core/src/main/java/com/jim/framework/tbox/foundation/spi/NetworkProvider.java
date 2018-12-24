package com.jim.framework.tbox.foundation.spi;

/**
 * Created by celiang.hu on 2018-12-18.
 */
public interface NetworkProvider extends Provider {
    /**
     *
     * @return get the host address
     */
    String getHostAddress();

    /**
     *
     * @return get the host name
     */
    String getHostName();
}
