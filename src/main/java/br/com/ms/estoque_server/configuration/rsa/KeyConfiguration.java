package br.com.ms.estoque_server.configuration.rsa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

@Configuration
public class KeyConfiguration {

    @Value("${security.jwt.public-key}")
    private Resource publicKey;

    @Bean
    public RSAPublicKey publicKey() throws Exception {

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(publicKey.getInputStream(), StandardCharsets.UTF_8)
        )) {
            String key = bufferedReader.lines().collect(Collectors.joining())
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(key);

            return (RSAPublicKey) KeyFactory
                    .getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
