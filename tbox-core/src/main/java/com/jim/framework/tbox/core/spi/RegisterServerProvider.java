package com.jim.framework.tbox.core.spi;

import com.jim.framework.tbox.core.enums.Env;

/**
 * Created by celiang.hu on 2018-12-16.
 */
public interface RegisterServerProvider extends Ordered {
    String getRegisterServerAddress(Env targetEnv);
}
