package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.PasswordRepository;
import eu.matrus.passmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("passwords/{userName}")
public class PasswordController {

    @Autowired
    PasswordRepository passRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<String> getPasswords(@PathVariable("userName") String name){
        String id = getUserIdFromName(name);
        if (id != null) {
            List<Password> json = passRepository.findByUserId(id);
            return new ResponseEntity<>(json.toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping()
    public ResponseEntity<String> addPassword(@PathVariable("userName") String name, @RequestBody Password pass){
        String id = getUserIdFromName(name);
        if (id != null) {
            pass.setUserId(id);
            passRepository.save(pass);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{passId}")
    public ResponseEntity<String> deletePassword(@PathVariable("userName") String name, @PathVariable String passId){
        String id = getUserIdFromName(name);
        if (id != null) {
            Password pass = passRepository.findById(passId);
            if (pass != null) {
                passRepository.delete(pass);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{passId}")
    public  ResponseEntity<String> changePassword(@PathVariable("userName") String name, @PathVariable String passId){
        String id = getUserIdFromName(name);
        if (id != null) {
            Password pass = passRepository.findById(passId);
            if (pass != null) {
                pass.setUserId(id);
                passRepository.save(pass);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private String getUserIdFromName(String name) {
        User user = userRepository.findByName(name);
        return user != null
                ? user.getId()
                : null;
    }
}
