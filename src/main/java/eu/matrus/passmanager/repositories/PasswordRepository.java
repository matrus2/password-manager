package eu.matrus.passmanager.repositories;

import eu.matrus.passmanager.models.Password;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PasswordRepository extends MongoRepository<Password, String> {

    List<Password> findByUserId(String userId);

    Password findById(String id);
}
