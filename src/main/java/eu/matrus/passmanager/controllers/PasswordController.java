package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.services.DataManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("passwords/{userName}")
public class PasswordController {

    private DataManagerService service;

    @Autowired
    public PasswordController(DataManagerService service) {
        this.service = service;
    }

    @GetMapping()
    @PreAuthorize("#name == authentication.name")
    public List<Password> getPasswords(@PathVariable("userName") String name) {
        return service.retrievePasswordsForUserName(name);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#name == authentication.name")
    public void addPassword(@PathVariable("userName") String name, @Valid @RequestBody Password passwordData) {
        service.addNewPassword(name, passwordData);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("#name == authentication.name")
    public void deletePasswords(@PathVariable("userName") String name) {
        service.deletePasswords(name);
    }

    @DeleteMapping("{passId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("#name == authentication.name")
    public void deleteSinglePassword(@PathVariable("userName") String name, @PathVariable String passId) {
        service.deleteSinglePassword(name, passId);
    }

    @PutMapping("{passId}")
    @PreAuthorize("#name == authentication.name")
    public void changePassword(@PathVariable("userName") String name, @PathVariable String passId, @Valid @RequestBody Password password) {
        service.changePassword(name, passId, password);
    }
}
