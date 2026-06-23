
# Api de agendamento de notificações

Projeto feito para um desafio técnico backend.


---

## Rodando localmente

### Pré-requisitos

- Docker
- Docker Compose

No Linux, caso o Docker retorne erro de permissão ao acessar o daemon, execute os comandos com `sudo` ou configure seu usuário no grupo `docker`.

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

### 3. Construa a imagem do Docker e suba os containers

O projeto usa Docker para rodar os serviços (PostgreSQL e a API). Para construir a imagem da API e iniciar os containers, execute:

```bash
  docker compose up --build
```

Se a porta `8080` já estiver em uso na sua máquina, escolha outra porta local:

```bash
  API_PORT=18080 docker compose up --build
```

Isso irá iniciar dois containers:

- **PostgreSQL**: Banco de dados usado pela API.
- **API Service**: A aplicação Spring, que estará disponível na porta `8080`, ou na porta definida em `API_PORT`.

### 4. Verifique se tudo está funcionando

Após os containers subirem, verifique se os serviços estão rodando corretamente. O Docker deve exibir os logs, e a API estará disponível em:

```bash
  http://localhost:8080
```

Se você usou `API_PORT=18080`, acesse `http://localhost:18080`.

Para confirmar que o banco de dados PostgreSQL está funcionando, o Docker Compose também configura um health check para o serviço do PostgreSQL. Caso o banco esteja pronto, a API será liberada para conexão.

Para parar os containers:

```bash
  docker compose down
```

---

## Documentação da API

### Cria um novo agendamento

```http
  POST /agendamento
```

| Parâmetro             | Tipo                | Descrição                                                                            |
| :-------------------- | :------------------ | :---------------------------------------------------------------------------------- |
| `destinatario`        | `string`            | **Obrigatório**. Destinatário da comunicação, como e-mail, telefone ou identificador push. |
| `mensagem`            | `string`            | **Obrigatório**. A mensagem a ser enviada ao destinatário.                           |
| `canalNotificacao`    | `string`            | **Obrigatório**. Canal de envio: `EMAIL`, `SMS`, `PUSH` ou `WHATSAPP`.              |
| `dataEnvio`           | `LocalDateTime`     | **Obrigatório**. A data e hora em que a mensagem será enviada, no formato "dd-MM-yyyy HH:mm:ss". |

**Exemplo de requisição**:

```bash
curl -X POST http://localhost:8080/agendamento \
  -H "Content-Type: application/json" \
  -d '{
    "destinatario": "exemplo@dominio.com",
    "mensagem": "Mensagem de exemplo",
    "canalNotificacao": "EMAIL",
    "dataEnvio": "18-02-2030 10:30:00"
  }'
```

**Resposta de Sucesso**:

- Código: `200 OK`
- Corpo: Retorna o agendamento criado com todos os detalhes, incluindo o status da notificação.

```json
{
  "id": 1,
  "destinatario": "exemplo@dominio.com",
  "emailDestinatario": null,
  "telefoneDestinatario": null,
  "mensagem": "Mensagem de exemplo",
  "canalNotificacao": "EMAIL",
  "dataEnvio": "18-02-2030 10:30:00",
  "statusNotificacao": "AGENDADO"
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

**Exemplo de requisição**:

```bash
curl http://localhost:8080/agendamento/1
```

```json
{
  "id": 1,
  "destinatario": "exemplo@dominio.com",
  "emailDestinatario": null,
  "telefoneDestinatario": null,
  "mensagem": "Mensagem de exemplo",
  "canalNotificacao": "EMAIL",
  "dataEnvio": "18-02-2030 10:30:00",
  "statusNotificacao": "AGENDADO"
}
```

---

### Cancela um agendamento

```http
  DELETE /agendamento/{id}
```

| Parâmetro   | Tipo       | Descrição                                          |
| :---------- | :--------- | :------------------------------------------------- |
| `id`        | `Long`     | **Obrigatório**. O ID do agendamento que você quer cancelar. |

**Resposta de Sucesso**:

- Código: `204 No Content`

**Exemplo de requisição**:

```bash
curl -X DELETE http://localhost:8080/agendamento/1
```

---
## Rodando os testes

Para rodar os testes, rode o seguinte comando

```bash
  ./mvnw test
```
