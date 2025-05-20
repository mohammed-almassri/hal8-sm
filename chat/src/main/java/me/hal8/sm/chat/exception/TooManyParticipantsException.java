package me.hal8.sm.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooManyParticipantsException extends RuntimeException{
    public TooManyParticipantsException(String message){
        super(message);
    }
}
