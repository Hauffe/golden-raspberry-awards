package com.gra.goldenraspberryawards.service;

import com.gra.goldenraspberryawards.model.Films;
import com.gra.goldenraspberryawards.repository.FilmsRepository;
import com.gra.goldenraspberryawards.helper.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FilmsService {

    @Autowired
    FilmsRepository repository;

    public void save(MultipartFile file) {
        try {
            List<Films> tutorials = CSVHelper.csvToFilms(file.getInputStream());
            repository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Films> getAllFilms() {
        return (List<Films>)repository.findAll();
    }
}
