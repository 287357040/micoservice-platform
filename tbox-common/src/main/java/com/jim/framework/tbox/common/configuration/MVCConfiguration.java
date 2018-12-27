package com.jim.framework.tbox.common.configuration;

import com.jim.framework.tbox.common.mvc.error.DefaultErrorHandler;
import com.jim.framework.tbox.common.mvc.error.ErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by celiang.hu on 2018-12-27.
 */
@Configuration
public class MVCConfiguration {
    @Bean
    public ErrorHandler errorHandler(){
        return new DefaultErrorHandler();
    }
}
