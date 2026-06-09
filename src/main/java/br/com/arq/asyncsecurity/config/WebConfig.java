package br.com.arq.asyncsecurity.config;

import br.com.arq.asyncsecurity.interceptor.AccessControlInterceptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    private final AccessControlInterceptor interceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");

    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("Registrando AccessControlInterceptor");

       registry.addInterceptor(interceptor)
                .excludePathPatterns(
                        "/api/auth/**",
                        "/api/admin/contas",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/error"
                );


        logger.info("Interceptor configurado corretamente (Swagger liberado)");
    }

}