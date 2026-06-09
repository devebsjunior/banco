package br.com.arq.service;

import br.com.arq.audit.AppLog;
import br.com.arq.repository.AppLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppLogService {

    private final AppLogRepository repository;

    public void registrar(
            AppLog log
                         ) {

        repository.save(
                log
                       );
    }

    public void registrar(
            String nivel,
            String mensagem,
            String origem,
            String error
                         ) {

        AppLog log =
                AppLog.builder()
                        .nivel(nivel)
                        .mensagem(mensagem)
                        .origem(origem)
                        .error(error)
                        .build();

        repository.save(
                log
                       );
    }

    public void info(
            String mensagem,
            String origem
                    ) {

        registrar(
                "INFO",
                mensagem,
                origem,
                null
                 );
    }

    public void warn(
            String mensagem,
            String origem
                    ) {

        registrar(
                "WARN",
                mensagem,
                origem,
                null
                 );
    }

    public void error(
            String mensagem,
            String origem,
            String erro
                     ) {

        registrar(
                "ERROR",
                mensagem,
                origem,
                erro
                 );
    }

    public void usuarioCriado(
            String email
                             ) {

        info(
                "Usuário criado: " + email,
                "UsuarioService"
            );
    }

    public void usuarioErro(
            String email,
            Exception ex
                           ) {

        error(
                "Erro ao criar usuário: " + email,
                "UsuarioService",
                ex.getMessage()
             );
    }

    public void agenciaCriada(
            String numeroAgencia
                             ) {

        info(
                "Agência criada: " + numeroAgencia,
                "AgenciaService"
            );
    }

    public void agenciaErro(
            String numeroAgencia,
            Exception ex
                           ) {

        error(
                "Erro ao criar agência: " + numeroAgencia,
                "AgenciaService",
                ex.getMessage()
             );
    }

    public void contaCriada(
            String numeroConta
                           ) {

        info(
                "Conta criada: " + numeroConta,
                "ContaService"
            );
    }

    public void contaErro(
            String numeroConta,
            Exception ex
                         ) {

        error( "Erro ao criar conta: " + numeroConta,
                "ContaService",
                ex.getMessage()
             );
    }
}