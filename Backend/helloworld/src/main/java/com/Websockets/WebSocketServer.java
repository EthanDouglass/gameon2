package com.Websockets;
import com.coms309.entities.HistoryEntity;
import com.coms309.repositories.*;
import com.coms309.entities.LobbyEntity;
import com.coms309.repositories.DBPlayerRepository;
import com.coms309.entities.PlayerEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.websocket.*;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.PathParam;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *v
 * @author Ahmed Nasereddin
 *
 */
@ServerEndpoint("/connectPlayers/{playerid}/{lobby_code}")
@RestController
public class WebSocketServer {
    private static DBPlayerRepository DBPlayerRepository;
    private static DBLobbyRepository DBLobbyRepository;
    private static DBQuestionsRepository DBQuestionsRepository;
    private static DBHistoryRepository DBHistoryRepository;

    PlayerEntity testing;
    int count = 0;
    final int SKETCHITTABLEEND = 200;
    final int SKETCHITTABLEBEGIN = 128;
    final int FACTORCAPTABLEEND = 75;
    final int FACTORCAPTABLEBEGIN = 0;

    //Use to get playerentity
    private static Map<LobbyEntity, String> lobbyentityGuessMap = new Hashtable<>();
    private static Map<LobbyEntity, String> lobbyentityDrawingMap = new Hashtable<>();
    private static Map<Session, PlayerEntity> sessionPlayerEntityMap = new Hashtable<>();
    //Use to get session
    private static Map<PlayerEntity, Session> PlayerEntitySessionMap = new Hashtable<>();

    private static Map<PlayerEntity, Integer> PlayerEntityScoreMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public void setDBPlayerRepository(DBPlayerRepository repo) {
        DBPlayerRepository = repo;  // we are setting the static variable
    }

    @Autowired
    public void setDBLobbyRepository(DBLobbyRepository repo) {
        DBLobbyRepository = repo;  // we are setting the static variable
    }

    @Autowired
    public void setDBQuestionRepository(DBQuestionsRepository repo) {
        DBQuestionsRepository = repo;  // we are setting the static variable
    }

    @Autowired
    public void setDBHistoryRepository(DBHistoryRepository repo) {
        DBHistoryRepository = repo;  // we are setting the static variable
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("playerid") int playerid, @PathParam("lobby_code") String lobbyCode) throws IOException {
        logger.info("Player connecting");

        try {

            PlayerEntity player = DBPlayerRepository.findById(playerid);
            logger.info(Integer.toString(player.getPlayerid()));
            LobbyEntity lobby = DBLobbyRepository.findById(playerid);
            if(!(sessionPlayerEntityMap.containsKey(session) || PlayerEntitySessionMap.containsKey(player))) {
                sessionPlayerEntityMap.put(session, player);
                PlayerEntitySessionMap.put(player, session);

                if (DBLobbyRepository.countLobbyCode(lobby.getLobbyCode()) != 0 & lobby.getHost() == 0) {
                    sendPlayerHasConnected(player, lobbyCode, player.getDisplayName() + " has joined the game session");
                    logger.info("Player has connected");
                } else {
                    logger.info("Host has connected");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }


    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        ObjectNode responseNode = objectMapper.createObjectNode();
        PlayerEntity PE = sessionPlayerEntityMap.get(session);
        logger.info(sessionPlayerEntityMap.toString());
        LobbyEntity LE = DBLobbyRepository.findById(PE.getPlayerid());
        Map<String,String> messageMap = objectMapper.readValue(message, Map.class);
        logger.info(messageMap.toString());

    try {

        if (messageMap.get("request").equals("Start Fact or Cap") && LE.getHost() == 1) {
            setLobbyGameID(LE, 1);
            responseNode.put("response", "Start Fact or Cap");
            broadcastsToLobby(responseNode.toString(), PE);
        } else if (messageMap.get("request").equals("Start SketchIt") && LE.getHost() == 1){
            setLobbyGameID(LE, 2);
            responseNode.put("response", "Start SketchIt");
            broadcastsToLobby(responseNode.toString(), PE);
        }else if (messageMap.get("request").equals("question")) {
            String questionIdStr = messageMap.get("question_id");
            int qnum = Integer.parseInt(questionIdStr);
            logger.info(qnum + " <-this num");
            sendQuestions(PE, qnum);
        } else if (messageMap.get("request").equals("End Lobby") && LE.getHost() == 1) {
            responseNode.put("response", "End Lobby");
            broadcastsToLobby(responseNode.toString(), PE);
        } else if (messageMap.get("request").equals("Game Score")) {
            String score = messageMap.get("Game Points");
            updateScore(session, PE, LE, score); //MIGHT BE AN ISSUE
            responseNode.put("response", "Added Scores");

            sendMessageToClient(PE, responseNode.toString());
        } else if (messageMap.get("request").equals("Leaderboard Info") && LE.getHost() == 1) {
            endLobby(PE, LE);
        } else if (messageMap.get("request").equals("Kick Out") && LE.getHost() == 1) {

            responseNode.put("response", "close");
            broadcastsToOthersInLobby(responseNode.toString(), PE);
            sendMessageToClient(PE, responseNode.toString());
        } else if (messageMap.get("request").equals("Get Prompt")){
            responseNode.put("response", "New Prompt");
            responseNode.put("prompt", getRandomPrompt(LE));
            sendMessageToClient(PE,responseNode.toString());
        } else if (messageMap.get("request").equals("Player Sketch")){
            PlayerSketch(PE, messageMap.get("drawing"));
        } else if (messageMap.get("request").equals("Player Guess")){
            PlayerSketchGuess(PE, messageMap.get("guess"),messageMap.get("playerid"));
        } else if (messageMap.get("request").equals("Next Drawing")){
            getDrawing(PE, LE);
        }


    } catch (Exception e){
        e.printStackTrace();
        logger.info(e.getMessage());
    }
    }

    private void PlayerSketchGuess(PlayerEntity PE, String guess, String playerId){
        ObjectNode responseNode = objectMapper.createObjectNode();
        LobbyEntity LE = DBLobbyRepository.findById(PE.getPlayerid());
        int numOfPlayersInLobby  = numOfPlayersInLobby(LE);
        int countCurrentGuesses = 0;

        lobbyentityGuessMap.put(LE, guess);

        for (LobbyEntity currentLE : lobbyentityGuessMap.keySet()){
            if (currentLE.getLobbyCode().equals(LE.getLobbyCode())){
                countCurrentGuesses += 1;
            }
        }
        try {
            logger.info("Current # of guesses: " + countCurrentGuesses);
            logger.info("Current # of players: " + numOfPlayersInLobby);
            if (countCurrentGuesses == numOfPlayersInLobby - 1) {
                LobbyEntity drawingLobby = DBLobbyRepository.findById(Integer.parseInt(playerId));


                logger.info("drawing: " + lobbyentityDrawingMap.get(drawingLobby));
                logger.info("drawing LE: " + drawingLobby);
                logger.info("drawing LE playerid: " + drawingLobby);
                responseNode.put("response", "Guess List");
                responseNode.put("Num Guesses", countCurrentGuesses);
                responseNode.put("drawing", lobbyentityDrawingMap.get(drawingLobby));
                responseNode.put("prompt", DBQuestionsRepository.findByGameId(drawingLobby.getQuestionid(), 2).getText());
                responseNode.put("playerid", drawingLobby.getPlayerid());
                logger.info(responseNode.toString());
                countCurrentGuesses = 0;
                for (LobbyEntity currentLE : lobbyentityGuessMap.keySet()) {
                    if (currentLE.getLobbyCode().equals(LE.getLobbyCode())) {
                        responseNode.put("guess " + countCurrentGuesses, lobbyentityGuessMap.get(currentLE));
                        countCurrentGuesses += 1;
                    }
                }
                for (LobbyEntity currentLE : lobbyentityGuessMap.keySet()) {
                    if (currentLE.getLobbyCode().equals(LE.getLobbyCode())) {
                        lobbyentityGuessMap.remove(LE);
                    }
                }
                lobbyentityDrawingMap.remove(drawingLobby);
                broadcastsToLobby(responseNode.toString(), PE);
            }
        } catch (Exception e){
            e.printStackTrace();
            logger.info(e.toString());
            logger.info(e.getMessage());
        }
    }
    private void PlayerSketch(PlayerEntity PE, String drawing){
        LobbyEntity LE = DBLobbyRepository.findById(PE.getPlayerid());
        int numOfPlayersInLobby  = numOfPlayersInLobby(LE);
        lobbyentityDrawingMap.put(LE, drawing);

        int countCurrentDrawings = countCurrentDrawings(LE);
        if (countCurrentDrawings == numOfPlayersInLobby){
            getDrawing(PE, LE);
        }
    }



    
    private int countCurrentDrawings(LobbyEntity LE){
        int count = 0;
        for (LobbyEntity currentLE : lobbyentityDrawingMap.keySet()){
            if (currentLE.getLobbyCode().equals(LE.getLobbyCode())){
                count += 1;
            }
        }
        return count;
    }

    private void getDrawing(PlayerEntity PE, LobbyEntity LE){
        ObjectNode responseNode = objectMapper.createObjectNode();
        int countCurrentDrawings = 0;
        Random rand = new Random();
        ArrayList<LobbyEntity> LElist = new ArrayList<>();
        for (LobbyEntity currentLE : lobbyentityDrawingMap.keySet()){
            if (currentLE.getLobbyCode().equals(LE.getLobbyCode())){
                LElist.add(currentLE);
                countCurrentDrawings += 1;
            }
        }
        if (countCurrentDrawings == 0){
            responseNode.put("response", "Close SketchIt");
            broadcastsToLobby(responseNode.toString(), PE);
        } else {
            int randomDrawing = rand.nextInt(countCurrentDrawings);
            responseNode.put("response", "Guess Drawing");
            responseNode.put("drawing", lobbyentityDrawingMap.get(LElist.get(randomDrawing)));
            responseNode.put("prompt", DBQuestionsRepository.findByGameId(LElist.get(randomDrawing).getQuestionid(), 2).getText());
            responseNode.put("playerid", LElist.get(randomDrawing).getPlayerid());
            broadcastsToLobby(responseNode.toString(), PE);
        }

    }

    private void setLobbyGameID(LobbyEntity LE, int gameid){

        sessionPlayerEntityMap.forEach((session, playerEntity) -> {
            try {
                //LoggerInfo
                LobbyEntity currentLE = DBLobbyRepository.findById((playerEntity.getPlayerid()));
                if(LE.getLobbyCode().equals(currentLE.getLobbyCode())){
                    //logger.info("Please work");
                    currentLE.setGameid(gameid);
                    DBLobbyRepository.save(currentLE);
                }
            } catch (Exception e) {
                logger.info("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private String getRandomPrompt(LobbyEntity LE){
        Random rand = new Random();
        int randomprompt = rand.nextInt(SKETCHITTABLEEND - SKETCHITTABLEBEGIN + 1) + SKETCHITTABLEBEGIN;
        LE.setQuestionid(randomprompt);
        DBLobbyRepository.save(LE);
        return DBQuestionsRepository.findByGameId(randomprompt, 2).getText();
    }
@Operation(summary = "When a user wants to broadcast a message to their lobby")
    private void broadcastsToLobby(String message, PlayerEntity player) {
        LobbyEntity LE = DBLobbyRepository.findById((player.getPlayerid()));
        sessionPlayerEntityMap.forEach((session, playerEntity) -> {
            try {
                //LoggerInfo
                LobbyEntity currentLE = DBLobbyRepository.findById((playerEntity.getPlayerid()));
                if(LE.getLobbyCode().equals(currentLE.getLobbyCode())){
                    //logger.info("Please work");
                    session.getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                logger.info("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void broadcastsToOthersInLobby(String message, PlayerEntity player) {
        LobbyEntity LE = DBLobbyRepository.findById((player.getPlayerid()));
        sessionPlayerEntityMap.forEach((session, playerEntity) -> {
            try {
                //LoggerInfo
                LobbyEntity currentLE = DBLobbyRepository.findById((playerEntity.getPlayerid()));
                if(LE.getLobbyCode().equals(currentLE.getLobbyCode()) && LE.getPlayerid() != currentLE.getPlayerid()){
                    //logger.info("Please work");
                    session.getBasicRemote().sendText(message);
                }
            } catch (Exception e) {
                logger.info("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");
        ObjectNode responseNode = objectMapper.createObjectNode();
        PlayerEntity player = sessionPlayerEntityMap.get(session);

        String message = player.getDisplayName() + " disconnected";
        responseNode.put("response", message);
        broadcastsToOthersInLobby(responseNode.toString(), player);

        LobbyEntity LE = DBLobbyRepository.findById(player.getPlayerid());
        DBLobbyRepository.delete(LE);

        lobbyentityGuessMap.remove(LE);
        lobbyentityDrawingMap.remove(LE);
        sessionPlayerEntityMap.remove(session);
        PlayerEntitySessionMap.remove(player);


    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        logger.info(throwable.getMessage());
        logger.info(throwable.toString());
        throwable.printStackTrace();
    }

    private void updateScore(Session session, PlayerEntity PE, LobbyEntity LE, String score) {
        count++;
        if (count == 1) {
            logger.info("updateScore called " + count + " time");
        } else {
            logger.info("updateScore called " + count + " times");
        }
        sessionPlayerEntityMap.remove(session);
        PlayerEntitySessionMap.remove(PE);
        String tempDrawing = null;
        if (lobbyentityDrawingMap.containsKey(LE)) {
            tempDrawing = lobbyentityDrawingMap.get(LE);
        }
        String tempGuess = null;
        if (lobbyentityGuessMap.containsKey(LE)) {
            tempGuess = lobbyentityGuessMap.get(LE);
        }

        lobbyentityGuessMap.remove(LE);
        lobbyentityDrawingMap.remove(LE);
        LE.setLobbyScore(Integer.parseInt(score));
        if (count == 1) {
            logger.info("lobby score is now " + LE.getLobbyScore() + " points");
        } else {
            logger.info("lobby score is now changed twice in same endlobby and is" + LE.getLobbyScore() + " points");
        }
        PE.setRunningScore(Integer.parseInt(PE.getRunningScore()) + Integer.parseInt(score) + "");
        if (count == 1) {
            logger.info("lifetime score is now " + PE.getRunningScore() + " points (before adding lobby score)");
            logger.info("lifetime score " + Integer.parseInt(PE.getRunningScore()) + " <-before adding lobby score + lobbyscore to be added to lifetime-> " + Integer.parseInt(score) + " points ");
            logger.info(Integer.parseInt(PE.getRunningScore()) + Integer.parseInt(score) + " is the lifetime score");
        } else {
            logger.info("THESE ARE THE EXTRA REQUESTS WHICH ARE NOT SUPPOSED TO HAPPEN vv");
            logger.info("lifetime score is now " + PE.getRunningScore() + " points (before adding lobby score)");
            logger.info("lifetime score " + Integer.parseInt(PE.getRunningScore()) + " <-before adding lobby score + lobbyscore to be added to lifetime-> " + Integer.parseInt(score) + " points ");
            logger.info(Integer.parseInt(PE.getRunningScore()) + Integer.parseInt(score) + " is the lifetime score");
        }
        DBLobbyRepository.save(LE);
        DBPlayerRepository.save(PE);

        if (!(tempGuess == null)){
            lobbyentityGuessMap.put(LE, tempGuess);
        }
        if (!(tempDrawing == null)){
            lobbyentityDrawingMap.put(LE, tempDrawing);
        }

        sessionPlayerEntityMap.put(session, PE);
        PlayerEntitySessionMap.put(PE, session);
    }

    private void sendMessageToClient(PlayerEntity player, String message){
        try{
            PlayerEntitySessionMap.get(player).getBasicRemote().sendText(message);
        } catch (Exception e){
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }

    }
    private void sendPlayerHasConnected(PlayerEntity player, String lobbyCode, String message) {
        try {
            ObjectNode objectNode = objectMapper.createObjectNode();
            if (message.equals(player.getDisplayName() + " has joined the game session")) {
                //logger.info("This works");
                LobbyEntity lobbyHost = DBLobbyRepository.findHostByLobbyCode(lobbyCode);
                PlayerEntity playerHost = DBPlayerRepository.findById(lobbyHost.getPlayerid());
                objectNode.put("response", message);
                PlayerEntitySessionMap.get(playerHost).getBasicRemote().sendText(objectNode.toString());
            }
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void endLobby(PlayerEntity player, LobbyEntity LE){
        try {
            logger.info("this is the session to playerentity map" + sessionPlayerEntityMap.toString());
            logger.info("this is the playerentity to session map" + sessionPlayerEntityMap.toString());
            ObjectNode objectNode = objectMapper.createObjectNode();
            AtomicInteger count = new AtomicInteger();
            AtomicInteger highestScore = new AtomicInteger();
            AtomicInteger tiedHighScore = new AtomicInteger();
            AtomicInteger highScorerID = new AtomicInteger();
            tiedHighScore.set(1);
            objectNode.put("response", "Leaderboard Results");
            sessionPlayerEntityMap.forEach((session, playerEntity) -> {
                LobbyEntity currentLE = DBLobbyRepository.findById((playerEntity.getPlayerid()));
                if (LE.getLobbyCode().equals(currentLE.getLobbyCode())) {
                    count.addAndGet(1);
                    objectNode.put("player " + count.get(), playerEntity.getDisplayName());
                    objectNode.put("Icon " + count.get(), playerEntity.getIcon());
                    objectNode.put("Lobby Score " + count.get(), currentLE.getLobbyScore());
                    objectNode.put("Lifetime Score " + count.get(), playerEntity.getRunningScore());
                    if(currentLE.getLobbyScore() >= highestScore.get()) {
                        if(currentLE.getLobbyScore() == highestScore.get()) {
                            tiedHighScore.addAndGet(1);
                        }
                        else{
                            highestScore.set(currentLE.getLobbyScore());
                            highScorerID.set(currentLE.getPlayerid());
                        }
                    }
                }


            });
            if(tiedHighScore.get() == 1) {
                HistoryEntity HE = new HistoryEntity();
                HE.setGameid(LE.getGameid());
                HE.setLobbyCode(LE.getLobbyCode());
                HE.setHighestScorerPlayerid(highScorerID.get());
                HE.setScore(highestScore.get() + "");
                DBHistoryRepository.save(HE);
            }
            count.set(0);
            sessionPlayerEntityMap.forEach((session, playerEntity) -> {
                LobbyEntity currentLE = DBLobbyRepository.findById((playerEntity.getPlayerid()));
                if(LE.getLobbyCode().equals(currentLE.getLobbyCode())){
                    count.addAndGet(1);
                    int historyWins = DBHistoryRepository.countHistoryWins(playerEntity.getPlayerid());

                    System.out.println(historyWins + " this is the amount of wins");
                    if(DBHistoryRepository.countHistoryWins(playerEntity.getPlayerid()) != 0){
                        HistoryEntity h = DBHistoryRepository.findById(playerEntity.getPlayerid());
                        if(historyWins >= 1  && !LE.getLobbyCode().equals(h.getLobbyCode())) {
                            objectNode.put("Previous Lobby Wins " + count.get(), historyWins);
                        } else {
                            objectNode.put("Previous Lobby Wins " + count.get(), 0);
                        }
                    }
                    else {
                        objectNode.put("Previous Lobby Wins " + count.get(), 0);
                    }
                }
            });
// change something

            objectNode.put("Num Players ", count + "");
            broadcastsToLobby(objectNode.toString(), player);
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * returns number of players in a specific lobby
     * @param LE
     * @return number of players in lobby as an int
     */
    private int numOfPlayersInLobby( LobbyEntity LE){
        AtomicInteger count = new AtomicInteger();
        try {
            sessionPlayerEntityMap.forEach((session, playerEntity) -> {
                LobbyEntity currentLE = DBLobbyRepository.findById((playerEntity.getPlayerid()));
                if (LE.getLobbyCode().equals(currentLE.getLobbyCode())) {
                    count.addAndGet(1);
                }
            });
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return count.get();
    }
    private void sendQuestions(PlayerEntity player, int questNum) {
        try {
            LobbyEntity LE = DBLobbyRepository.findById(player.getPlayerid());
            int gameid = LE.getGameid();

            String question = DBQuestionsRepository.findByGameId(questNum, gameid).getText();
            String answer;
            if(DBQuestionsRepository.findByGameId(questNum, gameid).getAnswer().equals("0")){
                answer = "false";
            }
            else if (DBQuestionsRepository.findByGameId(questNum, gameid).getAnswer().equals("1")){
                answer = "true";
            }
            else{
                answer = DBQuestionsRepository.findByGameId(questNum, gameid).getAnswer();
            }

            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("response", "question");
            objectNode.put("questiontext", question);
            objectNode.put("answertext", answer);

            sendMessageToClient(player, objectNode.toString());
            logger.info("to string: "+ objectNode);
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
