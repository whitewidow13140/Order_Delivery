# 🛒 Petit projet de test

**Auteur :** Alex

---

## 📌 Présentation

Ce projet met en place **un environnement complet** composé de **deux applications Spring Boot** communiquant entre elles via **API REST** et **JMS**.  
Il est conçu pour servir de **support de démonstration**, couvrant :

- Développement backend & frontend (Java + Spring Boot + Thymeleaf)
- Communication inter-services (REST + JMS)
- Authentification & autorisations (Spring Security)
- Persistance (H2 en mémoire)
- Tests automatisés (Robot Framework + Selenium)
- Orchestration locale (Docker Compose)

---

## 🏗 Architecture fonctionnelle

```
[User] --> [Order Manager UI/REST] --REST--> [Delivery Tracker UI/REST]
                |                                        ^
                v                                        |
           JMS queue.orders.new  via ActiveMQ Artemis ----
```

- **Order Manager**
  - UI + API REST `/orders`
  - Création / modification / consultation de commandes
  - Émission d'un message JMS `OrderCreated` → `queue.orders.new`
  
- **Delivery Tracker**
  - Consommation JMS `queue.orders.new`
  - UI + API REST `/deliveries`
  - Suivi de livraison + mise à jour de statut
  - Notification REST vers Order Manager

---

## 🛠 Stack technique

- **Java 17**
- **Spring Boot 3.x**
- **Thymeleaf** (UI simple)
- **Spring Security**
- **ActiveMQ Artemis** (broker JMS)
- **H2 Database** (persistance en mémoire)
- **Maven**
- **Docker Compose**
- **Robot Framework + Selenium** (tests automatisés UI & API)

---

## 📂 Structure du projet

```
my-demo-project/
├── docker-compose.yml
├── order-manager/
│ ├── pom.xml
│ └── src/...
├── delivery-tracker/
│ ├── pom.xml
│ └── src/...
├── tests/
│ ├── robot/
│ │ ├── api_tests.robot
│ │ ├── ui_tests.robot
│ │ └── resources/
├── docs/
│ ├── architecture.png
│ └── functional_spec.md
└── README.md
```
---

## 🚀 Installation et démarrage

**Prérequis :**
- Docker + Docker Compose
- Java 17
- Maven

**1️⃣ Cloner le projet**

```
git clone https://github.com/TON_COMPTE/my-demo-project.git
cd my-demo-project```
```

**2️⃣ Lancer l'environnement**

> docker-compose up --build

### **Lancer les applications unitairement**

#### Lancer ActiveMQ Artemis en local :

```
docker run -it --rm \
    -e ARTEMIS_USER=admin \
    -e ARTEMIS_PASSWORD=admin \
    -p 8161:8161 -p 61616:61616 \
    quay.io/artemiscloud/activemq-artemis-broker:latest
```

#### Lancer order-manager :

```
cd order-manager
mvn spring-boot:run
```


#### Lancer delivery-tracker :

```
cd delivery-tracker
mvn spring-boot:run
```


3️⃣ Accéder aux applications

* Order Manager : http://localhost:8081

* Delivery Tracker : http://localhost:8082

* ActiveMQ Console : http://localhost:8161 (admin/admin)


4️⃣ Comptes par défaut

| Login | Mot de passe | Rôle  |
| ----- | ------------ | ----- |
| admin | admin123     | ADMIN |
| user  | user123      | USER  |




# 🧪 Lancer les tests automatisés

1️⃣ Installer les dépendances

> pip install -r tests/requirements.txt


2️⃣ Lancer les tests API & UI

> robot tests/robot


📈 Roadmap

* V1 - Base fonctionnelle (UI + API + JMS + tests)

* V2 - Passage à PostgreSQL

* V3 - Monitoring (Prometheus + Grafana)

* V4 - Pipeline CI/CD GitHub Actions

* V5 - Tests de charge (JMeter)

* V6 - Tests d’accessibilité (axe-core)