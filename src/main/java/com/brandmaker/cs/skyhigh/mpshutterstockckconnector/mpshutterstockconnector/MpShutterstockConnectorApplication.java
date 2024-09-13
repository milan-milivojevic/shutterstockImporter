package com.brandmaker.cs.skyhigh.mpshutterstockckconnector.mpshutterstockconnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MpShutterstockConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpShutterstockConnectorApplication.class, args);
    }

}
