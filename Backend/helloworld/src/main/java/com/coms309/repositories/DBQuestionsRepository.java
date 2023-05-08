package com.coms309.repositories;

import com.coms309.entities.QuestionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBQuestionsRepository extends JpaRepository<QuestionsEntity, Integer> {
    @Query("SELECT QE FROM QuestionsEntity QE WHERE QE.gameid = :gameid AND QE.questionid = :id")
    QuestionsEntity findByGameId(@Param("id") int id, @Param("gameid") int gameid);

    QuestionsEntity findById(int id);


}
