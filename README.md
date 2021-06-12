# rock-paper-scissors game

Implementation of game "Paper-Rock-Scissors" for production deployments.

# Tech stack

 * Java 16
 * Spring Boot
 * Gradle
 * JUnit
 * Mockito
 * Hamcrest
 * Apache Commons
 * Guava
 * Lombok
 * Micrometer
 * Docker
 * Kubernetes

# Architecture



# File structure

# Container execution

Run container application:
```
docker run -it -p 8080:8080 -p 6969:6969 denis256/paper-rock-scissors:91349
```
Interaction: 
 * over terminal
 * over tcp, with telnet `telnet localhost 6969`
 * over http, to get metrics/health status http://localhost:8080/actuator/prometheus http://localhost:8080/actuator/health


# Kubernetes

```
$ kubectl  apply -k kubernetes
configmap/app-config-5tt6t9gtfk created
service/game-service created
deployment.apps/paper-rock-scissors created
```

# License

Only for reference, distribution and/or commercial usage not allowed
