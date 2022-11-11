package repository.impl;

import au.com.bytecode.opencsv.CSVWriter;
import entity.Card;
import entity.CardStatus;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.CardRepository;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepositoryImpl implements CardRepository {
    private List<Card> cardList = new ArrayList<>();

    private final String dataBase = "C:\\Users\\User\\IdeaProjects\\SENLA-TECHNICAL-TASK\\src\\main\\java\\data\\Cards.csv";

    @Override
    public void save(Card card) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(dataBase, true))) {
            cardList = findAll();
            if (cardList.stream().noneMatch(card1 -> card1.getId().equals(card.getId()))) {
                cardList.add(card);
                String[] record = {String.valueOf(card.getId())
                        , String.valueOf(card.getPinCode())
                        , String.valueOf(card.getCardNumber())
                        , String.valueOf(card.getCardStatus())};
                writer.writeNext(record);
            } else {
                throw new NotAvailableException("Карта с таким id уже существует");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        cardList = findAll();
        cardList.remove(findById(id).orElseThrow(() -> new NoDataFoundException("Карты с таким id не существует")));
        refreshAfterDeleting(cardList);
    }

    @Override
    public void update(Card card) {
        Card updatedCard = findById(card.getId()).orElseThrow(() -> new NoDataFoundException("Карты с таким id не существует"));
        updatedCard.setId(card.getId());
        updatedCard.setCardNumber(card.getCardNumber());
        updatedCard.setPinCode(card.getPinCode());
        updatedCard.setCardStatus(card.getCardStatus());
        delete(card.getId());
        save(card);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return findAll().stream().filter(card -> card.getId().equals(id)).findFirst();
    }

    @Override
    public List<Card> findAll() {
        List<Card> list = new ArrayList<>();
        Path path = Paths.get(dataBase);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Card card = createCard(attributes);
                list.add(card);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Card createCard(String[] metadata) {
        Long id = Long.parseLong(metadata[0].replace("\"", ""));
        Integer pinCode = Integer.parseInt(metadata[1].replace("\"", ""));
        Long cardNumber = Long.parseLong(metadata[2].replace("\"", ""));
        CardStatus cardStatus = CardStatus.valueOf(metadata[3].replace("\"", ""));
        return new Card(id, pinCode, cardNumber, cardStatus);
    }

    private void refreshAfterDeleting(List<Card> list) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(dataBase, true));
             PrintWriter writer = new PrintWriter(dataBase)) {
            writer.print("");
            for (Card card : list) {
                String[] record = {String.valueOf(card.getId()).replace("\"", "")
                        , String.valueOf(card.getPinCode()).replace("\"", "")
                        , String.valueOf(card.getCardNumber()).replace("\"", "")
                        , String.valueOf(card.getCardStatus()).replace("\"", "")};
                csvWriter.writeNext(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
