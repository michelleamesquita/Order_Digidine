package com.fiap.digidine.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.fiap.digidine")
@EnableAutoConfiguration
public class TestConfig {
}
