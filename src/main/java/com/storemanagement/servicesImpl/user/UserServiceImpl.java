package com.storemanagement.servicesImpl.user;

import com.storemanagement.dao.user.UserOwnRepository;
import com.storemanagement.dao.user.UserRepository;
import com.storemanagement.entities.User;
import com.storemanagement.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserOwnRepository userOwnRepository;

    @Override
    public List<User> getAllUsers() {
        return userOwnRepository.getAllUsers();
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return userOwnRepository.getUserById(id);
    }

    @Override
    public User updateUser(long id, User user) {
        return userOwnRepository.updateUser(id, user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.delete(id);
    }

    public User getUserByName(String name){
        return userOwnRepository.getUserByName(name);
    }
}
