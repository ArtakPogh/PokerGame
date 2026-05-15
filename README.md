# Texas Hold'em Poker Game

A multiplayer Texas Hold'em Poker web application built with a Java Spring Boot backend and a responsive HTML/CSS/JavaScript frontend.

This project demonstrates object-oriented programming principles, REST API communication, multiplayer game logic, and full-stack web development.

Features

* Multiplayer Texas Hold'em gameplay
* REST API communication between frontend and backend
* Built using Java and Spring Boot
* Object-Oriented Programming (OOP) architecture
* Interactive frontend with HTML, CSS, and JavaScript
* Real-time game state updates
* Card dealing and betting system
* Poker hand evaluation logic
* Maven project structure

Technologies Used

### Backend Part

* Java
* Spring Boot
* Maven
* REST API

### Frontend Part

* HTML5
* CSS3
* JavaScript

---

## Project Structure

```bash
PokerGame/
│
├── .idea/                         
├── out/                           
├── target/                        
│
├── src/
│   └── main/
│       ├── java/poker/
│       │
│       │   ├── actions/
│       │   │   ├── Action.java
│       │   │   ├── BetAction.java
│       │   │   ├── CallAction.java
│       │   │   ├── CheckAction.java
│       │   │   ├── FoldAction.java
│       │   │   └── RaiseAction.java
│       │   │
│       │   ├── config/
│       │   │   └── TableConfig.java
│       │   │
│       │   ├── domain/
│       │   │   ├── enums/
│       │   │   │   ├── GamePhase.java
│       │   │   │   ├── HandRank.java
│       │   │   │   ├── Rank.java
│       │   │   │   └── Suit.java
│       │   │   │
│       │   │   ├── Card.java
│       │   │   ├── Deck.java
│       │   │   ├── GameState.java
│       │   │   ├── Hand.java
│       │   │   ├── HandEvaluation.java
│       │   │   ├── HandValue.java
│       │   │   └── Player.java
│       │   │
│       │   ├── game/
│       │   │   ├── PokerGame.java
│       │   │   ├── TablePositions.java
│       │   │   └── TurnManager.java
│       │   │
│       │   ├── session/
│       │   │   ├── GameSession.java
│       │   │   └── SessionManager.java
│       │   │
│       │   ├── web/
│       │   │   ├── controller/
│       │   │   │   ├── GameController.java
│       │   │   │   └── SessionController.java
│       │   │   │
│       │   │   └── dto/
│       │   │       ├── ActionRequest.java
│       │   │       ├── CreatePlayerRequest.java
│       │   │       ├── GameStateDTO.java
│       │   │       └── PlayerDTO.java
│       │   │
│       │   └── PokerApplication.java
│       │
│       └── resources/
│           └── static/
│               ├── index.html
│               ├── logic.js
│               └── style.css
│
├── PokerGame.iml
├── pom.xml
└── README.md
```

---

## Installation & Setup

### 1. Clone the repository

```bash
git clone https://github.com/ArtakPogh/PokerGame.git
cd PokerGame
```

### 2. Run the backend

Make sure Java and Maven are installed.

```bash
cd backend
mvn spring-boot:run
```

The backend server will start on:

```bash
http://localhost:8080
```

---
## Gameplay Overview

Players can:

* Join a poker table
* Receive hole cards
* Bet, call, raise, or fold
* Progress through poker rounds:

  * Pre-Flop
  * Flop
  * Turn
  * River
* Compete for the pot based on poker hand rankings

---

## Authors

Arthur Tigranyan
Artak Poghosyan
Narek Manukyan

* GitHub: [https://github.com/ArtakPogh](https://github.com/ArtakPogh)

---
