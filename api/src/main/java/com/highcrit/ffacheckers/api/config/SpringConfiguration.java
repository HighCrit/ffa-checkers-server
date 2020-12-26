package com.highcrit.ffacheckers.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.highcrit.ffacheckers.api")
@EntityScan("com.highcrit.ffacheckers.domain.entities")
public class SpringConfiguration {}
