package com.coms309.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.stereotype.Component;
import javax.persistence.*;


@Entity
@Table(name = "player_entity", schema = "gameon")
public class PlayerEntity {
    @Id
    @Basic
    @Column(name = "playerid", nullable = false)
    private int playerid;
    @Basic
    @Column(name = "display_name", nullable = false, length = 30)
    private String displayName;
    @Basic
    @Column(name = "running_score", nullable = false, length = 100)
    private String runningScore;
    @Basic
    @Column(name = "icon", nullable = false)
    private int icon;

    public PlayerEntity(int playerid, String displayName) {
        this.playerid = playerid;
        this.displayName = displayName;
        this.runningScore = "0";
        this.icon = 1;
    }

    public PlayerEntity() {

    }

    @JsonGetter
    public int getPlayerid() {
        return playerid;
    }

    @JsonSetter
    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    @JsonGetter
    public String getDisplayName() {
        return displayName;
    }

    @JsonSetter
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonGetter
    public String getRunningScore() {
        return runningScore;
    }

    @JsonSetter
    public void setRunningScore(String runningScore) {
        this.runningScore = runningScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerEntity that = (PlayerEntity) o;

        if (playerid != that.playerid) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (runningScore != null ? !runningScore.equals(that.runningScore) : that.runningScore != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerid;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (runningScore != null ? runningScore.hashCode() : 0);
        return result;
    }
    @JsonGetter
    public int getIcon() {
        return icon;
    }
    @JsonSetter
    public void setIcon(int icon) {
        this.icon = icon;
    }
}
