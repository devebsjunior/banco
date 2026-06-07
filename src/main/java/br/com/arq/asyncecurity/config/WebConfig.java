package br.com.arq.asyncecurity.config;

import br.com.arq.asyncecurity.interceptor.AccessControlInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AccessControlInterceptor interceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
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
            registry.addInterceptor(interceptor)
                    .addPathPatterns("/api/**")   // protege APIs
                    .excludePathPatterns(         // libera login
                            "/auth/**",
                            "/error"
                    );
        }
    }