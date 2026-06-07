package br.com.arq.service;

import br.com.arq.audit.Audit;
import br.com.arq.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

       private final AuditRepository repository;

        public void registrar(
                Audit log
                             ) {

            repository.save(
                    log
                           );
        }

        public void registrar(
                String usuario,
                String perfil,
                String operacao,
                boolean sucesso,
                String mensagem,
                String regra,
                String origem,
                Long tempoExecucaoMs
                             ) {

            Audit audit =
                    Audit.builder()
                            .usuario(usuario)
                            .perfil(perfil)
                            .operacao(operacao)
                            .sucesso(sucesso)
                            .mensagem(mensagem)
                            .regra(regra)
                            .origem(origem)
                            .tempoExecucaoMs(tempoExecucaoMs)
                            .build();

            repository.save(
                    audit
                           );
        }

        public void usuarioCriado(
                String email,
                Long tempo
                                 ) {

            registrar(
                    email,
                    "USER",
                    "CRIAR_USUARIO",
                    true,
                    "Usuário criado com sucesso",
                    null,
                    "UsuarioService",
                    tempo
                     );
        }

        public void usuarioErro(
                String email,
                Exception ex,
                Long tempo
                               ) {

            registrar(
                    email,
                    "USER",
                    "CRIAR_USUARIO",
                    false,
                    ex.getMessage(),
                    null,
                    "UsuarioService",
                    tempo
                     );
        }

        public void agenciaCriada(
                String numeroAgencia,
                Long tempo
                                 ) {

            registrar(
                    numeroAgencia,
                    "ADMIN",
                    "CRIAR_AGENCIA",
                    true,
                    "Agência criada com sucesso",
                    null,
                    "AgenciaService",
                    tempo
                     );
        }

        public void agenciaErro(
                String numeroAgencia,
                Exception ex,
                Long tempo
                               ) {

            registrar(
                    numeroAgencia,
                    "ADMIN",
                    "CRIAR_AGENCIA",
                    false,
                    ex.getMessage(),
                    null,
                    "AgenciaService",
                    tempo
                     );
        }
}


