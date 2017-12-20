package eu.matrus.passmanager.services;

import eu.matrus.passmanager.exceptions.ResourceAlreadyExistsException;
import eu.matrus.passmanager.exceptions.ResourceNotFoundException;
import eu.matrus.passmanager.models.Authorities;
import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.PasswordRepository;
import eu.matrus.passmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataManagerService {

    private final PasswordRepository passRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${security.encrypt.secret-key}")
    private String secretKey;

    @Autowired
    public DataManagerService(PasswordRepository passRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passRepository = passRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Password> retrievePasswordsForUserName(String userName) {
        User user = getUserFromName(userName);
        List<Password> passwords = passRepository.findByUserName(userName);
        passwords.forEach(password -> {
            password.setPassword(decryptPassword(user.getSalt(), password.getPassword()));
        });
        return passwords;
    }

    public void addNewPassword(String userName, Password password) {
        User user = getUserFromName(userName);
        password.setPassword(encryptPassword(user.getSalt(), password.getPassword()));
        password.setUserName(userName);
        passRepository.save(password);
    }

    public void deletePasswords(String userName) {
        checkIfUserExists(userName);
        List<Password> passwords = passRepository.findByUserName(userName);
        passRepository.delete(passwords);
    }

    public void deleteSinglePassword(String userName, String passId) {
        checkIfUserExists(userName);
        Password password = getPassword(userName, passId);
        passRepository.delete(password);
    }

    public void changePassword(String userName, String passId, Password password) {
        User user = getUserFromName(userName);
        Password passwordDB = getPassword(userName, passId);
        password.setId(passwordDB.getId());
        password.setUserName(passwordDB.getUserName());
        password.setPassword(encryptPassword(user.getSalt(), password.getPassword()));
        passRepository.save(password);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        User userDBname = userRepository.findByUsername(user.getUsername());
        if (userDBname != null) {
            throw new ResourceAlreadyExistsException(user.getUsername(), "User with this name already exists");
        }
        User userDBemail = userRepository.findByEmail(user.getEmail());
        if (userDBemail != null) {
            throw new ResourceAlreadyExistsException(user.getEmail(), "User with this email already exists");
        }
        user.setSalt(KeyGenerators.string().generateKey());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthority(Authorities.USER.toString());
        userRepository.save(user);
    }

    public User getUser(String userName) {
        return getUserFromName(userName);
    }

    public void deleteUser(String userName) {
        User user = getUserFromName(userName);
        List<Password> passwords = passRepository.findByUserName(userName);
        passRepository.delete(passwords);
        userRepository.delete(user);
    }

    public void changeUser(String userName, User user) {
        User userDBname = getUserFromName(userName);
        // Do not change the id, created date, salt and user name
        user.setId(userDBname.getId());
        user.setSalt(userDBname.getSalt());
        user.setCreatedDate(userDBname.getCreatedDate());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private String encryptPassword(String salt, String password) {
        TextEncryptor encryptor = Encryptors.queryableText(secretKey, salt);
        return encryptor.encrypt(password);
    }

    private String decryptPassword(String salt, String password) {
        TextEncryptor encryptor = Encryptors.queryableText(secretKey, salt);
        return encryptor.decrypt(password);
    }

    private User getUserFromName(String name) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new ResourceNotFoundException(name, "User not found");
        }
        return user;
    }

    private Password getPassword(String userName, String passId) {
        Password password = passRepository.findByUserNameAndId(userName, passId);
        if (password == null) {
            throw new ResourceNotFoundException(passId, "Password to be deleted not found");
        }
        return password;
    }

    private void checkIfUserExists(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new ResourceNotFoundException(userName, "User not found");
        }
    }
}
