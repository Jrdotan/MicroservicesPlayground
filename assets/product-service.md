# Product Micro Service

> Devido aos imprevistos que tivemos durante a implementação do projeto, para com as versões Java/Gradle, decidi 
> criar um microservice inicialmente mais simples e já trazer o monorepo configurado para recuperarmos as aulas anteriores.
> O processo de criação e configuração de um microservice já foi abordado em detalhes nas aulas anteriores, e o foco desta seção será mais na integração do `product-service` com o Eureka Server para registro e descoberta de serviços, do que na implementação detalhada do microservice em si.

> A configuração do `product-service` é feita de forma a permitir que ele se registre no Eureka Server, e que múltiplas instâncias possam ser executadas simultaneamente sem conflitos de porta, utilizando o mecanismo de atribuição de porta aleatória do Spring Boot.

## Estrutura do Projeto

O projeto `product-service` possui as seguintes dependencias:

| Dependência                                                                         | Objetivo                                                               |
|-------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| `project(':api')`                                                                   | Módulo compartilhado para interfaces e modelos da API.                 |
| `project(':utils')`                                                                 | Módulo de utilitários compartilhados.                                  |
| `org.springframework.boot:spring-boot-h2console`                                    | Console web para o banco de dados H2.                                  |
| `org.springframework.boot:spring-boot-starter-actuator`                             | Endpoints para monitoramento e gerenciamento da aplicação.             |
| `org.springframework.boot:spring-boot-starter-data-jpa`                             | Suporte para JPA (Java Persistence API) para acesso a bancos de dados. |
| `org.springframework.boot:spring-boot-starter-validation`                           | Validação de beans usando Bean Validation.                             |
| `org.springframework.boot:spring-boot-starter-webmvc`                               | Framework web MVC do Spring.                                           |
| `org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2`                           | Geração de documentação OpenAPI com interface Swagger UI.              |
| `org.springframework.cloud:spring-cloud-starter-netflix-eureka-client`              | Cliente para registro e descoberta de serviços no Eureka.              |
| `org.projectlombok:lombok` (compileOnly)                                            | Biblioteca para geração automática de código (getters, setters, etc.). |
| `org.springframework.boot:spring-boot-devtools` (developmentOnly)                   | Ferramentas de desenvolvimento, como recarregamento automático.        |
| `com.h2database:h2` (runtimeOnly)                                                   | Banco de dados H2 em memória para desenvolvimento.                     |
| `org.postgresql:postgresql` (runtimeOnly)                                           | Driver JDBC para PostgreSQL.                                           |
| `org.projectlombok:lombok` (annotationProcessor)                                    | Processador de anotações para Lombok.                                  |
| `org.springframework.boot:spring-boot-starter-actuator-test` (testImplementation)   | Testes para o starter actuator.                                        |
| `org.springframework.boot:spring-boot-starter-data-jpa-test` (testImplementation)   | Testes para o starter data-jpa.                                        |
| `org.springframework.boot:spring-boot-starter-validation-test` (testImplementation) | Testes para o starter validation.                                      |
| `org.springframework.boot:spring-boot-starter-webmvc-test` (testImplementation)     | Testes para o starter webmvc.                                          |
| `org.junit.platform:junit-platform-launcher` (testRuntimeOnly)                      | Launcher para testes JUnit.                                            |

## Configurações

Este microsserviço está configurado para se registrar no Eureka Server, permitindo que ele seja descoberto por outros serviços na arquitetura. As configurações principais estão definidas no arquivo `application.yml` e incluem:

```yaml
spring:
  application:
    name: product-service

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
    initialInstanceInfoReplicationIntervalSeconds: 5
    registryFetchIntervalSeconds: 5
  instance:
    leaseRenewalIntervalInSeconds: 5
    leaseExpirationDurationInSeconds: 5
    instanceId: ${spring.application.name}:${random.value}

server:
  port: 0

```

> Observe que a porta do servidor está configurada para `0`, o que significa que o Spring Boot irá atribuir uma porta aleatória disponível para cada instância do microsserviço, permitindo que múltiplas instâncias sejam executadas simultaneamente sem conflitos de porta. Além disso, as configurações do Eureka Client garantem que o microsserviço se registre corretamente no Eureka Server e mantenha o seu registro atualizado.

> O instanceId é configurado para ser único para cada instância do microsserviço, utilizando o nome da aplicação e um valor aleatório, garantindo que cada instância seja identificada de forma única no Eureka Server. As configurações de leaseRenewalIntervalInSeconds e leaseExpirationDurationInSeconds são definidas para garantir que o microsserviço mantenha o seu registro ativo no Eureka Server, renovando o seu lease a cada 5 segundos e expirando após 5 segundos sem renovação.


## Testando o Microsserviço

1. Inicie o Eureka Server seguindo as instruções do [Service Discovery](assets/service-discovery.md).

### Registrando o MicroService de Clientes

#### Confira as configurações do projeto product-service

1. Valide as configurações do `application.yml` do projeto `product-service`, conforme o exemplo abaixo, para garantir que ele se registre corretamente no Eureka Server e possa ser descoberto por outros serviços na arquitetura.
```yml
spring:
   application:
      name: product-service

eureka:
   client:
      serviceUrl:
         defaultZone: http://localhost:8761/eureka/
      initialInstanceInfoReplicationIntervalSeconds: 5
      registryFetchIntervalSeconds: 5
   instance:
      leaseRenewalIntervalInSeconds: 5
      leaseExpirationDurationInSeconds: 5
      instance-id: ${spring.application.name}:${spring.application.instance_id:${ramdom.value}}

server:
   port: 0
```
2. A partir da raiz do projeto `product-service`, inicie o projeto **product-service** com os comandos abaixo.
```shell
./gradlew microservices:product-service:bootRun`
```
3. Abra 2 terminais e inicialize outras 2 instancias com o comando acima, cada um num novo terminal;
4. Verifique o EurekaServer o registro de 3 instancias do micro-service **product-service**.
5. Clique no nome do serviço para verificar os detalhes de cada instância, como o status, a porta e o endereço IP.
6. Substitua no navegador a porta do serviço pelo IP e porta de cada instância para validar que elas estão ativas, e substitua por uma requisição para validar o retorno do serviço, por exemplo:
```
http://localhost:55632/product/1
http://localhost:32675/product/2
http://localhost:47651/product/3
```

> Observe que as portas são aleatórias, então substitua pelas portas que foram atribuídas para cada instância do microsserviço. Se tudo estiver configurado corretamente, você deverá receber uma resposta válida para cada requisição, confirmando que as instâncias do microsserviço estão ativas e registradas no Eureka Server.

# Reflexão

Neste ponto, temos um microsserviço `product-service` configurado para se registrar no Eureka Server, permitindo que ele seja descoberto por outros serviços na arquitetura.

Temos 3 instancias do `product-service` registradas no Eureka Server, cada uma com um `instanceId` único, e todas as instâncias estão ativas e respondendo corretamente às requisições.

> Reflita sobre o processo de criação e configuração do microsserviço `product-service`, e como ele se integra com o Eureka Server para registro e descoberta de serviços.

## Considere os seguintes pontos:
- A importância de configurar corretamente o `application.yml` para garantir que o microsserviço se registre no Eureka Server e possa ser descoberto por outros serviços.
- O papel do `instanceId` na identificação única de cada instância do microsserviço no Eureka Server, e como isso facilita a gestão de múltiplas instâncias.
- A configuração da porta do servidor para `0`, permitindo que o Spring Boot atribua uma porta aleatória, e como isso facilita a execução de múltiplas instâncias do microsserviço sem conflitos de porta.
- A importância de validar o registro das instâncias no Eureka Server e testar as requisições para garantir que as instâncias estão ativas e respondendo corretamente.

# Edge Server

Os proximos passos serão criar o Edge Server, que irá atuar como um gateway para as requisições, roteando e balanceando a carga entre os microserviços registrados no Service Discovery. Utilizaremos o Spring Cloud Gateway para esta função. O Edge Server irá se registrar no Eureka Server, permitindo que ele seja descoberto por outros serviços na arquitetura, e irá rotear as requisições para as instâncias do `product-service` de forma balanceada, utilizando o mecanismo de round-robin do Spring Cloud Gateway.

## Próxima seção

Iremos criar o Edge Server, configurar o roteamento e balanceamento de carga, e testar o cenário para validar que as requisições estão sendo roteadas corretamente para as instâncias do `product-service`.