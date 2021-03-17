package com.example.starter.router;

import com.example.starter.handler.UserHandler;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class UserRouter {

    private Vertx vertx;
    private UserHandler userHandler;

    public UserRouter(Vertx vertx, UserHandler userHandler) {
        this.vertx = vertx;
        this.userHandler = userHandler;
    }

    public Router getRouter() {
        final Router userRouter = Router.router(vertx);

        userRouter.route("/api/v1/users*").handler(BodyHandler.create());
        userRouter.get("/api/v1/users").handler(userHandler::getAll);
        userRouter.get("/api/v1/users/:id").handler(userHandler::getOne);
        userRouter.post("/api/v1/users").handler(userHandler::insertOne);
        userRouter.put("/api/v1/users/:id").handler(userHandler::updateOne);
        userRouter.delete("/api/v1/users/:id").handler(userHandler::deleteOne);

        return userRouter;
    }

}
