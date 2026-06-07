package br.com.arq.infraestructure.saga;



public interface SagaStep<T> {
     T execute(T context);

    void compensate(T context);
}