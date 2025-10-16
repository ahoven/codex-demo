package com.example.demo.colleagues;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ColleagueNotFoundException extends RuntimeException {

    public ColleagueNotFoundException(long id) {
        super("Colleague %d not found".formatted(id));
    }
}
