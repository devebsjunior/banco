package br.com.arq.service;

import br.com.arq.dto.request.UsuarioRequestDTO;
import br.com.arq.enums.TipoPerfil;
import br.com.arq.model.Perfil;
import br.com.arq.model.Usuario;
import br.com.arq.repository.ClienteRepository;
import br.com.arq.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AppLogService logService;

    @Mock
    private AuditService auditService;

    @Mock
    private EmailService emailService;



    @Test
    void deveCriarUsuario() {



        UsuarioRequestDTO dto = mock(UsuarioRequestDTO.class);

        when(dto.email()).thenReturn("teste@email.com");
        when(dto.username()).thenReturn("user");
        when(dto.senha()).thenReturn("123");
        when(dto.primeiroNome()).thenReturn("Edson");
        when(dto.ultimoNome()).thenReturn("Junior");


        Perfil perfil = Perfil.builder()
                .tipoPerfil(TipoPerfil.ADMIN)
                .nivelPermissao(1)
                .build();

        when(dto.perfil()).thenReturn(perfil);


        when(usuarioRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.empty());

        when(usuarioRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));


        Usuario result = service.criarUsuario(dto);


        assertNotNull(result);
        assertEquals("teste@email.com", result.getEmail());

        verify(usuarioRepository).save(any());
        verify(emailService).enviarGmail(any());
    }


}
