package com.coms309.repositories;

import com.coms309.entities.LobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface DBLobbyRepository extends JpaRepository<LobbyEntity, Integer> {
    LobbyEntity findById(int id);

    @Query("SELECT COUNT(1) FROM LobbyEntity L WHERE L.lobbyCode = :lobby_code")
    int countLobbyCode(@Param("lobby_code") String lobby_code);
    @Query("SELECT L.playerid FROM LobbyEntity L WHERE L.lobbyCode = :lobby_code")
    int getPlayeridFromLobbyCode(@Param("lobby_code") String lobby_code);

    @Query("SELECT L FROM LobbyEntity L WHERE L.lobbyCode = :lobbyCode AND L.host = 1")
    LobbyEntity findHostByLobbyCode(@Param("lobbyCode") String lobbyCode);
    @Transactional
    @Modifying
    @Query("UPDATE LobbyEntity L SET L.lobbyCode = :lobbyCode WHERE L.playerid = :playerid")
    void setLobbyCode(@Param("playerid") Integer playerid,@Param("lobbyCode") String lobbyCode);

    @Transactional
    @Modifying
    @Query("UPDATE LobbyEntity L SET L.host = :Host WHERE L.playerid = :playerid")
    void setHost(@Param("playerid") Integer playerid, @Param("Host") Integer Host);

    @Transactional
    @Modifying
    @Query("UPDATE LobbyEntity L SET L.lobbyCode = null WHERE L.playerid = :playerid")
    void clearCode(@Param("playerid") Integer playerid);
}
