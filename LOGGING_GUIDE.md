# 📋 Guia de Logging - SLF4J + Logback

## 📝 Resumo
Este projeto utiliza **SLF4J (Simple Logging Facade for Java)** com **Logback** como implementação para logging estruturado e rastreável.

## ✅ Por que NÃO usar Lombok para logs?

Embora Lombok ofereça `@Slf4j`, **não recomendamos** para este projeto porque:

1. **Menos controle**: A anotação do Lombok é uma "mágica" que esconde o logger
2. **Debugging difícil**: Difícil de rastrear em stack traces
3. **Padrão industrial**: SLF4J + LoggerFactory é o padrão em aplicações enterprise
4. **Configuração granular**: Melhor controle de níveis de log por pacote

## 🚀 Como usar SLF4J em suas classes

### 1. Importar no topo da classe

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```

### 2. Criar Logger como atributo estático

```java
private static final Logger logger = LoggerFactory.getLogger(SuaClasse.class);
```

### 3. Usar em seus métodos

```java
// INFO - Informações importantes
logger.info("Operação realizada: {}", operacao);

// DEBUG - Detalhes de execução (não aparece em produção por padrão)
logger.debug("Variável debug: {}", variavel);

// WARN - Avisos
logger.warn("Atenção: operação incomum: {}", evento);

// ERROR - Erros com stack trace
logger.error("Erro crítico", exception);
logger.error("Erro: {} em {}", mensagem, local, exception);
```

## 📂 Diretório de Logs

Todos os logs são salvos em: **`./logs/`**

### Arquivos de Log Gerados:

| Arquivo | Conteúdo | Retenção |
|---------|----------|----------|
| `app.log` | Todos os logs | 30 dias |
| `app-debug.log` | Apenas DEBUG | 7 dias |
| `app-info.log` | Apenas INFO | 30 dias |
| `app-warn.log` | Apenas WARN | 15 dias |
| `app-error.log` | Apenas ERROR | 30 dias |

Cada arquivo tem rotação diária com compressão `.gz`

## 🎯 Exemplos Reais

### Em um Controller
```java
@PostMapping("/deposito")
public ResponseEntity<String> depositar(@Valid @RequestBody OperacaoBancariaDTO dto) {
    try {
        logger.info("Depositando R$ {} na conta {}", dto.valor(), dto.numeroConta());
        contaService.depositar(dto);
        logger.info("Depósito bem-sucedido - Conta: {}", dto.numeroConta());
        return ResponseEntity.ok("Depósito realizado!");
    } catch (Exception e) {
        logger.error("Erro no depósito - Conta: {}, Valor: {}", dto.numeroConta(), dto.valor(), e);
        return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
    }
}
```

### Em um Service
```java
public void transferir(TransferenciaDTO dto) {
    long inicio = System.currentTimeMillis();
    try {
        logger.info("Transferência: {} → {}", dto.contaOrigem(), dto.contaDestino());
        // ... lógica de negócio ...
        logger.info("Transferência concluída em {}ms", System.currentTimeMillis() - inicio);
    } catch (Exception e) {
        logger.error("Falha na transferência", e);
        throw e;
    }
}
```

### Em Tratamento de Erros
```java
public String validarToken(String token) {
    try {
        String subject = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
            .verify(token)
            .getSubject();
        logger.debug("Token válido para: {}", subject);
        return subject;
    } catch (JWTVerificationException e) {
        logger.warn("Token inválido recebido", e);
        return "";
    } catch (Exception e) {
        logger.error("Erro inesperado na validação de token", e);
        return "";
    }
}
```

## 🔍 Padrão de Formato

Todos os logs seguem este padrão:

```
TIMESTAMP [THREAD] LEVEL LOGGER - MENSAGEM
2026-06-07 14:32:45.123 [main] INFO  br.com.arq.service.ContaService - Depósito realizado: 500.00
```

**Componentes:**
- `TIMESTAMP`: Data e hora com milissegundos
- `THREAD`: Thread que gerou o log
- `LEVEL`: DEBUG, INFO, WARN, ERROR
- `LOGGER`: Classe que gerou o log
- `MENSAGEM`: Mensagem formatada com variáveis

## 🎛️ Níveis de Log

### DEBUG
- Usado para informações detalhadas de desenvolvimento
- **Ativo apenas em desenvolvimento**
- Exemplo: valores de variáveis, entrada/saída de métodos

```java
logger.debug("Valor processado: {}", valor);
```

### INFO
- Informações importantes do negócio
- **Ativo sempre**
- Exemplo: operações bem-sucedidas, marcos importantes

```java
logger.info("Conta criada com sucesso: {}", numeroConta);
```

### WARN
- Situações anormais mas não críticas
- **Ativo sempre**
- Exemplo: validações falhadas, tentativas inválidas

```java
logger.warn("Saldo insuficiente: {}, necessário: {}", saldoAtual, necessario);
```

### ERROR
- Erros que precisam atenção
- **Ativo sempre com stack trace**
- Exemplo: exceções, falhas de operação

```java
logger.error("Erro ao processar transação", exception);
```

## 🔧 Configuração por Pacote

No `logback-spring.xml`, você pode controlar o nível de log por pacote:

```xml
<!-- DEBUG para segurança -->
<logger name="br.com.arq.asyncsecurity" level="DEBUG" />

<!-- DEBUG para serviços -->
<logger name="br.com.arq.service" level="DEBUG" />

<!-- INFO para controllers -->
<logger name="br.com.arq.controller" level="INFO" />

<!-- WARN para frameworks externos -->
<logger name="org.springframework" level="WARN" />
```

## 🚨 Erros Comuns

### ❌ NÃO FAÇA:
```java
// Evitar System.out.println
System.out.println("Processando: " + conta);

// Evitar concatenação de strings
logger.info("Valor: " + valor + " Conta: " + conta);

// Evitar sem exception
logger.error("Erro: " + e.toString());
```

### ✅ FAÇA:
```java
// Use SLF4J
logger.info("Processando: {}", conta);

// Use placeholders {}
logger.info("Valor: {} Conta: {}", valor, conta);

// Passe a exception como parâmetro
logger.error("Erro no processamento", exception);
```

## 📊 Monitorando Logs

### Ver logs em tempo real
```bash
tail -f logs/app.log
```

### Ver erros recentes
```bash
tail -f logs/app-error.log
```

### Buscar por padrão
```bash
grep "ContaService" logs/app.log
```

## ✨ Exemplo Completo de Classe

```java
package br.com.arq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MinhaService {
    
    private static final Logger logger = LoggerFactory.getLogger(MinhaService.class);
    
    public void processarOperacao(String id, BigDecimal valor) {
        long inicio = System.currentTimeMillis();
        
        try {
            logger.info("Iniciando processamento - ID: {}, Valor: {}", id, valor);
            
            // Validações
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                logger.warn("Valor inválido recebido: {}", valor);
                throw new IllegalArgumentException("Valor deve ser positivo");
            }
            
            logger.debug("Iniciando cálculos complexos");
            // ... processamento ...
            
            logger.info("Operação concluída - ID: {}, Tempo: {}ms", id, 
                System.currentTimeMillis() - inicio);
                
        } catch (Exception e) {
            logger.error("Erro ao processar operação - ID: {}", id, e);
            throw e;
        }
    }
}
```

## 📞 Suporte
Para dúvidas sobre logging, consulte a documentação oficial:
- [SLF4J Guide](https://www.slf4j.org/manual.html)
- [Logback Guide](https://logback.qos.ch/manual/index.html)

