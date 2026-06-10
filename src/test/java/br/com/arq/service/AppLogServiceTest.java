package br.com.arq.service;

import br.com.arq.audit.AppLog;
import br.com.arq.repository.AppLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppLogServiceTest {

    @InjectMocks
    private AppLogService service;

    @Mock
    private AppLogRepository repository;

    @Test
    void deveRegistrarLog() {

        AppLog log = new AppLog();

        service.registrar(log);

        verify(repository).save(log);
    }

    @Test
    void deveRegistrarInfo() {

        service.info("msg", "origem");

        verify(repository).save(any());
    }
}
