package br.com.arq.repository;

import br.com.arq.audit.Audit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditRepositoryTest {

    @Mock
    private AuditRepository repository;

    @Test
    void deveSalvarAudit() {

        // cenário
        Audit audit = new Audit();

        when(repository.save(any()))
                .thenReturn(audit);

        // execução
        Audit result = repository.save(audit);

        // validação
        assertNotNull(result);

        verify(repository).save(audit);
    }
}