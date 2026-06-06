package br.com.arq.mapper;
import br.com.arq.dto.ContaDTO;
import br.com.arq.model.Cliente;
import br.com.arq.model.Conta;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ContaMapper {



    public static final Function<Conta, ContaDTO> TO_DTO =
            conta -> {
                String nomeCliente = null;

                if (conta.getCliente() != null) {
                    nomeCliente = conta.getCliente().getNome();
                }

                return new ContaDTO(
                        conta.getNumeroConta(),
                        conta.getSaldo(),
                        conta.getPerfil(),
                        nomeCliente
                );
            };


        public static final Function<ContaDTO, Conta> TO_ENTITY =
                dto -> {
                    Conta conta = new Conta();
                    conta.setNumeroConta(dto.numeroConta());
                    conta.setSaldo(dto.saldo());
                    conta.setPerfil(dto.perfil());
                    return conta;
                };


        public static final BiFunction<ContaDTO, Cliente, Conta> TO_ENTITY_FULL =
                (dto, cliente) -> {
                    Conta conta = new Conta();
                    conta.setNumeroConta(dto.numeroConta());
                    conta.setSaldo(dto.saldo());
                    conta.setPerfil(dto.perfil());
                    conta.setCliente(cliente);
                    return conta;
                };


        public static final BiFunction<ContaDTO, Conta, Conta> MERGE =
                (dto, conta) -> {
                    conta.setSaldo(dto.saldo());
                    conta.setPerfil(dto.perfil());
                    return conta;
                };


}

