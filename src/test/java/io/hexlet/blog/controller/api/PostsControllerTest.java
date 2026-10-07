package io.hexlet.blog.controller.api;

import io.hexlet.blog.model.Post;
import io.hexlet.blog.repository.PostRepository;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
public class PostsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired private PostRepository postRepository;

    @Autowired private ObjectMapper om;

    @Test
    void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).isObject();
    }

    @Test
    void testShow() throws Exception {
        var post =
                Instancio.of(Post.class)
                        .ignore(Select.field(Post::getId))
                        .create();
        postRepository.save(post);

        var expectedTitle = post.getTitle();
        var expectedContent = post.getContent();

        var request = get("/api/posts/" + post.getId()).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.content").value(expectedContent));
    }

    @Test
    void testCreate() throws Exception {
        var data = new HashMap<>();
        data.put("title", "New post");
        data.put("content", "Post content");

        var request =
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data));

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        Long postId = ((Number) JsonPath.read(responseJson, "$.id")).longValue();

        var user = postRepository.findById(postId).get();
        assertThat(user.getTitle()).isEqualTo(("New post"));
        assertThat(user.getContent()).isEqualTo(("Post content"));
    }

    @Test
    public void testUpdate() throws Exception {
        var post =
                Instancio.of(Post.class)
                        .ignore(Select.field(Post::getId))
                        .create();
        postRepository.save(post);

        var data = new HashMap<>();
        data.put("title", "New post 2");
        data.put("content", "Post content 2");

        var request =
                put("/api/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isOk());

        post = postRepository.findById(post.getId()).get();
        assertThat(post.getTitle()).isEqualTo(("New post 2"));
        assertThat(post.getContent()).isEqualTo(("Post content 2"));
    }

    @Test
    public void testDestroy() throws Exception {
        var post =
                Instancio.of(Post.class)
                        .ignore(Select.field(Post::getId))
                        .create();
        postRepository.save(post);

        var deleteRequest =
                delete("/api/posts/" + post.getId());
        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());

        var getRequest = get("/api/posts/" + post.getId());
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

        boolean existsInDb = postRepository.existsById(post.getId());
        assertThat(existsInDb).isFalse();
    }
}
