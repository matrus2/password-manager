package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.services.DataManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private DataManagerService dataService;

    @Autowired
    public UserController(DataManagerService dataService) {
        this.dataService = dataService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getUsers() {
        return dataService.getUsers();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@Valid @RequestBody User user) {
        dataService.addUser(user);
    }

    @GetMapping("{name}")
    @PreAuthorize("#name == authentication.name OR hasAuthority('ADMIN')")
    public User getUser(@PathVariable("name") String name) {
        return dataService.getUser(name);
    }

    @DeleteMapping("{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("#name == authentication.name OR hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable("name") String name) {
        dataService.deleteUser(name);
    }

    @PutMapping("{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("#name == authentication.name OR hasAuthority('ADMIN')")
    public void changeUser(@PathVariable("name") String name, @Valid @RequestBody User user) {
        dataService.changeUser(name, user);
    }
}
