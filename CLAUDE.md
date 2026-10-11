# Blastnite — project context

Pair project for the CBL assignment in Programming (2IP90). A 2D side-scrolling shooter
platformer: run, jump and shoot your way through a level to reach the flag.
Controls: A / D run, Space jump, J shoot, R restart. Levels are loaded from text files (one char = one tile).

## Hard requirements (from the assignment)
- Java Swing GUI only. No external libraries: it must build and run on Windows, macOS
  and Linux, and be usable within 1 minute of launch with only the README instructions.
- Clear object-oriented design and modular architecture (this is graded).
- Two independently researched advanced topics. Topic 1: Git/GitHub branching workflow
  (branch per feature, pull requests, code review). Topic 2: to be confirmed with the tutor.
- Final hand-in (Fri Oct 23, 23:59): runnable project + source, README.md,
  final BACKLOG.md, 2-page PDF reflection on the two topics.

## Working style
- Keep it simple: this is meant to be a light, basic game. Don't add features beyond BACKLOG.md.
- Both team members must be able to explain every design decision to their tutor.
  When making changes, explain *why*, keep steps small, and prefer letting us write
  code with review over generating large chunks at once.
- Work backlog item by item. Each item must be demoable on its own.
- Use a Git branch per backlog item and merge through a pull request (this is topic 1).

## Architecture (Maven project; packages under src/main/java/blastnite)
- `Main` — starts the GUI on the EDT.
- `ui` — `GameWindow` (JFrame), `GamePanel` (Swing Timer game loop, delta time, painting), `Hud` (hearts, controls hint, win/game-over overlay).
- `core` — `GameWorld` (all game state and rules; update order: input, move, collisions, remove dead, win/lose),
  `GameState` (PLAYING / WON / GAME_OVER), `Camera` (horizontal scrolling).
- `entity` — `Entity` (rectangle, dead flag) → `Creature` (gravity + X-then-Y collision) → `Player`, `Walker`;
  `Bullet` and `Flag` extend `Entity` directly.
- `level` — `Level` loads `src/main/resources/levels/level1.txt` (# block, P player, E walker, F flag), `Platform` (solid rectangle).
- `input` — `InputHandler`: Key Bindings (not KeyListener), tracks held keys.

## Conventions
- Movement in pixels/second times dt; dt capped at 0.05s.
- Entities don't read input directly; the world passes them what they need.
- Collision: move on X and resolve, then move on Y and resolve.
- Remove dead entities after iterating, never during.
- Load resources (levels, images, sounds) with getResource, never absolute paths.
  Resource files go in src/main/resources.

## Status
- Done: backlog items 1–9 (one complete, playable level).
- Next: item 10 (start menu & pause), 11 (shooting enemy), 12 (coins & score).

## Build & run
IDE: open as a Maven project and run blastnite.Main.
Maven: mvn package && java -jar target/blastnite-1.0-SNAPSHOT.jar
No Maven: javac -d out $(find src/main/java -name "*.java") && java -cp out:src/main/resources blastnite.Main
The team uses Apache NetBeans and GitHub Desktop.
