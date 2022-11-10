package service.impl;

import entity.Card;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import repository.CardRepository;
import service.CardService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        cardRepository.delete(id);
    }

    @Override
    public void update(Card card) {
        cardRepository.update(card);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
