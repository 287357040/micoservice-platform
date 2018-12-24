package com.jim.framework.tbox.adminservice;

import com.jim.framework.tbox.adminservice.spring.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import javax.sql.DataSource;

/**
 * Created by celiang.hu on 2018-12-14.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath*:dubbo-admin.xml"})
@EnableEurekaClient
public class AdminApplicaion {

    public static void main(String[] args) {
        ApplicationContext act = SpringApplication.run(AdminApplicaion.class, args);
        SpringUtil.setApplicationContext(act);
    }
}
