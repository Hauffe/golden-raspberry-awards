package com.gra.goldenraspberryawards.controller;


import com.gra.goldenraspberryawards.helper.CSVHelper;
import com.gra.goldenraspberryawards.model.Films;
import com.gra.goldenraspberryawards.service.FilmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping(value = "/")
public class FilmsController {

    @Autowired
    FilmsService filmService;

    @RequestMapping("")
    public String hello() {
        return "Golden Raspberry Awards Service (Version 1.0) created by Alexandre Hauffe";
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                log.info(String.valueOf(file));
                filmService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @GetMapping("/films")
    public ResponseEntity<List<Films>> getAllTutorials() {
        try {
            List<Films> tutorials = filmService.getAllFilms();

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
