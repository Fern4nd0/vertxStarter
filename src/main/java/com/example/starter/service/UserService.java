package com.example.starter.service;

import com.example.starter.model.User;
import com.example.starter.repository.UserRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Single<List<User>> getAll() {
        return userRepository.getAll();
    }

    public Maybe<User> getById(String id) {
        return userRepository.getById(id);
    }

    public Maybe<User> insert(User user) {
        return userRepository.insert(user);
    }

    public Completable update(String id, User user) {
        return userRepository.update(id, user);
    }

    public Completable delete(String id) {
        return userRepository.delete(id);
    }

}
