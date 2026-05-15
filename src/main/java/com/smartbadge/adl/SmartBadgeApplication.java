package com.smartbadge.adl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableMongoAuditing
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SmartBadgeApplication {
    public static void main(String[] args) {
    	log.info("SmartBadge Application Starting...");
        SpringApplication.run(SmartBadgeApplication.class, args);
        log.info("SmartBadge Application Successfully Started...");
    }
}
