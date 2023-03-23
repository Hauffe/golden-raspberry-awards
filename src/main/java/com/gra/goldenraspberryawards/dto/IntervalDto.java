package com.gra.goldenraspberryawards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntervalDto implements Comparable<IntervalDto>{
    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;

    @Override
    public int compareTo(IntervalDto o) {
        return this.getInterval().compareTo(o.getInterval());
    }
}
