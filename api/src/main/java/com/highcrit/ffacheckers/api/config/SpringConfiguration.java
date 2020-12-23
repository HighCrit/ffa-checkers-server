package com.highcrit.ffacheckers.api.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EntityScan("com.highcrit.ffacheckers.domain.entities")
public class SpringConfiguration {
}
