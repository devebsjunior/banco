package br.com.arq.asyncsecurity.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço responsável pelo gerenciamento de sessões de usuário.
 * Controla criação, validação, remoção e invalidação das sessões
 * utilizadas na autenticação da aplicação.
 * Cada sessão possui um identificador único (UUID) e tempo de expiração.
 *
 * @author Edson Belém
 * @version 1.0
 * @since 2026
 */
@Slf4j
@Service
public class SessionService {

    private final Map<String, SessionToken> sessions = new ConcurrentHashMap<>();


    /**
     * Cria uma nova sessão para o usuário.
     *
     * @param usuarioId identificador do usuário
     * @return sessão criada
     */
     public SessionToken criarSessao(Long usuarioId) {
        SessionToken token = new SessionToken(usuarioId);
         sessions.put(token.getUuid(), token);
         log.info(" Sessão criada | usuarioId={} | uuid={}", usuarioId, token.getUuid());
        return token;
    }


    /**
     * Valida se a sessão está ativa e não expirou.
     *
     * @param uuid identificador da sessão
     * @return true se a sessão for válida, false caso contrário
     */
    public boolean validar(String uuid) {
            SessionToken session = sessions.get(uuid);
            if (session == null) {
                log.warn("Sessão não encontrada: {}", uuid);
                return false;
            }
            boolean valido = session.isValido();
            if (!valido) {
                log.warn("Sessão inválida ou expirada: {}", uuid);
                sessions.remove(uuid); // limpeza automática
            }
            return valido;
    }

    /**
     * Marca a sessão como negada.
     *
     * @param uuid identificador da sessão
     */
    public void negar(String uuid) {
            SessionToken session = sessions.get(uuid);
            if (session != null) {
                session.negar();
                log.warn("Sessão negada: {}", uuid);
            }
    }


    /**
     * Remove a sessão (logout do usuário).
     *
     * @param uuid identificador da sessão
     */
    public void remover(String uuid) {
            sessions.remove(uuid);
            log.info("🗑 Sessão removida (logout): {}", uuid);
    }


    /**
     * Busca uma sessão pelo identificador.
     *
     * @param uuid identificador da sessão
     * @return sessão encontrada ou vazio
    */
    public Optional<SessionToken> buscar(String uuid) {
            return Optional.ofNullable(sessions.get(uuid));
   }

    /**
    * Invalida todas as sessões de um usuário específico.
    *
    * @param usuarioId identificador do usuário
    */
   public void invalidarPorUsuario(Long usuarioId) {
            sessions.values().removeIf(session -> {
                boolean match = session.getUsuarioId().equals(usuarioId);
                if (match) {
                    log.warn("Sessão invalidada por usuário: {}", session.getUuid());
                }
                return match;
            });
        }
   }