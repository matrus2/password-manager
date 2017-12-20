package eu.matrus.passmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    @JsonIgnore
    private String id;

    @NotEmpty
    @Indexed(unique = true)
    @Size(max = 60, message = "Name cannot be null or exceeds 60 chars")
    private String username;

    @NotEmpty
    @Email(message = "Provide valid email value")
    private String email;

    @NotEmpty
    @Size(min = 7, max = 60, message = "Password must have more then 7 chars and less then 60 chars")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    private String salt;

    @JsonIgnore
    @CreatedDate
    private Date createdDate;

    @JsonIgnore
    private Collection<GrantedAuthority> authorities = new ArrayList<>();

    public User() {
    }

    public User(String username, String email, String password, Date createdDate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
    }

    public void setAuthority(String a) {
        authorities.add(new SimpleGrantedAuthority(a));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
