package eu.matrus.passmanager.controllers;

import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.PasswordRepository;
import eu.matrus.passmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("passwords/{userName}")
public class PasswordController {

    @Autowired
    PasswordRepository passRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<List<Password>> getPasswords(@PathVariable("userName") String name){
        String id = getUserIdFromName(name);
        if (id != null) {
            List<Password> json = passRepository.findByUserId(id);
            return new ResponseEntity<>(json, HttpStatus.OK);
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

    @DeleteMapping()
    public ResponseEntity<String> deletePasswords(@PathVariable("userName") String name){
        String userId = getUserIdFromName(name);
        if (userId != null) {
            List<Password> userPasswords = passRepository.findByUserId(userId);
            if (!userPasswords.isEmpty()) {
                passRepository.delete(userPasswords);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{passId}")
    public ResponseEntity<String> deletePassword(@PathVariable("userName") String name, @PathVariable String passId){
        String userId = getUserIdFromName(name);
        if (userId != null) {
            System.out.println(userId);
            List<Password> userPasswords = passRepository.findByUserId(userId);
            System.out.println(userPasswords);
            if (!userPasswords.isEmpty()) {
                Password toBeDeleted = userPasswords.stream().filter(password -> password.getId().equals(passId))
                        .findFirst().orElse(null);
                System.out.println("toBeDeleted");
                System.out.println(toBeDeleted);
                if (toBeDeleted != null) {
                    passRepository.delete(toBeDeleted);
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
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
