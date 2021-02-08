package com.karol.users.application;

import com.karol.users.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getUserTest() throws Exception {

        //given
        User testUser = new User();
        testUser.setId(Long.valueOf(1));
        testUser.setUsername("USERNAME-1");
        testUser.setCountry("Germany");
        testUser.setDateOfBirth(LocalDate.parse("29/01/1991", DateTimeFormatter.ofPattern("dd/MM/uuuu")));
        testUser.setTestUser(true);
        when(userRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(testUser));

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        //then
        JSONAssert.assertEquals(
                "{\"id\":1,\"username\":\"USERNAME-1\",\"dateOfBirth\":\"29/01/1991\",\"country\":\"Germany\",\"testUser\":false}",
                mvcResult.getResponse().getContentAsString(),
                false
        );
    }

    @Test
    public void addUserTest() throws Exception {

        //given
        User testUser = new User();
        testUser.setId(Long.valueOf(1));
        testUser.setUsername("USERNAME-1");
        testUser.setCountry("France");
        testUser.setDateOfBirth(LocalDate.parse("29/01/1991", DateTimeFormatter.ofPattern("dd/MM/uuuu")));
        testUser.setTestUser(true);

        when(userRepository.save(Mockito.any())).thenReturn(testUser);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"username\":\"USERNAME-1\",\"dateOfBirth\":\"29/01/1991\",\"country\":\"France\",\"testUser\":false}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void addUserMinorTest() throws Exception {

        //given
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);

        User testUser = new User();
        testUser.setId(Long.valueOf(1));
        testUser.setUsername("USERNAME-1");
        testUser.setCountry("France");
        testUser.setDateOfBirth(oneYearAgo);
        testUser.setTestUser(true);

        when(userRepository.save(Mockito.any())).thenReturn(testUser);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .content(String.format("{\"id\":1,\"username\":\"USERNAME-1\",\"dateOfBirth\":\"%s\",\"country\":\"France\",\"testUser\":false}", oneYearAgo.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void addUserOutsideFranceTest() throws Exception {
        //given
        User testUser = new User();
        testUser.setId(Long.valueOf(1));
        testUser.setUsername("USERNAME-1");
        testUser.setCountry("France");
        testUser.setDateOfBirth(LocalDate.parse("29/01/1991", DateTimeFormatter.ofPattern("dd/MM/uuuu")));
        testUser.setTestUser(true);

        when(userRepository.save(Mockito.any())).thenReturn(testUser);

        //when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"username\":\"USERNAME-1\",\"dateOfBirth\":\"29/01/1991\",\"country\":\"Germany\",\"testUser\":false}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
