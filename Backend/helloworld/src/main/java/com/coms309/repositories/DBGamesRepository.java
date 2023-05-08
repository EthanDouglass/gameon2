package com.coms309.repositories;

import com.coms309.entities.GamesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBGamesRepository extends JpaRepository<GamesEntity, Integer> {
    GamesEntity findById(int id);



}
