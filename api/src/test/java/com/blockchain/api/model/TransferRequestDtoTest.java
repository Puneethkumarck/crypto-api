package com.blockchain.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TransferRequestDtoTest {

  private static Validator validator;

  @BeforeAll
  public static void setUp() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testValidTransferRequestDto() {
    // given
    var transferRequestDto = TransferRequestDto.builder().to("to").amount(5000L).build();

    // when
    var violations = validator.validate(transferRequestDto);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  public void testAmountIsRequired() {
    // given
    var transferRequestDto = TransferRequestDto.builder().to("to").build();

    // when
    var violations = validator.validate(transferRequestDto);

    // then
    assertEquals("Amount is required", violations.iterator().next().getMessage());
  }

  @Test
  public void testToAddressIsRequired() {
    // given
    var transferRequestDto = TransferRequestDto.builder().to("").amount(5000L).build();

    // when
    var violations = validator.validate(transferRequestDto);

    // then
    assertEquals("To address is required", violations.iterator().next().getMessage());
  }
}
