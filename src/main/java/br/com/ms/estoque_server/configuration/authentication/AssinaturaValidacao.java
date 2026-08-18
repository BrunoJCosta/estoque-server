package br.com.ms.estoque_server.configuration.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Service
class AssinaturaValidacao {

    private final RSAPublicKey publicKey;
    @Value("${security.jwt.secret}")
    private String secretToken;

    AssinaturaValidacao(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    boolean valido(String hashAssinatura) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(this.publicKey);

        signature.update(this.secretToken.getBytes(StandardCharsets.UTF_8));

        byte[] assinatura = Base64.getDecoder().decode(hashAssinatura);

        return signature.verify(assinatura);
    }
}
