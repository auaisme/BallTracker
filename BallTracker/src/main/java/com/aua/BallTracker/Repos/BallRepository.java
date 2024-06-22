package com.aua.BallTracker.Repos;

import com.aua.BallTracker.Enities.Ball;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface BallRepository extends JpaRepository<Ball, Long> {
    @Query(
            "SELECT b FROM Ball b WHERE b.stamp = (" +
                    "SELECT MAX(b2.stamp) FROM Ball b2" +
                ")"
    )
    List<Ball> findMostRecentByTimeStamp();

    @Query(
            "SELECT b FROM Ball b WHERE b.id = (" +
                    "SELECT MAX(b2.id) FROM Ball b2" +
                    ")"
    )
    Ball findMostRecentByID();

    @Query(
            "SELECT b FROM Ball b WHERE b.stamp = (" +
                    "SELECT MAX(b2.stamp) FROM Ball b2" +
                    ") AND " +
                    "b.id = (" +
                    "SELECT MAX(b2.id) FROM Ball b2" +
                    ")"
    )
    Ball findMostRecent();
}
