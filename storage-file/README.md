# Storage File

API simples para upload de arquivo (imagem) usando AWS S3 via LocalStack.

## Requisitos

- Java 17+
- Maven (ou use o wrapper `mvnw`)
- Docker (para rodar o LocalStack)
- AWS CLI (opcional, para criar bucket)

## Como rodar

1) Suba o LocalStack:

```
docker run --rm -it -p 4566:4566 -p 4571:4571 localstack/localstack
```

2) Crie o bucket no LocalStack:

```
aws --endpoint-url=http://localhost:4566 s3 mb s3://s3integration
```

3) Rode a aplicacao:

```
./mvnw spring-boot:run
```

## Configuracao

As configuracoes estao em `src/main/resources/application.properties`:

- `aws.endpoint`: `http://localhost:4566`
- `aws.bucket-name`: `s3integration`
- `aws.accesskey`: `accessKey`
- `aws.secretkey`: `secretKey`
- H2 em memoria habilitado com console

## Endpoint

Upload de PDF:

- Metodo: `POST`
- URL: `http://localhost:8080/api/v1/user/upload`
- Body (form-data):
  - `file` (file) ou `profileImageFile` (file)
  - Apenas arquivos PDF sao aceitos

Info (GET):

- Metodo: `GET`
- URL: `http://localhost:8080/api/v1/user/upload`

Buscar arquivo por nome:

- Metodo: `GET`
- URL: `http://localhost:8080/api/v1/user/file?key=seu-arquivo.pdf`

Listar arquivos:

- Metodo: `GET`
- URL: `http://localhost:8080/api/v1/user/files`
- Opcional: `prefix` para filtrar (ex.: `?prefix=doc`)

Download de arquivo:

- Metodo: `GET`
- URL: `http://localhost:8080/api/v1/user/download?key=seu-arquivo.pdf`

Exemplo com curl:

```
curl -X POST "http://localhost:8080/api/v1/user/upload" ^
  -H "Content-Type: multipart/form-data" ^
  -F "file=@documento.pdf"
```

Resposta esperada: URL do arquivo no LocalStack, por exemplo:

```
http://localhost:4566/s3integration/documento.pdf
```

## H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:storagedb`
- User: `sa`
- Password: (vazio)
