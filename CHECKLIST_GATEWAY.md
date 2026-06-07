CHECKLIST PRATICO: IMPLEMENTAR API GATEWAY

Etapa 1: Preparacao (30 minutos)

[  ] 1. Ler GATEWAY_GUIDE.md completamente
[  ] 2. Confirmar que banco roda em 8080
[  ] 3. Testar endpoints do banco
[  ] 4. Ter Maven 3.8.1+ instalado
[  ] 5. Ter Java 17+ instalado

Verificar Banco:
    curl http://localhost:8080/api/auth/login
    Esperado: resposta de erro (login sem dados)


Etapa 2: Criar Projeto Gateway (15 minutos)

[  ] 1. Abrir terminal
[  ] 2. Executar comando Maven:
    
    mvn archetype:generate \
        -DgroupId=br.com.arq \
        -DartifactId=banco-gateway \
        -DarchetypeArtifactId=maven-archetype-quickstart \
        -DinteractiveMode=false

[  ] 3. Entrar na pasta: cd banco-gateway
[  ] 4. Editar pom.xml
[  ] 5. Adicionar dependências (copiar de GATEWAY_GUIDE.md seção "PASSO 2")

Dependências a Adicionar:
    - spring-cloud-starter-gateway
    - spring-cloud-starter-netflix-eureka-client
    - com.auth0:java-jwt
    - spring-boot-starter-logging (já vem)

[  ] 6. Executar: mvn clean install


Etapa 3: Configuracao Basica (20 minutos)

[  ] 1. Criar pasta: src/main/resources/
[  ] 2. Criar arquivo: application.yml
[  ] 3. Copiar conteúdo de GATEWAY_GUIDE.md seção "PASSO 3"
[  ] 4. Créar arquivo: logback-spring.xml
[  ] 5. Copiar conteúdo de logback-spring.xml original e adaptar para "gateway"

Estrutura esperada:
    banco-gateway/
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   │   ├── java/br/com/arq/gateway/
    │   │   │   └── BancoGatewayApplication.java
    │   │   └── resources/
    │   │       ├── application.yml
    │   │       └── logback-spring.xml
    │   └── test/


Etapa 4: Classe Principal (10 minutos)

[  ] 1. Acessar: src/main/java/br/com/arq/gateway/
[  ] 2. Editar: BancoGatewayApplication.java
[  ] 3. Copiar conteúdo de GATEWAY_GUIDE.md seção "PASSO 4"
[  ] 4. Salvar arquivo

Conteúdo esperado:
    - @SpringBootApplication
    - main() method
    - @Bean RouteLocator


Etapa 5: Criar Filters (45 minutos)

[  ] 1. Criar pasta: src/main/java/br/com/arq/gateway/filter/

[  ] 2. Criar AuthenticationFilter.java
    [  ] 2.1. Copiar de GATEWAY_GUIDE.md seção "PASSO 5"
    [  ] 2.2. Salvar na pasta filter

[  ] 3. Criar RateLimitFilter.java
    [  ] 3.1. Copiar de GATEWAY_GUIDE.md seção "PASSO 6"
    [  ] 3.2. Adicionar dependência bucket4j no pom.xml
    [  ] 3.3. Salvar na pasta filter

[  ] 4. Criar LoggingFilter.java
    [  ] 4.1. Copiar de GATEWAY_GUIDE.md seção "PASSO 7"
    [  ] 4.2. Salvar na pasta filter

Estrutura esperada:
    filter/
    ├── AuthenticationFilter.java
    ├── RateLimitFilter.java
    └── LoggingFilter.java


Etapa 6: Compilacao (10 minutos)

[  ] 1. Terminal: cd banco-gateway
[  ] 2. Executar: mvn clean package
[  ] 3. Verificar: target/banana-gateway-1.0-SNAPSHOT.jar
[  ] 4. Se erro, verificar:
    - Java version
    - Maven version
    - Dependências no pom.xml


Etapa 7: Teste Local (15 minutos)

[  ] 1. Certificar que banco roda em 8080
    Terminal 1: cd banco ; mvn spring-boot:run

[  ] 2. Iniciar gateway em outra janela
    Terminal 2: cd banco-gateway ; mvn spring-boot:run

[  ] 3. Verificar logs:
    Esperado: "Tomcat started on port 8888"

[  ] 4. Testar rota publica (login):
    Terminal 3 (novo):
    
    curl -X POST http://localhost:8888/auth/login \
        -H "Content-Type: application/json" \
        -d '{"login":"user1","senha":"pass123"}'
    
    Esperado: resposta JSON com token ou erro auth

[  ] 5. Se falhar, verificar:
    - Banco está rodando?
    - Gateway iniciou?
    - Logs dos errors?


Etapa 8: Teste com Token (20 minutos)

[  ] 1. Fazer login para obter token:
    
    RESPONSE=$(curl -s -X POST http://localhost:8888/auth/login \
        -H "Content-Type: application/json" \
        -d '{"login":"user1","senha":"pass123"}')
    
    TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
    echo "Token: $TOKEN"

[  ] 2. Testar rota protegida (com token):
    
    curl -X POST http://localhost:8888/api/usuarios/contas/deposito \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $TOKEN" \
        -d '{"numeroConta":"001","valor":100.00}'
    
    Esperado: resposta de sucesso ou erro de negocio

[  ] 3. Testar sem token (deve falhar):
    
    curl -X POST http://localhost:8888/api/usuarios/contas/deposito
    
    Esperado: 401 Unauthorized

[  ] 4. Testar rate limit (fazer 101+ requisições):
    
    for i in {1..105}; do
        curl http://localhost:8888/api/auth/login
    done
    
    Esperado: requisição 101+ retorna 429 Too Many Requests


Etapa 9: Verificar Logs (10 minutos)

[  ] 1. Acessar pasta logs do gateway:
    ls -la banco-gateway/logs/

[  ] 2. Verificar arquivos:
    [  ] gateway.log (todos)
    [  ] gateway-error.log (erros)
    [  ] gateway-debug.log (debug)

[  ] 3. Visualizar log em tempo real:
    tail -f banco-gateway/logs/gateway.log

Esperado na tela:
    2026-06-07 15:45:22 [INFO] ... Requisicao recebida
    2026-06-07 15:45:23 [DEBUG] ... Token validado
    2026-06-07 15:45:23 [INFO] ... Requisicao finalizada


Etapa 10: Deploy Basico (30 minutos)

[  ] 1. Empurrar para repositório (se usar Git):
    git add banco-gateway/
    git commit -m "Implementar API Gateway com Spring Cloud"
    git push

[  ] 2. Se usar Docker:
    [  ] Criar Dockerfile no banco-gateway/
    [  ] Exemplo:
        FROM openjdk:17-jdk-slim
        COPY target/banco-gateway-1.0-SNAPSHOT.jar app.jar
        ENTRYPOINT ["java","-jar","/app.jar"]
    
    [  ] Build: docker build -t banco-gateway:1.0 .
    [  ] Run: docker run -p 8888:8888 banco-gateway:1.0

[  ] 3. Testar em outra porta (ex: 9999):
    [  ] Modificar application.yml: server.port = 9999
    [  ] Recompilar: mvn clean package
    [  ] Testar URLs apontando para 9999


TROUBLESHOOTING

Se o gateway não inicia:
1. Verificar porta 8888 em uso:
   netstat -an | grep 8888
2. Matar processo: lsof -ti:8888 | xargs kill -9
3. Tentar porta 8888 novamente

Se recebe 502 Bad Gateway:
1. Verificar se banco está rodando em 8080
2. Verificar URL em application.yml
3. Verificar rota no RouteLocator

Se token não valida:
1. Verificar se TokenService do banco funciona
2. Implementar TokenService externo no gateway
3. Ou passar token para o banco validar

Se rate limit não funciona:
1. Verificar se bucket4j foi adicionado
2. Importar correto: import io.github.bucket4j.*
3. Reiniciar gateway


CHECKLIST DE VALIDACAO FINAL

[  ] Gateway roda em puerto 8888
[  ] Login retorna token
[  ] Rota protegida com token funciona
[  ] Rota protegida sem token retorna 401
[  ] Rate limit funciona (101+ requisições)
[  ] Logs registram todas requisições
[  ] AuthenticationFilter valida token
[  ] LoggingFilter registra tempo
[  ] RateLimitFilter bloqueia acesso
[  ] Sem System.out.println
[  ] logback-spring.xml rodando
[  ] Arquivo app.jar criado
[  ] Pode ser dockerizado


PROXIMAS OTIMIZACOES (p/ depois)

[  ] 1. Adicionar Eureka Server
[  ] 2. Implementar Circuit Breaker
[  ] 3. Adicionar Health Checks
[  ] 4. Centralizar logs em ELK
[  ] 5. Adicionar Prometheus metrics
[  ] 6. Implementar Jaeger tracing
[  ] 7. WebSocket support (se necessario)
[  ] 8. Request/response transformation


TEMPO TOTAL

Etapa 1: 30 min
Etapa 2: 15 min
Etapa 3: 20 min
Etapa 4: 10 min
Etapa 5: 45 min
Etapa 6: 10 min
Etapa 7: 15 min
Etapa 8: 20 min
Etapa 9: 10 min
Etapa 10: 30 min

TOTAL: ~205 minutos (3.5 horas)

Recomendacao: Fazer em 1 dia com pausas


DOCUMENTOS DE REFERENCIA

Consulte enquanto implementa:
- GATEWAY_GUIDE.md (código)
- STATUS.md (visão geral)
- ARQUITETURA.md (padrões)
- logback-spring.xml (logging)


PRONTO!

Após concluir este checklist, você terá:
- API Gateway funcional
- Autenticação centralizada
- Rate limiting ativo
- Logging profissional
- Pronto para producção
- Escalável e seguro

