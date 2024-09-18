package com.blockchain.api.domain.service.blockhash;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BlockHashServiceTest {
  @InjectMocks private BlockHashService blockHashService;

  @Mock private BlockhashClient blockhashClient;

  @Test
  void shouldReturnBlockHash() {
    // given
    var expectedBlockhash = "dummyBlockhash";
    when(blockhashClient.getLatestBlockhash()).thenReturn(completedFuture(expectedBlockhash));

    // when
    var actualBlockhash = blockHashService.getBlockhash();

    // then
    assertThat(actualBlockhash).isEqualTo(expectedBlockhash);
  }

  @Test
  void testGetBlockhashFailure() {
    // given
    CompletableFuture<String> failedFuture = new CompletableFuture<>();
    failedFuture.completeExceptionally(new RuntimeException("Failed to fetch blockhash"));
    when(blockhashClient.getLatestBlockhash()).thenReturn(failedFuture);

    // when, then
    assertThrows(RuntimeException.class, () -> blockHashService.getBlockhash());
  }
}
