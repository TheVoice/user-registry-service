package com.karol.users.application;

import com.karol.users.model.User;
import com.karol.users.utils.DateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private static final String ALLOWED_COUNTRY = "France";

    private final UserRepository userRepository;

    @GetMapping("/user/{id}")
    public User getOne(@PathVariable Long id) {
        long startTime = System.nanoTime();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %d not found", id)));

        log.info(String.format("Returning user with id %d: %s (processing time: %d ms)", id, user, TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)));
        return user;
    }

    @PostMapping("/user")
    public ResponseEntity addUser(@Valid @RequestBody User user) {
        long startTime = System.nanoTime();

        if (!ALLOWED_COUNTRY.equals(user.getCountry())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Creation of users outside of France is not allowed");
        }
        if (!DateValidator.isAdultWithDateOfBirth(user.getDateOfBirth())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Creation of users younger than 18 not allowed");
        }
        User createdUser = userRepository.save(user);

        log.info(String.format("Saved user %s (processing time: %d ms)", createdUser, TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

}
