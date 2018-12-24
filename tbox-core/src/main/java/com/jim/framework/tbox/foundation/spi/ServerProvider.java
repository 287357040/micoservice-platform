package com.jim.framework.tbox.foundation.spi;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by celiang.hu on 2018-12-18.
 */
public interface ServerProvider  extends Provider{
    /**
     * @return current environment or {@code null} if not set
     */
    public String getEnvType();

    /**
     * @return whether current environment is set or not
     */
    public boolean isEnvTypeSet();

    /**
     * @return current data center or {@code null} if not set
     */
    public String getDataCenter();

    /**
     * @return whether data center is set or not
     */
    public boolean isDataCenterSet();

    /**
     * Initialize server provider with the specified input stream
     *
     * @throws IOException
     */
    public void initialize(InputStream in) throws IOException;
}
