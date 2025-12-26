# lncr-ms-food-item

## Descrição

Microserviço responsável pelo gerenciamento de **Itens de Alimentação** no sistema Lanches Caieiras. Este serviço implementa as funcionalidades relacionadas ao cadastro, atualização, consulta e gerenciamento de imagens dos produtos alimentícios disponíveis no cardápio.

## Funcionalidades

### Endpoints de Itens de Alimentação (`/foodItems`)

| Método | Path | Descrição |
|--------|------|-----------|
| `POST` | `/foodItems` | Criar novo item de alimentação |
| `GET` | `/foodItems` | Listar todos os itens de alimentação |
| `GET` | `/foodItems/{foodItemId}` | Buscar item por ID |
| `GET` | `/foodItems/list/{foodItemIdList}` | Buscar itens por lista de IDs |
| `PATCH` | `/foodItems/{foodItemId}` | Atualizar parcialmente item |
| `DELETE` | `/foodItems/{foodItemId}` | Deletar item |
| `POST` | `/foodItems/{foodItemId}/image` | Adicionar imagem ao item |
| `GET` | `/foodItems/{foodItemId}/images` | Listar imagens do item |
| `DELETE` | `/foodItems/{foodItemId}/images` | Deletar todas as imagens do item |

### Endpoints de Imagens (`/foodItems/image`)

| Método | Path | Descrição |
|--------|------|-----------|
| `GET` | `/foodItems/image/{foodItemImageId}` | Buscar imagem por ID |
| `PATCH` | `/foodItems/image/{foodItemImageId}` | Atualizar imagem |
| `DELETE` | `/foodItems/image/{foodItemImageId}` | Deletar imagem |

**Parâmetros de consulta:**
- `_limit`: Limitar número de resultados
- `category`: Filtrar por categoria
- `includeImages`: Incluir dados de imagens na resposta
- `includeData`: Incluir dados binários da imagem

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- PostgreSQL
- Maven
- Cucumber (BDD)
- JUnit 5

## Sonar Quality Gate

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=11soat-f4-lanches-caieiras_lncr-ms-food-item&metric=alert_status&token=3d0fd93336381033d2446d033f98252b691f9b93)](https://sonarcloud.io/summary/new_code?id=11soat-f4-lanches-caieiras_lncr-ms-food-item)

Acesse o dashboard completo: [SonarCloud - lncr-ms-food-item](https://sonarcloud.io/project/overview?id=11soat-f4-lanches-caieiras_lncr-ms-food-item)

## Dependências

- **lncr-core** (versão 3.0) - Biblioteca com regras de negócio e entidades de domínio
- **lncr-commons** (versão 1.0) - Biblioteca comum compartilhada com configurações e utilitários

## Guia de Download e Execução

### Pré-requisitos

- **Java 21** instalado
- **Maven 3.8+** instalado
- **PostgreSQL 13+** em execução
- **Git** instalado

### Configuração do Banco de Dados

```sql
-- Criar database
CREATE DATABASE lncr_food_item;

-- Criar usuário (opcional)
CREATE USER lncr_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE lncr_food_item TO lncr_user;
```

### Variáveis de Ambiente

Crie um arquivo `.env` ou configure as seguintes variáveis de ambiente:

```bash
# Configuração do Servidor
SERVER_PORT=8080

# PostgreSQL
POSTGRES_URL=jdbc:postgresql://localhost:5432/lncr_food_item
POSTGRES_USER=lncr_user
POSTGRES_PASSWORD=your_password

# URLs da Aplicação
LNCR_INTERNAL_URL=http://localhost:8080
LNCR_EXTERNAL_URL=http://localhost:8080
```

### Download e Instalação

```bash
# Clone o repositório
git clone https://github.com/11soat-f4-lanches-caieiras/lncr-ms-food-item.git

# Entre no diretório do projeto
cd lncr-ms-food-item/fooditem

# Configure o GitHub Packages (necessário para dependências lncr-core e lncr-commons)
# Crie o arquivo ~/.m2/settings.xml com suas credenciais do GitHub

# Compile o projeto
mvn clean install

# Execute a aplicação
mvn spring-boot:run
```

### Executando com Docker

```bash
# Build da imagem
docker build -t lncr-ms-food-item:latest .

# Execute o container
docker run -p 8080:8080 \
  -e POSTGRES_URL=jdbc:postgresql://host.docker.internal:5432/lncr_food_item \
  -e POSTGRES_USER=lncr_user \
  -e POSTGRES_PASSWORD=your_password \
  -e LNCR_INTERNAL_URL=http://localhost:8080 \
  -e LNCR_EXTERNAL_URL=http://localhost:8080 \
  lncr-ms-food-item:latest
```

### Executando os Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn test -Pcoverage

# Executar apenas testes BDD
mvn test -Dcucumber.filter.tags="@bdd"
```

### Verificando a Aplicação

Após iniciar a aplicação, acesse:

- **Health Check**: http://localhost:8080/actuator/health
- **API Base**: http://localhost:8080/foodItems
