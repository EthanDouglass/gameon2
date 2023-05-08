package com.example.frontendfinal;

import java.io.Serializable;

/*
This class is for creating the player object that is created when NewUser is selected, OR when guest is selected
Partially a test class, this might be deleted later if/when it's switched to the server
We can try to track fun stats with this on the database, such as lifetime games, lifetime wins, lifetime score
 */
public class Player implements Serializable {

    private static String username = "5";
    private static String password = "";
    private static String displayName = "";
    private static int playerid = 0;
    private static int characterIcon = 0;
    private static int lifetimePts = 0;
    private static int currentGamePts = 0;
    private static int gameCode = 0;
    private static boolean host = false;


    //Constructor for the Player
    public Player(String username, String password, String displayName){
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.characterIcon = 5;
    }

    public static String getUsername(){
        return username;
    }
    public static String getPassword(){
        return password;
    }
    public static String getDisplayName(){
        return displayName;
    }
    public static void setPlayerHost() {host = true;}
    public static boolean getPlayerHost() {return host;}
    public static int getPlayerid() {return playerid;}
    public static int getCharacterIcon(){
        return characterIcon;
    }
    public static int getCurrentGamePts(){
        return currentGamePts;
    }
    public static int getLifetimePts(){
        return lifetimePts;
    }
    public static void setPlayerID(int response) {playerid = response;}
    public static void setDisplayName(String response) {displayName = response;}
    public static void setLifetimePts(int response) {lifetimePts = response;}
    public static void setUsername(String response) {username = response;}
    public static void setPassword(String response) {password = response;}
    public static void setCharacterIcon(int response) {characterIcon = response;}
    public static void addLifetimePts(int pts) {
        lifetimePts += pts;
    }
    public static void setGameCode(int response) {gameCode = response;}
    public static int getGameCode() {return gameCode;}
    public static void addCurrentGamePts(int pts) {
        currentGamePts += pts;
    }
    public static void resetCurrentGamePts(){
        currentGamePts = 0;
    }
}

