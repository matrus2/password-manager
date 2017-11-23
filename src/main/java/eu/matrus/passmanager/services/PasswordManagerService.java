package eu.matrus.passmanager.services;

import eu.matrus.passmanager.exceptions.ResourceAlreadyExistsException;
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
        String id = getUserFromName(userName).getId();
        return passRepository.findByUserId(id);
    }

    public void addNewPassword(String userName, Password password) {
        String id = getUserFromName(userName).getId();
        password.setUserId(id);
        passRepository.save(password);
    }

    public void deletePasswords(String userName) {
        String userId = getUserFromName(userName).getId();
        deletePasswordsByUserId(userId);
    }

    public void deleteSinglePassword(String userName, String passId) {
        String userId = getUserFromName(userName).getId();
        List<Password> userPasswords = getPasswordsForUser(userId);
        Password toBeDeleted = userPasswords.stream().filter(password -> password.getId().equals(passId))
                .findFirst().orElse(null);
        if (toBeDeleted == null) {
            throw new ResourceNotFoundException(passId, "Password to be deleted not found");
        }
        passRepository.delete(toBeDeleted);
    }

    public void changePassword(String userName, String passId) {
        String userId = getUserFromName(userName).getId();
        Password password = getPassword(passId);
        password.setUserId(userId);
        passRepository.save(password);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        User userDBname = userRepository.findByName(user.getName());
        if (userDBname != null) {
            throw new ResourceAlreadyExistsException(user.getName(), "User with this name already exists");
        }
        User userDBemail = userRepository.findByEmail(user.getEmail());
        if (userDBemail != null) {
            throw new ResourceAlreadyExistsException(user.getEmail(), "User with this email already exists");
        }
        userRepository.save(user);
    }

    public User getUser(String userName) {
        return getUserFromName(userName);
    }

    public void deleteUser(String userName) {
        User user = getUserFromName(userName);
        deletePasswordsByUserId(user.getId());
        userRepository.delete(user);
    }

    public void changeUser(String userName, User user) {
        User userDBname = getUserFromName(userName);
        user.setId(userDBname.getId());
        userRepository.save(user);
    }

    private void deletePasswordsByUserId(String userId) {
        List<Password> userPasswords = getPasswordsForUser(userId);
        passRepository.delete(userPasswords);
    }

    private List<Password> getPasswordsForUser(String userId) {
        List<Password> userPasswords = passRepository.findByUserId(userId);
        if (userPasswords.isEmpty()) {
            throw new ResourceNotFoundException(userId, "Passwords for the user not found");
        }
        return userPasswords;
    }

    private User getUserFromName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new ResourceNotFoundException(name, "User not found");
        }
        return user;
    }

    private Password getPassword(String passId) {
        Password password = passRepository.findById(passId);
        if (password == null) {
            throw new ResourceNotFoundException(passId, "User not found");
        }
        return password;
    }

}
