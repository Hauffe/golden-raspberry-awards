package com.gra.goldenraspberryawards.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "FILMS")
public class Films {

//    year;title;studios;producers;winner

    @Id
    @GeneratedValue
    @JsonIgnore
    @Column(name = "ID")
    private Integer id;

    @Column(name = "YEAR_DATE")
    private Integer year;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "STUDIOS")
    private String studios;

    @Column(name = "PRODUCERS")
    private String producers;

    @Column(name = "WINNER")
    private Boolean winner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Films films = (Films) o;
        return getId() != null && Objects.equals(getId(), films.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
