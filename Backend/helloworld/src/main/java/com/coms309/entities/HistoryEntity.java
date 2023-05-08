package com.coms309.entities;

import org.springframework.stereotype.Component;
import javax.persistence.*;


@Entity
@Component
@Table(name = "history", schema = "gameon")
public class HistoryEntity {

    @Basic
    @Column(name = "gameid", nullable = false)
    private int gameid;
    @Basic
    @Column(name = "lobby_code", nullable = false, length = 10)
    private String lobbyCode;

    @Id
    @Basic
    @Column(name = "highest_scorer_playerid", nullable = false)
    private int highestScorerPlayerid;
    @Basic
    @Column(name = "score", nullable = false, length = 100)
    private String score;

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    public int getHighestScorerPlayerid() {
        return highestScorerPlayerid;
    }

    public void setHighestScorerPlayerid(int highestScorerPlayerid) {
        this.highestScorerPlayerid = highestScorerPlayerid;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryEntity that = (HistoryEntity) o;

        if (gameid != that.gameid) return false;
        if (highestScorerPlayerid != that.highestScorerPlayerid) return false;
        if (lobbyCode != null ? !lobbyCode.equals(that.lobbyCode) : that.lobbyCode != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = gameid;
        result = 31 * result + (lobbyCode != null ? lobbyCode.hashCode() : 0);
        result = 31 * result + highestScorerPlayerid;
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
