# Product Backlog — Blastnite

A 2D side-scrolling shooter platformer in Java Swing. Run, jump and shoot your way to the flag.
Controls: A / D run, Space jump, left click shoot, Esc pause, R restart, M menu.

## Must have (target: Presentation 2, Oct 8)

| # | Name | How to Demo | Notes |
|---|---|---|---|
| 1 | Window & game loop | Launch the app. A window opens and an FPS counter shows the loop running. | **Done.** Swing Timer at ~60 ticks/s; movement uses delta time, capped so a lag spike can't move anything through a wall. |
| 2 | Run, jump & gravity | A/D moves left and right. Space jumps; the player falls back down. You can't jump again in mid-air. | **Done.** Vertical velocity plus a gravity constant each tick. A jump is only allowed while standing on something. |
| 3 | Platforms & collisions | The player lands on platforms, can't fall through them, walls block sideways movement, and jumping into a platform from below bumps your head. | **Done.** Everything is a rectangle. Move and resolve X first, then Y, so you never get stuck in corners. |
| 4 | Level from a text file | Change a character in `src/main/resources/levels/level1.txt`, restart, and the level changes. | **Done.** One character = one tile: `#` block, `P` player, `E` walker, `T` shooter, `C` coin, `F` flag. Loaded with `getResource`. |
| 5 | Scrolling camera | Walk right: the view follows the player through a level wider than the window. | **Done.** Everything is drawn shifted by the camera offset; the camera stops at the level edges. |
| 6 | Shooting | Click the left mouse button: a bullet flies in the direction the player faces and disappears when it hits a wall or has flown far enough. | **Done.** Short cooldown, so holding the mouse button fires at a steady rate instead of spamming. |
| 7 | Walker enemies | Orange enemies walk back and forth, turning at walls and at the edge of platforms. One bullet kills a walker. | **Done.** Dead enemies and bullets are marked and removed after the update loop, never during it. |
| 8 | Hearts & damage | Touch an enemy or get shot: lose a heart, bounce, and blink for a moment. Three hearts show in the corner. | **Done.** One second of invincibility after a hit, so you can't lose all hearts at once. |
| 9 | Win & game over | Reach the flag: "YOU WIN!". Lose all hearts or fall into a pit: "GAME OVER". Press R to restart. | **Done.** Restart fully resets the level. |

## Should have (target: Presentation 3, Oct 15)

| # | Name | How to Demo | Notes |
|---|---|---|---|
| 10 | Start menu & pause | Launch: a menu with Play: Easy, Play: Difficult, How to Play and Quit. Esc pauses and resumes; M on the pause or end screen goes back to the menu. | **Done.** `CardLayout` switches between the menu and the game. While paused nothing updates. |
| 11 | Shooting enemy | A purple enemy stands still, turns to face the player, and fires a slow red bullet when you are close. It takes two hits. | **Done.** `T` in the level file. Enemy bullets only hurt the player. |
| 12 | Coins & score | Collect a coin: the score next to the hearts goes up. The final score shows on the win and game over screens. | **Done.** Coin 10, walker 50, shooter 100 points. |

## Could have (if time allows)

| # | Name | How to Demo | Notes |
|---|---|---|---|
| 13 | Second level | Choose Play: Difficult: a longer level with more shooters, wider gaps with stepping stones, and taller walls. | **Done.** `level2.txt`, designed by the team and tested so every jump is possible. |
| 14 | Level select | On the menu, choose Play: Easy or Play: Difficult. The HUD shows which level you are on; R restarts that same level. | **Done.** The chosen level number is passed from the menu through `GameWindow` and `GamePanel` to `GameWorld`. |
| 15 | High scores | Finish a game with a better score: the end screen shows "NEW BEST!". Close and reopen the game: the best score for each level is still shown on the menu. | **Done.** Saved in `.blastnite-highscores.properties` in the user's home folder; if saving fails the game still works. |
| 16 | Animated menu | Launch the game: the menu shows a night sky with twinkling stars, drifting hills and a small animated scene; buttons light up when you hover over them. | **Done.** Drawn with Swing `Graphics2D`; custom rounded buttons with a coloured stripe per difficulty. |
