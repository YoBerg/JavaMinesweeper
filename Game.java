import java.util.ArrayList;
import java.math.*;
import java.util.Objects;

/**
 * Java Minesweeper - Game
 * 
 * A class that runs a Minesweeper game
 * 
 * @author Yohan Berg
 * @version January 4, 2021
 */
public class Game {

    private Tile[][] board; // The board of the minesweeper game. Tile[cols][rows]
    private Long startTime; // The start time of the game.
    private Long endTime; // The current time or end time of the game.
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
        this.endTime = null;
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
                r = (int) Math.floor(Math.random() * rows);
                c = (int) Math.floor(Math.random() * cols);
                // redo if tile is not null, or if it is neighboring the starting tile.
            } while (!Objects.isNull(board[c][r]) || ((c <= col + 1 && c >= col - 1) && (r <= row + 1 && r >= row - 1)));

            board[c][r] = new Tile(Tile.TileType.BOMB);
        }

        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                // Prevents overwriting existing tiles
                if (!Objects.isNull(board[c][r])) continue;

                // Calculates how many tiles neighboring the pos are bombs.
                int adjBombs = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        try {
                            if (board[c + i][r + j].getType().equals(Tile.TileType.BOMB)) adjBombs++;
                        } catch (IndexOutOfBoundsException e) {
                            continue;
                        } catch (NullPointerException e) {
                            continue;
                        }
                    }
                }

                // Sets the tile at the pos based on how many neighbor bombs there are.
                board[c][r] = new Tile(switch (adjBombs) {
                    case 0 -> Tile.TileType.ZERO;
                    case 1 -> Tile.TileType.ONE;
                    case 2 -> Tile.TileType.TWO;
                    case 3 -> Tile.TileType.THREE;
                    case 4 -> Tile.TileType.FOUR;
                    case 5 -> Tile.TileType.FIVE;
                    case 6 -> Tile.TileType.SIX;
                    case 7 -> Tile.TileType.SEVEN;
                    case 8 -> Tile.TileType.EIGHT;
                    default -> Tile.TileType.ZERO;
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
        if (!Objects.isNull(endTime)) return endTime - startTime;
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
    private ArrayList<int[]> revealTileRecursive(int row, int col) {
        // Checks if the tile can be revealed.
        if (col < 0 || col >= cols || row < 0 || row >= rows) return new ArrayList<int[]>();
        if (board[col][row].isRevealed()) return new ArrayList<int[]>();

        // If the tile is a bomb, game over.
        if (board[col][row].getType().equals(Tile.TileType.BOMB)) {
            return gameOver();
        }

        // Reveals the tile and saves its position.
        board[col][row].reveal();
        int[] pos = {row, col};
        
        // If the tile is a ZERO, reveal the neighboring tiles. Otherwise just return the position.
        if (board[col][row].getType().equals(Tile.TileType.ZERO)) {
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
     * Reveals the selected tile. If the tile is a zero, then reveals all the neighbor tiles recursively.
     * Returns an empty ArrayList if the tile is out of bounds or is already revealed.
     * 
     * @param row - The row of the tile to be revealed
     * @param col - The column of the tile to be revealed
     * @return An ArrayList of the positions of all revealed tiles.
     */
    public ArrayList<int[]> revealTile(int row, int col) {
        ArrayList<int[]> toReturn = revealTileRecursive(row, col);
        if (checkWin()) {
            endTime = System.currentTimeMillis();
        }
        return toReturn;
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

    /**
     * Attempts to unflag the tile at the specified position.
     * 
     * @param row - The row of the tile to be unflagged
     * @param col - The column of the tile to be unflagged
     */
    public void unflagTile(int row, int col) {
        board[col][row].unFlag();
    }

    /**
     * Ends the game by revealing all bomb tiles and setting endTime.
     * 
     * @return An ArrayList of all bomb positions.
     */
    public ArrayList<int[]> gameOver() {
        
        endTime = System.currentTimeMillis();
        ArrayList<int[]> arr = new ArrayList<int[]>();
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                if (board[c][r].getType().equals(Tile.TileType.BOMB)) {
                    board[c][r].reveal();
                    int[] pos = {r, c};
                    arr.add(pos);
                }
            }
        }
        return arr;
    }

    /**
     * Checks if all non-bomb tiles have been revealed. If so, returns true.
     * 
     * @return <code>true</code> if all non-bomb tiles are revealed.
     */
    public boolean checkWin() {
        if (Objects.isNull(board[0][0])) return false;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                // Return false if the tile is not a bomb and is not revealed.
                if (!board[c][r].getType().equals(Tile.TileType.BOMB) && !board[c][r].isRevealed()) return false;
            }
        }
        return true;
    }

    /**
     * Returns the board
     * 
     * @return the board
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Returns the Tile at position row,col
     * 
     * @return the TIle at position row,col
     */
    public Tile getTile(int row, int col) {
        return board[col][row];
    }

    /**
     * Returns total bombs - flagged tiles and total bombs
     * 
     * @return {total bombs - total flagged tiles, total bombs}
     */
    public int[] getBombInfo() {
        if (Objects.isNull(startTime)) return new int[]{numBombs, numBombs};
        int totalFlagged = 0;
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                if (tile.getFlagged()) totalFlagged++;
            }
        }
        int[] returnArr = {numBombs - totalFlagged, numBombs};
        return returnArr;
    }

    /**
     * Returns whether or not the game was started
     * 
     * @return <code>true</code> if the game has started
     */
    public boolean isStarted() {
        return !Objects.isNull(startTime);
    }
}