package com.bastiansmn.scoreserver.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FunctionalException extends Exception {

    private String clientMessage;
    private HttpStatus httpStatus;

}
