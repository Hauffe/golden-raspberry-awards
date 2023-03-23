package com.gra.goldenraspberryawards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmsIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void upload() {
        //Assert
        var multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file());

        //Act
        final ResponseEntity<String> post =
                testRestTemplate.postForEntity("/upload", new HttpEntity<>(multipart, headers()), String.class);

        //Assert
        assertEquals(HttpStatus.OK, post.getStatusCode());

    }


    @Test
    @Order(2)
    void winners() {
        //Act

    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    private FileSystemResource file() {
        return new FileSystemResource(Path.of("src", "test", "resources", "movielist.csv"));
    }
}
