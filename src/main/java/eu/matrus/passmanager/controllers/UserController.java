package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.services.PasswordManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private PasswordManagerService service;

    public UserController(PasswordManagerService service) {
        this.service = service;
    }

    @GetMapping()
    public List<User> getUsers() {
        return service.getUsers();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@Valid @RequestBody User user) {
        service.addUser(user);
    }

    @GetMapping("{name}")
    public User getUser(@PathVariable("name") String name) {
        return service.getUser(name);
    }

    @DeleteMapping("{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable("name") String name) {
        service.deleteUser(name);
    }

    @PutMapping("{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changeUser(@PathVariable("name") String name, @Valid @RequestBody User user) {
        service.changeUser(name, user);
    }
}
