package com.blockchain.api.domain.service.balance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {
    @Test
    void testUSDCAddress() {
        assertEquals("0x94a9D9AC8a22534E3FaCa9F4e7F2E2cf85d5E4C8", Contract.USDC.address);
    }

    @Test
    void testUSDTAddress() {
        assertEquals("0x7169D38820dfd117C3FA1f22a697dBA58d90BA06", Contract.USDT.address);
    }
}