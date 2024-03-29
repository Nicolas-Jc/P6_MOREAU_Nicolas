package Service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;
    //@Mock
    //@Mock
    @InjectMocks
    UserService userService;
    User userTestExistDB;
    User userTestNotExistDB;
    List<User> contactsListTest;

    @BeforeEach
    void dataSets() {
        contactsListTest = new ArrayList<>();
        userTestExistDB = new User("NomUser1", "PrenomUser1", "mailuser1@gmail.com", "123456", 0.00f, contactsListTest);
        userTestNotExistDB = new User("NomUserNotExistDB", "PrenomUserNotExistDB", "mailuserNotExistDB@gmail.com", "123456", 0.00f, contactsListTest);
    }

    @Test
    void GetUserByEmailTest() {
        // GIVEN
        when(userRepository.findByEmail(userTestExistDB.getEmail())).thenReturn(userTestExistDB);
        // WHEN
        User userTest = userService.getUserByEmail(userTestExistDB.getEmail());
        // THEN
        assertEquals(userTestExistDB, userTest);
        verify(userRepository, Mockito.times(1)).findByEmail(userTestExistDB.getEmail());
    }

    @Test
    void AddNotExistUserTest() {   // GIVEN - retour null - inexistant en BDD
        when(userService.getUserByEmail(userTestNotExistDB.getEmail())).thenReturn(null);
        // WHEN
        User userToAdd = userService.addUser(userTestNotExistDB);
        // THEN --
        assertEquals(userTestNotExistDB.getLastname(), userToAdd.getLastname());
        assertEquals(userTestNotExistDB.getFirstname(), userToAdd.getFirstname());
        assertEquals(userTestNotExistDB.getEmail(), userToAdd.getEmail());
        assertEquals(userTestNotExistDB.getBalance(), userToAdd.getBalance());

        verify(userRepository, Mockito.times(1)).saveAndFlush(userToAdd);
    }

    @Test
    void AddExistUserTest() {
        // GIVEN --- retour <> null
        when(userService.getUserByEmail(userTestExistDB.getEmail())).thenReturn(userTestExistDB);
        // WHEN
        User userToAdd = userService.addUser(userTestExistDB);
        // THEN --- retour attendu => User to add = User existant en BDD renvoyé en retour
        assertEquals(userTestExistDB, userToAdd);
    }

    // --------------------------------------------------------------------------------

    @Test
    void UpdateUserTest() {
        // GIVEN
        when(userRepository.findByEmail(userTestExistDB.getEmail())).thenReturn(userTestExistDB);
        // WHEN
        User userExistsToUpdate = userService.updateUser(
                userTestExistDB.getEmail(),
                userTestExistDB.getLastname(),
                userTestExistDB.getFirstname(),
                userTestExistDB.getPassword());
        // THEN
        assertEquals(userTestExistDB, userExistsToUpdate);
        verify(userRepository, Mockito.times(1)).findByEmail(userTestExistDB.getEmail());
    }

}
