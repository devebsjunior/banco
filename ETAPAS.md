## Caminho do PayLoad


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

