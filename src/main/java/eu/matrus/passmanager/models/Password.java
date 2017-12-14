package eu.matrus.passmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

@Data
@Document(collection = "passwords")
public class Password {

    @Id
    private String id;

    @NotEmpty(message = "Name cannot be null")
    @Size(max = 60, message = "Name cannot exceed 60 chars")
    private String name;

    @JsonIgnore
    private String userName;

    @NotEmpty(message = "Login cannot be null")
    @Size(max = 60, message = "Login cannot exceed 60 chars")
    private String login;

    @NotEmpty(message = "Password cannot be null")
    @Size(max = 70, message = "Password cannot exceed 60 chars")
    private String password;

    @URL(message = "Url must be valid")
    private String url;

    public Password() {
    }

    public Password(String name, String userName, String login, String password, String url) {
        this.name = name;
        this.userName = userName;
        this.login = login;
        this.password = password;
        this.url = url;
    }
}
