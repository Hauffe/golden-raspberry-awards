package com.gra.goldenraspberryawards.repository;

import com.gra.goldenraspberryawards.model.Films;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmsRepository extends CrudRepository<Films, Long> {
}
