package com.blockchain.api.domain.service.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blockchain.api.application.exception.AccountLoaderException;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.p2p.solanaj.core.Account;

@ExtendWith(MockitoExtension.class)
class AccountLoaderServiceTest {

  @Mock private SolanaConfigProperties solanaConfigProperties;

  @InjectMocks private AccountLoaderService accountLoaderService;

  @Test
  void shouldLoadSenderKeypair_fromBase64() {
    // given
    var base64Key =
        "WzE0LDEyNSwxODcsMTM2LDIzOSwxNzMsMTk4LDcwLDEzMiwxMTYsNjgsMTcxLDQxLDE5MywyMTIsMTU2LDIyMSwxNywxOTIsMTU1LDQxLDY5LDEwNCwxODUsNDcsNDAsMTU4LDI0MSwxMTMsMTcsMjQsMzEsMTIsMTgsMjUzLDE2NCw0Nyw4NSwxMjksMTk2LDExNiwxODcsNzMsMTY4LDIwMywyMDIsMTcwLDIyNCwxMTcsNDAsNjksNSwxMTYsMjAzLDUyLDE5NSwyMjUsNjksNDMsMjQ3LDgyLDIyNywxLDk2XQ==";
    when(solanaConfigProperties.keypair()).thenReturn(base64Key);

    // when
    var account = accountLoaderService.loadSenderKeypair();

    // then
    assertNotNull(account);
  }

  @Test
  void shouldLoadSenderKeypair_fromArray() {
    // given
    var keyPair =
        "[14,125,187,136,239,173,198,70,132,116,68,171,41,193,212,156,221,17,192,155,41,69,104,185,47,40,158,241,113,17,24,31,12,18,253,164,47,85,129,196,116,187,73,168,203,202,170,224,117,40,69,5,116,203,52,195,225,69,43,247,82,227,1,96]";
    when(solanaConfigProperties.keypair()).thenReturn(keyPair);

    // when
    var account = accountLoaderService.loadSenderKeypair();

    // then
    assertNotNull(account);
  }

  @Test
  void shouldThrowException_whenKeyPairIsBlank() {
    // given
    when(solanaConfigProperties.keypair()).thenReturn("");

    // when
    var exception =
        assertThrows(AccountLoaderException.class, () -> accountLoaderService.loadSenderKeypair());

    // then
    assertEquals("SECRET_KEY not found in properties.", exception.getMessage());
  }

  @Test
  void shouldThrowException_whenKeyPairIsNull() {
    // given
    when(solanaConfigProperties.keypair()).thenReturn(null);

    // when
    var exception =
        assertThrows(AccountLoaderException.class, () -> accountLoaderService.loadSenderKeypair());

    // then
    assertEquals("SECRET_KEY not found in properties.", exception.getMessage());
  }

  @Test
  void shouldMatchPublicKeyGeneratedFromSecret() {
    // given
    var secretKeyString =
        "[14,125,187,136,239,173,198,70,132,116,68,171,41,193,212,156,221,17,192,155,41,69,104,185,47,40,158,241,113,17,24,31,12,18,253,164,47,85,129,196,116,187,73,168,203,202,170,224,117,40,69,5,116,203,52,195,225,69,43,247,82,227,1,96]";

    // when
    Account account = Account.fromJson(secretKeyString);

    // then
    var expected = "p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75";
    assertThat(account.getPublicKey().toString()).isEqualTo(expected);
  }

  @Test
  void shouldMatchPublicKeyGeneratedFromBase64EncodedSecretKey() {
    // given
    var secretKeyString =
        new String(
            Base64.getDecoder()
                .decode(
                    "WzE0LDEyNSwxODcsMTM2LDIzOSwxNzMsMTk4LDcwLDEzMiwxMTYsNjgsMTcxLDQxLDE5MywyMTIsMTU2LDIyMSwxNywxOTIsMTU1LDQxLDY5LDEwNCwxODUsNDcsNDAsMTU4LDI0MSwxMTMsMTcsMjQsMzEsMTIsMTgsMjUzLDE2NCw0Nyw4NSwxMjksMTk2LDExNiwxODcsNzMsMTY4LDIwMywyMDIsMTcwLDIyNCwxMTcsNDAsNjksNSwxMTYsMjAzLDUyLDE5NSwyMjUsNjksNDMsMjQ3LDgyLDIyNywxLDk2XQ=="));
    // when
    Account account = Account.fromJson(secretKeyString);

    // then
    var expected = "p8guAeE7naqQcT2JMCp8Q376MLyzt5XynfGw3uCHM75";
    assertThat(account.getPublicKey().toString()).isEqualTo(expected);
  }
}
