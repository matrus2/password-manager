package eu.matrus.passmanager.services;

import eu.matrus.passmanager.exceptions.ResourceNotFoundException;
import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.PasswordRepository;
import eu.matrus.passmanager.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordManagerService {

    private final PasswordRepository passRepository;
    private final UserRepository userRepository;

    public PasswordManagerService(PasswordRepository passRepository, UserRepository userRepository) {
        this.passRepository = passRepository;
        this.userRepository = userRepository;
    }

    public List<Password> retrievePasswordsForUserName(String userName) {
        String id = getUserIdFromName(userName);
        return passRepository.findByUserId(id);
    }

    public void addNewPassword(String userName, Password password) {
        String id = getUserIdFromName(userName);
        password.setUserId(id);
        passRepository.save(password);
    }

    public void deletePasswords(String userName) {
        String userId = getUserIdFromName(userName);
        List<Password> userPasswords = getPasswordsForUser(userId);
        passRepository.delete(userPasswords);
    }

    public void deleteSinglePassword(String userName, String passId) {
        String userId = getUserIdFromName(userName);
        List<Password> userPasswords = getPasswordsForUser(userId);
        Password toBeDeleted = userPasswords.stream().filter(password -> password.getId().equals(passId))
                .findFirst().orElse(null);
        if (toBeDeleted == null) {
            throw new ResourceNotFoundException(passId, "Password to be deleted not found");
        }
        passRepository.delete(toBeDeleted);
    }

    public void changePassword(String userName, String passId ){
        String userId = getUserIdFromName(userName);
        Password password = getPassword(passId);
        password.setUserId(userId);
        passRepository.save(password);
    }

    private List<Password> getPasswordsForUser(String userId){
        List<Password> userPasswords = passRepository.findByUserId(userId);
        if (userPasswords.isEmpty()) {
            throw new ResourceNotFoundException(userId, "Passwords for the user not found");
        }
        return userPasswords;
    }

    private String getUserIdFromName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new ResourceNotFoundException(name, "User not found");
        }
        return user.getId();
    }

    private Password getPassword(String passId) {
        Password password = passRepository.findById(passId);
        if (password == null) {
            throw new ResourceNotFoundException(passId, "User not found");
        }
        return password;
    }

}
