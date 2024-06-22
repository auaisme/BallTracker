package com.aua.BallTracker.Services;

import com.aua.BallTracker.Enities.User;
import com.aua.BallTracker.Enities.UserDTO;
import com.aua.BallTracker.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public boolean addUser(UserDTO user) {
        user.setUsername(user.getUsername().trim().toLowerCase());
        Optional<User> inDB = userRepository.findByUsernameIgnoreCase(user.getUsername());
        if (inDB.isPresent())
        {
            System.out.println("USER " + user.getUsername() + " ALREADY EXISTS");
            return false;
        }
        userRepository.save(User.builder().username(user.getUsername()).build());
        return true;
    }

    public boolean deleteUser(UserDTO user) {
        user.setUsername(user.getUsername().trim().toLowerCase());
        Optional<User> inDB = userRepository.findByUsernameIgnoreCase(user.getUsername());
        if (inDB.isEmpty())
        {
            System.out.println("USER " + user.getUsername() + " DOES NOT EXIST");
            return false;
        }
        userRepository.deleteById(inDB.get().getId());
        return true;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username.trim().toLowerCase());
    }
}
