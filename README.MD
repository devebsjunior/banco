# 🏦 Banco CastGroup API

Bem-vindo ao projeto **Banco**, uma API robusta desenvolvida em Spring Boot para gerenciamento de contas bancárias, clientes e transações. Este projeto foi desenhado com foco em segurança, escalabilidade e testabilidade.

## 🚀 Tecnologias Utilizadas

* **Java 21**: Versão estável mais recente com as últimas melhorias de performance.
* **Spring Boot 3.2.2**: Framework base para construção da API.
* **Spring Data JPA**: Abstração de banco de dados para persistência.
* **MySQL**: Banco de dados relacional utilizado em ambiente de produção/desenvolvimento.
* **H2 Database**: Banco de dados em memória utilizado para garantir a integridade dos testes.
* **Lombok**: Para redução de código boilerplate (Getters, Setters, Builders).
* **Swagger/OpenAPI**: Documentação interativa de todos os endpoints.
* **Java Dotenv**: Gerenciamento de variáveis de ambiente seguras.

## 🛠️ Configuração do Ambiente

### Variáveis de Ambiente
O projeto utiliza um arquivo `.env` para proteger dados sensíveis. Crie um arquivo `.env` na raiz do projeto com as seguintes chaves:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=banco
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

### Banco de Dados (Produção)
Certifique-se de que o MySQL está rodando e que o schema definido em DB_NAME foi criado.

## 🧪 Testes e Qualidade
O projeto conta com uma suíte de 42 testes automatizados cobrindo Repositories, Services e Controllers.

Para rodar os testes utilizando o perfil de H2 (Banco em memória), utilize o comando:

```
mvn clean test
```
Nota: Os testes utilizam @ActiveProfiles("test") para isolar o ambiente de produção do ambiente de testes, garantindo que o banco real nunca seja afetado.

## 📖 Documentação da API (Swagger)
Com a aplicação rodando, você pode acessar a documentação interativa e testar os endpoints em:
```
http://localhost:8080/swagger-ui/index.html
```
### Principais Endpoints:
- Auth: /api/auth/login (Autenticação de usuários)
- Admin: /api/admin/contas (Criação e listagem de contas)
- Usuário: /api/usuario/transacoes (Saques, depósitos e transferências)

## 📦 Como Rodar o Projeto
1. Clone o repositório:

```
git clone [https://github.com/devebsjunior/banco.git](https://github.com/devebsjunior/banco.git)
```
2. Instale as dependências:
```
mvn clean install
```

3. Inicie a aplicação:

```
mvn spring-boot:run
```

## ✒️ Autor
Edson Belem de Souza Junior - Desenvolvimento e Arquitetura - devebsjunior
