package com.blockchain.api.application.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blockchain.api.model.ApiError;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;
  @Mock private BindException bindException;
  @Mock private BindingResult bindingResult;

  @Test
  public void testHandleInvalidAddressException() {
    // given
    var ex = new InvalidAddressException("Invalid address");

    // when
    var apiError = globalExceptionHandler.handleResponseStatusException(ex);

    // then
    var expected =
        ApiError.builder()
            .status(HttpStatus.BAD_REQUEST.toString())
            .message("Invalid address")
            .build();

    assertThat(apiError).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void shouldHandleInsufficientBalanceException() {
    // given
    var ex = new InsufficientBalanceException("Insufficient balance");

    // when
    var apiError = globalExceptionHandler.handleResponseStatusException(ex);

    // then
    var expected =
        ApiError.builder()
            .status(HttpStatus.BAD_REQUEST.toString())
            .message("Insufficient balance")
            .build();

    assertThat(apiError).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void testHandleBindException() {
    // given
    var errors =
        Stream.of(
                new ObjectError("objectName", "First error message"),
                new ObjectError("objectName", "Second error message"))
            .collect(Collectors.toList());

    when(bindException.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getAllErrors()).thenReturn(errors);

    // when
    var apiError = globalExceptionHandler.handleBindException(bindException);

    // then
    var expected =
        ApiError.builder()
            .status(HttpStatus.BAD_REQUEST.toString())
            .message("First error message, Second error message")
            .build();

    assertThat(apiError).usingRecursiveComparison().isEqualTo(expected);
  }
}
