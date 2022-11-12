package repository;

import entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    void save(Card card);

    void delete(Long id);

    void update(Card card);

    Optional<Card> findById(Long id);

    List<Card> findAll();
}
