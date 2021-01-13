package com.highcrit.ffacheckers.api.config;

import com.highcrit.ffacheckers.domain.communication.objects.ActionFailed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ActionFailed> malformedUUID() {
    return new ResponseEntity<>(
        new ActionFailed("Malformed UUID"), HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
