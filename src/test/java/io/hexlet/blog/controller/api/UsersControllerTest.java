package io.hexlet.blog.controller.api;

import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.UserRepository;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import net.datafaker.Faker;

import org.instancio.Instancio;
import org.instancio.Select;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import tools.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {
    @Autowired private MockMvc mockMvc;

    @Autowired private Faker faker;

    @Autowired private UserRepository userRepository;

    @Autowired private ObjectMapper om;

    @Test
    void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/users")).andExpect(status().isOk()).andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray();
    }

    @Test
    void testShow() throws Exception {
        var user =
                Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                        .create();
        userRepository.save(user);

        var expectedFirstName = user.getFirstName();
        var expectedLastname = user.getLastName();
        var expectedEmail = user.getEmail();

        var request = get("/api/users/" + user.getId()).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(expectedEmail))
                .andExpect(jsonPath("$.firstName").value(expectedFirstName))
                .andExpect(jsonPath("$.lastName").value(expectedLastname));
    }

    @Test
    void testCreate() throws Exception {

        var data = new HashMap<>();
        data.put("firstName", "John");
        data.put("lastName", "Connor");
        data.put("email", "terminator85@yahoo.com");

        var request =
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data));

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        Long userId = ((Number) JsonPath.read(responseJson, "$.id")).longValue();

        var user = userRepository.findById(userId).orElseThrow(
                () -> new AssertionError("User '" + userId + "' not found"));

        assertThat(user.getFirstName()).isEqualTo(("John"));
        assertThat(user.getLastName()).isEqualTo(("Connor"));
        assertThat(user.getEmail()).isEqualTo(("terminator85@yahoo.com"));
    }

    @Test
    public void testUpdate() throws Exception {
        var user =
                Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                        .create();
        userRepository.save(user);

        var data = new HashMap<>();
        data.put("firstName", "Mike");
        data.put("lastName", "Shinoda");
        data.put("email", "lp-forever@gmail.com");

        var request =
                put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());
        var userId = user.getId();
        user = userRepository.findById(userId).orElseThrow(
                () -> new AssertionError("Post '" + userId + "' not found"));

        assertThat(user.getFirstName()).isEqualTo(("Mike"));
        assertThat(user.getLastName()).isEqualTo(("Shinoda"));
    }

    @Test
    public void testDestroy() throws Exception {
        var user =
                Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                        .create();
        userRepository.save(user);

        var deleteRequest =
                delete("/api/users/" + user.getId());
        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());

        var getRequest = get("/api/users/" + user.getId());
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

        boolean existsInDb = userRepository.existsById(user.getId());
        assertThat(existsInDb).isFalse();
    }
}
