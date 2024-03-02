package demo.APIGateway.config;

import demo.APIGateway.constant.MessageConstants;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;

@Configuration
@ConfigurationProperties("api")
@Data
public class ApiProperties {

    private final static String ALGORITHM_RSA = "RSA";
    private final static String PUBLIC_KEY_FILE_START = "-----BEGIN PRIVATE KEY-----";
    private final static String PUBLIC_KEY_FILE_END = "-----END PRIVATE KEY-----";

    private Set<String> openRequestPaths;
    private String publicKeyPath;

    @SneakyThrows
    public RSAPublicKey readPublicKey(String filepath) {
        Objects.requireNonNull(filepath, MessageConstants.FILEPATH_NOT_NULL);
        final var inputStream = this.getClass().getResourceAsStream(filepath);
        Objects.requireNonNull(inputStream, MessageConstants.RESOURCE_NOT_FOUND + filepath);
        final var keyBytes = inputStream.readAllBytes();
        final var pemPublicKey = new String(keyBytes, StandardCharsets.UTF_8);
        final var base64PublicKey = this.extractBase64KeyFromPEM(pemPublicKey);
        final var publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
        final var keySpec = new X509EncodedKeySpec(publicKeyBytes);

        return (RSAPublicKey) KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(keySpec);
    }

    private String extractBase64KeyFromPEM(String pemPrivateKey) {
        return pemPrivateKey
                .replaceAll(PUBLIC_KEY_FILE_START, "")
                .replaceAll(PUBLIC_KEY_FILE_END, "")
                .replaceAll("\\s", "");
    }
}