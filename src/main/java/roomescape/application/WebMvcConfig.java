package roomescape.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthenticationPrincipalArgumentResolver authResolver;

    public WebMvcConfig(AuthenticationPrincipalArgumentResolver authResolver) {
        this.authResolver = authResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authResolver);
    }
}