package com.gra.goldenraspberryawards.repository;

import com.gra.goldenraspberryawards.model.Films;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmsRepository extends CrudRepository<Films, Long> {

    @Query("SELECT f FROM Films f " +
            "where f.winner = true")
    List<Films> findWinners();


}
