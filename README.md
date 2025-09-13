# 🚀 OpenTelemetry & Jaeger with Spring Boot

This is a sample microservices setup demonstrating observability using **OpenTelemetry** and **Jaeger** with a Spring Boot architecture.

The goal is to show how to enable tracing across multiple services, visualize those traces via Jaeger, and use Docker Compose for a quick local environment.

---

## 🧩 Architecture & Components

This repo contains several microservices + support infrastructure:

- **service-registry** — Eureka / Consul or similar for service discovery  
- **configserver** — Central configuration server for all services  
- **api-gateway** — Entry point (REST API) routing to downstream services  
- **inventory-service** — Handles inventory related operations  
- **order-service** — Manages orders  
- **cart-service** — Shopping cart logic  
- (Possibly more, depending on the setup)  

Plus:

- `docker-compose.yaml` to bring up Jaeger.
- Distributed tracing setup using **OpenTelemetry** to send trace data to **Jaeger**  

---

## ⚙️ Prerequisites

Make sure you have the following installed:

- Java 17 (or whichever version the services are using)  
- Maven / Gradle (depending on how the projects are built)  
- Docker & Docker Compose  
- Git  

---

## 🚀 Getting Started

These steps will get the project up and running locally.

1. **Clone the repo**

   ```bash
   git clone https://github.com/moshclouds/OpenTelemetry-And-Jaeger-With-Springboot.git
   cd OpenTelemetry-And-Jaeger-With-Springboot

2. **Build the services**

   For each microservice, you might run something like:

   ```bash
   cd <service-folder>
   mvn clean package   # or ./gradlew build
   ```

3. **Start everything with Docker Compose**

   ```bash
   docker-compose up --build
   ```

   This will bring up:

   * Jaeger (UI + collector)
   * All microservices
   * Service registry
   * Config server
   * Gateway

4. **Verify**

   * Go to Jaeger UI, usually at: `http://localhost:16686`
   * Make some requests to the API Gateway or services via e.g. `curl` or Postman
   * Observe traces appearing in Jaeger UI

---

## 🔧 Configuration

Some key configuration items:

* **application.properties / application.yml** in each service must have:

  * Service name (e.g. `spring.application.name`)
  * OpenTelemetry exporter endpoint (e.g. OTLP or Jaeger collector)
  * Sampling probability (e.g. send all vs some traces)

* **Docker Compose**:

  * Ports for Jaeger UI (e.g. 16686)
  * Ports for OTLP / gRPC if needed
  * Dependencies between services so they wait for registry/config server

---

## 📂 File Structure

Here’s roughly how the repo is organized:

```
/
├── api-gateway/
├── cart-service/
├── inventory-service/
├── order-service/
├── configserver/
├── service-registry/
├── docker-compose.yaml
├── README.md
└── LICENSE
```

Each folder represents a Spring Boot service with its source, configs, etc.

---

## 🧪 Usage & Examples

After everything is up:

* Hit endpoints through the `api-gateway` (for example: `GET /orders/{id}`, or whatever the exposed endpoints are)
* Watch how the trace spans propagate through `api-gateway → order-service → inventory-service` (or similar flow)
* In Jaeger, you should see a trace UI showing each span, timings, response times, etc.

<br/>

<img width="1891" height="878" alt="Image" src="https://github.com/user-attachments/assets/cd86a75a-6203-41cb-b85d-10ad98da7784" /> <br/>

<img width="1913" height="831" alt="Image" src="https://github.com/user-attachments/assets/dcac8657-8999-4c8a-a03f-e82102221eed" /> <br/>

<img width="1813" height="905" alt="Image" src="https://github.com/user-attachments/assets/a1de0bca-1b8e-4eed-b8bf-3379b9e6540c" /> <br/>

<img width="1225" height="335" alt="Image" src="https://github.com/user-attachments/assets/13e289b1-b817-4726-8b5c-c9225ef2c6aa" /> 

<br/>

---

## 💡 Tips & Best Practices

* Use high sampling probability during development; lower it in production to reduce overhead
* Tag spans with useful metadata (HTTP method, status code, service names)
* Use correlation IDs in your logs so that logs + traces together help diagnose issues
* Secure your Jaeger / OTLP endpoints if used in non-local environments

---

## 🛠️ Troubleshooting

| Problem                                | Possible Causes                                              | Solutions                                      |
| -------------------------------------- | ------------------------------------------------------------ | ---------------------------------------------- |
| No traces showing in Jaeger            | Exporter misconfigured or wrong endpoint                     | Check `application.yml` / OTLP / jaeger config |
| Services not registering with registry | Registry address wrong or service not started after registry | Ensure service-registry is up and reachable    |
| Docker ports conflict                  | Another service using same port                              | Modify ports in `docker-compose.yaml`          |

---

## 📜 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.

---

## 🙌 Acknowledgements

* OpenTelemetry community
* Jaeger project
* Spring Boot / Spring Cloud

---

*If you have suggestions or improvements, pull requests are very welcome!* 💬

