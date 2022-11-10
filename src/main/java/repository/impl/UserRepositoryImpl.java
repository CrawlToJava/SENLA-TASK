package repository.impl;

import entity.User;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final List<User> userList = new ArrayList<>();

    @Override
    public void save(User user) {
        if (userList.stream().noneMatch(user1 -> user1.getId().equals(user.getId()))) {
            userList.add(user);
        } else {
            throw new NotAvailableException("Пользователь с таким id уже существует");
        }
    }

    @Override
    public void delete(Long id) {
        userList.remove(findById(id).orElseThrow(() -> new NoDataFoundException("Пользователя с таким id не существует")));
    }

    @Override
    public void update(User user) {
        User updatedUser = findById(user.getId()).orElseThrow(() -> new NoDataFoundException("Пользователя с таким id не существует"));
        updatedUser.setId(user.getId());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUserStatus(user.getUserStatus());
    }

    @Override
    public Optional<User> findById(Long id) {
        return userList.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return userList;
    }
}
