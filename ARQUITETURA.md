ARQUITETURA DO PROJETO BANCO

Nome da Arquitetura: Microserviços com API Gateway Pattern

CAMADAS ARQUITETURAIS

1. API Gateway (a implementar)
   - Entrada única para requisições
   - Roteamento para microsserviços
   - Autenticação centralizada
   - Rate limiting
   - Load balancing

2. Camada de Autenticacao (IMPLEMENTATO)
   - JWT Token Service
   - AuthController (/api/auth/login)
   - AuthService (validação de credenciais)
   - AccessControlInterceptor (proteção de rotas)

3. Camada Web Configuration (IMPLEMENTADA)
   - WebConfig (CORS, Interceptors)
   - Configuracao de seguranca

4. Camada de Negocio (IMPLEMENTADA)
   - ContaController (/api/usuarios/contas/*)
   - ContaService (logica de contas)
   - AuthService (autenticacao)

5. Camada de Persistencia (IMPLEMENTADA)
   - ContaRepository
   - TransacaoRepository
   - ClienteRepository
   - UsuarioRepository

6. Camada de Auditoria (IMPLEMENTADA)
   - AuditService
   - AppLogService
   - AppLog entity

STACK TECNOLOGICO

Backend:
- Spring Boot 3.x
- Spring MVC (REST API)
- Spring Data JPA (ORM)
- Spring Security (autenticacao)
- JWT (auth tokens)
- Logback (logging)
- SLF4J (logging facade)
- JUnit/Mockito (testes)

Banco de Dados:
- Qualquer relacional (H2, PostgreSQL, MySQL)

Frontend:
- Angular ou similar em http://localhost:4200

FLUXO DE REQUISICAO ATUAL

Cliente HTTP (Frontend)
    |
    v
WebConfig (CORS)
    |
    v
AccessControlInterceptor (token + sessao)
    |
    v
Controllers
    |
    v
Services
    |
    v
Repositories
    |
    v
Banco de Dados


PROXIMA ETAPA: API GATEWAY

Com o gateway, o fluxo sera:

Cliente HTTP
    |
    v
API GATEWAY (Spring Cloud Gateway)
    |--- Roteamento
    |--- Rate Limiting
    |--- Load Balancing
    |--- Log Centralizado
    |
    v
Microsservico BANCO (porta 8080)
    |
    v
WebConfig (CORS)
    |
    v
AccessControlInterceptor

PADROES DE DESIGN IMPLEMENTADOS

1. Model-View-Controller (MVC)
   - Controllers: Camada de apresentacao
   - Services: Logica de negocio
   - Repositories: Persistencia

2. Dependency Injection
   - Spring @Autowired
   - Lombok @RequiredArgsConstructor

3. Token-based Authentication
   - JWT tokens
   - Stateless

4. Interceptor Pattern
   - AccessControlInterceptor
   - Validacao centralizada

5. Repository Pattern
   - Data access abstraction
   - Spring Data JPA

6. Exception Handling
   - GlobalExceptionHandler
   - Custom exceptions

7. Data Transfer Object (DTO)
   - Separacao de entidades
   - API contracts

SEGURANCA

Autenticacao:
- JWT Token com HMAC256
- Expiracao: configuravel

Autorizacao:
- AccessControlInterceptor valida token
- Protege todas as rotas /api/**
- Rotas publicas: /auth/**, /error

CORS:
- Origem: http://localhost:4200
- Metodos: GET, POST, PUT, DELETE, OPTIONS
- Headers: *
- Credentials: true

Validacao de sessao via UUID

LOGGING

SLF4J + Logback
Arquivos separados por nivel:
- app.log (todos)
- app-debug.log (DEBUG)
- app-info.log (INFO)
- app-warn.log (WARN)
- app-error.log (ERROR)

Rotacao: diaria + tamanho (100MB)
Retencao: 7-30 dias

NAMESPACES DO PROJETO

br.com.arq
├── asyncsecurity (autenticacao e seguranca)
│   ├── AuthController
│   ├── AuthService
│   ├── config/
│   │   └── WebConfig
│   ├── interceptor/
│   │   └── AccessControlInterceptor
│   ├── security/
│   │   └── TokenService
│   └── session/
│
├── controller (endpoints REST)
│   ├── AdminController
│   └── ContaController
│
├── service (logica de negocio)
│   ├── AppLogService
│   ├── AuditService
│   └── ContaService
│
├── model (entidades JPA)
│   ├── Cliente
│   ├── Conta
│   ├── Endereco
│   ├── Perfil
│   ├── Transacao
│   └── Usuario
│
├── repository (data access)
│   ├── AppLogRepository
│   ├── AuditRepository
│   ├── ClienteRepository
│   ├── ContaRepository
│   ├── TransacaoRepository
│   └── UsuarioRepository
│
├── dto (contracts de API)
│   ├── ContaDTO
│   ├── LoginRequestDTO
│   ├── TransacaoDTO
│   ├── TransferenciaDTO
│   └── ...
│
├── exception (tratamento de erros)
│   ├── BusinessException
│   ├── ContaNaoEncontradaException
│   ├── GlobalExceptionHandler
│   └── ...
│
├── enums
│   ├── TipoPerfil
│   └── TipoTransacao
│
├── mapper (transformacoes)
│   ├── ContaMapper
│   └── TransacaoMapper
│
├── utils (auxiliares)
│   ├── RequestUtils
│   └── TimeUtils
│
├── rules (engine de regras)
│   ├── core/
│   │   └── RuleEngine
│   └── audit (auditoria)
│       └── Audit
│
└── infraestructure (infrastructure)
    └── saga (orquestracao)


ENDPOINTS IMPLEMENTADOS

Autenticacao:
POST /api/auth/login

Contas (protegido):
POST /api/usuarios/contas/deposito
POST /api/usuarios/contas/saque
POST /api/usuarios/contas/transferir
GET  /api/usuarios/contas/{numero}/extrato
GET  /api/usuarios/contas/{numero}

Admin:
GET  /api/usuarios/contas/admin/usuarios


PROXIMAS MELHORIAS

1. API Gateway (URGENTE)
   - Spring Cloud Gateway
   - Roteamento intelligente
   - Rate limiting

2. Microsserviços separados
   - Servico de Autenticacao
   - Servico de Transacoes
   - Servico de Usuarios

3. Circuit Breaker
   - Resilience4j
   - Fallback strategies

4. Caching
   - Redis
   - Cache distribuido

5. Message Broker
   - RabbitMQ ou Kafka
   - Processamento assincrono

6. Observabilidade
   - Prometheus (metricas)
   - Jaeger (distributed tracing)
   - ELK Stack (logs centralizados)

7. Testes
   - Testes de integracao
   - Testes de carga

8. CI/CD
   - GitHub Actions
   - Deploy automatizado

9. Documentacao API
   - Swagger/OpenAPI

10. Containerizacao
    - Docker
    - Docker Compose
    - Kubernetes (futura)


RECOMENDACOES PROFISSIONAIS

1. Separar responsabilidades por microsservico
2. Implementar API Gateway como ponto de entrada
3. Usar rate limiting no gateway
4. Centralizar logging
5. Implementar circuit breaker
6. Adicionar metricas
7. Configurar alerts
8. Documentar API
9. Testes automaticos
10. CI/CD pipeline


PROXIMOS PASSOS

1. CRIAR API GATEWAY
   - Spring Cloud Gateway
   - Roteamento: /:version/**
   - Autenticacao centralizada
   - Rate limiting: 100 req/min

2. CONFIGURAR EUREKA SERVER (service discovery)
   - Registro automatico de servicos
   - Load balancing

3. IMPLEMENTAR CIRCUIT BREAKER
   - Resilience4j
   - Protecion contra falhas

4. CENTRALIZAR LOGS
   - ELK Stack
   - Jaeger para tracing

5. DOCUMENTACAO
   - Swagger
   - Architecture Decision Records


STATUS ATUAL: PRONTO PARA GATEWAY

A arquitetura esta preparada para escalabilidade.
Proximo passo: Implementar API Gateway.

Recomendado: Spring Cloud Gateway

