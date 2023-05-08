package com.coms309.entities;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.stereotype.Component;
import javax.persistence.*;


@Entity
@Component
@Table(name = "lobby", schema = "gameon")
public class LobbyEntity {
    @Basic
    @Column(name = "lobby_code", nullable = false, length = 10)
    private String lobbyCode;
    @Id
    @Basic
    @Column(name = "playerid", nullable = false)
    private int playerid;
    @Basic
    @Column(name = "Host", nullable = false)
    private int host;
    @Basic
    @Column(name = "gameid", nullable = false)
    private int gameid;
    @Basic
    @Column(name = "lobby_score")
    private int lobbyScore;
    @Basic
    @Column(name = "questionid", nullable = true)
    private Integer questionid;

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    public int getPlayerid() {
        return playerid;
    }

    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getLobbyScore() {
        return lobbyScore;
    }

    public void setLobbyScore(int lobbyScore) {
        this.lobbyScore = lobbyScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LobbyEntity that = (LobbyEntity) o;

        if (playerid != that.playerid) return false;
        if (host != that.host) return false;
        if (gameid != that.gameid) return false;
        if (lobbyCode != null ? !lobbyCode.equals(that.lobbyCode) : that.lobbyCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lobbyCode != null ? lobbyCode.hashCode() : 0;
        result = 31 * result + playerid;
        result = 31 * result + host;
        result = 31 * result + gameid;
        return result;
    }

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }
}
