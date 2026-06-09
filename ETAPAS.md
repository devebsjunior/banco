## Caminho do PayLoad






{
"login": "12345",
"senha": "123456"
}


curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
"login": "12345",
"senha": "123456"
}'


#####################


POST /api/admin/contas

{
"numeroConta": "99999",
"senha": "123456",
"cpf": "12345678900",
"nome": "Carlos Silva",
"email": "carlos@email.com",
"saldo": 500,
"codigoAgencia": "001",
"nomeAgencia": "Banco XPTO",
"logradouro": "Rua A",
"bairro": "Centro",
"cidade": "Rio",
"estado": "RJ",
"cep": "20000-000"
}







curl -X POST http://localhost:8080/api/admin/contas \
-H "Authorization: Bearer SEU_TOKEN_AQUI" \
-H "Content-Type: application/json" \
-d '{ ...payload acima... }'



POST /api/usuarios/contas/deposito

{
"numeroConta": "99999",
"valor": 200,
"agencia": "Banco XPTO",
"numeroAgencia": "001"
}

POST /api/usuarios/contas/saque


{
"numeroConta": "99999",
"valor": 100,
"agencia": "Banco XPTO",
"numeroAgencia": "001"
}


$ curl -X POST http://localhost:8081/api/admin/contas \
-H "Content-Type: application/json" \
-d '{"nome":"Edson","cpf":"12345678999","email":"edson@email.com","numeroConta":"0001","codigoAgencia":"001","nomeAgencia":"Banco Teste","cep":"01001000","saldo":2000,"senha":"123456","logradouro":"Rua A","numero":"100","bairro":"Centro","cidade":"Sao Paulo","estado":"SP"}'
{"id":1,"agencia":{"id":1,"codigo":"001","nomeAgencia":"Banco Teste","numeroAgencia":"001"},"numeroConta":"0001","saldo":2000,"perfil":"CLIENTE","senha":"$2a$12$Hru.pwscSEnGKM/v53pav.OtYm0Q2G1XTA7gLCy3PaMWkv7QFGzqa","version":0}


POST /api/usuarios/contas/transferir

{
"contaOrigem": "99999",
"contaDestino": "88888",
"valor": 50,
"bancoOrigem": "Banco XPTO",
"agenciaOrigem": "001",
"bancoDestino": "Banco XPTO",
"agenciaDestino": "001"
}

GET /api/usuarios/contas/{numero}/extrato


curl -X GET http://localhost:8080/api/usuarios/contas/99999/extrato \
-H "Authorization: Bearer SEU_TOKEN_AQUI"


POST /clientes


{
"nome": "Maria Souza",
"cpf": "98765432100",
"email": "maria@email.com"
}

POST /usuarios

{
"username": "admin",
"email": "admin@email.com",
"senha": "123456",
"primeiroNome": "Admin",
"ultimoNome": "Sistema",
"perfil": {
"tipoPerfil": "ADMIN",
"nivelPermissao": 10
}
}

POST /api/admin/agencias


{
"numeroAgencia": "001",
"nomeAgencia": "Banco XPTO",
"logradouro": "Rua A",
"numero": "100",
"bairro": "Centro",
"cidade": "Rio",
"estado": "RJ",
"cep": "20000-000"
}




```java=


POST   /clientes                      → onboarding completo ✅
GET    /clientes/{id}                → cliente + endereco ✅
POST   /auth/login                  → login ✅

POST   /contas/deposito             → depósito ✅
POST   /contas/saque                → saque ✅
POST   /contas/transferencia        → transferência ✅
GET    /contas/{numero}/extrato     → extrato ✅

```

```java=
{
  "nome": "Edson Souza",
  "cpf": "123.456.789-00",
  "email": "edson@email.com",

  "usuario": {
    "senha": "123456",
    "primeiroNome": "Edson",
    "ultimoNome": "Souza"
  },

  "conta": {
    "numeroConta": "12345",
    "saldoInicial": 1000
  }
}
``` 

## Post Usuarios

```java=

{
  "clienteId": 1,
  "senha": "123456",
  "primeiroNome": "Edson",
  "ultimoNome": "Souza"
}
```

##  POST /auth/login

```java=

{
  "email": "edson@email.com",
  "senha": "123456"
}
```

## RESPONSE

```java=

{
  "token": "JWT_TOKEN_AQUI",
  "username": "edson@email.com",
  "perfis": ["USER"]
}
```


## POST /contas/deposito

```java=

{
  "numeroConta": "12345",
  "valor": 500.00
}

```


## SAQUE

```java=
{
  "numeroConta": "12345",
  "valor": 200.00
}
```


## Transferencia

```java=

{
  "origem": "12345",
  "destino": "67890",
  "valor": 300.00
}

```


## EXTRATO 

```java=
{
"origem": "12345",
"destino": "67890",
"valor": 300.00
}
```




## GET /contas/12345/extrato

```java=
[
  {
    "tipo": "DEPOSITO",
    "valor": 500.00,
    "dataHora": "2026-06-06T10:30:00"
  }
]

```

 
## EnderecoUsuarioConta

```json=
{
  "nome": "Edson Souza",
  "cpf": "123.456.789-00",
  "email": "edson@email.com",

  "endereco": {
    "logradouro": "Rua A",
    "numero": "123",
    "complemento": "Apto 10",
    "bairro": "Centro",
    "cidade": "Resende",
    "estado": "RJ",
    "cep": "27500-000"
  },

  "usuario": {
    "senha": "123456",
    "primeiroNome": "Edson",
    "ultimoNome": "Souza"
  },

  "conta": {
    "numeroConta": "12345",
    "saldoInicial": 1000,
    "perfil": "CORRENTE"
  }
}

----------------------



{
  "cliente": {
    "id": 1,
    "nome": "Edson Souza",
    "cpf": "123.456.789-00",
    "email": "edson@email.com",

    "endereco": {
      "logradouro": "Rua A",
      "numero": "123",
      "cidade": "Resende",
      "estado": "RJ",
      "cep": "27500-000"
    }
  },

  "usuario": {
    "id": 10,
    "username": "edson@email.com",
    "perfis": ["USER"]
  },

  "conta": {
    "numeroConta": "12345",
    "saldo": 1000.00,
    "perfil": "CORRENTE"
  }
}
 

```


GET /contas/{numero}/extrato

```java=
[
  {
    "tipo": "DEPOSITO",
    "valor": 500.00,
    "dataHora": "2026-06-06T10:30:00"
  },
  {
    "tipo": "SAQUE",
    "valor": 200.00,
    "dataHora": "2026-06-06T11:00:00"
  }
]
```


## Post CREATE USUÁRIO
```json=
{
"email": "edson@email.com",
"senha": "123456",
"primeiroNome": "Edson",
"ultimoNome": "Souza",
"clienteId": 1
}
```


## Response 
```json=
{
"id": 10,
"email": "edson@email.com",
"primeiroNome": "Edson",
"ultimoNome": "Souza"
}
```

1. Login
```json=
{
"email": "edson@email.com",
"senha": "123456"
}
```


```json=
{
  "token": "jwt-aqui",
  "usuario": {
    "id": 1,
    "username": "edson",
    "email": "edson@email.com",
    "cliente": {
      "id": 10,
      "nome": "Edson Belém",
      "cpf": "12345678900",
      "email": "edson@email.com"
    }
  }
}
```

```json=
{
"cliente": {
"id": 10,
"nome": "Edson",
"cpf": "12345678900",
"email": "edson@email.com",
"endereco": {
"cidade": "Resende"
}
}
}
```

```java=
usuario
{
  "username": "edson",
  "email": "edson@email.com",
  "senha": "123456",
  "primeiroNome": "Edson",
  "ultimoNome": "Belem",
  "clienteId": 1,
  "tipoPerfil": "CLIENTE"
}
``` 

```json=
POST /agencias
{
  "nomeAgencia": "Agencia Centro",
  "numeroAgencia": "1001",
  "logradouro": "Rua das Flores",
  "numero": "100",
  "bairro": "Centro",
  "cidade": "Volta Redonda",
  "estado": "RJ",
  "cep": "27200-000"
}
```
----------------


```json=
POST /usuarios

{
  "username": "edson",
  "email": "edson@email.com",
  "senha": "123456",
  "primeiroNome": "Edson",
  "ultimoNome": "Belem",
  "perfil": {
    "tipoPerfil": "CLIENTE",
    "nivelPermissao": 5
  }
}
```

POST /contas

```json=
{
  "nome": "Edson Belem",
  "cpf": "02295351782",
  "email": "edson@email.com",
  "numeroConta": "123456",
  "numeroAgencia": "1001",
  "perfil": "CLIENTE",
  "senha": "123456",
  "saldo": 1000.00
}
```


```
POST /contas/sacar

{
  "numeroConta": "123456",
  "valor": 200.00,
  "banco": "Banco Arq",
  "agencia": "1001"
}
```