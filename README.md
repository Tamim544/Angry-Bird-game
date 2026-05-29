# Angry Birds Game - Improved with Box2D Physics

A fully featured, physics-driven 2D clone of the classic **Angry Birds** game, built using **Java**, the **LibGDX** framework, and **Box2D** for high-fidelity physics simulation.

This project implements realistic bird launching, structures constructed from materials with unique physical properties (Wood, Glass, Stone), destructible entities, collision-based damage calculations, level progression, and comprehensive GUI state management (Main Menu, Settings, Level Selection, Pause, Win/Loss).

---

## 🎮 Key Features & Gameplay Mechanics

### 1. Box2D Physical Simulation
*   **Realistic Physics World**: Gravity, mass, density, friction, and restitution (bounciness) are configured to replicate authentic classic gameplay.
*   **Collision Detection**: Implements a custom `GameContactListener` that dispatches and resolves contacts between birds, blocks, ground, and pigs.
*   **Contact-Impulse Damage**: Entities take damage based on the relative velocity of the impact. Light taps do nothing, while high-velocity hits damage or instantly shatter targets.

### 2. Interactive Slingshot & Launch System
*   **Elastic Drag Limit**: Click and drag the active bird from the slingshot anchor. The drag distance is capped to prevent infinite launch power.
*   **Trajectory Prediction**: Displays a dotted path representing the bird's predicted flight path under the influence of gravity before launch.
*   **Camera Tracking**: The camera automatically tracks the bird after launch, panning smoothly to follow the trajectory of destruction.
*   **Settle/Idle Detection**: The game detects when the launched bird and all moving debris come to a complete rest, prompting the next bird to load or triggering the level-end conditions.

### 3. Material-Specific Destructible Blocks
Structures are composed of blocks with distinct behaviors:
*   🪵 **Wood Blocks**: Medium weight, medium friction, and medium durability. Good general-purpose construction blocks.
*   🧪 **Glass Blocks**: Lightweight, low friction, and low durability. Extremely easy to break or shatter under impact.
*   🪨 **Stone Blocks**: Very heavy, high friction, and highly durable. Resistant to standard hits; requires high-speed impacts to break.

### 4. Interactive Screens & State Management
*   **MainMenuScreen**: Launching point with animated buttons, level selection, and settings.
*   **GameScreen**: The main gameplay area hosting the Box2D physics loops, rendering layer, and camera controls.
*   **SettingScreen / SettingsScreen2**: Configures game options like volume and audio controls.
*   **PauseScreen**: Allows players to pause the action, resume, adjust settings, restart the level, or quit.
*   **LevelEndScreen**: Triggered upon completion. Shows a success banner if all pigs are popped, or a defeat banner if you run out of birds.

---

## 🛠️ Tech Stack & Requirements

*   **Language**: Java 11 (Supports Java 11+)
*   **Game Framework**: [LibGDX](https://libgdx.com/) 1.12.1
*   **Physics Engine**: Box2D (libgdx-box2d)
*   **Build System**: Gradle 8.10.2
*   **Desktop Backend**: LWJGL3

---

## 🚀 Getting Started

### 📋 Prerequisites
Ensure you have the following installed on your system:
1. **Java Development Kit (JDK 11 or higher)**
2. A terminal with bash/zsh support (macOS/Linux) or Command Prompt/PowerShell (Windows).

### 📥 Clone the Repository
```bash
git clone https://github.com/Tamim544/Angry-Bird-game.git
cd "Angry birds game improved "
```

### ⚙️ Build and Run the Game

To launch the desktop launcher directly:
```bash
./gradlew lwjgl3:run
```

To clean and compile all code:
```bash
./gradlew clean build
```

---

## 🕹️ Game Controls

| Control | Action |
| :--- | :--- |
| **Mouse Click + Drag** | Drag the bird back from the slingshot to aim. |
| **Release Mouse Click** | Launch the bird into the structures. |
| **Esc / P** | Pause the game during a level. |
| **R** | Quickly restart the current level. |
| **N** | Advance to the next level (when level is cleared). |

---

## 📁 Project Structure

The project follows a standard multi-project Gradle layout recommended by LibGDX:

```
├── assets/                  # Shared game resources
│   ├── b.mp3                # Main background music
│   ├── b2.mp3               # Secondary background music
│   └── (sprites, fonts, and game assets)
├── core/                    # Core game code (platform-independent)
│   └── src/main/java/com/angrybirds/
│       ├── GameObject.java          # Base physics-sprite bridge class
│       ├── Bird.java                # Box2D Circular Bird class
│       ├── Pig.java                 # Pigs with velocity damage models
│       ├── Block.java               # Materials: Wood, Glass, Stone
│       ├── Slingshot.java           # Sling anchor and drag limits
│       ├── GameContactListener.java # Collision-based damage dispatcher
│       ├── Level.java               # Level builder
│       ├── LevelFactory.java        # Predefined levels (Level 1 & 2)
│       ├── GameScreen.java          # Box2D rendering, trajectory, camera
│       └── MainGame.java            # Main entry point (extends Game)
├── lwjgl3/                  # Desktop launcher (LWJGL3 backend)
│   └── src/main/java/com/angrybirds/lwjgl3/
│       └── Lwjgl3Launcher.java      # Application settings (1280x720 window)
├── UML diagram.pdf          # Project UML class diagram
└── build.gradle             # Main build script configuration
```

---

## 📐 Design Architecture

For a visual breakdown of the structural components and inheritance hierarchies utilized in this project, please refer to the **[UML diagram.pdf](file:///Users/tamimchowdhury/Angry%20birds%20game%20improved%20/UML%20diagram.pdf)** file located in the root of the repository.

---

## 👤 Contacts & Credits

*   **Developer**: [Tamim544](https://github.com/Tamim544)
*   **Email**: tamim23544@iiitd.ac.in
*   **Credits**: Inspired by the classic original Angry Birds by Rovio Entertainment.
