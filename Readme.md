# Tic Tac Toe - Low Level Design in Java

## 📌 Overview

This project is a console-based implementation of the classic **Tic Tac Toe** game using **Java**, with a strong focus on **object-oriented programming (OOP)** and **low-level design (LLD)** principles.

The application supports two human players playing against each other on a 3x3 board, with scalable design to support future enhancements like AI players or dynamic board sizes.

---

## 🧠 Design Principles

The project demonstrates key software design concepts:

- **Single Responsibility Principle**
- **Open/Closed Principle**
- **Encapsulation and Abstraction**
- **Strategy Design Pattern** (used for move strategies)
- **State Design Pattern** (used for managing game states)

---

## 🧱 Class Structure

### 1. `Player`
Represents a player in the game.
- Attributes: `id`, `name`, `symbol`, and `strategy`.
- Uses Strategy Pattern via `IPlayerMoveStrategy`.

### 2. `GameManager`
Controls the game loop and manages turn-switching, game start, and end-state display.

### 3. `Board`
Handles the grid logic, validating and placing moves, checking for a winner or draw.

### 4. `Positon`
A simple class to represent coordinates `(x, y)` on the board.

### 5. `Symbol`
An enum for `X`, `O`, and `EMPTY`.

### 6. `IPlayerMoveStrategy` & `HumanePlayerMovingStrategy`
Defines and implements the move behavior for human players. Can be extended for AI strategies.

### 7. `IGameState` and State Implementations
Implements the **State Pattern** to track the current game state:
- `XPlayerState`
- `YPlayerState`
- `XWonState`
- `YWonState`
- `DrawState`

### 8. `GameContext`
Maintains the current state and facilitates transitions.

---

## ▶️ How to Run

Ensure you have Java installed. Compile and run using:

```bash
javac Main.java
java Main

