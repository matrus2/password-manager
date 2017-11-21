package eu.matrus.passmanager;

import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.validation.constraints.Null;
import java.text.ParseException;
import java.util.List;

public class PasswordControllerTests extends TestConfigurator {

    final private static String PASSWORD_ENDPOINT = "/passwords/";


    @Test
    public void testGetPasswordsIfUserExists() throws JSONException, ParseException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.GET, entity, String.class);

        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArray expected = factory.createArrayBuilder()
                .add(factory.createObjectBuilder()
                        .add("name", "olx")
                        .add("login", "test")
                        .add("password", "test1")
                        .add("url", "http://olx.pl"))
                .add(factory.createObjectBuilder()
                        .add("name", "allegro")
                        .add("login", "test")
                        .add("password", "test1")
                        .add("url", "http://allegro.pl"))
                .build();

        JSONAssert.assertEquals(expected.toString(), response.getBody(), false);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetPasswordsIfUserNotExists() throws JSONException, ParseException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + USER_DOESNT_EXISTS),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testPostPasswordIfUserExists() {

        JsonObject newPassword = Json.createObjectBuilder()
                .add("name", "gumtree")
                .add("login", "test")
                .add("password", "test1")
                .add("url", "http://gumtree.pl")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newPassword.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testPostPasswordIfUserNotExists() {

        JsonObject newPassword = Json.createObjectBuilder()
                .add("name", "gumtree")
                .add("login", "test")
                .add("password", "test1")
                .add("url", "http://gumtree.pl")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newPassword.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + USER_DOESNT_EXISTS),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeletePasswordsIfUserExist() throws JSONException {
        User user = userRepository.findByName(CORRECT_USER_NAME);
        List<Password> userPasswords = passwordRepository.findByUserId(user.getId());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        // Assert that there no passwords any more
        ResponseEntity<String> responseWithPass = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.GET, entity, String.class);

        JSONAssert.assertEquals("[]", responseWithPass.getBody(), false);
    }

    @Test
    public void testDeleteSinglePasswordIfUserAndPasswordExist() {
        User user = userRepository.findByName(CORRECT_USER_NAME);
        List<Password> userPasswords = passwordRepository.findByUserId(user.getId());

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME + "/" + userPasswords.get(1).getId()),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }


    @Test
    public void testDeleteSinglePasswordIfUserNotExists() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + USER_DOESNT_EXISTS + "/itdoesntmatter"),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteSinglePasswordIfUserExistsAndPasswordNotExists() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME + "d1d12d21d321d"),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPutPasswordIfUserNotExists() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + USER_DOESNT_EXISTS + "/itdoesntmatter"),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPutPasswordIfUserExists() throws JSONException {
        User user = userRepository.findByName(CORRECT_USER_NAME);
        List<Password> userPasswords = passwordRepository.findByUserId(user.getId());

        JsonObject newData = Json.createObjectBuilder()
                .add("id", userPasswords.get(1).getId())
                .add("name", CORRECT_USER_NAME)
                .add("userId", user.getId())
                .add("login", "whatever")
                .add("password", "whatever")
                .add("url", "www.whatever.com")
                .build();

        HttpEntity<String> entity = new HttpEntity<>(newData.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME + "/" + userPasswords.get(1).getId()),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
