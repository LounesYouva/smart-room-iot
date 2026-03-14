# smart-room-iot
IoT Smart Room prototype using Arduino, FreeRTOS, Spring Boot and React with real-time monitoring dashboard.

#  Smart Room – IoT System

Prototype IoT permettant de superviser et contrôler une pièce intelligente en temps réel.

Ce projet combine **systèmes embarqués, backend Java et interface web** afin de construire une architecture IoT complète, depuis le matériel jusqu’au dashboard de supervision.

---

#  Architecture du système

Le système repose sur une architecture en plusieurs couches :

1. **Couche embarquée**
   - Arduino Mega exécutant **FreeRTOS**
   - Lecture des capteurs environnementaux
   - Détection RFID
   - Gestion des actionneurs

2. **Couche communication**
   - Communication **série USB**
   - Script Python servant de **bridge** entre Arduino et backend

3. **Backend**
   - API REST développée avec **Spring Boot**
   - Stockage des données environnementales

4. **Frontend**
   - Dashboard développé avec **React**
   - Visualisation des données en temps réel

---

#  Fonctionnalités

-  Surveillance de la température
-  Surveillance de l’humidité
-  Contrôle d’accès par badge **RFID**
-  Gestion d’actionneurs (chauffage / ventilation / lumière)
-  Dashboard de supervision temps réel
-  Historique des mesures environnementales

---

# 🧰 Stack technique

## Embedded

- Arduino Mega
- C++
- FreeRTOS

## Backend

- Java
- Spring Boot
- REST API
- Base de données H2

## Frontend

- React
- JavaScript

## Communication

- Serial communication
- Python bridge
- JSON / HTTP

## Environnement

- Linux (WSL)
- Visual Studio Code
- Postman
- Git

---


# 🔧 Installation

## Backend
```bash

cd backend-springboot
./mvnw spring-boot:run

>>>>>>>>>>>>>>>>>>>>>>>>>             Le serveur démarre sur :

                                              http://localhost:8080

## Frontend
```bash
  cd frontend-dashboard
  npm install
  npm run dev
      >>>>>>>>>>>>>>>>>>>>>>>>>>> interface accessible depuis : http://localhost:5173

## Bridge Python
```bash
  cd python-bridge
  pip install -r requirements.txt
  python arduino_to_backend.py
  python arduino_to_backend.py
