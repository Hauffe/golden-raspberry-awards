package com.gra.goldenraspberryawards.service;

import com.gra.goldenraspberryawards.dto.IntervalDto;
import com.gra.goldenraspberryawards.dto.WinnerDto;
import com.gra.goldenraspberryawards.model.Films;
import com.gra.goldenraspberryawards.repository.FilmsRepository;
import com.gra.goldenraspberryawards.helper.CSVHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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


    public WinnerDto getWinner() {
        List<Integer> temp = new ArrayList<>();
        Map<String, List<Integer>> producerList = getSequentialWinner();
        List<IntervalDto> intervalList = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : producerList.entrySet()){
            if(entry.getValue().size()>1){
                Collections.sort(entry.getValue());
                for(int first = 0, next = 1; next<entry.getValue().size(); first++, next = first+1){
                    Integer previousWin = entry.getValue().get(first);
                    Integer followingWin = entry.getValue().get(next);
                    intervalList.add(new IntervalDto(entry.getKey(), followingWin-previousWin, previousWin, followingWin));
                }
            }
        }
        Collections.sort(intervalList);

        return setMinMaxLists(intervalList, intervalList.size());
    }

    public WinnerDto setMinMaxLists(List<IntervalDto> intervalList, int size){
        int first_position = 1;
        int last_position = size-2;
        WinnerDto winners = new WinnerDto();
        winners.getMin().add(intervalList.get(0));
        winners.getMax().add(intervalList.get(size-1));

        while (Objects.equals(intervalList.get(first_position).getInterval(), intervalList.get(0).getInterval())){
            winners.getMin().add(intervalList.get(first_position));
            first_position ++;
        }

        while (Objects.equals(intervalList.get(last_position).getInterval(), intervalList.get(size - 1).getInterval())){
            winners.getMax().add(intervalList.get(last_position));
            last_position --;
        }

        return winners;
    }

    public Map<String, List<Integer>> getSequentialWinner() {
        List<Films> films = repository.findWinners();
        Map<String, List<Integer>> producerList = new HashMap<>();
        for (Films film : films){
            String[] strArr = StringUtils.splitByWholeSeparator(film.getProducers(), ", ");
            for(String str : strArr){
                String[] producers =  StringUtils.splitByWholeSeparator(str, " and ");
                for(String producer : producers){
                    if(producerList.containsKey(producer)){
                        producerList.get(producer).add(film.getYear());
                    }else{
                        List<Integer> year = new ArrayList<>();
                        year.add(film.getYear());
                        producerList.put(producer, year);
                    }
                }
            }
        }
        return producerList;
    }

    public String delete() {
        repository.deleteAll();
        return "Database cleared";
    }
}
