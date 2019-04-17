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
import java.io.IOException;

public class UserControllerTests extends TestConfigurator {

    @Test
    public void testGetSingleUserIfExists() throws JSONException, IOException {

        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userStandardName),
                HttpMethod.GET, entity, String.class);

        JSONAssert.assertEquals(DataHelper.getStandardUserResponse().toString(), response.getBody(), true);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testGetSingleUserIfNotExists() throws IOException {

        String access = getAccessToken(DataHelper.userAdminName, DataHelper.userAdminPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userNotSavedName),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

/*
    @Test
    public void testPostSingleUserIfNotExists() {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(DataHelper.getUserNotInDBData().toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testPostSingleUserIfExists() {
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(DataHelper.getStandardUserData().toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
*/
    @Test
    public void testPutSingleUserIfExists() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(DataHelper.getUserNotInDBData().toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userStandardName),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        // Second call to check if we can get data after user name changed
        headers.remove("Authorization");
        String accessAdmin = getAccessToken(DataHelper.userAdminName, DataHelper.userAdminPassword);
        headers.add("Authorization", "Bearer " + accessAdmin);
        entity = new HttpEntity<>(DataHelper.getUserNotInDBData().toString(), headers);

        ResponseEntity<String> responseNonUser = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userNotSavedName),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals(HttpStatus.OK, responseNonUser.getStatusCode());
        // Third call to check if there is adam as a user
        ResponseEntity<String> responseStandardUser = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userStandardName),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseStandardUser.getStatusCode());
    }

    @Test
    public void testPutSingleUserIfNotExists() throws IOException {
        String access = getAccessToken(DataHelper.userAdminName, DataHelper.userAdminPassword);
        headers.add("Authorization", "Bearer " + access);
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(DataHelper.getUserNotInDBData().toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userNotSavedName),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAllUsersIfAdmin() throws JSONException, IOException {
        String access = getAccessToken(DataHelper.userAdminName, DataHelper.userAdminPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT),
                HttpMethod.GET, entity, String.class);

        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonArray expected = factory.createArrayBuilder()
                .add(DataHelper.getStandardUserResponse())
                .add(DataHelper.getAdminUserResponse())
                .build();
        JSONAssert.assertEquals(expected.toString(), response.getBody(), true);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteSingleUserIfNotExists() throws IOException {
        String access = getAccessToken(DataHelper.userAdminName, DataHelper.userAdminPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userNotSavedName),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteSingleUserIfExists() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(USER_ENDPOINT + "/" + DataHelper.userStandardName),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }
}
