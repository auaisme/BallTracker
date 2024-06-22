package com.aua.BallTracker.Controllers;

import com.aua.BallTracker.Constants;
import com.aua.BallTracker.Enities.User;
import com.aua.BallTracker.Enities.UserDTO;
import com.aua.BallTracker.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @MessageMapping(Constants.USER_ADDING_TO_SESSION_ENDPOINT)
    @SendTo(Constants.USER_SENDING_ENDPOINT)
    public User addUserToSession(
            @Payload User user,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // adding user in session
        headerAccessor.getSessionAttributes().put("username", user.getUsername());
        return user;
    }

    @RequestMapping(Constants.API_PREFIX + Constants.USER_REGISTERING_ENDPOINT)
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user)
    {
        if (!userService.addUser(user))
        {
            System.out.println("COULD NOT REGISTER USER");
            return new ResponseEntity<>("false", HttpStatus.FOUND);
        }
        return new ResponseEntity<>("true", HttpStatus.OK);
    }

    @RequestMapping(Constants.API_PREFIX + Constants.USER_CONNECTING_ENDPOINT)
    public ResponseEntity<String> allowConnection(@RequestBody UserDTO user)
    {
        if (userService.findUserByUsername(user.getUsername()).isPresent())
        {
            return new ResponseEntity<>("Allowed to connect", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not allowed to connect", HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/ping")
    public ResponseEntity<String> pong()
    {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }
}
