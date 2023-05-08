package com.coms309.repositories;

import com.coms309.entities.LoginInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBLoginRepository extends JpaRepository<LoginInfoEntity, Integer> {


    @Query("SELECT LE.playerid FROM LoginInfoEntity LE WHERE (LE.username = :username AND LE.password = :password)")
    int getPlayerId(@Param("username") String username, @Param("password") String password);

    @Query("SELECT COUNT(1) FROM LoginInfoEntity LE WHERE LE.username = :username")
    int usernameNameExists(@Param("username") String username);

}