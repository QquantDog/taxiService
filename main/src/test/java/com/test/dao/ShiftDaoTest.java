package com.test.dao;

import com.test.config.TestContainerConfig;
import com.senla.dao.shift.ShiftDao;
import com.senla.model.shift.Shift;
import com.senla.specification.ShiftSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = TestContainerConfig.class)
//@Testcontainers
public class ShiftDaoTest {

    @Autowired
    private ShiftDao shiftDao;

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


    @Test
    void testShiftBySpecification(){
        var startFrom = LocalDateTime.of(2023, 1, 1, 0, 0);
        var startTo = LocalDateTime.of(2025, 1, 1, 0, 0);
        boolean isActive = true;
        Specification<Shift> spec = ShiftSpecification.buildSpecification(startFrom, startTo, null, null, isActive);
        List<Shift> shifts = shiftDao.findShiftBySpecification(spec);

        Assertions.assertEquals(2, shifts.size());
        Assertions.assertNull(shifts.get(0).getEndtime());
        Assertions.assertNull(shifts.get(1).getEndtime());
    }

    @Test
    void testFindOpenShifts(){
        List<Shift> shifts = shiftDao.findOpenShifts();
        Assertions.assertEquals(2, shifts.size());

        for(Shift shift : shifts){
            Assertions.assertNull(shift.getEndtime());
        }
    }

}