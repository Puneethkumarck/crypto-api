package com.blockchain.api.domain.service.balance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Contract {
    USDC("0x94a9D9AC8a22534E3FaCa9F4e7F2E2cf85d5E4C8"),
    USDT("0x7169D38820dfd117C3FA1f22a697dBA58d90BA06");
    public final String address;
}
