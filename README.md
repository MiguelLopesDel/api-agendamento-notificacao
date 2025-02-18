
# Api de agendamento de notificações

Projeto feito para um desafio técnico backend.


---

## Rodando localmente

### 1. Clone o projeto

Primeiro, clone o repositório do projeto:

```bash
  git clone https://github.com/MiguelLopesDel/api-agendamento-notificacao.git
```

### 2. Entre no diretório do projeto

Acesse o diretório do projeto clonado:

```bash
  cd api-agendamento-notificacao
```

### 3. Construa a imagem do Docker e Suba os containers

O projeto usa Docker para rodar os serviços (PostgreSQL e a API). Para construir a imagem da API e iniciar os containers, execute:

```bash
  docker-compose up --build
```

Isso irá iniciar dois containers:

- **PostgreSQL**: Banco de dados usado pela API.
- **API Service**: A aplicação Spring, que estará disponível na porta `8080`.

### 4. Verifique se tudo está funcionando

Após os containers subirem, verifique se os serviços estão rodando corretamente. O Docker deve exibir os logs, e a API estará disponível em:

```bash
  http://localhost:8080
```

Para confirmar que o banco de dados PostgreSQL está funcionando, o Docker Compose também configura um health check para o serviço do PostgreSQL. Caso o banco esteja pronto, a API será liberada para conexão.---

## Documentação da API

### Cria um novo agendamento

```http
  POST /agendamento
```

| Parâmetro             | Tipo                | Descrição                                                                            |
| :-------------------- | :------------------ | :---------------------------------------------------------------------------------- |
| `emailDestinatario`   | `string`            | **Obrigatório**. O e-mail do destinatário.                                          |
| `telefoneDestinatario`| `string`            | **Obrigatório**. O telefone do destinatário.                                         |
| `mensagem`            | `string`            | **Obrigatório**. A mensagem a ser enviada ao destinatário.                           |
| `dataEnvio`           | `LocalDateTime`     | **Obrigatório**. A data e hora em que a mensagem será enviada, no formato "dd-MM-yyyy HH:mm:ss". |

**Resposta de Sucesso**:

- Código: `200 OK`
- Corpo: Retorna o agendamento criado com todos os detalhes, incluindo o status da notificação.

```json
{
  "id": 1,
  "emailDestinatario": "exemplo@dominio.com",
  "telefoneDestinatario": "123456789",
  "mensagem": "Mensagem de exemplo",
  "dataEnvio": "18-02-2025 10:30:00",
  "statusNotificacao": "PENDENTE"
}
```

---

### Retorna um agendamento específico

```http
  GET /agendamento/{id}
```

| Parâmetro   | Tipo       | Descrição                                          |
| :---------- | :--------- | :------------------------------------------------- |
| `id`        | `Long`     | **Obrigatório**. O ID do agendamento que você quer. |

**Resposta de Sucesso**:

- Código: `200 OK`
- Corpo: Retorna os detalhes do agendamento especificado.

```json
{
  "id": 1,
  "emailDestinatario": "exemplo@dominio.com",
  "telefoneDestinatario": "123456789",
  "mensagem": "Mensagem de exemplo",
  "dataEnvio": "18-02-2025 10:30:00",
  "statusNotificacao": "PENDENTE"
}
```

---

### Cancela um agendamento

```http
  PUT /agendamento/{id}
```

| Parâmetro   | Tipo       | Descrição                                          |
| :---------- | :--------- | :------------------------------------------------- |
| `id`        | `Long`     | **Obrigatório**. O ID do agendamento que você quer cancelar. |

**Resposta de Sucesso**:

- Código: `200 OK`
- Corpo: Retorna um status de sucesso, sem corpo no retorno.

```json
{}
```

---
## Rodando os testes

Para rodar os testes, rode o seguinte comando

```bash
  mvn test
```

