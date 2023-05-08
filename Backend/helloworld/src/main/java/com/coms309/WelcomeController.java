package com.coms309;

import com.coms309.entities.LobbyEntity;
import com.coms309.entities.LoginInfoEntity;
import com.coms309.entities.PlayerEntity;
import com.coms309.repositories.DBLoginRepository;
import com.coms309.repositories.DBPlayerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;


@RestController
public class WelcomeController {
    @Autowired
    public com.coms309.repositories.DBLoginRepository DBLoginRepository;
    @Autowired
    public com.coms309.repositories.DBPlayerRepository DBPlayerRepository;
    @Autowired
    public com.coms309.repositories.DBLobbyRepository DBLobbyRepository;

    ResponseEntity RE;

    public WelcomeController(DBLoginRepository DBLoginRepository, DBPlayerRepository DBPlayerRepository) {
        this.DBLoginRepository = DBLoginRepository;
        this.DBPlayerRepository = DBPlayerRepository;
    }


//    @GetMapping("/test")
//    public String welcome() {
//        return "Hello and welcome to COMS 309";
//    }

    /**
     * Used to login after user was created. Checks to see if inputted user/pass matches db user/pass
     * for specific id
     *
     * @param information
     * @return ObjectNode SR PlayerEntity
     * @throws JSONException
     */
    @Operation(summary = "Allows clients to login to the application", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Login"),
            @ApiResponse(responseCode = "400", description = "Unsuccessful Login"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @RequestMapping(value = "/Login/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ObjectNode> login(@RequestBody LoginInfoEntity information) throws JSONException {
        int playerid;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        PlayerEntity PE;

        try {
            playerid = DBLoginRepository.getPlayerId(information.getUsername(), information.getPassword());
            PE = DBPlayerRepository.findById(playerid);

        } catch (Exception ex) {
            if (ex.getMessage().contains("Null")) {
                objectNode.put("SR", "Account Info not found");
                return ResponseEntity.status(400).body(objectNode);
            }
            objectNode.put("SR", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        objectNode = objectMapper.convertValue(PE, ObjectNode.class);
        return ResponseEntity.ok(objectNode);
    }

    /**
     * This is to change the current display name to a new displayname.
     * Can't change display name if that display name is already in use by somebody else.
     *
     * @param id
     * @param displayName
     * @return ObjectNode SR displayName
     * @throws JSONException
     */

    @Operation(summary = "Allows clients to change their display name", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Changed Displany Name"),
            @ApiResponse(responseCode = "400", description = "Given player details are empty"),
            @ApiResponse(responseCode = "401", description = "Wanted display name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/changeDisplayName/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ObjectNode> changeDisplayName(@RequestParam String id, @RequestParam String displayName) throws JSONException {
        PlayerEntity testing = DBPlayerRepository.findById(Integer.parseInt(id));
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            if (testing == null) {
                objectNode.put("SR", "PLayer Details Empty");
                return ResponseEntity.status(400).body(objectNode);
            }
            if (DBPlayerRepository.displayNameExists(displayName) != 0) {
                objectNode.put("SR", "Display name already exists");
                return ResponseEntity.status(401).body(objectNode);
            }
            testing.setDisplayName(displayName);
            DBPlayerRepository.save(testing);
        } catch (Exception e) {
            objectNode.put("SR", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        objectNode.put("SR", "Display Name was successfully changed");
        objectNode.put("displayName", testing.getDisplayName());
        return ResponseEntity.ok(objectNode);
    }

    @Operation(summary = "Allows clients to change their icon", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Changed Icon"),
            @ApiResponse(responseCode = "400", description = "Given player details are empty"),
            @ApiResponse(responseCode = "401", description = "Request Icon invalid"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/changeIcon/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ObjectNode> changePlayerIcon(@RequestParam String id, @RequestParam int playerIcon) throws JSONException {
        PlayerEntity testing = DBPlayerRepository.findById(Integer.parseInt(id));
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        boolean iconExists = false;
        try {
            if (testing == null) {
                objectNode.put("SR", "PLayer Details Empty");
                return ResponseEntity.status(400).
                        body(objectNode);
            }
            for (int i = 1; i < 9; ++i) {
                if (playerIcon == i) {
                    iconExists = true;
                    break;
                }
            }
            if (iconExists) {
                testing.setIcon(playerIcon);
                DBPlayerRepository.save(testing);
                objectNode.put("SR", "Icon was successfully changed");
                objectNode.put("icon", testing.getIcon());
            } else {
                objectNode.put("SR", "Icon does not exit");
                return ResponseEntity.status(401).body(objectNode);
            }

        } catch (Exception e) {
            objectNode.put("SR", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(objectNode);
    }

    /**
     * Creates a new user, requests username, password and displayname
     * Username, Display can not be currently existing in database.
     *
     * @param newUser
     * @param displayName
     * @return ObjectNode SR playerid
     */
    @Operation(summary = "Allows clients to signup for a new user account", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Signed up"),
            @ApiResponse(responseCode = "400", description = "Username already exists"),
            @ApiResponse(responseCode = "401", description = "display name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/signup/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ObjectNode> signup(@RequestBody LoginInfoEntity newUser, @RequestParam String displayName) throws JSONException, JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {

            if (DBLoginRepository.usernameNameExists(newUser.getUsername()) != 0) {
                objectNode.put("SR", "Username already exists");
                return ResponseEntity.status(400).body(objectNode);
            }
            if (DBPlayerRepository.displayNameExists(displayName) != 0) {
                objectNode.put("SR", "display name already exists");
                return ResponseEntity.status(401).body(objectNode);
            }


            newUser = DBLoginRepository.save(newUser);
            PlayerEntity PE = new PlayerEntity(newUser.getPlayerid(), displayName);
            DBPlayerRepository.save(PE);
            objectNode.put("icon", PE.getIcon());

        } catch (Exception ex) {
            objectNode.put("SR", ex.getMessage());
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        objectNode.put("SR", "success");
        objectNode.put("playerid", newUser.getPlayerid());

        return ResponseEntity.ok(objectNode);

    }

    @Operation(summary = "Allows clients to join an existing lobby", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully joined lobby"),
            @ApiResponse(responseCode = "400", description = "Game code does not exist"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/joinLobby/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ObjectNode> joinGame(@RequestParam String lobbyCode, @RequestParam int ID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        LobbyEntity LE = new LobbyEntity();
        if (DBLobbyRepository.countLobbyCode(lobbyCode) == 0) {
            objectNode.put("SR", "Game code does not exist");
            return ResponseEntity.status(400).body(objectNode);
        }
        try {
            LE.setGameid(1);
            LE.setHost(0);
            LE.setLobbyCode(lobbyCode);
            LE.setPlayerid(ID);
            DBLobbyRepository.save(LE);
        } catch (Exception e) {
            objectNode.put("SR", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        objectNode.put("SR", "success");
        objectNode.put("lobbyCode", lobbyCode);
        return ResponseEntity.ok(objectNode);

    }

    /**
     * Assigns player host, (they must click the host session button) assigns them a 5 digit join code.
     * Host shows other players the join code so they can join the session prior to playing. (NEEDS TO BE FIXED)
     *
     * @param ID
     * @return ObjectNode SR gameCode
     */
    @Operation(summary = "Allows clients to become a hosts", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully assigned host"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/assignHost/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ObjectNode> assignHost(@RequestParam int ID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        LobbyEntity LE = new LobbyEntity();

        Random rand = new Random();
        int temp;
        String lobbyCodeStr;
        do {
            lobbyCodeStr = "";
            temp = 0;
            for (int i = 0; i < 5; i++) {
                temp = rand.nextInt(9);
                lobbyCodeStr = lobbyCodeStr + temp;
            }
        } while (DBLobbyRepository.countLobbyCode(lobbyCodeStr) > 0);
        System.out.println((lobbyCodeStr));

        try {
            LE.setPlayerid(ID);
            LE.setHost(1);
            LE.setGameid(1);
            LE.setLobbyCode(lobbyCodeStr);
            DBLobbyRepository.save(LE);
        } catch (Exception e) {
            objectNode.put("SR", e.getMessage());
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


        objectNode.put("SR", "success");
        objectNode.put("lobbyCodeStr", lobbyCodeStr);
        return ResponseEntity.ok(objectNode);
    }

    /**
     * @param numPlayers
     * @param JSONplayerids
     * @return ObjectNode SR
     * @throws JSONException
     */
    @Operation(summary = "Allows clients, more specifically hosts to end games" , responses = {
            @ApiResponse(responseCode = "200", description = "Successfully ended game"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/endGame/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ObjectNode> endGame(@RequestParam int numPlayers, @RequestBody String JSONplayerids, @RequestBody String lobbyScores) throws JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        JSONObject JO = new JSONObject(JSONplayerids);
        JSONArray idArray = JO.getJSONArray("playerids");
        JSONObject GS = new JSONObject(lobbyScores);
        JSONArray gsArray = JO.getJSONArray("lobbyScores");
        for (int i = 0; i < numPlayers; i++) {
            try {
                DBLobbyRepository.clearCode((Integer) idArray.get(i));
                DBLobbyRepository.setHost((Integer) idArray.get(i), 0);
            } catch (Exception e) {
                objectNode.put("SR", e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        objectNode.put("SR", "success");
        return ResponseEntity.ok(objectNode);
    }


    /**
     * Deletes a user, the ID of a user will not be seen/used again. If user id 1 was deleted,
     * id 1 will never be used again.
     *
     * @param ID
     * @return
     */



    @Operation(summary = "Allows clients to delete their accounts", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully Deleted"),
            @ApiResponse(responseCode = "400", description = "Player doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/deleteaccount/")
    public @ResponseBody ResponseEntity<ObjectNode> deleteaccount(@RequestParam int ID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            if(DBPlayerRepository.findById(ID) == null){
                objectNode.put("SR", "Can't delete account, as it doesn't exist.");
                return ResponseEntity.status(400).body(objectNode);
            }
            DBPlayerRepository.deleteById(ID);
            DBLoginRepository.deleteById(ID);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        objectNode.put("SR", "Account Deleted Successfully");
        return ResponseEntity.ok(objectNode);

    }
}
