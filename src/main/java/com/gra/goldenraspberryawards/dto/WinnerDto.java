package com.gra.goldenraspberryawards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinnerDto {

    private List<IntervalDto> min;
    private List<IntervalDto> max;


    public WinnerDto() {
        this.min = new ArrayList<>();
        this.max = new ArrayList<>();
    }
}
