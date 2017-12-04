package eu.matrus.passmanager;

import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.PasswordRepository;
import eu.matrus.passmanager.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class TestConfigurator {

    @Autowired UserRepository userRepository;
    @Autowired PasswordRepository passwordRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    final static String CORRECT_USER_NAME = "adam";
    final static String USER_DOESNT_EXISTS = "mateusz";
    final static String PASSWORD_ENDPOINT = "/passwords/";
    final static String USER_ENDPOINT = "/users/";

    @LocalServerPort private String port;
    private User adam;
    private User pawel;

    TestConfigurator() {
        try {
            adam = new User("adam", "adam@adam.pl", "adamadam", new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23"));
            pawel = new User("pawel", "pawel@pawel.pl", "pawelpawel", new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void prepareData() {
        saveUser(adam);
        saveUser(pawel);
    }

    private void saveUser(User user) {
        userRepository.save(user);
        Password olx = new Password("olx", user.getName(), "test", "test1", "http://olx.pl");
        Password allegro = new Password("allegro", user.getName(), "test", "test1", "http://allegro.pl");
        passwordRepository.save(Arrays.asList(olx, allegro));
    }

    @After
    public void removeData() {
        passwordRepository.deleteAll();
        userRepository.deleteAll();
    }

    String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
