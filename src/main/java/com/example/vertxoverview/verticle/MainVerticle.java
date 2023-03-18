package com.example.vertxoverview.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.createHttpServer(new HttpServerOptions())
                .requestHandler(getRouter())
                .listen(8080,asyncResult -> {
                    if (asyncResult.succeeded()) {
                        System.out.println("SUCCESS");
                        startPromise.complete();
                    }
                    else {
                        System.out.println("FAILED");
                        startPromise.fail(asyncResult.cause());
                    }
                });

    }

    private Router getRouter() throws FileNotFoundException {
        Router router = Router.router(vertx);

        // call API and place message in MQ after completion
        router.get("/api/v1/hello").handler(this::placeMessageInMq);

//        GET request
        router.get("/testing").handler(handler -> handler.response().end("Hello "));
//        GET request with path parameters
        router.get("/greet/:name").handler(handler -> {
            String name = handler.pathParam("name");
            handler.response().end(String.format("Hello %s ", name));
        });

//        POST request
        router.post("/test").handler(context ->
        {
            context.request().bodyHandler(System.out::println);
            context.response().end("done");
        });

        return router;
    }

    private void placeMessageInMq(RoutingContext ctx) {
//        perform API call task...
        vertx.eventBus().request("mqHandlerOnAPICompletion","", reply -> ctx.request().response().end((String) reply.result().body()));
    }

}