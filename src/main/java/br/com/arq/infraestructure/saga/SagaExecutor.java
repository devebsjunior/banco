package br.com.arq.infraestructure.saga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class SagaExecutor {

    private final List<Runnable> compensations = new ArrayList<>();

    public <T> T execute(java.util.function.Supplier<T> execution) {

        try {

            log.info("Iniciando Saga ...");

            T result = execution.get();

            log.info(" Saga finalizada com sucesso");

            return result;

        } catch (Exception e) {

            log.error("Erro na Saga, executando rollback");

            rollback();

            throw new RuntimeException("Erro na execução da Saga", e);
        } finally {
            compensations.clear();
        }
    }


    public void addCompensation(Runnable action) {
        compensations.add(action);
    }


    private void rollback() {

        Collections.reverse(compensations);

        for (Runnable action : compensations) {
            try {
                action.run();
            } catch (Exception e) {
                log.error("Erro na compensação: {}", e.getMessage());
            }
        }
    }
}