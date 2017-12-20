package eu.matrus.passmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.matrus.passmanager.models.Authorities;
import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.PasswordRepository;
import eu.matrus.passmanager.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class TestConfigurator {

    final static String PASSWORD_ENDPOINT = "/passwords/";
    final static String USER_ENDPOINT = "/users";
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordRepository passwordRepository;
    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String secret;
    @Value("${security.encrypt.secret-key}")
    private String secretKey;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private String port;
    private User userAdmin;
    private User userStandard;

    TestConfigurator() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try {
            userAdmin = new User(DataHelper.userStandardName,
                    DataHelper.userStandardEmail,
                    passwordEncoder.encode(DataHelper.userStandardPassword),
                    new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23"));
            userAdmin.setAuthority(Authorities.USER.toString());
            userAdmin.setSalt(DataHelper.userAdminSalt);
            userStandard = new User(DataHelper.userAdminName,
                    DataHelper.userAdminEmail,
                    passwordEncoder.encode(DataHelper.userAdminPassword),
                    new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23"));
            userStandard.setAuthority(Authorities.ADMIN.toString());
            userStandard.setSalt(DataHelper.userStandardSalt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Before
    public void prepareData() {
        saveUser(userAdmin);
        saveUser(userStandard);
    }

    private void saveUser(User user) {
        userRepository.save(user);
        List<Password> passwords = new ArrayList<>();
        TextEncryptor encryptor = Encryptors.queryableText(secretKey, user.getSalt());
        DataHelper.passwords.forEach(password ->
                passwords.add(new Password(
                        password.get("name"),
                        user.getUsername(),
                        password.get("login"),
                        encryptor.encrypt(password.get("password")),
                        password.get("url"))));

        passwordRepository.save(passwords);
    }

    @After
    public void removeData() {
        passwordRepository.deleteAll();
        userRepository.deleteAll();
    }

    String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    String getAccessToken(String username, String password) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " +
                new String(Base64Utils.encode((clientId + ":" + secret).getBytes())));
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        String body = "grant_type=password&username=" + username + "&password=" + password;
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/oauth/token"),
                HttpMethod.POST, entity, String.class);
        return new ObjectMapper().readTree(response.getBody()).path("access_token").asText();
    }
}
