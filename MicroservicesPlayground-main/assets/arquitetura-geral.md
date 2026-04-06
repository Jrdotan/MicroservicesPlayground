# MicroServices Playground

## Arquitetura Geral

A arquitetura geral do projeto é composta por diversos microserviços, cada um responsável por uma funcionalidade específica. A seguir, estão descritos os principais componentes da arquitetura;

- **Service Discovery**: Responsável por registrar e localizar os microserviços disponíveis na arquitetura. Utilizamos o Eureka Server para esta função.
- **Edge Server**: Atua como um gateway para as requisições, roteando e balanceando a carga entre os microserviços registrados no Service Discovery. Utilizamos o Spring Cloud Gateway para esta função.
- **API**: Iremos utilizar o sub-projeto `api` para definir as interfaces e contratos de comunicação entre os microserviços, utilizando o OpenAPI para documentação.
- **Utils**: O sub-projeto `utils` será utilizado para compartilhar código comum entre os microserviços, como classes de domínio, utilitários e configurações.
- **Microserviços Específicos**: Cada microserviço é responsável por uma funcionalidade específica, como `product-service`, `review-service`, `recomendation-service`, e `product-composite-service`,  o `product-composite-service` irá orquestrar os demais 3 micro-serviços, eles serão criados posteriormente.
- **Config Server**: Responsável por centralizar as configurações dos microserviços, permitindo que eles busquem suas configurações de forma dinâmica. Utilizaremos o Spring Cloud Config Server para esta função.
- **Message Broker**: Utilizado para comunicação assíncrona entre os microserviços, permitindo que eles se comuniquem de forma desacoplada. Utilizaremos o RabbitMQ para esta função.
- **Database**: Cada microserviço terá seu próprio banco de dados, seguindo o princípio de banco de dados por serviço, garantindo a independência e escalabilidade dos microserviços.
- **Monitoring and Logging**: Utilizaremos ferramentas como Spring Boot Actuator para monitoramento e ELK Stack (Elasticsearch, Logstash, Kibana) para centralização e análise de logs.

> Ainda temos muitos outros componentes que podem ser adicionados a esta arquitetura, como autenticação e autorização, cache, entre outros, mas os mencionados acima são os principais para a construção de uma arquitetura de microserviços robusta e escalável. Os micro-serviços são criados de forma independente, mas seguem um padrão de comunicação e integração definido pela API e pelo Service Discovery, garantindo a coesão e a interoperabilidade entre eles.