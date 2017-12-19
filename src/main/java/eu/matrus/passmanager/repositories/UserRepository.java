package eu.matrus.passmanager.repositories;

import eu.matrus.passmanager.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String name);

    User findByEmail(String email);
}

