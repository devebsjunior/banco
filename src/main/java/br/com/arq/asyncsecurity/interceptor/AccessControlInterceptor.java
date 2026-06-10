package br.com.arq.asyncsecurity.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import br.com.arq.asyncsecurity.security.TokenService;
import br.com.arq.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccessControlInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String path = request.getRequestURI();
        if (path.contains("swagger") ||
                path.contains("api-docs") ||
                path.contains("webjars")) {
            return true;
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setStatus(HttpServletResponse.SC_OK); // Retorna 200 OK
            return false; // Retorna false para interromper o fluxo aqui e responder direto ao browser
        }
        if (path.contains("swagger") ||
                path.contains("api-docs") ||
                path.contains("webjars") ||
                path.startsWith("/api/auth")) {
            return true;
        }
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("JWT ausente | IP={} | Agent={}",
                        RequestUtils.getIp(request),
                        RequestUtils.getUserAgent(request));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            String token = authHeader.replace("Bearer ", "");
            String numeroConta = tokenService.validarToken(token);
            if (numeroConta == null || numeroConta.isBlank()) {
                log.warn("JWT inválido");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            log.debug("OK | Conta={} | IP={} | Agent={}",
                    numeroConta,
                    RequestUtils.getIp(request),
                    RequestUtils.getUserAgent(request));
            request.setAttribute("numeroConta", numeroConta);
            return true;
        } catch (Exception e) {
            log.warn("Falha na validação do token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}