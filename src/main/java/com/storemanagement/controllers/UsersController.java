package com.storemanagement.controllers;

import com.storemanagement.entities.User;
import com.storemanagement.services.user.UserService;
import com.storemanagement.util.SecurityUtils;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/secured/users")
public class UsersController {
    @Autowired
    private UserService userService;

    @RequestMapping(method=RequestMethod.GET)
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public User getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }

    @RequestMapping(method=RequestMethod.POST)
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public User updateUser(@PathVariable long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public void deleteUser(@PathVariable long id){
        userService.deleteUser(id);

    }
}
