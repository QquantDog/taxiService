package com.test.dao;

import com.test.config.TestContainerConfig;
import com.senla.dao.user.UserDao;
import com.senla.exception.DaoException;
import com.senla.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = TestContainerConfig.class)
//@Testcontainers
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

//    @Container
    static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_container_db")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432)
            .withReuse(true);

    @BeforeAll
    static void beforeAll() {
        postgresDB.start();
    }

    //    штука нужна чтобы автоматически подменить нужные проперти и потом использовать их
    @DynamicPropertySource
    static void pgProperties(DynamicPropertyRegistry registry) {
        registry.add("testcontainer.url", postgresDB::getJdbcUrl);
        registry.add("testcontainer.user", postgresDB::getUsername);
        registry.add("testcontainer.pass", postgresDB::getPassword);
    }

    @Test
    void testFindById() throws DaoException {
        Optional<User> result = userDao.findById(3L);
        Assertions.assertTrue(result.isPresent());

        User user = result.get();
        Assertions.assertEquals(user.getUserId(), 3L);
        Assertions.assertEquals(user.getFirstName(), "Oleg");
        Assertions.assertEquals(user.getLastName(), "aaa");
        Assertions.assertEquals(user.getEmail(), "pleg@gmail.com");
        Assertions.assertEquals(user.getPhoneNumber(), "+375-72-3324214");
        Assertions.assertEquals(user.getPassword(), "hashedPWD3");
        Assertions.assertNotNull(user.getRegistrationDate());
    }

    @Test
    void testFindByEmail(){
        String email = "max@gmail.com";
        Optional<User> result = userDao.findByEmail(email);
        Assertions.assertTrue(result.isPresent());

        User user = result.get();
        Assertions.assertEquals(user.getEmail(), email);
    }

    @Test
    void TestAllJoinWithPassengers(){
        List<Object[]> result = userDao.allJoinWithPassengers();
        Assertions.assertFalse(result.isEmpty());

        for(Object[] objects : result){
            Assertions.assertEquals(objects.length, 2);
            Assertions.assertEquals((String)objects[1], "customer");
        }
    }
    @Test
    void testFindAllActiveDriversWithAccountRegisteredForMoreThanMonths(){
        List<User> drivers = userDao.findAllActiveDriversWithAccountRegisteredForMoreThanMonths(10);
        Assertions.assertEquals(drivers.size(), 2);

        List<User> noDrivers = userDao.findAllActiveDriversWithAccountRegisteredForMoreThanMonths(20);
        Assertions.assertTrue(noDrivers.isEmpty());

    }

    @Test
    void testFindAllAdmins(){
        List<User> admins = userDao.findAllAdmins();
        Assertions.assertFalse(admins.isEmpty());
        User admin = admins.getFirst();
        Assertions.assertEquals(admin.getFirstName(), "Admin");
    }

    @Test
    @Transactional
    void testFindAllDefaultUsers(){
        List<User> defaultUsersWithRoles = userDao.findAllDefault();
        Assertions.assertFalse(defaultUsersWithRoles.isEmpty());

        for(User user : defaultUsersWithRoles){
            String roleName = user.getRole().getRoleName();
            Assertions.assertNotEquals(roleName, "admin");
            Assertions.assertTrue(roleName.equals("customer") || roleName.equals("driver"));
        }
    }

    @Test
    @Transactional
    void testFindUsersWithRole(){
        List<User> customers = userDao.findUsersWithRole("customer");
        List<User> drivers = userDao.findUsersWithRole("driver");

        Assertions.assertFalse(customers.isEmpty());
        Assertions.assertFalse(drivers.isEmpty());

        for(User user : customers){
            String roleName = user.getRole().getRoleName();
            Assertions.assertEquals(roleName, "customer");
        }

        for(User user : drivers){
            String roleName = user.getRole().getRoleName();
            Assertions.assertEquals(roleName, "driver");
        }
    }

    @Test
    @Transactional
    void testFindUsersWithPrivileges(){
        List<User> invalidPrivilegeCode = userDao.findUsersWithPrivileges("weg");
        Assertions.assertTrue(invalidPrivilegeCode.isEmpty());

        List<User> defaultPrivilegeCode = userDao.findUsersWithPrivileges("usr");
        Assertions.assertFalse(defaultPrivilegeCode.isEmpty());

        List<String> privileges = new ArrayList<>();
        privileges.add("adm");
        privileges.add("usr");
        List<User> allUsers = userDao.findUsersWithPrivileges(privileges);
        Assertions.assertFalse(allUsers.isEmpty());

        Assertions.assertEquals(allUsers.size(), 5);
        Assertions.assertEquals(defaultPrivilegeCode.size(), 4);
    }


}

