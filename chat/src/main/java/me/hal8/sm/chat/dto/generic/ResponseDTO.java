package me.hal8.sm.chat.dto.generic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private String message;
    private T data;
}
