package com.coms309.repositories;

import com.coms309.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DBPlayerRepository extends JpaRepository<PlayerEntity, Integer> {
    PlayerEntity findById(int id);


    @Query("SELECT COUNT(1) FROM PlayerEntity PE WHERE PE.displayName = :displayName")
    int displayNameExists(@Param("displayName") String displayName);


//    @Query("SELECT CASE WHEN PE.host IS NULL THEN false ELSE true END FROM PlayerEntity PE WHERE PE.playerid = :playerid")
//    Boolean isHost(@Param("playerid") Integer playerid);



//    @Transactional
//    @Modifying
//    @Query("UPDATE PlayerEntity PE SET PE.runningScore = :gameCode WHERE PE.runningScore = :runningScore")
//    void updateScore(@Param("playerid") Integer playerid,@Param("gameCode") Integer runningScore);
    void deleteById(Integer id);
}
