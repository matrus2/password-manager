package eu.matrus.passmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Document(collection = "users")
public class User {
    @Id
    @JsonIgnore
    private String id;

    @NotEmpty
    @Indexed(unique = true)
    @Size(max = 60, message = "Name cannot be null or exceeds 60 chars")
    private String name;

    @NotEmpty
    @Email(message = "Provide valid email value")
    private String email;

    @NotEmpty
    @Size(min = 7, max = 60, message = "Password must have more then 7 chars and less then 60 chars")
    private String password;

    @CreatedDate
    private Date createdDate;

    public User() {
    }

    public User(String name, String email, String password, Date createdDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
    }
}
