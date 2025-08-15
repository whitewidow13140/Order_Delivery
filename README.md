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
[Order Manager] --REST--> [Delivery Tracker]
| ^
v |
JMS --> [ActiveMQ Artemis] ---
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


3️⃣ Exemple de test API (tests/robot/api_tests.robot)

```
*** Settings ***
Library           RequestsLibrary

*** Variables ***
${ORDER_MANAGER}  http://localhost:8081
${DELIVERY_TRACKER}  http://localhost:8082

*** Test Cases ***
Create Order And Verify Delivery
    Create Session    order    ${ORDER_MANAGER}
    POST On Session   order    /orders    json={"item":"Laptop","quantity":1}
    Sleep    2s
    Create Session    delivery    ${DELIVERY_TRACKER}
    ${resp}=    GET On Session    delivery    /deliveries
    Should Contain    ${resp.text}    Laptop
```