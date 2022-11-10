package repository.impl;

import entity.Card;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepositoryImpl implements CardRepository {
    private final List<Card> cardList = new ArrayList<>();

    @Override
    public void save(Card card) {
        if (cardList.stream().noneMatch(card1 -> card1.getId().equals(card.getId()))) {
            cardList.add(card);
        } else {
            throw new NotAvailableException("Карта с таким id уже существует");
        }
    }

    @Override
    public void delete(Long id) {
        cardList.remove(cardList.stream()
                .filter(card -> card.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("Карты с таким id не существует")));
    }

    @Override
    public void update(Card card) {
        Card updatedCard = cardList.stream()
                .filter(card1 -> card1.getId().equals(card.getId()))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("Карты с таким id не существует"));
        updatedCard.setId(card.getId());
        updatedCard.setCardNumber(card.getCardNumber());
        updatedCard.setUser(card.getUser());
        updatedCard.setPinCode(card.getPinCode());
        updatedCard.setCardStatus(card.getCardStatus());
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardList.stream().filter(card -> card.getId().equals(id)).findFirst();
    }

    @Override
    public List<Card> findAll() {
        return cardList;
    }
}
