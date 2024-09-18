package com.blockchain.api.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Address {
  private String address;
  private String tag;
  private BigDecimal balance;
  private Object state;
}
