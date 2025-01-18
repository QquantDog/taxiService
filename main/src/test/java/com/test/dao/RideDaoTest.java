package com.test.dao;

import com.test.config.TestContainerConfig;
import com.senla.dao.ride.RideDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = TestContainerConfig.class)
//@Testcontainers
public class RideDaoTest {

    @Autowired
    private RideDao rideDao;

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

    @DynamicPropertySource
    static void pgProperties(DynamicPropertyRegistry registry) {
        registry.add("testcontainer.url", postgresDB::getJdbcUrl);
        registry.add("testcontainer.user", postgresDB::getUsername);
        registry.add("testcontainer.pass", postgresDB::getPassword);
    }


}