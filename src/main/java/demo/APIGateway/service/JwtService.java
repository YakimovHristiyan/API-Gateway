package demo.APIGateway.service;

import demo.APIGateway.config.ApiProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final RSAPublicKey publicKey;

    public JwtService(ApiProperties apiProperties) {
        this.publicKey = apiProperties.readPublicKey(apiProperties.getPublicKeyPath());
    }

    public String extractId(String token) {
        return this.extractClaim(token, Claims::getId);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        Jws<Claims> parsedJwt = Jwts.parserBuilder()
                .setSigningKey(this.publicKey)
                .build()
                .parseClaimsJws(token);

        return parsedJwt.getBody();
    }

    private Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }
}