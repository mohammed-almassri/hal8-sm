package me.hal8.sm.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooFewParticipantException extends RuntimeException{
    public TooFewParticipantException(String message){
        super(message);
    }
}
