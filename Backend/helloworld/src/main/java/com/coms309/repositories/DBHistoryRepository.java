package com.coms309.repositories;

import com.coms309.entities.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBHistoryRepository extends JpaRepository<HistoryEntity, Integer> {
    HistoryEntity findById(int id);

    @Query("SELECT COUNT(1) FROM HistoryEntity H WHERE H.highestScorerPlayerid = :highestScorerPlayerid")
    int countHistoryWins(@Param("highestScorerPlayerid") int highestScorerPlayerid);


}
