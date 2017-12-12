package eu.matrus.passmanager.services;

import eu.matrus.passmanager.exceptions.ResourceNotFoundException;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Data
@Service("userDetailsService")
public class AccountService implements UserDetailsService {

    final UserRepository userRepository;

    @Autowired
    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByName(s);
        if (user == null) {
            throw new ResourceNotFoundException(s, "User with this name not found");
        }
        return user;
    }
}