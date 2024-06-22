package com.aua.BallTracker.Services;

import com.aua.BallTracker.Enities.Ball;
import com.aua.BallTracker.Enities.BallDTO;
import com.aua.BallTracker.Repos.BallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BallService {
    @Autowired
    private BallRepository ballRepository;

    public boolean updateBallInDB(BallDTO ballDTO)
    {
        ballRepository.save(
                Ball.builder().x(ballDTO.getX())
                        .y(ballDTO.getY())
                        .z(ballDTO.getZ())
                        .build()
        );
        return true;
    }

    public BallDTO getMostRecentBallLocation()
    {
        try {
            Ball mostRecent = ballRepository.findMostRecent();
            return BallDTO.builder()
                    .x(mostRecent.getX())
                    .y(mostRecent.getY())
                    .z(mostRecent.getZ())
                    .build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return BallDTO.builder().x(0).y(0).z(0).build();
        }
    }

    public BallDTO getMostRecentBallLocationByID()
    {
        try {
            Ball mostRecent = ballRepository.findMostRecent();
            return BallDTO.builder()
                    .x(mostRecent.getX())
                    .y(mostRecent.getY())
                    .z(mostRecent.getZ())
                    .build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return BallDTO.builder().x(0).y(0).z(0).build();
        }
    }
}
