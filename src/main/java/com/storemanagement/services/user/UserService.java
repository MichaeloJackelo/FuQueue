package com.storemanagement.services.user;

import com.storemanagement.entities.User;

import java.util.List;

public interface UserService {
     List<User> getAllUsers();
     User addUser(User user);
     User getUserById(long id);
     User updateUser(long id, User user);
     void deleteUser(long id);
     User getUserByName(String name);

}
