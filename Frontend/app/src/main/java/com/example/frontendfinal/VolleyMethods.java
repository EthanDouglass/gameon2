package com.example.frontendfinal;

import static com.example.frontendfinal.StaticsVariables.URL_CHANGE_PLAYER_ICON;
import static com.example.frontendfinal.StaticsVariables.URL_CREATE_LOBBY;
import static com.example.frontendfinal.StaticsVariables.URL_CREATE_USER;
import static com.example.frontendfinal.StaticsVariables.URL_CURRENT_PTS;
import static com.example.frontendfinal.StaticsVariables.URL_GET_LOBBY_PLAYERS_ICON;
import static com.example.frontendfinal.StaticsVariables.URL_LOGIN_USER;
import static com.example.frontendfinal.StaticsVariables.URL_START_GAME;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * READ ME
 *
 * So I've made some work with connecting a requestQueue and the requests together
 * As of now, the getCurrentPts() method is everything we need to have in a simple method
 * The url is currently connected to my postman, and it is indeed connecting to postman, but I have
 * no clue how Postman works so I just keep getting a null return value
 *
 * All current work with it is in the testActivity branch, where the button calls the method and tries to update
 * the textView box next to it to the number that's returned. Once we get postman set up, this SHOULD all be working
 * Once the currentPts is working, we're golden, so once we figure out POST, we're just waiting for backend
 */



public class VolleyMethods  {

    /**
     * The start of the requestQueue
     *
     * //Maybe - Testing
     * MySingleton.getInstance(this).addToRequestQueue(stringRequest);
     */


    /**
     I think we should make the general methods for simple get and post strings and jsons, and then make methods using those
     general methods to return the thing we want to pull (i.e. a method called "getLifetimePtsFromServer()" that asks the server
     for the lifetime points of a player, and then the method will ask the server for the pts and return it

     POST - To Create/Update

     METHOD NEEDS
     getCurrentPts() - To pull the current player's current pts
     getGameCurrentPts() - To pull down an array of all of the player's scores so we can make the leaderboard
     postCurrentPts() - To update a player's current points for their game
     postLifetimePts() - To update a player's lifetime points
     postDisplayName() - For when they update their displayName (This will update online)
     requestLogin() - This will send the info they would like to login with and will return if it worked
     requestNewUser() - This will send the info they would like to make their account with and return if it worked
     postCharacterIcon() - Updates the player's chosen icon, for the next time they login
     */

    /**
     * This method is the general GET method for Strings
     */
    String url = "https:// string_url/";
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String stringResponse = response.toString();
                    //TODO - Whatever the request should do on response
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            //Add this when inside a method
            //queue.add(stringRequest);


    /**
     * This method is the general GET method for JSON Objects
     */
    JsonObjectRequest
            jsonObjectRequest
            = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    //TODO - Whatever the request should do on response
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });


    /**
     * This is the general POST method for JSON objects
     */
    public void sendJsonRequest() {
        try {

            // Make new json object and put params in it
            //The first part (myParam1) is the title, and the second part (myVar1) is the item (Change the type as needed)
            JSONObject jsonParams = new JSONObject();
            String myVar1 = "";
            jsonParams.put("myParam1", myVar1);


            // Building a request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, url, jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the response
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error
                        }
                    });


        } catch (JSONException ex) {
            // Catch if something went wrong with the params
        }
    }

    /**
     * getCurrentPts() Method!
     *
     * No clue why it requires it to be a 1 element array, but it does
     */
    public static String getCurrentPts(Context ctx) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();

        final String[] playerCurrentPts = {"0"};
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CURRENT_PTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //String stringResponse = response.toString();
                        playerCurrentPts[0] = response;
                        //TODO - Whatever the request should do on response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        playerCurrentPts[0] = "error";
                    }
                });
        queue.add(stringRequest);
        return playerCurrentPts[0];
    }



    /**
     * createNewUser() Method
     *
     * This is called in the "NewUserActivity" activity, where the info of a new user is sent up to the server
     *
     * If it worked, a success string is sent back
     */
    public static void createNewUser(Context ctx, Player currentPlayer, final NewUserResponseCallback callback) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();

        // Create JSON Object
        JSONObject newPlayer = new JSONObject();
        try {
            newPlayer.put("username", currentPlayer.getUsername());
            newPlayer.put("password", currentPlayer.getPassword());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Create JsonObjectRequest and add the queue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_CREATE_USER + currentPlayer.getDisplayName(), newPlayer,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        try {
                            if (responseObj != null) {
                                callback.onSuccess(responseObj);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    // Callback instance which is used to read the responses from Backend for creating a new user
    // Define the ResponseCallback interface
    interface NewUserResponseCallback {
        void onSuccess(JSONObject response) throws JSONException;
        void onError();
    }

    /**
     * This is the loginReturningUser() method
     *
     * This method sends up the entered info by the player as a request to login the player to their previous account
     *
     * This will have to be paired with a GET method that receives the player's info, so we can use that info
     * SO, either that info needs to be sent back to us in the POST request (IDK if that's possible), or we make a check that
     * the login worked, and then we will sent a GET request (GET request will be  a separate method, and the IF statement checking
     * if it worked will be in the "returningUserActivity", since that will need the "if" statement to know if it's ok to go to the
     * next page or not)
     */
    public static void loginReturningUser(Context ctx, String username, String password, final LoginResponseCallback callback) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();

        // Create JSON Object
        JSONObject returningPlayer = new JSONObject();
        try {
            returningPlayer.put("username", username);
            returningPlayer.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Create JsonObjectRequest and add the queue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_LOGIN_USER, returningPlayer,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        try {
                            callback.onSuccess(responseObj);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    // Callback instance which is used to read the responses from Backend for logging in
    // Define the ResponseCallback interface
    interface LoginResponseCallback {
        void onSuccess(JSONObject response) throws JSONException;
        void onError();
    }

    /**
     * This is the createHostGame() method
     *
     * This method sends up the host's username and waits for a return that a game has been created
     *
     * This MIGHT need to be paired with a GET to get the hostCode back, otherwise it can be the returned value
     */
    public static void createHostGame(Context ctx, final CreateGameResponseCallback callback) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();

        // Create JSON Object
        JSONObject hostPlayer = new JSONObject();
        try {
            hostPlayer.put("username", Player.getUsername());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Create JsonObjectRequest and add the queue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_CREATE_LOBBY + Player.getPlayerid(), hostPlayer,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        String gameCode;
                        String successCheck;
                        try {
                            successCheck = responseObj.getString("SR");
                            gameCode = responseObj.getString("lobbyCodeStr");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if (successCheck.equals("success")) {
                            callback.onSuccess(gameCode);
                        }
                        else {
                            callback.onError();
                        }
                    }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                    }
                });
        queue.add(jsonObjectRequest);
    }
//     Callback instance which is used to read the responses from Backend for joining a game
//     Define the ResponseCallback interface
    interface CreateGameResponseCallback {
        void onSuccess(String response);
        void onError();
    }

//    public static WebSocketClient getHostSocket() {
//        return hostcc;
//    }


    /**
     * This is the joinGame() method
     *
     * This method sends up the entered code and the player's username in an attempt to join the host's game
     *
     * This MIGHT need to be paired with a GET to get the host's username back
     */
    public static void joinGame(Context ctx, String enteredCode, final JoinGameResponseCallback callback) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();

        // Create JSON Object
        JSONObject joinPlayer = new JSONObject();
        try {
            joinPlayer.put("username", Player.getUsername());
            joinPlayer.put("gameCode", enteredCode);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String URL_JOIN_GAME = "http://coms-309-008.class.las.iastate.edu:8080/joinLobby/?lobbyCode=" + enteredCode + "&" + "ID=" + Player.getPlayerid();
        // Create JsonObjectRequest and add the queue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_JOIN_GAME, joinPlayer,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        try {
                            String joinCheck = responseObj.getString("SR");
                            if (joinCheck.equals("success")) {
                                callback.onSuccess(joinCheck);
                            } else {
                                callback.onError();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    // Callback instance which is used to read the responses from Backend for joining a game
    // Define the ResponseCallback interface
    interface JoinGameResponseCallback {
        void onSuccess(String response);
        void onError();
    }

    public static void startGame(Context ctx, Player currentPlayer, String enteredCode, final StartGameResponseCallback callback) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();


        // Create JSON Object
        JSONObject startGameObj = new JSONObject();
        try {
            startGameObj.put("playerid", Player.getPlayerid());
            startGameObj.put("gameCode", Player.getGameCode());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Create JsonObjectRequest and add the queue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_START_GAME, startGameObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        try {
                            callback.onSuccess(responseObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    interface StartGameResponseCallback {
        void onSuccess(JSONObject response) throws JSONException;
        void onError();
    }

    public static void changePlayerIcon(Context ctx, final LoginResponseCallback callback) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();

        // Create JSON Object
        JSONObject iconChange = new JSONObject();
        try {
            iconChange.put("playerId", Player.getPlayerid());
            iconChange.put("gameCode", Player.getGameCode());
            iconChange.put("icon_id", Player.getCharacterIcon());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Create JsonObjectRequest and add the queue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_CHANGE_PLAYER_ICON, iconChange,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        try {
                            callback.onSuccess(responseObj);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public static void getGameLobbyPlayers(Context ctx, final LoginResponseCallback callback) {
        RequestQueue queue = MySingleton.getInstance(ctx).getRequestQueue();

        // Create JSON Object
        JSONObject getLobby = new JSONObject();
        try {
            getLobby.put("playerId", Player.getPlayerid());
            getLobby.put("gameCode", Player.getGameCode());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Create JsonObjectRequest and add the queue
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_LOBBY_PLAYERS_ICON, getLobby,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        try {
                            callback.onSuccess(responseObj);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                    }
                });
        queue.add(jsonObjectRequest);
    }

}