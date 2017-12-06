package eu.matrus.passmanager;

import eu.matrus.passmanager.models.Password;
import org.json.JSONException;
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
import java.text.ParseException;
import java.util.List;

public class PasswordControllerTests extends TestConfigurator {


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

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
        List<Password> userPasswords = passwordRepository.findByUserName(CORRECT_USER_NAME);
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
        List<Password> userPasswords = passwordRepository.findByUserName(CORRECT_USER_NAME);
        JsonObject newData = Json.createObjectBuilder()
                .add("name", "whatever")
                .add("login", "whatever")
                .add("password", "whatever")
                .add("url", "http://www.whatever.com")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newData.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME + "/" + userPasswords.get(1).getId()),
                HttpMethod.PUT, entity, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
