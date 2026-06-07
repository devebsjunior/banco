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
        logger.info("Configurando CORS para origem: http://localhost:4200");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        logger.debug("CORS configurado com metodos: GET, POST, PUT, DELETE, OPTIONS");
    }

    /**
     * Registra o interceptor responsável pelo controle de acesso da aplicação.
     * Protege todas as rotas que começam com "/api/**"
     * e libera acesso para rotas públicas como "/auth/**" e "/error".
     * O interceptor valida:
     * - Token JWT
     * - Sessão ativa (UUID)
     * Se a validação falhar, a requisição é bloqueada.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("Registrando AccessControlInterceptor");

        registry.addInterceptor(interceptor)
                .addPathPatterns("/api/**")   // protege APIs
                .excludePathPatterns(         // libera login
                        "/auth/**",
                        "/error"
                );

        logger.debug("Interceptor configurado - Protegendo: /api/** | Liberando: /auth/**, /error");
    }
}
