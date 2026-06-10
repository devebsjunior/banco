package br.com.arq.service;

import br.com.arq.repository.AuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @InjectMocks
    private AuditService service;

    @Mock
    private AuditRepository repository;

    @Test
    void deveRegistrarAudit() {

        service.registrar("user", "ADMIN", "OP", true, "msg", null, "origem", 10L);

        verify(repository).save(any());
    }
}