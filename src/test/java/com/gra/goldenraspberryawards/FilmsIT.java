package com.gra.goldenraspberryawards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        multipart.add("file", file("movielist.csv"));

        //Act
        final ResponseEntity<String> post =
                testRestTemplate.postForEntity("/raspberry/upload", new HttpEntity<>(multipart, headers()), String.class);

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
                testRestTemplate.getForEntity("/raspberry/winners", String.class);
        var responseBody = objectMapper.readValue(response.getBody(), WinnerDto.class);

        //Act
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody.toString(), responseBody.toString());
    }

    @Test
    @Order(3)
    void clearBase(){
        //Act
        testRestTemplate.delete("/raspberry/clear");
    }

    @Test
    @Order(4)
    void uploadDuplicatedValues() {
        //Assert
        var multipart = new LinkedMultiValueMap<>();
        multipart.add("file", file("movielist_replicated.csv"));

        //Act
        final ResponseEntity<String> post =
                testRestTemplate.postForEntity("/raspberry/upload", new HttpEntity<>(multipart, headers()), String.class);

        //Assert
        assertEquals(HttpStatus.OK, post.getStatusCode());
    }

    @Test
    @Order(5)
    void winnersWithDuplicatedValues() throws JsonProcessingException {
        //Assert
        WinnerDto expectedResponseBody = generateExpectedWinnerForDuplicatedDto();

        //Act
        final ResponseEntity<String> response =
                testRestTemplate.getForEntity("/raspberry/winners", String.class);
        var responseBody = objectMapper.readValue(response.getBody(), WinnerDto.class);

        //Act
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody.toString(), responseBody.toString());
    }

    @Test
    @Order(6)
    void clearBaseAgain(){
        //Act
        testRestTemplate.delete("/raspberry/clear");
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

    private WinnerDto generateExpectedWinnerForDuplicatedDto(){
        List<IntervalDto> min = new ArrayList<>();
        List<IntervalDto> max = new ArrayList<>();
        min.add(new IntervalDto(
                "Ray Stark",
                1,
                1980,
                1981
        ));
        min.add(new IntervalDto(
                "Ray Stark",
                1,
                1981,
                1982
        ));
        min.add(new IntervalDto(
                "Allan Carr",
                1,
                1980,
                1981
        ));
        max.add(new IntervalDto(
                "Ray Stark",
                5,
                1982,
                1987
        ));
        return new WinnerDto(min, max);
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    private FileSystemResource file(String name) {
        return new FileSystemResource(Path.of("src", "test", "resources", name));
    }
}
