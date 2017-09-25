package io.vertx.book.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.HttpRequest;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;

import java.util.ArrayList;
import java.util.List;

public class HelloConsumerMicroservice extends AbstractVerticle {

    private WebClient client;

    @Override
    public void start() {
        client = WebClient.create(Vertx.vertx());

        Router router = Router.router(vertx);
        router.get("/").handler(this::invokeWebService);
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void invokeWebService(RoutingContext routingContext) {
        HttpRequest<JsonObject> req1 = client.get(8080, "54.67.95.0", "/insertCount/2000")
                .as(BodyCodec.jsonObject());
        Single<JsonObject> s1 = req1.rxSend().map(HttpResponse::body);
        List<Single> list =new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            req1.send(httpResponseAsyncResult -> {});
        }

        routingContext.response().end("Created 500 requests");
        System.out.println("Request Sent");
    }

}
