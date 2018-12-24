package com.jim.framework.tbox.register;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = TboxMetaServiceConfig.class)
public class TboxMetaServiceConfig {
//    @Bean
//    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
//        return new StrictHttpFirewall();
//    }
}
