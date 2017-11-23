package eu.matrus.passmanager;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserControllerTests extends TestConfigurator {

    final private static String USER_ENDPOINT = "/users/";

    @Test
    public void testGetSingleUserIfExists() throws JSONException, ParseException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.GET, entity, String.class);

        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23");
        JsonObject expected = Json.createObjectBuilder()
                .add("name", "adam")
                .add("email", "adam@adam.pl")
                .add("password", "adamadam")
                .add("lastLogged", myDate.getTime())
                .build();

        JSONAssert.assertEquals(expected.toString(), response.getBody(), true);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetSingleUserIfNotExists() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + USER_DOESNT_EXISTS),
                HttpMethod.GET, entity, String.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPostSingleUserIfNotExists() {

        JsonObject newUser = Json.createObjectBuilder()
                .add("name", "maciej")
                .add("email", "maciej@maciej.pl")
                .add("password", "maciejmaciej")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newUser.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testPostSingleUserIfExists() {

        JsonObject newUser = Json.createObjectBuilder()
                .add("name", "adam")
                .add("email", "maciej@maciej.pl")
                .add("password", "maciejmaciej")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newUser.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testPutSingleUserIfExists() {
        JsonObject newData = Json.createObjectBuilder()
                .add("name", USER_DOESNT_EXISTS)
                .add("email", "maciej@maciej.pl")
                .add("password", "maciejmaciej")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newData.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Second call to check if there is maciej as a user
        ResponseEntity<String> responseFromMaciej = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + USER_DOESNT_EXISTS),
                HttpMethod.GET, null, String.class);

        Assert.assertEquals(HttpStatus.OK, responseFromMaciej.getStatusCode());
        // Third call to check if there is adam as a user
        ResponseEntity<String> responseFromAdam = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.GET, null, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseFromAdam.getStatusCode());
    }

    @Test
    public void testPutSingleUserIfNotExists() {
        JsonObject newData = Json.createObjectBuilder()
                .add("name", USER_DOESNT_EXISTS)
                .add("email", "maciej@maciej.pl")
                .add("password", "maciejmaciej")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newData.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + USER_DOESNT_EXISTS),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAllUsers() throws JSONException, ParseException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.GET, entity, String.class);

        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23");
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonArray expected = factory.createArrayBuilder()
                .add(factory.createObjectBuilder()
                        .add("name", "adam")
                        .add("email", "adam@adam.pl")
                        .add("lastLogged", myDate.getTime())
                        .add("password", "adamadam"))
                .add(factory.createObjectBuilder()
                        .add("name", "pawel")
                        .add("email", "pawel@pawel.pl")
                        .add("lastLogged", myDate.getTime())
                        .add("password", "pawelpawel"))
                .build();

        JSONAssert.assertEquals(expected.toString(), response.getBody(), true);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteSingleUserIfNotExists() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + USER_DOESNT_EXISTS),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteSingleUserIfExists() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }
}
