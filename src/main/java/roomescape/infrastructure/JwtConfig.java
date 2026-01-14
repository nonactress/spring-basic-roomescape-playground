package roomescape.infrastructure;


import auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

}
