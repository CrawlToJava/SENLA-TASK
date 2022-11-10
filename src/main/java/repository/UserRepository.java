package repository;

import entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    void delete(Long id);

    void update(User user);

    Optional<User> findById(Long id);

    List<User> findAll();
}
