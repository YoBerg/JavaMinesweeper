import java.util.ArrayList;
import java.math.*;
import java.util.Objects;

import Tile.TileType;

/**
 * Java Minesweeper - Game
 * 
 * A class that runs a Minesweeper game
 * 
 * @author Yohan Berg
 * @version December 4, 2020
 */
public class Game {

    private Tile[][] board; // The board of the minesweeper game.
    private Long startTime; // The start time of the game.
    private int numBombs; // The number of bombs in the game.
    private int hiddenBombs; // The number of unflagged bombs in the game.
    private int rows; // The number of rows in the game board.
    private int cols; // The number of columns in the game board.

    /**
     * Initializes a game of minesweeper.
     * 
     * @param rows - The number of rows in this minesweeper board.
     * @param cols - The number of columns in this minesweeper board.
     * @param numBombs - The number of boms in this minesweeper board.
     * @throws ImpossibleBoardException - When the board can not be made with the given parameters.
     */
    public Game(int rows, int cols, int numBombs) throws ImpossibleBoardException {
        if (rows * cols - 9 < numBombs) 
            throw new ImpossibleBoardException("Not enough tiles to support the given number of bombs");
        if (numBombs < 0) throw new ImpossibleBoardException("Number of bombs can't be less than 0");
        if (rows < 1) throw new ImpossibleBoardException("Rows can't be less than 1");
        if (cols < 1) throw new ImpossibleBoardException("Columns can't be less than 1");

        this.rows = rows;
        this.cols = cols;
        this.board = new Tile[cols][rows];
        this.startTime = null;
        this.numBombs = numBombs;
        this.hiddenBombs = 0;
    }

    /**
     * Populates the board with tiles. The tiles neighboring the starting position will not contain bombs.
     * 
     * @param row - The row of the starting position
     * @param col - The column of the starting position
     * @throws IndexOutOfBoundsException When the starting position is out of bounds on the board.
     */
    private void createBoard(int row, int col) throws IndexOutOfBoundsException {
        if (col < 0 || col >= cols) throw new IndexOutOfBoundsException("col is out of bounds!");
        if (row < 0 || row >= rows) throw new IndexOutOfBoundsException("row is out of bounds!");

        // Places bombs at random locations.
        for (hiddenBombs = 0; hiddenBombs < numBombs; hiddenBombs++) {
            int r = -1;
            int c = -1;

            do {
                r = Math.floor(Math.random() * rows);
                c = Math.floor(Math.random() * cols);
            } while (!Objects.isNull(board[c][r]) || ((c < col + 1 && c > col - 1) && (r < row + 1 && r > row - 1)));

            board[c][r] = new Tile(TileType.BOMB);
        }

        for (int c = 0; c < board.length; c++) {
            for (int r = 0; r < board.length; r++) {
                // Prevents overwriting existing tiles
                if (!Objects.isNull(board[c][r])) continue;

                // Calculates how many tiles neighboring the pos are bombs.
                int adjBombs = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        try {
                            if (board[c + i][r + j].getType().equals(TileType.BOMB)) adjBombs++;
                        } catch (IndexOutOfBoundsException e) {
                            continue;
                        } catch (NullPointerException e) {
                            continue;
                        }
                    }
                }

                // Sets the tile at the pos based on how many neighbor bombs there are.
                board[c][r] = new Tile(switch (adjBombs) {
                    case 0 -> TileType.ZERO;
                    case 1 -> TileType.ONE;
                    case 2 -> TileType.TWO;
                    case 3 -> TileType.THREE;
                    case 4 -> TileType.FOUR;
                    case 5 -> TileType.FIVE;
                    case 6 -> TileType.SIX;
                    case 7 -> TileType.SEVEN;
                    case 8 -> TileType.EIGHT;
                    default -> TileType.ZERO;
                });
            }
        }
    }

    /**
     * Starts the game with the given starting position.
     * 
     * @param row - The row of the starting position
     * @param col - The column of the starting position
     * @throws IndexOutOfBoundsException When the starting position is out of bounds on the board.
     */
    public void startGame(int row, int col) throws IndexOutOfBoundsException {
        createBoard(row, col);
        startTime = System.currentTimeMillis();
        revealTile(row, col);
    }

    /**
     * Gets the current time in milliseconds. Returns <code>null</code> if the game has not been started.
     * 
     * @return The current time in milliseconds or <code>null</code>
     */
    public Long getTime() {
        if (Objects.isNull(startTime)) return null;
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Reveals the selected tile. If the tile is a zero, then reveals all the neighbor tiles recursively.
     * Returns an empty ArrayList if the tile is out of bounds or is already revealed.
     * 
     * @param row - The row of the tile to be revealed
     * @param col - The column of the tile to be revealed
     * @return An ArrayList of the positions of all revealed tiles.
     */
    public ArrayList<int[]> revealTile(int row, int col) {
        // TODO Make if tile is bomb, game over.

        // Checks if the tile can be revealed.
        if (col < 0 || col >= cols || row < 0 || row >= rows) return new ArrayList<int[]>();
        if (board[col][row].isRevealed()) return new ArrayList<int[]>();

        // Reveals the tile and saves its position.
        board[col][row].reveal();
        int[] pos = {row, col};
        
        // If the tile is a ZERO, reveal the neighboring tiles. Otherwise just return the position.
        if (board[col][row].getType().equals(TileType.ZERO)) {
            ArrayList<int[]> arr = new ArrayList<int[]>();
            arr.addAll(revealTile(row - 1, col - 1));
            arr.addAll(revealTile(row    , col - 1));
            arr.addAll(revealTile(row + 1, col - 1));
            arr.addAll(revealTile(row - 1, col    ));
            arr.addAll(revealTile(row + 1, col    ));
            arr.addAll(revealTile(row - 1, col + 1));
            arr.addAll(revealTile(row    , col + 1));
            arr.addAll(revealTile(row + 1, col + 1));
            return arr;
        } else {
            ArrayList<int[]> arr = new ArrayList<int[]>();
            arr.add(pos);
            return arr;
        }
    }

    /**
     * Attempts to flag the tile at the specified position. Cannot flag revealed tiles.
     * 
     * @param row - The row of the tile to be flagged
     * @param col - The column of the tile to be flagged
     */
    public void flagTile(int row, int col) {
        board[col][row].flag();
    }
}