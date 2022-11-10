package service;

import entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);

    void delete(Long id);

    void update(User user);

    Optional<User> findById(Long id);

    List<User> findAll();
}
