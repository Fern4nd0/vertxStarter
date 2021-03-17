package com.example.starter.repository;

import com.example.starter.model.User;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";

    private final MongoClient client;

    public UserRepository(MongoClient client) {
        this.client = client;
    }

    public Single<List<User>> getAll() {
        final JsonObject query = new JsonObject();

        return client.rxFind(COLLECTION_NAME, query)
                .flatMap(result -> {
                    final List<User> users = new ArrayList<>();
                    result.forEach(user -> users.add(new User(user)));

                    return Single.just(users);
                });
    }

    public Maybe<User> getById(String id) {
        final JsonObject query = new JsonObject().put("_id", id);

        return client.rxFindOne(COLLECTION_NAME, query, null)
                .flatMap(result -> {
                    final User user = new User(result);

                    return Maybe.just(user);
                });
    }

    public Maybe<User> insert(User user) {
        return client.rxInsert(COLLECTION_NAME, JsonObject.mapFrom(user))
                .flatMap(result -> {
                    final JsonObject jsonObject = new JsonObject().put("_id", result);
                    final User insertedUser = new User(jsonObject);

                    return Maybe.just(insertedUser);
                });
    }

    public Completable update(String id, User user) {
        final JsonObject query = new JsonObject().put("_id", id);

        return client.rxReplaceDocuments(COLLECTION_NAME, query, JsonObject.mapFrom(user))
                .flatMapCompletable(result -> {
                    if (result.getDocModified() == 1) {
                        return Completable.complete();
                    } else {
                        return Completable.error(new NoSuchElementException("No user with id " + id));
                    }
                });
    }

    public Completable delete(String id) {
        final JsonObject query = new JsonObject().put("_id", id);

        return client.rxRemoveDocument(COLLECTION_NAME, query)
                .flatMapCompletable(result -> {
                    if (result.getRemovedCount() == 1) {
                        return Completable.complete();
                    } else {
                        return Completable.error(new NoSuchElementException("No user with id " + id));
                    }
                });
    }

}
