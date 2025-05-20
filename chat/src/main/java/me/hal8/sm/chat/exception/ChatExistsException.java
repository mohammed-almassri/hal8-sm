package me.hal8.sm.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChatExistsException extends RuntimeException{
    public ChatExistsException(String message){
        super(message);
    }
}
