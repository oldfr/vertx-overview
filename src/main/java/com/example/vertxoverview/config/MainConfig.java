package com.example.vertxoverview.config;

import com.example.vertxoverview.verticle.MQHandleVerticle;
import com.example.vertxoverview.verticle.StudentVerticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MainConfig {

    @Autowired
    StudentVerticle studentVerticle;

    @Autowired
    MQHandleVerticle MQHandleVerticle;

    @PostConstruct
    public void deployVerticle() {
        // deploy the verticles
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(studentVerticle);
        vertx.deployVerticle(MQHandleVerticle);
    }
}
