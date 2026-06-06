package br.com.arq.service;

import br.com.arq.audit.AppLog;
import br.com.arq.repository.AppLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppLogService {

    private final AppLogRepository repository;

    public void info(String msg, String origem) {
        repository.save(
                AppLog.builder()
                        .nivel("INFO")
                        .mensagem(msg)
                        .origem(origem)
                        .build()
        );
    }

    public void warn(String msg, String origem) {
        repository.save(
                AppLog.builder()
                        .nivel("WARN")
                        .mensagem(msg)
                        .origem(origem)
                        .build()
        );
    }

    public void error(String msg, String origem, String error) {
        repository.save(
                AppLog.builder()
                        .nivel("ERROR")
                        .error(error)
                        .mensagem(msg)
                        .origem(origem)
                        .build()
        );
    }
}