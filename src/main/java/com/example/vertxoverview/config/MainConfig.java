package com.example.vertxoverview.config;

import com.example.vertxoverview.verticle.MQHandleVerticle;
import com.example.vertxoverview.verticle.MainVerticle;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MainConfig {

    @Autowired
    MainVerticle mainVerticle;

    @Autowired
    MQHandleVerticle MQHandleVerticle;

    @PostConstruct
    public void deployVerticle() {
        // deploy the verticles
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(mainVerticle);
        vertx.deployVerticle(MQHandleVerticle);
    }
}
