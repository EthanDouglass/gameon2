package com.coms309.entities;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import javax.persistence.*;


@Entity
@Table(name = "login_info", schema = "gameon")
public class LoginInfoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Id
    @Column(name = "playerid", nullable = false)
    private Integer playerid;
    @Basic
    @Column(name = "username", nullable = false, length = 20)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 20)
    private String password;

    public LoginInfoEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginInfoEntity() {

    }
    @JsonGetter
    public Integer getPlayerid() {
        return playerid;
    }
    @JsonSetter
    public void setPlayerid(int playerid) {
        this.playerid = playerid;
    }

    @JsonGetter
    public String getUsername() {
        return username;
    }
    @JsonSetter
    public void setUsername(String username) {
        this.username = username;
    }
    @JsonGetter
    public String getPassword() {
        return password;
    }
    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginInfoEntity that = (LoginInfoEntity) o;

        if (username != that.username) return false;
        if (password != that.password) return false;
        if (playerid != null ? !playerid.equals(that.playerid) : that.playerid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerid != null ? playerid.hashCode() : 0;
        return result;
    }

}
