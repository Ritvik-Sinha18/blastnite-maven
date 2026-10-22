# Blastnite

A 2D side-scrolling shooter platformer made with Java Swing for the CBL project of
Programming (2IP90). Run, jump and blast your way through the level to reach the flag.

**Team:** Rehan Badar & [Partner name]

![Blastnite menu](docs/screenshot.png)

## Features
- Two levels to choose from on the menu: **Easy** and **Difficult**
- Running, jumping with gravity, and collisions with platforms and walls
- Levels loaded from text files, with a camera that scrolls through them
- Two kinds of enemies: walkers that patrol, and shooters that fire back
- Three hearts, coins and a score; the best score per level is saved between sessions
- Start menu, pause screen, and win / game over screens

## How to play
Reach the flag at the end of the level. Shoot enemies and collect coins for points.
Getting touched by an enemy or hit by a bullet costs a heart; lose all three, or fall into a pit, and it's game over.

| Key | Action |
|---|---|
| A / D | run left / right |
| Space | jump |
| Left mouse click | shoot (hold to keep firing) |
| Esc | pause / resume |
| R | restart the level (on the pause, win or game over screen) |
| M | back to the menu (on the pause, win or game over screen) |

| Thing | Points |
|---|---|
| Coin | 10 |
| Orange walker (1 hit) | 50 |
| Purple shooter (2 hits) | 100 |

## Requirements
Java 17 or newer (JDK). No external libraries.

## Build & run

### In an IDE (easiest)
Open the project folder in NetBeans, IntelliJ or VS Code. It is a Maven project, so the IDE
recognises it from `pom.xml`. Run `blastnite.Main`.
(In NetBeans: **File → Open Project**, select the folder, then click **Run**.)

### With Maven
    mvn package
    java -jar target/blastnite-1.0-SNAPSHOT.jar

### Without Maven (plain JDK)
macOS / Linux:

    javac -d out $(find src/main/java -name "*.java")
    java -cp out:src/main/resources blastnite.Main

Windows (PowerShell):

    javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName
    java -cp "out;src/main/resources" blastnite.Main

## High scores
The best score for each level is saved in `.blastnite-highscores.properties` in your home folder.
Delete that file to reset the scores.

## Making your own level
Levels are text files in `src/main/resources/levels/`. Every character is one 40 × 40 tile:

| Character | Meaning |
|---|---|
| `#` | solid block |
| `.` | empty air |
| `P` | player start (exactly one) |
| `F` | goal flag (exactly one) |
| `E` | walker enemy |
| `T` | shooter enemy |
| `C` | coin |

Keep levels 16 rows tall. A jump reaches about 3 blocks up and 5 blocks across.
To add a level to the menu, add its file and name to `LEVEL_FILES` and `LEVEL_NAMES` in `GameWorld`.

## Project structure
Source code is in `src/main/java/blastnite/`:

| Package | What it contains |
|---|---|
| `blastnite` | `Main`: starts the game on Swing's event dispatch thread |
| `ui` | `GameWindow` (the window, switches between menu and game), `MenuPanel`, `GamePanel` (the game loop), `Hud` |
| `core` | `GameWorld` (game state and rules), `GameState`, `Camera`, `HighScores` |
| `entity` | `Entity` → `Creature` → `Player`, `Enemy` → `Walker`, `Shooter`; plus `Bullet`, `Coin`, `Flag` |
| `level` | `Level` (reads the level files), `Platform` |
| `input` | `InputHandler` (keyboard and mouse) |

## Development
We worked with a Git branching workflow: every feature was built on its own branch and merged
into `main` through a pull request. The product backlog is in `BACKLOG.md`.
Our two advanced topics, Git branching workflows and UX & game design, are described in the reflection document.
