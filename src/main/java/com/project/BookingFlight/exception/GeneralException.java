package com.project.BookingFlight.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class GeneralException extends RuntimeException{

    private String message;
    private Object content;
}
