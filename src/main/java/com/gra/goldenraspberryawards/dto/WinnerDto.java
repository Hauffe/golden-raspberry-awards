package com.gra.goldenraspberryawards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WinnerDto {

    private List<IntervalDto> min;
    private List<IntervalDto> max;
}
