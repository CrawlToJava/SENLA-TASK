package service.impl;

import entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import repository.UserRepository;
import service.UserService;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
