package com.example.starter.handler;

import com.example.starter.model.User;
import com.example.starter.service.UserService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import java.util.NoSuchElementException;

public class UserHandler {

    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void getAll(RoutingContext rc) {
        userService.getAll()
                .subscribe(
                        result -> onSuccessResponse(rc, 200, result),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    public void getOne(RoutingContext rc) {
        final String id = rc.pathParam("id");

        userService.getById(id)
                .subscribe(
                        result -> onSuccessResponse(rc, 200, result),
                        throwable -> onErrorResponse(rc, 400, throwable),
                        () -> onErrorResponse(rc, 400, new NoSuchElementException("No user with id " + id)));
    }

    public void insertOne(RoutingContext rc) {
        final User user = mapRequestBodyToUser(rc);

        userService.insert(user)
                .subscribe(
                        result -> onSuccessResponse(rc, 201, result),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    public void updateOne(RoutingContext rc) {
        final String id = rc.pathParam("id");
        final User user = mapRequestBodyToUser(rc);

        userService.update(id, user)
                .subscribe(
                        () -> onSuccessResponse(rc, 204, null),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    public void deleteOne(RoutingContext rc) {
        final String id = rc.pathParam("id");

        userService.delete(id)
                .subscribe(
                        () -> onSuccessResponse(rc, 204, null),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    // Mapping between user class and request body JSON object
    private User mapRequestBodyToUser(RoutingContext rc) {
        User user = new User();

        try {
            user = rc.getBodyAsJson().mapTo(User.class);
        } catch (IllegalArgumentException ex) {
            onErrorResponse(rc, 400, ex);
        }

        return user;
    }

    // Generic responses
    private void onSuccessResponse(RoutingContext rc, int status, Object object) {
        rc.response()
                .setStatusCode(status)
                .putHeader("Content-Type", "application/json")
                .end(Json.encodePrettily(object));
    }

    private void onErrorResponse(RoutingContext rc, int status, Throwable throwable) {
        final JsonObject error = new JsonObject().put("error", throwable.getMessage());

        rc.response()
                .setStatusCode(status)
                .putHeader("Content-Type", "application/json")
                .end(Json.encodePrettily(error));
    }

}
