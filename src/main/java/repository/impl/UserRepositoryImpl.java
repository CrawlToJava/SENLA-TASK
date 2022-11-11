package repository.impl;

import au.com.bytecode.opencsv.CSVWriter;
import entity.User;
import entity.UserStatus;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.UserRepository;

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

public class UserRepositoryImpl implements UserRepository {
    private List<User> userList = new ArrayList<>();

    private final String dataBase = "C:\\Users\\User\\IdeaProjects\\SENLA-TECHNICAL-TASK\\src\\main\\java\\data\\Users.csv";

    @Override
    public void save(User user) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(dataBase, true))) {
            userList = findAll();
            if (userList.stream().noneMatch(user1 -> user1.getId().equals(user.getId()))) {
                userList.add(user);
                String[] record = {String.valueOf(user.getId())
                        , user.getFirstName()
                        , user.getLastName()
                        , String.valueOf(user.getUserStatus())};
                writer.writeNext(record);
            } else {
                throw new NotAvailableException("Пользователь с таким id уже существует");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        userList = findAll();
        userList.remove(findById(id).orElseThrow(() -> new NoDataFoundException("Пользователя с таким id не существует")));
        refreshAfterDeleting(userList);
    }

    @Override
    public void update(User user) {
        User updatedUser = findById(user.getId()).orElseThrow(() -> new NoDataFoundException("Пользователя с таким id не существует"));
        updatedUser.setId(user.getId());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUserStatus(user.getUserStatus());
        delete(user.getId());
        save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return findAll().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        Path path = Paths.get(dataBase);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                User user = createUser(attributes);
                list.add(user);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private User createUser(String[] metadata) {
        Long id = Long.parseLong(metadata[0].replace("\"", ""));
        String firstName = metadata[1];
        String lastName = metadata[2];
        UserStatus userStatus = UserStatus.valueOf(metadata[3].replace("\"", ""));
        return new User(id, firstName, lastName, userStatus);
    }

    private void refreshAfterDeleting(List<User> list) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(dataBase, true));
             PrintWriter writer = new PrintWriter(dataBase)) {
            writer.print("");
            for (User user : list) {
                String[] record = {String.valueOf(user.getId()).replace("\"", "")
                        , user.getFirstName().replace("\"", "")
                        , user.getLastName().replace("\"", "")
                        , String.valueOf(user.getUserStatus()).replace("\"", "")};
                csvWriter.writeNext(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

