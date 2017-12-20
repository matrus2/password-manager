package eu.matrus.passmanager;

import javax.json.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class DataHelper {
    static String userStandardName = "adam";
    static String userStandardPassword = "adamadam";
    static String userStandardEmail = "adam@adam.pl";
    static String userStandardSalt = "9d0545c63e322475";

    static String userAdminName = "pawel";
    static String userAdminPassword = "pawelpawel";
    static String userAdminEmail = "pawel@pawel.pl";
    static String userAdminSalt = "d303e6ab8af7e61f";

    static String userNotSavedName = "maciej";

    static List<Map<String, String>> passwords;
    static JsonObject passwordNotInDB;

    static {
        passwords = new ArrayList<>();
        passwords.add(new HashMap<String, String>() {
            {
                put("name", "allegro");
                put("login", "test");
                put("password", "test1");
                put("url", "http://allegro.pl");
            }
        });

        passwords.add(new HashMap<String, String>() {
            {
                put("name", "olx");
                put("login", "test");
                put("password", "test1");
                put("url", "http://olx.pl");
            }
        });

        passwordNotInDB = Json.createObjectBuilder()
                .add("name", "gumtree")
                .add("login", "test")
                .add("password", "test1")
                .add("url", "http://gumtree.pl")
                .build();
    }

    static JsonObject getStandardUserResponse() {
        return Json.createObjectBuilder()
                .add("username", userStandardName)
                .add("email", userStandardEmail)
                .add("accountNonExpired", true)
                .add("accountNonLocked", true)
                .add("credentialsNonExpired", true)
                .add("enabled", true)
                .build();
    }

    static JsonObject getAdminUserResponse() {
        return Json.createObjectBuilder()
                .add("username", userAdminName)
                .add("email", userAdminEmail)
                .add("accountNonExpired", true)
                .add("accountNonLocked", true)
                .add("credentialsNonExpired", true)
                .add("enabled", true)
                .build();
    }

    static JsonObject getStandardUserData() {
        return Json.createObjectBuilder()
                .add("username", userStandardName)
                .add("email", userStandardEmail)
                .add("password", userStandardName + userStandardName)
                .build();
    }

    static JsonObject getUserNotInDBData() {
        return Json.createObjectBuilder()
                .add("username", userNotSavedName)
                .add("email", "maciej@maciej.pl")
                .add("password", "maciejmaciej")
                .build();
    }

    static JsonArray getPasswordsData() {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder expected = factory.createArrayBuilder();
        passwords.forEach(password ->
                expected.add(factory.createObjectBuilder()
                        .add("name", password.get("name"))
                        .add("login", password.get("login"))
                        .add("password", password.get("password"))
                        .add("url", password.get("url"))));

        return expected.build();
    }
}