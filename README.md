# Java Terminal Game

This was a simple game I had to make for one of my early coursework projects at university. I was given the premise of the game, along with instructions on how to play the game and a how it is set up. This repository contains my solution with very small changes (tidying up comments and fixing some errors) made to it since the submission deadline. Almost all of the source code has been left untouched, only minor alterations have been made.


### Contents:
- [Build and Run](#build-and-run)
- [Game Premise](#game-premise)
- [Game Commands](#game-commands)
- [The Bot](#the-bot)
- [Custom Maps](#custom-maps)


### Build and Run
---
First, clone the repository with:
```
git clone https://github.com/cg-2611/java-terminal-game.git
```
Next, open the directory created by the `git clone` command:
```
cd java-terminal-game
```
Once in the project root directory, if gradle is installed run:
```
gradle --console=plain run
```
Otherwise, the gradle wrapper can be used, on mac/linux run:
```
./gradlew --console=plain run
```
and on windows run:
```
gradlew.bat --console=plain run
```
> Note: using the `--console=plain` option is optional but provides a better user experience when providing input when run with gradle.


### Game Premise
---
The aim of the game is to collect enough gold from the dungeon to be able to escape.
To escape and win, the player must be standing on an exit tile when the exit command is executed.

However, to make it more of a challenge, there is a computer controlled player trying to prevent the player from winning. 
If the bot catches the player, then the game is over and the player loses. 

The players take it in turns to make perform an action.
Every command a player uses takes up their turn, no matter if the action was a success, failure or invalid.

The game map can be loaded from a text file, and each player starts the game at a random position within the map.

Both players can only view the map in 5x5 sections, shown using the [LOOK](#game-commands) command.

A map is made up of 5 different types of tiles:
- 'P' - Player til:  this character is displayed at the location of the player.

- 'B' - Bot tile: this character is displayed at the location of the bot player.

- '.' - Empty space tile: both players can walk on this tile.

- 'G' - Gold til:  allows the player to collect the gold at this location using the [PICKUP](#game-commands) command. If the player decides to do this, the tile is replaced with empty space.

- 'E' - Exit tile: allows the human player to exit the dungeon by walking onto it and using the [EXIT](#game-commands) command.

- '#' - Wall tile: either player cannot move through this tile.


### Game Commands
---
These are the commands that the player can use to interact with the game (commands can be either uppercase or lowercase):
- HELLO: prints the amount of gold required to exit the map and win.

- GOLD: prints the amount of gold the human player currently owns.

- MOVE \<D\>: moves the player one tile in a given direction D. The direction must be either N, E, S or W, for example MOVE S. The command returns feedback on whether the move was successful or not.

- PICKUP: prints whether or not the player successfully collected some gold, as well as the amount of gold the player owns.

- LOOK: Entering this command will return a 5x5 view of the map around a player. The grid shows every relevant tile including any other player.

- EXIT: Entering this command will quit the game. If the player owns the amount of gold required to win and is standing on an exit tile, the player has won and is informed of this, the game ends the player loses all progress.


### The Bot
---
The bot is the computer controlled player that is chasing the player, trying to make them lose. Just like the player, the bot can only see the map using the [LOOK](#game-commands) command and can only move using the [MOVE](#game-commands) command. The bot does not collect gold and cannot leave the dungeon, its only purpose is to catch the player. Every other turn, the bot will perform a [LOOK](#game-commands) action, and if it sees the player, on its next turn will move towards them. If the bot cannot see the player, then it makes random moves until the player becomes visible again.


### Custom Maps
---
The game supports the use of custom maps that the player can play with. The game can be loaded with either the default map, or the user can provide the name of a file that contains a map. All maps must be placed in the directory `./src/main/resources/maps/`.

The correct formatting for a file that can be successfully read as a map: 
- The first line needs to be the desired name of the map, preceded by "name " e.g. "name map_name".
- The second line must contain the amount of gold required to win for that dungeon, preceded by "win ", e.g "win 4"
- The third and subsequent lines of the file must be filled with the map.

Maps must be rectangular, contain only the empty space, wall, exit and gold tiles and need at least one exit tile and to have an appropriate number of gold tiles compared with the amount of gold required to exit the dungeon successfully, i.e. if the player needs 2 gold to win, there must be at least 2 gold tiles in the map.
