package eu.matrus.passmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Data
@Document(collection = "users")
public class User implements UserDetails{
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

    @JsonIgnore
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
