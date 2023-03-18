package com.example.vertxoverview.verticle;

import io.vertx.core.AbstractVerticle;
import org.springframework.stereotype.Component;

@Component
public class MQHandleVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.eventBus().consumer("mqHandlerOnAPICompletion",msg -> {
            System.out.println("placing message in MQ");
            // place a success message in MQ with required details
            msg.reply("Success");
        });
    }
}