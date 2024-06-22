package com.aua.BallTracker.Controllers;

import com.aua.BallTracker.Constants;
import com.aua.BallTracker.Enities.BallDTO;
import com.aua.BallTracker.Services.BallService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
public class BallController {
    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    BallService service;

    @MessageMapping(Constants.BALL_RECEIVING_ENDPOINT)
    @SendTo(Constants.BALL_SENDING_ENDPOINT)
    public BallDTO receiveBall(
            @Payload BallDTO ballDTO
    )
    {
        service.updateBallInDB(ballDTO);
        return service.getMostRecentBallLocation();
    }

    @Scheduled(fixedRate = 5000L)
    @SendTo(Constants.BALL_SENDING_ENDPOINT)
    public void streamBallLocation()
    {
        template.convertAndSend(Constants.BALL_SENDING_ENDPOINT,
                    service.getMostRecentBallLocation()
                );
    }

    @Scheduled(fixedRate = 10000L)
    @SendTo(Constants.PONG_ENDPOINT)
    public void sendPong()
    {
        template.convertAndSend(Constants.PONG_ENDPOINT, Constants.PONG);
    }
}
