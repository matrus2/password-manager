package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("{name}")
    public User getUser(@PathVariable("name") String name) {
        return userRepository.findByName(name);
    }

    @PostMapping("{name}")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (userRepository.findByName(user.getName()) != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        } else {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    //deleteByLastname
}
