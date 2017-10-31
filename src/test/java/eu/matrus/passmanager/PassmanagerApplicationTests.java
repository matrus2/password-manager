package eu.matrus.passmanager;

import eu.matrus.passmanager.models.User;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

public class PassmanagerApplicationTests extends TestConfigurator {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private String port;

    @Test
    public void testGetSingleUser() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users/adam"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"name\":\"adam\",\"email\":\"adam@adam.pl\",\"password\":\"adamadam\",\"lastLogged\":1416697200000}";

        JSONAssert.assertEquals(expected, response.getBody(), true);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.FOUND);
    }

    @Test
    public void testPostSingleUser() {

        User newUser = new User("maciej", "maciej@maciej.pl", "maciejmaciej");
        headers.add("Content-Type", "application/json");
        HttpEntity<User> entity = new HttpEntity<User>(newUser, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
