# 🔧 TEMPLATES DE LOGGING - COPIAR E COLAR

Modelos prontos para usar em suas classes. Basta copiar e adaptar!

---

## 1️⃣ Template para SERVICE

```java
package br.com.arq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeuService {
    
    private static final Logger logger = LoggerFactory.getLogger(MeuService.class);
    
    public void meuMetodo(String parametro) {
        long inicio = System.currentTimeMillis();
        
        try {
            logger.info("Iniciando operação - Parâmetro: {}", parametro);
            
            // Sua lógica aqui
            
            logger.info("Operação concluída em {}ms", System.currentTimeMillis() - inicio);
            
        } catch (Exception e) {
            logger.error("Erro ao executar operação - Parâmetro: {}, Tempo: {}ms", 
                paramametro, System.currentTimeMillis() - inicio, e);
            throw e;
        }
    }
}
```

---

## 2️⃣ Template para CONTROLLER

```java
package br.com.arq.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/meu-recurso")
@RequiredArgsConstructor
public class MeuController {
    
    private static final Logger logger = LoggerFactory.getLogger(MeuController.class);
    
    private final MeuService meuService;
    
    @PostMapping
    public ResponseEntity<String> criar(@RequestBody MeuDTO dto) {
        try {
            logger.info("Criando recurso - ID: {}", dto.id());
            
            meuService.criar(dto);
            
            logger.info("Recurso criado com sucesso - ID: {}", dto.id());
            return ResponseEntity.ok("Criado com sucesso!");
            
        } catch (RuntimeException e) {
            logger.error("Erro ao criar recurso - ID: {}, Motivo: {}", dto.id(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar recurso - ID: {}", dto.id(), e);
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MeuDTO> obter(@PathVariable Long id) {
        try {
            logger.debug("Buscando recurso - ID: {}", id);
            
            MeuDTO resultado = meuService.obter(id);
            
            logger.debug("Recurso encontrado - ID: {}", id);
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            logger.error("Erro ao buscar recurso - ID: {}", id, e);
            throw e;
        }
    }
}
```

---

## 3️⃣ Template para REPOSITORY

```java
package br.com.arq.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeuRepository extends JpaRepository<MeuEntity, Long> {
    
    // Os logs do Spring Data são gerenciados automaticamente
    // Você pode configurar em logback-spring.xml:
    // <logger name="org.hibernate" level="DEBUG" />
}
```

---

## 4️⃣ Template para SECURITY/AUTH

```java
package br.com.arq.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeuAuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(MeuAuthService.class);
    
    public void autenticar(String usuario, String senha) {
        try {
            logger.info("Tentativa de autenticação - Usuário: {}", usuario);
            
            // Validar entrada
            if (usuario == null || usuario.isEmpty()) {
                logger.warn("Autenticação falhou - usuário vazio");
                throw new RuntimeException("Usuário inválido");
            }
            
            // Sua lógica de autenticação
            
            logger.info("Autenticação bem-sucedida - Usuário: {}", usuario);
            
        } catch (RuntimeException e) {
            logger.error("Falha na autenticação - Usuário: {}, Motivo: {}", usuario, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado na autenticação - Usuário: {}", usuario, e);
            throw new RuntimeException("Erro ao autenticar", e);
        }
    }
}
```

---

## 5️⃣ Template para EXCEPTION HANDLER

```java
package br.com.arq.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MeuExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(MeuExceptionHandler.class);
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRuntime(RuntimeException e) {
        logger.error("Erro de negócio: {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneral(Exception e) {
        logger.error("Erro inesperado", e);
        return ResponseEntity.internalServerError()
            .body(new ErrorDTO("Erro interno do servidor"));
    }
}
```

---

## 6️⃣ Níveis de Log - Quando Usar

```java
// DEBUG - Detalhes de desenvolvimento
// Use para: entrada/saída de métodos, valores de variáveis
logger.debug("Valor recebido: {} Tipo: {}", valor, tipo);

// INFO - Operações importantes
// Use para: sucesso de operações, marcos importantes
logger.info("Conta criada com sucesso: {}", numeroConta);

// WARN - Situações anormais
// Use para: validações falhadas, valores estranhos
logger.warn("Saldo baixo na conta {}, Valor: {}", conta, saldo);

// ERROR - Erros críticos com exception
// Use para: exceções, operações que falharam
logger.error("Erro ao processar transação", exception);
logger.error("Erro ao buscar conta: {}", id, exception);
```

---

## 7️⃣ Exemplo Real: Transferência Bancária

```java
@Transactional
public void transferir(String contaOrigem, String contaDestino, BigDecimal valor) {
    long inicio = System.currentTimeMillis();
    
    try {
        logger.info("Iniciando transferência - De: {} Para: {}, Valor: {}", 
            contaOrigem, contaDestino, valor);
        
        // Validações
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Valor inválido para transferência: {}", valor);
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        
        logger.debug("Validação de contas");
        Conta origem = obterConta(contaOrigem);
        Conta destino = obterConta(contaDestino);
        
        // Verificação de saldo
        if (origem.getSaldo().compareTo(valor) < 0) {
            logger.warn("Saldo insuficiente - Conta: {}, Saldo: {}, Solicitado: {}", 
                contaOrigem, origem.getSaldo(), valor);
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
        
        logger.debug("Processando transferência");
        origem.debitar(valor);
        destino.creditar(valor);
        
        salvarContas(origem, destino);
        
        long duracao = System.currentTimeMillis() - inicio;
        logger.info("Transferência concluída - De: {} Para: {}, Valor: {}, Duração: {}ms", 
            contaOrigem, contaDestino, valor, duracao);
        
    } catch (SaldoInsuficienteException e) {
        logger.error("Saldo insuficiente para transferência - De: {} Para: {}", 
            contaOrigem, contaDestino, e);
        throw e;
        
    } catch (Exception e) {
        long duracao = System.currentTimeMillis() - inicio;
        logger.error("Erro ao processar transferência - De: {} Para: {}, Duração: {}ms", 
            contaOrigem, contaDestino, duracao, e);
        throw e;
    }
}
```

---

## 8️⃣ Padrão Recomendado

**Toda classe deve ter:**

```java
// 1. Import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 2. Logger estático
private static final Logger logger = LoggerFactory.getLogger(SuaClasse.class);

// 3. Usar em MÉTODOs PÚBLICOS
public void metodo() {
    try {
        logger.info("Iniciando...");
        // seu código
        logger.info("Concluído!");
    } catch (Exception e) {
        logger.error("Erro", e);
        throw e;
    }
}

// 4. DEBUG em MÉTODOS PRIVADOS (opcional)
private void metodoInterno() {
    logger.debug("Detalhes internos");
}
```

---

## 9️⃣ Erros Comuns - NÃO FAÇA

```java
// ❌ ERRADO - System.out.println
System.out.println("Processando " + conta);

// ❌ ERRADO - Concatenação de strings
logger.info("Valor: " + valor + " tipo: " + tipo);

// ❌ ERRADO - Exception sem passar
logger.error("Erro: " + e.toString());

// ❌ ERRADO - Sem contexto
logger.info("OK");

// ❌ ERRADO - Lombok (para este projeto)
@Slf4j
public class Classe { }
```

---

## 🔟 Certo - FAÇA

```java
// ✅ CORRETO - SLF4J
logger.info("Processando conta: {}", conta);

// ✅ CORRETO - Placeholders
logger.info("Valor: {} tipo: {}", valor, tipo);

// ✅ CORRETO - Com exception
logger.error("Erro ao processar", exception);

// ✅ CORRETO - Com contexto
logger.info("Depósito processado - Conta: {}, Valor: {}", conta, valor);

// ✅ CORRETO - SLF4J LoggerFactory
private static final Logger logger = LoggerFactory.getLogger(Classe.class);
```

---

## 🎯 Quick Start - 3 Passos

**1. Copiar imports:**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```

**2. Adicionar logger:**
```java
private static final Logger logger = LoggerFactory.getLogger(MeuaClasse.class);
```

**3. Usar:**
```java
logger.info("Operação: {}", descricao);
```

**Pronto!** Logs aparecem em `./logs/` 🎉

---

**Dúvidas?** Consulte:
- `LOGGING_GUIDE.md` - Documentação completa
- `LOGGING_SUMMARY.md` - Resumo de mudanças
- Exemplos de classes já modificadas:
  - TokenService.java
  - ContaController.java
  - AuthService.java

