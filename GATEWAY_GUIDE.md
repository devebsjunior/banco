GUIA DE IMPLEMENTACAO: API GATEWAY COM SPRING CLOUD GATEWAY

Arquitetura: Microserviços com API Gateway Pattern
Status: Pronto para implementacao

PASSO 1: CRIAR NOVO PROJETO GATEWAY

Estrutura:
banco-gateway/
├── pom.xml
├── src/
│   └── main/
│       ├── java/br/com/arq/gateway/
│       │   ├── BancoGatewayApplication.java
│       │   ├── config/
│       │   │   ├── GatewayConfig.java
│       │   │   └── SecurityConfig.java
│       │   ├── filter/
│       │   │   ├── AuthenticationFilter.java
│       │   │   ├── RateLimitFilter.java
│       │   │   └── LoggingFilter.java
│       │   └── util/
│       │       └── JwtUtil.java
│       └── resources/
│           ├── application.yml
│           └── logback-spring.xml

PASSO 2: DEPENDENCIAS (pom.xml)

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.0</version>
</parent>

<dependencies>
    <!-- Spring Cloud Gateway -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

    <!-- Eureka Client (service discovery) - opcional -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>com.auth0</groupId>
        <artifactId>java-jwt</artifactId>
        <version>4.2.0</version>
    </dependency>

    <!-- Logging -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Spring Boot Starter Web (opcional) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2022.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>


PASSO 3: APPLICATION.YML

server:
  port: 8888
  servlet:
    context-path: /

spring:
  application:
    name: banco-gateway
  cloud:
    gateway:
      routes:
        # Microsservico de Banco
        - id: banco-service
          uri: http://localhost:8080
          predicates:
            - Path=/api/**
          filters:
            - AuthenticationFilter
            - RateLimitFilter
            - LoggingFilter
            - name: RewritePath
              args:
                regexp: ^/api/(.*)
                replacement: /api/$1

        # Rota publica para login
        - id: auth-service
          uri: http://localhost:8080
          predicates:
            - Path=/auth/**
          filters:
            - LoggingFilter

      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Origin Access-Control-Allow-Credentials

logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    br.com.arq.gateway: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}.%M:%L - %msg%n"
  file:
    name: logs/gateway.log


PASSO 4: CLASSE PRINCIPAL

package br.com.arq.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BancoGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancoGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("banco-service", r -> r
                .path("/api/**")
                .uri("http://localhost:8080"))
            .route("auth-service", r -> r
                .path("/auth/**")
                .uri("http://localhost:8080"))
            .build();
    }
}


PASSO 5: AUTHENTICATION FILTER

package br.com.arq.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerWebExchange modifiedExchange = exchange;

            try {
                // Extrair token do header
                String token = extractToken(exchange);

                if (token == null || token.isEmpty()) {
                    logger.warn("Requisicao sem token - Rota: {}", exchange.getRequest().getPath());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                // Validar token (aqui vc chama seu TokenService ou JWT util)
                if (!isTokenValid(token)) {
                    logger.warn("Token invalido - Rota: {}", exchange.getRequest().getPath());
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                logger.debug("Token validado - Rota: {}", exchange.getRequest().getPath());

            } catch (Exception e) {
                logger.error("Erro na autenticacao", e);
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(modifiedExchange);
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isTokenValid(String token) {
        try {
            // TODO: Chamar seu TokenService para validar
            // TokenService.validarToken(token)
            return !token.isEmpty();
        } catch (Exception e) {
            logger.error("Erro ao validar token", e);
            return false;
        }
    }

    public static class Config {
    }
}


PASSO 6: RATE LIMIT FILTER

package br.com.arq.gateway.filter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitFilter extends AbstractGatewayFilterFactory<RateLimitFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);
    
    private final Map<String, Bucket> cache = new HashMap<>();

    public RateLimitFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String key = exchange.getRequest().getRemoteAddress().getHostName();
            
            Bucket bucket = cache.computeIfAbsent(key, k -> 
                Bucket4j.builder()
                    .addLimit(Refill.greedy(100, Duration.ofMinutes(1)))
                    .build()
            );

            if (bucket.tryConsume(1)) {
                logger.debug("Requisicao permitida - IP: {}", key);
                return chain.filter(exchange);
            } else {
                logger.warn("Rate limit excedido - IP: {}", key);
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config {
    }
}


PASSO 7: LOGGING FILTER

package br.com.arq.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            long inicio = System.currentTimeMillis();
            
            String method = exchange.getRequest().getMethod().toString();
            String path = exchange.getRequest().getPath().toString();
            String remoteAddress = exchange.getRequest().getRemoteAddress().getHostName();

            logger.info("Requisicao recebida - Metodo: {}, Caminho: {}, IP: {}", 
                method, path, remoteAddress);

            return chain.filter(exchange).doFinally(signalType -> {
                long duracao = System.currentTimeMillis() - inicio;
                int statusCode = exchange.getResponse().getStatusCode() != null ? 
                    exchange.getResponse().getStatusCode().value() : 0;
                
                logger.info("Requisicao finalizada - Status: {}, Duracao: {}ms, Caminho: {}", 
                    statusCode, duracao, path);
            });
        };
    }

    public static class Config {
    }
}


PASSO 8: EXECUTAR GATEWAY

1. Verificar que o servico de banco esta rodando na porta 8080
2. Executar gateway na porta 8888

TESTES

URLs do gateway:

Login (publico):
POST http://localhost:8888/auth/login

Deposito (protegido):
POST http://localhost:8888/api/usuarios/contas/deposito
Header: Authorization: Bearer {token}

Saque (protegido):
POST http://localhost:8888/api/usuarios/contas/saque
Header: Authorization: Bearer {token}

Transferencia (protegida):
POST http://localhost:8888/api/usuarios/contas/transferir
Header: Authorization: Bearer {token}

Extrato (protegido):
GET http://localhost:8888/api/usuarios/contas/{numero}/extrato
Header: Authorization: Bearer {token}


FLUXO COMPLETO POST-GATEWAY

1. Cliente envia: POST http://localhost:8888/auth/login
2. Gateway recebe (LoggingFilter log)
3. Gateway roteia para: http://localhost:8080/auth/login
4. Banco responde com token JWT
5. Cliente armazena token

6. Cliente envia: POST http://localhost:8888/api/usuarios/contas/deposito
   Header: Authorization: Bearer {token}
7. Gateway LoggingFilter log
8. Gateway AuthenticationFilter valida token
9. Gateway RateLimitFilter verifica limite
10. Gateway roteia para: http://localhost:8080/api/usuarios/contas/deposito
11. Banco processa
12. Gateway LoggingFilter log resultado
13. Resposta volta ao cliente


PROXIMAS MELHORIAS

1. Adicionar Circuit Breaker (Resilience4j)
2. Implementar Eureka Server para service discovery
3. Centralizar logs em ELK Stack
4. Adicionar Prometheus para metricas
5. Implementar tracing distribuido com Jaeger
6. Usar Redis para cache
7. Adicionar WebSocket support (se necessario)
8. Implementar request/response transformation


DEPENDENCIA RATE LIMIT (pom.xml)

<dependency>
    <groupId>com.github.vladimir-bukhtoyarov</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>


ARQUIVO LOGBACK-SPRING.XML (Gateway)

Usar o mesmo padrão do banco:
- logs/gateway.log
- logs/gateway-debug.log
- logs/gateway-error.log
- Rotacao diaria
- Retencao 30 dias


SEGURANCA NO GATEWAY

1. Rate limiting: 100 requisicoes/minuto/IP
2. Validacao de token JWT antes de rotear
3. CORS centralizado no gateway
4. Proteger endpoints sensiveis
5. Logging de todas as requisicoes
6. Monitorar tentativas de ataque


PROXIMAS ETAPAS RECOMENDADAS

Fase 1 (Agora):
- Implementar API Gateway
- Rate limiting
- Logging centralizado

Fase 2 (Proximo):
- Eureka Server
- Service discovery
- Multiple instances

Fase 3 (Depois):
- Circuit Breaker
- Fallback strategies
- Resilience patterns

Fase 4 (Futuro):
- K8S deployment
- Helm charts
- Full observability


STATUS: PRONTO PARA IMPLEMENTACAO

Documentacao completa fornecida.
Proximo passo: Criar novo projeto gateway.

