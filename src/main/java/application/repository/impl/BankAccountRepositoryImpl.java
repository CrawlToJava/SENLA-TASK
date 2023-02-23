package application.repository.impl;

import application.entity.BankAccount;
import application.entity.BankAccountStatus;
import application.entity.Card;
import application.entity.User;
import application.exception.NoDataFoundException;
import application.exception.NotAvailableException;
import application.repository.BankAccountRepository;
import application.repository.CardRepository;
import application.repository.UserRepository;
import au.com.bytecode.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BankAccountRepositoryImpl implements BankAccountRepository, Serializable {
    private List<BankAccount> bankAccountList = new ArrayList<>();

    private final UserRepository userRepository;

    private final CardRepository cardRepository;

    private final String dataBase = "C:\\Users\\User\\IdeaProjects\\SENLA-TECHNICAL-TASK\\src\\main\\java\\data\\BankAccounts.csv";

    @Override
    public void save(BankAccount bankAccount) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(dataBase, true))) {
            bankAccountList = findAll();
            if (bankAccountList.stream().noneMatch(bankAccount1 -> bankAccount1.getId().equals(bankAccount.getId()))) {
                bankAccountList.add(bankAccount);
                String[] record = {String.valueOf(bankAccount.getId())
                        , String.valueOf(bankAccount.getAmountOfMoney())
                        , String.valueOf(bankAccount.getCard().getId())
                        , String.valueOf(bankAccount.getUser().getId())
                        , String.valueOf(bankAccount.getBankAccountStatus())};
                writer.writeNext(record);
            } else {
                throw new NotAvailableException("Bank account already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        bankAccountList = findAll();
        bankAccountList.remove(findById(id)
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist")));
        refreshAfterDeleting(bankAccountList);
    }

    @Override
    public void update(BankAccount bankAccount) {
        BankAccount updatedBankAccount = findById(bankAccount.getId())
                .orElseThrow(() -> new NoDataFoundException("Bank account doesn't exist"));
        updatedBankAccount.setId(bankAccount.getId());
        updatedBankAccount.setCard(bankAccount.getCard());
        updatedBankAccount.setUser(bankAccount.getUser());
        updatedBankAccount.setAmountOfMoney(bankAccount.getAmountOfMoney());
        updatedBankAccount.setBankAccountStatus(bankAccount.getBankAccountStatus());
        delete(bankAccount.getId());
        save(updatedBankAccount);
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return findAll().stream().filter(bankAccount -> bankAccount.getId().equals(id)).findFirst();
    }

    @Override
    public List<BankAccount> findAll() {
        List<BankAccount> list = new ArrayList<>();
        Path path = Paths.get(dataBase);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                BankAccount bankAccount = createBankAccount(attributes);
                list.add(bankAccount);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private BankAccount createBankAccount(String[] metadata) {
        Long id = Long.parseLong(metadata[0].replace("\"", ""));
        BigDecimal amountOfMoney = new BigDecimal(Long.parseLong(metadata[1].replace("\"", "")));
        Card card = cardRepository
                .findById(Long.parseLong(metadata[2].replace("\"", "")))
                .orElseThrow(() -> new NoDataFoundException("Card doesn't exist"));
        User user = userRepository
                .findById(Long.parseLong(metadata[3].replace("\"", "")))
                .orElseThrow(() -> new NoDataFoundException("User doesn't exist"));
        BankAccountStatus bankAccountStatus = BankAccountStatus.valueOf(metadata[4].replace("\"", ""));
        return new BankAccount(id, amountOfMoney, card, user, bankAccountStatus);
    }

    private void refreshAfterDeleting(List<BankAccount> list) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(dataBase, true));
             PrintWriter writer = new PrintWriter(dataBase)) {
            writer.print("");
            for (BankAccount bankAccount : list) {
                String[] record = {String.valueOf(bankAccount.getId())
                        , String.valueOf(bankAccount.getAmountOfMoney())
                        , String.valueOf(bankAccount.getCard().getId())
                        , String.valueOf(bankAccount.getUser().getId())
                        , String.valueOf(bankAccount.getBankAccountStatus())};
                csvWriter.writeNext(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
