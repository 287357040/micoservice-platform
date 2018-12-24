package com.jim.framework.tbox.foundation.spi;

/**
 * Created by celiang.hu on 2018-12-18.
 */
public interface Provider {
    /**
     * 获取对应的provider类型
     *
     * @return
     */
    Class<? extends Provider> getType();

    /**
     *
     * @param key key
     * @param defaultValue if not found,return default value
     * @return value
     */
    String getProperty(String key,String defaultValue);
    /**
     * 初始化属性
     */
    void initialize();
}
