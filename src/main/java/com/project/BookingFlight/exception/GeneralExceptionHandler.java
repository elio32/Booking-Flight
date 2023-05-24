package com.project.BookingFlight.exception;

import com.project.BookingFlight.model.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;


@ControllerAdvice @Slf4j
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<BaseResponse> handeGeneralException(GeneralException e){
        log.info("General Exception");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessages(List.of("Bad Request"));
        return ResponseEntity.status(403).body(baseResponse);
    }
}
