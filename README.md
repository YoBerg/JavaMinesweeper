# JavaMinesweeper
The minesweeper game written in Java. This casual project was created as a hobby for both coding and Minesweeper.
## Game.java
The class containing the logic behind a Minesweeper game. Includes methods to create a game, getting time, revealing tiles, flagging tiles, and checking the win-condition (that all non-bomb tiles have been revealed).
## Tile.java
A class representing a tile in the Minesweeper board. The tile can have a position, type (an enum), its hidden status, and its flagged status.
## ConsolePlayer.java
A class that allows the user to play the Minesweeper game through the console. Used to convert Game.java outputs into visual indicators in the console and allows the user to interact with the game using console inputs.
