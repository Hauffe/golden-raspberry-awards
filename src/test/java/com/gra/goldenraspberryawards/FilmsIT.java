package com.gra.goldenraspberryawards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gra.goldenraspberryawards.dto.IntervalDto;
import com.gra.goldenraspberryawards.dto.WinnerDto;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;

import java.nio.file.Path;
import java.util.Arrays;

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
    void winners() throws JsonProcessingException {
        //Assert
        WinnerDto expectedResponseBody = generateExpectedWinnerDto();

        //Act
        final ResponseEntity<String> response =
                testRestTemplate.getForEntity("/winners", String.class);
        var responseBody = objectMapper.readValue(response.getBody(), WinnerDto.class);

        //Act
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody.toString(), responseBody.toString());
    }

    private WinnerDto generateExpectedWinnerDto(){
        IntervalDto min_interval = new IntervalDto(
                "Joel Silver",
                1,
                1990,
                1991
        );
        IntervalDto max_interval = new IntervalDto(
                "Matthew Vaughn",
                13,
                2002,
                2015
        );
        return new WinnerDto(Arrays.asList(min_interval), Arrays.asList(max_interval));
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
