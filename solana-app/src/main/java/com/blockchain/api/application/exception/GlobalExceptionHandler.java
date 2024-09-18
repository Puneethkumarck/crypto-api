package com.blockchain.api.application.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.blockchain.api.model.ApiError;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidAddressException.class)
  @ResponseStatus(BAD_REQUEST)
  public ApiError handleResponseStatusException(InvalidAddressException e) {
    return ApiError.builder().status(BAD_REQUEST.toString()).message(e.getMessage()).build();
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(BAD_REQUEST)
  public ApiError handleBindException(BindException e) {
    return ApiError.builder()
        .status(BAD_REQUEST.toString())
        .message(
            e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")))
        .build();
  }
}
