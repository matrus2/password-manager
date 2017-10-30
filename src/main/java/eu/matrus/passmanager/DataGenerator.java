package eu.matrus.passmanager;

/*

    Leaving this for now as an example of CommandLineRunner

*/

import eu.matrus.passmanager.models.Password;
import eu.matrus.passmanager.models.User;
import eu.matrus.passmanager.repositories.PasswordRepository;
import eu.matrus.passmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


//@Component
//public class DataGenerator implements CommandLineRunner{
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordRepository passwordRepository;
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        User user1 = new User("adam", "adam@adam.pl", "adamadam", new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23"));
//        User user2 = new User("pawel", "pawel@pawel.pl", "pawelpawel", new SimpleDateFormat("yyyy-MM-dd").parse("2014-11-23"));
//        saveUser(user1);
//        saveUser(user2);
//    }
//
//    private void saveUser(User user) {
//        try {
//            userRepository.save(user);
//            passwordRepository.save(new Password("olx", user.getId(), "test", "test1", "http://olx.pl"));
//            passwordRepository.save(new Password("allegro", user.getId(), "test", "test1", "http://allegro.pl"));
//        } catch (Exception e) {
//            System.out.println("User " + user.getName() + " has already been saved.");
//        }
//    }
//}
