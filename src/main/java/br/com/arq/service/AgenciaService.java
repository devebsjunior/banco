package br.com.arq.service;


import br.com.arq.dto.request.AgenciaRequestDTO;
import br.com.arq.model.Agencia;
import br.com.arq.repository.AgenciaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenciaService {

  private static final Logger logger = LoggerFactory.getLogger(AgenciaService.class);

  private final AgenciaRepository repository;

  @Transactional
  public Agencia criar(AgenciaRequestDTO dto) {

    repository.findByNumeroAgencia(dto.numeroAgencia()).ifPresent(a -> {
      throw new RuntimeException("Agência já cadastrada");
    });

    Agencia agencia = Agencia.builder().
            nomeAgencia(dto.nomeAgencia()).numeroAgencia(dto.numeroAgencia()).
            logradouro(dto.logradouro()).numero(dto.numero()).bairro(dto.bairro())
            .cidade(dto.cidade()).estado(dto.estado()).cep(dto.cep()).build();

    Agencia salva = repository.save(agencia);
    logger.info("Agência criada {}", salva.getNumeroAgencia());
    return salva;
  }

  public Agencia buscarPorNumero(String numeroAgencia) {

    return repository.findByNumeroAgencia(numeroAgencia).
             orElseThrow(() -> new RuntimeException("Agência não encontrada"));
  }

  public List<Agencia> buscarTodas() {

    return repository.findAll();
  }

  @Transactional
  public void excluir(Long id) {

    repository.deleteById(id);
  }

  private Agencia construirAgencia(AgenciaRequestDTO dto) {
    return Agencia.builder().nomeAgencia(dto.nomeAgencia()).
            numeroAgencia(dto.numeroAgencia()).
            logradouro(dto.logradouro()).
            numero(dto.numero()).
            bairro(dto.bairro()).
            cidade(dto.cidade()).
            estado(dto.estado()).cep(dto.cep()).build();
  }


}