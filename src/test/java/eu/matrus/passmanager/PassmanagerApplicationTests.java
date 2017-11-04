package eu.matrus.passmanager;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PassmanagerApplicationTests extends TestConfigurator {

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private String port;

    @Test
    public void testGetSingleUserIfExists() throws JSONException, ParseException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users/adam"),
                HttpMethod.GET, entity, String.class);

        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23");
        JsonObject expected = Json.createObjectBuilder()
                .add("name", "adam")
                .add("email", "adam@adam.pl")
                .add("password", "adamadam")
                .add("lastLogged", myDate.getTime())
                .build();

        JSONAssert.assertEquals(expected.toString(), response.getBody(), true);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.FOUND);
    }

    @Test
    public void testGetSingleUserIfNotExists() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users/adamo"),
                HttpMethod.GET, entity, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
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
                createURLWithPort("/users"),
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
                createURLWithPort("/users"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testPutSingleUserIfExists() {
        JsonObject newData = Json.createObjectBuilder()
                .add("name", "maciej")
                .add("email", "maciej@maciej.pl")
                .add("password", "maciejmaciej")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newData.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users/adam"),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Second call to check if there is maciej as a user
        ResponseEntity<String> responseFromMaciej = restTemplate.exchange(
                createURLWithPort("/users/maciej"),
                HttpMethod.GET, null, String.class);

        Assert.assertEquals(HttpStatus.FOUND, responseFromMaciej.getStatusCode());
        // Third call to check if there is adam as a user
        ResponseEntity<String> responseFromAdam = restTemplate.exchange(
                createURLWithPort("/users/adam"),
                HttpMethod.GET, null, String.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, responseFromAdam.getStatusCode());
    }

    @Test
    public void testPutSingleUserIfNotExists() {
        JsonObject newData = Json.createObjectBuilder()
                .add("name", "maciej")
                .add("email", "maciej@maciej.pl")
                .add("password", "maciejmaciej")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newData.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users/maciej"),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
