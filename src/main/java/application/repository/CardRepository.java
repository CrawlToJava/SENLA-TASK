package application.repository;

import application.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    /**
     * This method saves the Card entity to the database
     *
     * @param card the entity we need to save
     */
    void save(Card card);

    /**
     * This method deletes the Card entity by id from the database
     *
     * @param id of the entity to be checked for containment in the database
     */
    void delete(Long id);

    /**
     * This method updates the Card entity in the database
     *
     * @param card the entity with which we need to update the entity in the database
     */
    void update(Card card);

    /**
     * This method finds the Card entity by id in the database
     *
     * @param id of the entity to be checked for containment in the database
     * @return the Card entity wrapped in an optional
     */
    Optional<Card> findById(Long id);

    /**
     * This method finds all Card entities in the database
     *
     * @return the List of Cards
     */
    List<Card> findAll();
}
