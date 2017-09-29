package eu.matrus.passmanager.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.Date;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String email;
    private String password;
    private Date lastLogged;

    public User() {
        this.lastLogged = new Date();
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastLogged = new Date();
    }

}
