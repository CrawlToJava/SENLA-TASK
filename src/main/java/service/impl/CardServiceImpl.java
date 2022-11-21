package service.impl;

import entity.BankAccount;
import entity.Card;
import entity.CardStatus;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import lombok.AllArgsConstructor;
import repository.BankAccountRepository;
import repository.CardRepository;
import service.CardService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;

    private BankAccountRepository bankAccountRepository;

    @Override
    public void blockCardIfAttemptsMoreThenThree(int attempt, Integer pinCode, Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Банковского аккаунта с таким id не существует"));
        if (!bankAccount.getCard().getPinCode().equals(pinCode)) {
            if (attempt == 3) {
                cardRepository.update(new Card(bankAccount.getCard().getId()
                        , bankAccount.getCard().getPinCode()
                        , bankAccount.getCard().getCardNumber()
                        , CardStatus.BLOCKED));
                throw new NotAvailableException("Ваша карта заблокирована!");
            }
            System.out.println("Пинкод введен не правильно, попробуйте еще раз");
        }
    }

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
