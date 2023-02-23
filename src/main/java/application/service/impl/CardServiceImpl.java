package application.service.impl;

import application.entity.BankAccount;
import application.entity.Card;
import application.entity.CardStatus;
import application.exception.NoDataFoundException;
import application.exception.NotAvailableException;
import application.repository.BankAccountRepository;
import application.repository.CardRepository;
import application.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    private final BankAccountRepository bankAccountRepository;

    @Override
    public void blockCardIfAttemptsMoreThenThree(int attempt, Integer pinCode, Long bankAccountId) {
        BankAccount bankAccount = bankAccountRepository
                .findById(bankAccountId)
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist"));
        if (!bankAccount.getCard().getPinCode().equals(pinCode)) {
            if (attempt == 3) {
                cardRepository.update(new Card(bankAccount.getCard().getId()
                        , bankAccount.getCard().getPinCode()
                        , bankAccount.getCard().getCardNumber()
                        , CardStatus.BLOCKED));
                throw new NotAvailableException("Your card is blocked!");
            }
            System.out.println("PIN code entered incorrectly, please try again");
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
