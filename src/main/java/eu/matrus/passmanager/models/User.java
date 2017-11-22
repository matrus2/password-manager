package eu.matrus.passmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "users")
public class User {
    @Id
    @JsonIgnore
    private String id;
    @Indexed(unique = true)
    private String name;
    private String email;
    private String password;
    @CreatedDate
    private Date lastLogged;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, Date lastLogged) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastLogged = lastLogged;
    }
}
