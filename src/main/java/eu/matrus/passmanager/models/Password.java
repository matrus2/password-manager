package eu.matrus.passmanager.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class Password {

    @Id
    private String id;
    private String name;
    private String userId;
    private String login;
    private String password;
    private String url;

    public Password(){}

    public Password(String name, String login, String password, String url) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.url = url;
    }

}
