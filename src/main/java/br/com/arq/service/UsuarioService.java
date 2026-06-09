package br.com.arq.service;

import br.com.arq.dto.request.UsuarioRequestDTO;
import br.com.arq.model.Cliente;
import br.com.arq.model.Perfil;
import br.com.arq.model.Usuario;
import br.com.arq.repository.ClienteRepository;
import br.com.arq.repository.UsuarioRepository;
import br.com.arq.rules.core.Facts;
import br.com.arq.rules.core.Rule;
import br.com.arq.rules.core.RuleEngine;
import br.com.arq.rules.core.catalog.UsuarioRules;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
  private final UsuarioRepository usuarioRepository;
  private final ClienteRepository clienteRepository;
  private final AppLogService logService;
  private final AuditService auditService;

  @Transactional
  public Usuario criarUsuario(UsuarioRequestDTO dto) {

    long inicio = System.currentTimeMillis();

    try {
      logger.info("Criando usuário email={}", dto.email());

      usuarioRepository.findByEmail( dto.email() ).ifPresent(
              usuario -> {
                throw new RuntimeException("Email já cadastrado" );
              } );

      Facts facts = new Facts()
              .add(UsuarioRequestDTO.class, dto);
      executarRegras(facts, UsuarioRules.emailObrigatorio(), UsuarioRules.senhaObrigatoria());
      Usuario usuario = construirUsuario(dto);
      Usuario salvo = usuarioRepository.save(usuario);

      auditoriaSucesso("CRIAR_USUARIO", "Usuário criado", inicio);
      return salvo;

    } catch (Exception ex) {

      auditoriaErro("CRIAR_USUARIO", ex, inicio);

      throw ex;
    }
  }



  public Usuario buscarPorId(Long id) {

    return usuarioRepository.findById(id).orElseThrow(() ->
                           new RuntimeException("Usuário não encontrado"));
  }

  public Usuario buscarPorEmail(String email) {

    return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
  }

  public List<Usuario> buscarTodos() {

    return usuarioRepository.findAll();
  }

  @Transactional
  public Usuario alterarSenha(Long usuarioId, String novaSenha) {

    Usuario usuario = buscarPorId(usuarioId);

    usuario.setSenha(BCrypt.hashpw(novaSenha, BCrypt.gensalt()));

    return usuarioRepository.save(usuario);
  }


  private void executarRegras(Facts facts, Rule... rules) {

    RuleEngine.RuleEngineBuilder builder = RuleEngine.builder().
            facts(facts).logService(logService).auditService(auditService);

    for (Rule rule : rules) {
      builder.rule(rule);
    }

    builder.build().run();
  }

  private void auditoriaSucesso(String operacao, String mensagem, long inicio) {

    auditService.registrar("user", "USER", operacao, true, mensagem, null, "UsuarioService", tempo(inicio));
  }

  private void auditoriaErro(String operacao, Exception ex, long inicio) {

    auditService.registrar("user", "USER", operacao, false, ex.getMessage(), null, "UsuarioService", tempo(inicio));
  }

  private long tempo(long inicio) {

    return System.currentTimeMillis() - inicio;
  }

  private Usuario construirUsuario(
          UsuarioRequestDTO dto

                                  ) {

    Usuario usuario = Usuario.builder()
            .username( dto.username() )
            .email( dto.email() )
            .senha( BCrypt.hashpw(dto.senha(), BCrypt.gensalt()) )
            .primeiroNome( dto.primeiroNome() )
            .ultimoNome( dto.ultimoNome() )
            .build();

    Perfil perfil = Perfil.builder()
            .tipoPerfil( dto.perfil().getTipoPerfil() )
            .nivelPermissao( dto.perfil().getNivelPermissao() )
            .usuario( usuario )
            .build();

    usuario.setPerfis( List.of(perfil) );

    return usuario;
  }
}
