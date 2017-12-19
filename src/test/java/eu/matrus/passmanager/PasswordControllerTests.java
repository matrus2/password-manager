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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class PasswordControllerTests extends TestConfigurator {


    @Test
    public void testGetPasswordsIfUserExists() throws JSONException, ParseException, IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userStandardName),
                HttpMethod.GET, entity, String.class);

        JSONAssert.assertEquals(DataHelper.getPasswordsData().toString(), response.getBody(), false);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetPasswordsIfUserNotExists() throws JSONException, ParseException, IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userNotSavedName),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testPostPasswordIfUserExists() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(DataHelper.passwordNotInDB.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userStandardName),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testPostPasswordIfUserNotExists() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(DataHelper.passwordNotInDB.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userNotSavedName),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testDeletePasswordsIfUserExist() throws JSONException, IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userStandardName),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        // Assert that there no passwords any more
        ResponseEntity<String> responseWithPass = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userStandardName),
                HttpMethod.GET, entity, String.class);

        JSONAssert.assertEquals("[]", responseWithPass.getBody(), false);
    }

    @Test
    public void testDeleteSinglePasswordIfUserAndPasswordExist() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        List<Password> userPasswords = passwordRepository.findByUserName(DataHelper.userStandardName);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userStandardName + "/" + userPasswords.get(1).getId()),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }


    @Test
    public void testDeleteSinglePasswordIfUserNotExists() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userNotSavedName + "/itdoesntmatter"),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testDeleteSinglePasswordIfUserExistsAndPasswordNotExists() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userStandardName + "/d1d12d21d321d"),
                HttpMethod.DELETE, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPutPasswordIfUserNotExists() throws IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userNotSavedName + "/itdoesntmatter"),
                HttpMethod.PUT, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testPutPasswordIfUserExists() throws JSONException, IOException {
        String access = getAccessToken(DataHelper.userStandardName, DataHelper.userStandardPassword);
        headers.add("Authorization", "Bearer " + access);
        List<Password> userPasswords = passwordRepository.findByUserName(DataHelper.userStandardName);

        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(DataHelper.passwordNotInDB.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(PASSWORD_ENDPOINT + DataHelper.userStandardName + "/" + userPasswords.get(1).getId()),
                HttpMethod.PUT, entity, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
