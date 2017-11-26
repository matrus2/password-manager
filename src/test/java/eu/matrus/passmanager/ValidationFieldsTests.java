package eu.matrus.passmanager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.json.Json;
import javax.json.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ValidationFieldsTests extends TestConfigurator {

    private final static String ERROR_MESSAGE = "Validation Error";

    @Test
    public void testPostPasswordName() throws JSONException {
        JsonObject newPassword = Json.createObjectBuilder()
                .add("name", "")
                .add("login", "test")
                .add("password", "test1")
                .add("url", "http://gumtree.pl")
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newPassword.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.POST, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(ERROR_MESSAGE, json.get("errorMessage"));
    }

    @Test
    public void testPostPasswordUrl() {
        List<String> urlsToCheck = Arrays.asList("asd","gumtree.pl", "asd@asd.pl","as://asd");
        urlsToCheck.forEach(this::testWrongUrl);
    }

    private void testWrongUrl(String url) {
        JsonObject newPasswordwithWrongUrl = Json.createObjectBuilder()
                .add("name", "gumtree")
                .add("login", "test")
                .add("password", "test1")
                .add("url", url)
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newPasswordwithWrongUrl.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + CORRECT_USER_NAME),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("Bad Request", HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPostUserName() throws ParseException, JSONException {
        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23");
        JsonObject newUser = Json.createObjectBuilder()
                .add("name", "")
                .add("email", "adas@adam.pl")
                .add("password", "adamadam")
                .add("lastLogged", myDate.getTime())
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newUser.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.POST, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(ERROR_MESSAGE, json.get("errorMessage"));

    }

    @Test
    public void testPostUserPassword() {
        List<String> passwordsToCheck = Arrays.asList("", "asdasd", "asdasdasdasdasdasdasdsadasdasdasdasdasdasdasdasdasdasdasdasdasd");
        passwordsToCheck.forEach(password -> {
            try {
                testWrongPassword(password);
            } catch (ParseException | JSONException e){
                e.printStackTrace();
            }
        });
    }

    public void testWrongPassword(String password) throws JSONException, ParseException {
        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23");
        JsonObject newUser = Json.createObjectBuilder()
                .add("name", "asdasd")
                .add("email", "adas@adam.pl")
                .add("password", password)
                .add("lastLogged", myDate.getTime())
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newUser.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.POST, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(ERROR_MESSAGE, json.get("errorMessage"));

    }

    @Test
    public void testPostUserEmail() {
        List<String> emailsToCheck = Arrays.asList("","ads@dfg.", "@a.pl", "asda");
        emailsToCheck.forEach(email -> {
            try {
                testWrongEmail(email);
            } catch (ParseException | JSONException e){
                e.printStackTrace();
            }
        });
    }

    private void testWrongEmail(String email) throws ParseException, JSONException {
        Date myDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23");
        JsonObject newUser = Json.createObjectBuilder()
                .add("name", "maciej")
                .add("email", email)
                .add("password", "adamadam")
                .add("lastLogged", myDate.getTime())
                .build();

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(newUser.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.POST, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(ERROR_MESSAGE, json.get("errorMessage"));
    }

    @Test
    public void testPutPasswordName() {
    }

    @Test
    public void testPutPasswordUrl() {
    }

    @Test
    public void testPutUserName() {
    }

    @Test
    public void testPutUserEmail() {
    }

}