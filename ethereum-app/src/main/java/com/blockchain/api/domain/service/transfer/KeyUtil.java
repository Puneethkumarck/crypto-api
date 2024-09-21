package com.blockchain.api.domain.service.transfer;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import lombok.SneakyThrows;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

public class KeyUtil {

  private static final KeyFactory ecdsaKeyFactory;

  static {
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    try {
      ecdsaKeyFactory = KeyFactory.getInstance("ECDSA", "BC");
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new IllegalStateException(e);
    }
  }

  @SneakyThrows
  public static KeyPair loadEcdsaKeyPair(byte[] encoded) {
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encoded);
    BCECPrivateKey privateKey = (BCECPrivateKey) ecdsaKeyFactory.generatePrivate(privateKeySpec);
    ECParameterSpec ecParameterSpec = privateKey.getParameters();
    ECPoint Q = ecParameterSpec.getG().multiply(privateKey.getD());
    ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(Q, ecParameterSpec);
    return new KeyPair(ecdsaKeyFactory.generatePublic(publicKeySpec), privateKey);
  }
}
