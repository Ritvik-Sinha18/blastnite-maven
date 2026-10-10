# Product Backlog — Blastnite

A 2D side-scrolling shooter platformer in Java Swing. Run, jump and shoot your way to the flag.
Controls: A / D run, W jump, Space shoot.

## Must have (target: Presentation 2, Oct 8)

| # | Name | How to Demo | Notes |
|---|---|---|---|
| 1 | Window & game loop | Launch the app. A window opens and a debug counter shows the loop running. | Done. Swing Timer at ~60 ticks/s, movement uses delta time. |
| 2 | Run, jump & gravity | A/D moves left and right. W jumps; the player falls back down. You can't jump again in mid-air. | Vertical velocity plus a gravity constant each tick. Only allow a jump when standing on something. |
| 3 | Platforms & collisions | The player lands on platforms, can't fall through them, and walls block sideways movement. | Use rectangles (not circles). Move and resolve X first, then Y, to avoid getting stuck in corners. |
| 4 | Level from a text file | Change a character in `level.txt`, restart, and the level changes. | One character = one tile (see slides). Load with `getResource`, never an absolute path. |
| 5 | Scrolling camera | Walk right: the view follows the player through a level wider than the window. | Draw everything shifted by a camera offset. Don't scroll past the level edges. |
| 6 | Shooting | Press Space: a bullet flies in the direction the player faces and disappears off-screen or on hitting a wall. | Short cooldown so holding Space doesn't spam. |
| 7 | Walker enemies | Enemies walk back and forth on platforms, turning at edges and walls. One bullet kills an enemy. | Mark enemies/bullets as dead and remove them after the update loop. |
| 8 | Hearts & damage | Touch an enemy: lose a heart and blink briefly. Hearts show in the corner. | Short invincibility after a hit so you don't lose all hearts at once. |
| 9 | Win & game over | Reach the flag: "You win" screen. Lose all hearts or fall into a pit: "Game over". Press R to restart. | Restart must fully reset the level. Test it several times. |

## Should have (target: Presentation 3, Oct 15)

| # | Name | How to Demo | Notes |
|---|---|---|---|
| 10 | Start menu & pause | Launch: menu with Start, How to Play, Quit. Esc pauses and resumes. | `CardLayout` to switch screens. Paused = no updates at all. |
| 11 | Shooting enemy | An enemy stands still and fires at the player every few seconds. | Reuse the bullet class; enemy bullets only hurt the player. |
| 12 | Coins & score | Collect a coin: the score in the corner goes up. Final score shows on the win screen. | Coins are `C` in the level file. |

## Could have (if time allows)

| # | Name | How to Demo | Notes |
|---|---|---|---|
| 13 | Second level | Reach the flag in level 1 and level 2 starts. | Just a second text file. |
| 14 | Sprites & animation | The player and enemies are drawn with images that animate while running. | Images in the project's resources folder. |
| 15 | Sound effects | Jumping, shooting and hits make sounds. | `javax.sound.sampled`, loaded via `getResource`. |
