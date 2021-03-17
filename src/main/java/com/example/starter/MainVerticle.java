package com.example.starter;

import com.example.starter.handler.UserHandler;
import io.reactivex.Single;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.Router;
import com.example.starter.repository.UserRepository;
import com.example.starter.router.UserRouter;
import com.example.starter.service.UserService;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    final ConfigStoreOptions store = new ConfigStoreOptions().setType("env");
    final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
    final ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  // Private methods
  private MongoClient createMongoClient(Vertx vertx, JsonObject configurations) {
    final JsonObject config = new JsonObject()
      .put("host", configurations.getString("HOST"))
      .put("username", configurations.getString("USERNAME"))
      .put("password", configurations.getString("PASSWORD"))
      .put("authSource", configurations.getString("AUTHSOURCE"))
      .put("db_name", configurations.getString("DB_NAME"))
      .put("useObjectId", true);

    return MongoClient.createShared(vertx, config);
  }

  private Single<HttpServer> createHttpServer(Router router, JsonObject configurations) {
    return vertx
      .createHttpServer()
      .requestHandler(router)
      .rxListen(configurations.getInteger("HTTP_PORT"));
  }

}
