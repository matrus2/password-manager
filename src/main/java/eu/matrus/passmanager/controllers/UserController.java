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

    @PostMapping()
    public ResponseEntity<String> addUser(@RequestBody User user) {
        User userDBname = userRepository.findByName(user.getName());
        User userDBemail = userRepository.findByEmail(user.getEmail());
        String responseBody = null;
        HttpStatus status = HttpStatus.CONFLICT;

        if (userDBname != null) responseBody = "User with that name already exists";
        if (userDBemail != null) responseBody = "User with that email already exists";
        if (userDBname != null && userDBemail != null) responseBody = "User with that name and email already exists";

        if (responseBody == null) {
            status = HttpStatus.CREATED;
            userRepository.save(user);
        }
        return new ResponseEntity<>(responseBody, status);
    }

    @GetMapping("{name}")
    public ResponseEntity<User> getUser(@PathVariable("name") String name) {
        User user = userRepository.findByName(name);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{name}")
    public ResponseEntity<String> deleteUser(@PathVariable("name") String name) {
        User user = userRepository.findByName(name);
        if (user != null) {
            userRepository.delete(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
