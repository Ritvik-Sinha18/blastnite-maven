# Blastnite — project context

Pair project for the CBL assignment in Programming (2IP90). A 2D side-scrolling shooter
platformer: run, jump and shoot your way through a level to reach the flag.
Controls: A / D run, Space jump, left click shoot, Esc pause, R restart, M menu. Levels are loaded from text files (one char = one tile).

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
- `Main` — starts the GUI on the EDT and shows the menu.
- `ui` — `GameWindow` (JFrame with a CardLayout: menu screen and game screen), `MenuPanel` (title + Swing buttons),
  `GamePanel` (Swing Timer game loop, delta time, painting; M key returns to menu), `Hud` (hearts, score, hints, pause/win/lose overlays).
- `core` — `GameWorld` (all game state and rules; update order: input, move, enemies may shoot, collisions, remove dead, win/lose),
  `GameState` (PLAYING / PAUSED / WON / GAME_OVER), `Camera` (horizontal scrolling).
- `entity` — `Entity` (rectangle, dead flag) → `Creature` (gravity + X-then-Y collision) → `Player`, and
  `Enemy` (hit points, points, `tryShoot`) → `Walker`, `Shooter`. `Bullet` (player or enemy), `Coin`, `Flag` extend `Entity` directly.
- `level` — `Level` loads `src/main/resources/levels/level1.txt` (# block, P player, E walker, T shooter, C coin, F flag), `Platform`.
- `input` — `InputHandler`: Key Bindings + left mouse button; "held" for continuous actions, "was pressed" (cleared each tick) for one-off actions like pause.

## Conventions
- Movement in pixels/second times dt; dt capped at 0.05s.
- Entities don't read input directly; the world passes them what they need.
- Collision: move on X and resolve, then move on Y and resolve.
- Remove dead entities after iterating, never during.
- Load resources (levels, images, sounds) with getResource, never absolute paths.
  Resource files go in src/main/resources.

## Status
- Done: backlog items 1–12 (complete game: menu, one level, two enemy types, coins and score).
- Next (could-haves): 13 second level, 14 sprites & animation, 15 sound effects. Also: README polish, reflection.

## Build & run
IDE: open as a Maven project and run blastnite.Main.
Maven: mvn package && java -jar target/blastnite-1.0-SNAPSHOT.jar
No Maven: javac -d out $(find src/main/java -name "*.java") && java -cp out:src/main/resources blastnite.Main
The team uses Apache NetBeans and GitHub Desktop.
