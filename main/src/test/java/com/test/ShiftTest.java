package com.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.config.TestContainerConfig;
import com.test.config.TestWebConfig;
import com.senla.dto.shift.ShiftFullResponseDto;
import com.senla.dto.user.UserResponseDto;
import com.senla.model.user.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {TestContainerConfig.class, TestWebConfig.class})
@TestPropertySource("testprops.properties")
@WebAppConfiguration
@Testcontainers
public class ShiftTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;



    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:16-master")
                    .asCompatibleSubstituteFor("postgres")
    )
            .withDatabaseName("test_container_db")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432);

    @BeforeAll
    public static void beforeAll() {
        postgresDB.start();
    }

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @DynamicPropertySource
    public static void pgProperties(DynamicPropertyRegistry registry) {
        registry.add("testcontainer.url", postgresDB::getJdbcUrl);
        registry.add("testcontainer.user", postgresDB::getUsername);
        registry.add("testcontainer.pass", postgresDB::getPassword);
    }

    @Test
    public void test1(){
        int a = 5;
    }

    private UsernamePasswordAuthenticationToken getCustomerMockedToken(){
        User user = User.builder().userId(1L).firstName("mocked_customer").build();
        String[] authorities = {"CUSTOMER"};
        return buildAuthToken(user, Arrays.asList(authorities));
    }

    private UsernamePasswordAuthenticationToken getAdminMockedToken(){
        User user = User.builder().userId(7L).firstName("mocked_admin").build();
        String[] authorities = {"SHIFT_GET_ALL"};
        return buildAuthToken(user, Arrays.asList(authorities));
    }

    private UsernamePasswordAuthenticationToken getInactiveDriverMockedToken(){
        User user = User.builder().userId(3L).firstName("mocked_inactive_driver").build();
        String[] authorities = {"SHIFT_GET_OWN", "SHIFT_PROCESS"};
        return buildAuthToken(user, Arrays.asList(authorities));
    }

    private UsernamePasswordAuthenticationToken getOnShiftDriverMockedToken(){
        User user = User.builder().userId(5L).firstName("mocked_onShift_driver").build();
        String[] authorities = {"DRIVER"};
        return buildAuthToken(user, Arrays.asList(authorities));
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(User user, List<String> authorities) {
        return new UsernamePasswordAuthenticationToken(
                modelMapper.map(user, UserResponseDto.class),
                null,
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    @Test
    public void testGetAllShifts() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(getAdminMockedToken());

        MvcResult result = mockMvc.perform(get("/shifts/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[*].shift_id").hasJsonPath())
                .andExpect(jsonPath("$[*].start_time").hasJsonPath())
                .andExpect(jsonPath("$[*].end_time").hasJsonPath())
                .andExpect(jsonPath("$[*].rate_info").hasJsonPath())
                .andExpect(jsonPath("$[*].cab_info").hasJsonPath())
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        List<ShiftFullResponseDto> arr = objectMapper.readValue(json, new TypeReference<>() {});
        Assertions.assertNotNull(arr);
        Assertions.assertEquals(8, arr.size());
    }

    @Test
    public void createShift() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(getInactiveDriverMockedToken());

        String startShiftJson = """
                {
                	"city_id": 2,
                	"cab_id": 4,
                	"shift_start_point": {
                			"type": "Point",
                			"coordinates": [
                				41.999,
                				23.999
                			]
                		}
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shifts/start")
                        .content(startShiftJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("shift_id").value(9))
                .andExpect(jsonPath("start_time").isNotEmpty())
                .andExpect(jsonPath("end_time").isEmpty())
                .andExpect(jsonPath("rate_info").hasJsonPath())
                .andExpect(jsonPath("rate_info.init_price").value(3.0))
                .andExpect(jsonPath("rate_info.rate_per_km").value(1.5))
                .andExpect(jsonPath("cab_info").hasJsonPath())
                .andExpect(jsonPath("cab_info.cab_id").value(4))
                .andExpect(jsonPath("cab_info.vin").value("vin4"))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void errorStartingShiftWithActiveShift() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(getInactiveDriverMockedToken());

        String startShiftJson = """
                {
                	"city_id": 2,
                	"cab_id": 4,
                	"shift_start_point": {
                			"type": "Point",
                			"coordinates": [
                				41.999,
                				23.999
                			]
                		}
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shifts/start")
                        .content(startShiftJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shifts/start")
                        .content(startShiftJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value(404));
    }

    @Test
    public void errorStartingShiftWithActiveCab() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(getInactiveDriverMockedToken());

        String startShift1Json = """
                {
                	"city_id": 2,
                	"cab_id": 6,
                	"shift_start_point": {
                			"type": "Point",
                			"coordinates": [
                				41.999,
                				23.999
                			]
                		}
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shifts/start")
                        .content(startShift1Json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("status").value(404));
    }

    @Test
    public void endShiftSuccessful() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(getOnShiftDriverMockedToken());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shifts/finish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("shift_id").value(5))
                .andExpect(jsonPath("start_time").isNotEmpty())
                .andExpect(jsonPath("end_time").isNotEmpty())
                .andExpect(jsonPath("rate_info").hasJsonPath())
                .andExpect(jsonPath("rate_info.init_price").hasJsonPath())
                .andExpect(jsonPath("rate_info.rate_per_km").hasJsonPath())
                .andExpect(jsonPath("cab_info").hasJsonPath())
                .andExpect(jsonPath("cab_info.cab_id").hasJsonPath())
                .andExpect(jsonPath("cab_info.vin").hasJsonPath())
                .andExpect(status().isCreated());
    }

    @Test
    public void endNonActiveShiftError() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(getInactiveDriverMockedToken());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shifts/finish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("status").value(404));
    }

    @Test
    public void nonAllowedCabStartShift() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(getInactiveDriverMockedToken());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shifts/finish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("status").value(404));
    }


}
