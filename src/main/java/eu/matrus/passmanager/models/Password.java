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
    private String loginId;
    private String password;
    private String url;

    public Password(String id, String name, String userId, String loginId, String password, String url) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.loginId = loginId;
        this.password = password;
        this.url = url;
    }

}
