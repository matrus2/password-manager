package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.services.PasswordManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("passwords/{userName}")
public class PasswordController {

    private PasswordManagerService service;

    public PasswordController(PasswordManagerService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Password> getPasswords(@PathVariable("userName") String name) {
        return service.retrievePasswordsForUserName(name);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addPassword(@PathVariable("userName") String name, @Valid @RequestBody Password passwordData) {
        service.addNewPassword(name, passwordData);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePasswords(@PathVariable("userName") String name) {
        service.deletePasswords(name);
    }

    @DeleteMapping("{passId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteSinglePassword(@PathVariable("userName") String name, @PathVariable String passId) {
        service.deleteSinglePassword(name, passId);
    }

    @PutMapping("{passId}")
    public void changePassword(@PathVariable("userName") String name, @PathVariable String passId, @Valid @RequestBody Password password) {
        service.changePassword(name, passId, password);
    }
}
