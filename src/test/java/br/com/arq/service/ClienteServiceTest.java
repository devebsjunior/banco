package br.com.arq.service;

import br.com.arq.dto.request.ClienteRequestDTO;
import br.com.arq.model.Cliente;
import br.com.arq.repository.AgenciaRepository;
import br.com.arq.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService service;

    @Mock
    private ClienteRepository repository;

    @Mock
    private AgenciaRepository agenciaRepository;

    @Mock
    private AppLogService logService;

    @Mock
    private AuditService auditService;

    @Mock
    private EmailService emailService;

    @Test
    void deveCriarCliente() {

        ClienteRequestDTO dto = mock(ClienteRequestDTO.class);

        when(dto.cpf()).thenReturn("123");
        when(dto.nome()).thenReturn("Edson");
        when(dto.email()).thenReturn("email@email.com");
        when(dto.conta()).thenReturn(null);

        when(repository.findByCpf("123"))
                .thenReturn(Optional.empty());

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Cliente result = service.criar(dto);

        assertNotNull(result);

        verify(repository).save(any());
        verify(emailService).enviarGmail(any());
    }
}