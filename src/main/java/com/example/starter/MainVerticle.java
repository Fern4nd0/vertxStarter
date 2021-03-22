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

    final JsonObject config = new JsonObject().put("HOST","localhost").put("USERNAME", "root")
      .put("PASSWORD", "starter").put("AUTHSOURCE", "admin").put("HTTP_PORT", 8080).put("DB_NAME", "starter");
    final ConfigStoreOptions store = new ConfigStoreOptions().setType("env").setConfig(config);
    final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
    final ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    retriever.rxGetConfig()
      .flatMap(configurations -> {
        final MongoClient client = createMongoClient(vertx, configurations);

        final UserRepository userRepository = new UserRepository(client);
        final UserService userService = new UserService(userRepository);
        final UserHandler userHandler = new UserHandler(userService);
        final UserRouter userRouter = new UserRouter(vertx, userHandler);

        return createHttpServer(userRouter.getRouter(), configurations);
      })
      .subscribe(
        server -> System.out.println("HTTP Server listening on port " + server.actualPort()),
        throwable -> {
          System.out.println("Error occurred before creating a new HTTP server: " + throwable.getMessage());
          System.exit(1);
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
