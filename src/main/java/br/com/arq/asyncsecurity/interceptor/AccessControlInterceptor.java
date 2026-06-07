package br.com.arq.asyncsecurity.interceptor;

import br.com.arq.asyncsecurity.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import br.com.arq.asyncsecurity.security.TokenService;
import br.com.arq.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




/**
 * Interceptor responsável por controlar o acesso às requisições HTTP.
 * Valida:
 * - Token JWT
 * - Sessão ativa
 * Bloqueia a requisição caso alguma validação falhe.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessControlInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final SessionService sessionService;

        @Override
        public boolean preHandle(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler) {

            String path = request.getRequestURI();

            if (path.startsWith("/auth")) {
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

                if (numeroConta.isBlank()) {
                    log.error("JWT inválido");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }


                String sessionId = request.getHeader("X-BBI-Session");

                if (sessionId == null || sessionId.isBlank()) {
                    log.warn("Sessão ausente");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }

                if (!sessionService.validar(sessionId)) {
                    log.warn("Sessão inválida: {}", sessionId);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }


                log.info(" OK | Conta={} | Sessão={} | IP={} | Agent={}",
                        numeroConta,
                        sessionId,
                        RequestUtils.getIp(request),
                        RequestUtils.getUserAgent(request));

                return true;

            } catch (Exception e) {
                log.error("Erro no interceptor: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return false;
            }
        }
}