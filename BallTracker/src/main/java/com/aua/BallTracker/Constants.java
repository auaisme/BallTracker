package com.aua.BallTracker;

public class Constants {

    // WHEN MAKING CHANGES, BE SURE TO UPDATE THE FRONT-END AS WELL

    public static final String
        BALL_SENDING_ENDPOINT = "/topic/ball",
        BALL_RECEIVING_ENDPOINT = "/update/ball",
        PONG_ENDPOINT = "/topic/ping",
        PONG = "pong",
//    USERS
        USER_SENDING_ENDPOINT = "/topic/userlist",
        USER_REGISTERING_ENDPOINT = "/register/user",
        USER_CONNECTING_ENDPOINT = "/connect/user",
        USER_ADDING_TO_SESSION_ENDPOINT = "/add/user",
//    MISC
        API_PREFIX = "/api"
    ;
}
