package eu.matrus.passmanager;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class PassmanagerApplicationTests extends TestConfigurator {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private String port;

    @Test
    public void testGetSingleUser() {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users/adam"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"name\":\"adam\",\"email\":\"adam@adam.pl\",\"password\":\"adamadam\",\"lastLogged\":1416697200000}";

        try {
            JSONAssert.assertEquals(expected, response.getBody(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
