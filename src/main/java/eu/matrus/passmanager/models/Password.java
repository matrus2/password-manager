package eu.matrus.passmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "passwords")
public class Password {

    @Id
    private String id;
    @NotNull(message = "Name cannot be null")
    private String name;
    @JsonIgnore
    private String userId;
    private String login;
    private String password;
    private String url;

    public Password(){}

    public Password(String name, String userId, String login, String password, String url) {
        this.name = name;
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.url = url;
    }
}
