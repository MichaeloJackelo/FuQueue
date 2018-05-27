package com.storemanagement.dao.user;

import com.storemanagement.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserOwnRepository extends Repository<User, Long> {

    @Query(value = "from User")
    List<User> getAllUsers();

    @Modifying
    @Query(value="update User u Set u=?2 where u.id=?1")
    User updateUser(long id, User user);

    @Query(value="from User u where u.id=?1")
    User getUserById(long id);

    @Query(value="from User u where u.name=?1")
    User getUserByName(String name);
}
