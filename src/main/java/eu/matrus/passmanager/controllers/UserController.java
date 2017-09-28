package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{name}")
    public User getUser(@PathVariable("name") String name){
        return userRepository.findByName(name);
    }

    @PostMapping("/users/{name}")
    public ResponseEntity<String> addUser(@RequestBody User user){
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
