package com.example.vertxoverview.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
public class StudentVerticle extends AbstractVerticle {

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
        router.post("/student").handler(this::createStudentRecAndplaceMessageInMq);

//        GET request
        router.get("/student").handler(handler -> handler.response().end("all Student details "));
//        GET request with path parameters
        router.get("/student/:name").handler(handler -> {
            String name = handler.pathParam("name");
            handler.response().end(String.format("Student %s details ", name));
        });

        router.get("/america").handler(a -> {
            a.reroute("/india");
        });

        router.get("/india").handler( i-> i.response().end("I love India"));

//        POST request
        router.post("/college").handler(context ->
        {
            context.request().bodyHandler(System.out::println);
            context.response().end("done");
        });

        return router;
    }

    private void createStudentRecAndplaceMessageInMq(RoutingContext ctx) {
//        perform API call task...
        vertx.eventBus().request("mqHandlerOnAPICompletion","", reply -> ctx.request().response().end((String) "Created student record. Placing message in MQ:"+reply.result().body()));
    }

}