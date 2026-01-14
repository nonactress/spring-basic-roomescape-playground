package roomescape;

import auth.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class MissionStepTest {
    @Test
    void 칠단계() {
        Component componentAnnotation = JwtTokenProvider.class.getAnnotation(Component.class);
        assertThat(componentAnnotation).isNull();
    }


    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Test
    void 팔단계() {
        assertThat(secretKey).isNotBlank();
    }
}
