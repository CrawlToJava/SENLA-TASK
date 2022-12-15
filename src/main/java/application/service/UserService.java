package application.service;

import application.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * This method saves the User entity to the database
     *
     * @param user the entity we need to save
     */
    void save(User user);

    /**
     * This method deletes the User entity by id from the database
     *
     * @param id of the entity to be checked for containment in the database
     */
    void delete(Long id);

    /**
     * This method updates the User entity in the database
     *
     * @param user the entity with which we need to update the entity in the database
     */
    void update(User user);

    /**
     * This method finds the User entity by id in the database
     *
     * @param id of the entity to be checked for containment in the database
     * @return the User entity wrapped in an optional
     */
    Optional<User> findById(Long id);

    /**
     * This method finds all User entities in the database
     *
     * @return the List of Users
     */
    List<User> findAll();
}
