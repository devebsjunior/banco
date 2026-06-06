package br.com.arq.service;

import br.com.arq.audit.Audit;
import br.com.arq.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository repository;



        /**
         * Método simples (mantém o atual)
         */
        public void registrar(Audit log) {
            repository.save(log);
        }

        /**
         * ✅ Método correto para uso no service
         */
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

            Audit audit = Audit.builder()
                    .usuario(usuario)
                    .perfil(perfil)
                    .operacao(operacao)
                    .sucesso(sucesso)
                    .mensagem(mensagem)
                    .regra(regra)
                    .origem(origem)
                    .tempoExecucaoMs(tempoExecucaoMs)
                    .build();

            repository.save(audit);
        }
}

