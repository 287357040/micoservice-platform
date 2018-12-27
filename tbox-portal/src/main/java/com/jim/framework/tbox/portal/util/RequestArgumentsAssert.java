package com.jim.framework.tbox.portal.util;

import com.jim.framework.tbox.common.exception.ParamValidationException;
import org.springframework.util.StringUtils;

/**
 * Created by celiang.hu on 2018-12-26.
 */
public class RequestArgumentsAssert {

    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new ParamValidationException(message);
        }
    }
}
