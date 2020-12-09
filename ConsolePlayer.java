import java.io.*;
import java.util.Objects;

/**
 * Java Minesweeper - ConsolePlayer
 * 
 * A class that allows the Minesweeper game to be played on a console.
 * 
 * @author Yohan Berg
 * @version December 8, 2020
 */
public class ConsolePlayer {

    public static void main(String[] args) throws IOException, ImpossibleBoardException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String in = null;

        System.out.println("Enter the desired board size (Format 'rows,columns'): ");
        in = reader.readLine();
        int rows = Integer.parseInt(in.split(",")[0]);
        int cols = Integer.parseInt(in.split(",")[1]);

        System.out.println("Enter the number of desired bombs: ");
        int numBombs = Integer.parseInt(reader.readLine());
        
        Game game = new Game(rows, cols, numBombs);

        boolean loss = false;
        boolean doLoop = true;
        while (doLoop) {
            System.out.println("Please enter your command. Type 'help' for list of commands");
            in = reader.readLine().toLowerCase();
            switch (in) {
                case "help":
                    System.out.println("Possible commands:");
                    System.out.println("'help': Shows this list");
                    System.out.println("'bombs': Displays total bombs - flagged tiles and how many total bombs there are");
                    System.out.println("'start_game:[row],[column]': Starts the game with the position row and column");
                    System.out.println("'reveal:[row],[column]': Reveals the tile at row and column. " +
                    "Game must be started.");
                    System.out.println("'flag:[row],[column]': Flags the tile at row and column");
                    System.out.println("'unflag:[row],[column]': Unflags the tile at row and column");
                    System.out.println("'display': Displays the current board");
                    System.out.println("'time': Displays your current time in seconds.");
                    System.out.println("'quit': Ends the program");
                    break;
                case "time":
                    Long time = game.getTime();
                    if (Objects.isNull(time)) {
                        System.out.println("Game has not been started yet!");
                    } else {
                        System.out.println("Time: " + (time / 1000));
                    }
                    break;
                case "display":
                    printBoard(game.getBoard(), game.isStarted());
                    break;
                case "bombs":
                    int[] bombInfo = game.getBombInfo();
                    System.out.println(bombInfo[0] + " flagged bombs out of " + bombInfo[1] + " total bombs");
                    break;
                case "quit":
                    System.out.println("Quitting...");
                    doLoop = false;
                    break;
                default:
                    if (in.contains(":")) {
                        int index = in.indexOf(":");
                        if (in.substring(0, index).equals("start_game")) {
                            try {
                                int commaIndex = in.indexOf(",");
                                if (commaIndex == -1) {
                                    System.out.println("Parameters could not be separated. Please separate with a ','");
                                    break;
                                }
                                int row = Integer.parseInt(in.substring(index + 1, commaIndex)) - 1;
                                int col = Integer.parseInt(in.substring(commaIndex + 1)) - 1;

                                game.startGame(row, col);
                                printBoard(game.getBoard(), game.isStarted());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid parameters. Please enter whole numbers");
                            }
                        } else if (in.substring(0, index).equals("reveal")) {
                            try {
                                int commaIndex = in.indexOf(",");
                                if (commaIndex == -1) {
                                    System.out.println("Parameters could not be separated. Please separate with a ','");
                                    break;
                                }
                                int row = Integer.parseInt(in.substring(index + 1, commaIndex)) - 1;
                                int col = Integer.parseInt(in.substring(commaIndex + 1)) - 1;

                                game.revealTile(row, col);
                                if (game.getBoard()[col][row].getType().equals(Tile.TileType.BOMB)) {
                                    loss = true;
                                }
                                printBoard(game.getBoard(), game.isStarted());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid parameters. Please enter whole numbers");
                            }
                        } else if (in.substring(0, index).equals("flag")) {
                            try {
                                int commaIndex = in.indexOf(",");
                                if (commaIndex == -1) {
                                    System.out.println("Parameters could not be separated. Please separate with a ','");
                                    break;
                                }
                                int row = Integer.parseInt(in.substring(index + 1, commaIndex)) - 1;
                                int col = Integer.parseInt(in.substring(commaIndex + 1)) - 1;

                                game.flagTile(row, col);
                                printBoard(game.getBoard(), game.isStarted());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid parameters. Please enter whole numbers");
                            }
                        } else if (in.substring(0, index).equals("unflag")) {
                            try {
                                int commaIndex = in.indexOf(",");
                                if (commaIndex == -1) {
                                    System.out.println("Parameters could not be separated. Please separate with a ','");
                                    break;
                                }
                                int row = Integer.parseInt(in.substring(index + 1, commaIndex)) - 1;
                                int col = Integer.parseInt(in.substring(commaIndex + 1)) - 1;

                                game.unflagTile(row, col);
                                printBoard(game.getBoard(), game.isStarted());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid parameters. Please enter whole numbers");
                            }
                        }
                        else {
                            System.out.println("Unknown request. Type 'help' to see valid commands");
                        }
                    } else {
                        System.out.println("Unknown request. Type 'help' to see valid commands");
                    }
            }
            if (loss) {
                System.out.println("You lost! Time: " + (game.getTime() / 1000));
                break;
            } else if (game.checkWin()) {
                System.out.println("You won! Time: " + (game.getTime() / 1000));
                break;
            }
        }
    }

    /**
     * Prints the board to the console.
     * 
     * @param board - The board of the game.
     */
    public static void printBoard(Tile[][] board, boolean isStarted) {
        if (isStarted) {
            System.out.print("  ");
            for (int c = 0; c < board.length; c++) {
                System.out.print(" " + (c < 9 ? "0" + (c + 1) : c + 1));
            }
            System.out.println();
            for (int r = 0; r < board.length; r++) {
                System.out.print((r < 9 ? "0" + (r + 1) : r + 1) + " ");
                for (int c = 0; c < board[0].length; c++) {
                    if (board[c][r].getFlagged()) System.out.print(" F ");
                    else if (board[c][r].getHidden()) System.out.print(" - ");
                    else if (board[c][r].getType().equals(Tile.TileType.BOMB))  System.out.print(" B ");
                    else if (board[c][r].getType().equals(Tile.TileType.ZERO))  System.out.print(" 0 ");
                    else if (board[c][r].getType().equals(Tile.TileType.ONE))   System.out.print(" 1 ");
                    else if (board[c][r].getType().equals(Tile.TileType.TWO))   System.out.print(" 2 ");
                    else if (board[c][r].getType().equals(Tile.TileType.THREE)) System.out.print(" 3 ");
                    else if (board[c][r].getType().equals(Tile.TileType.FOUR))  System.out.print(" 4 ");
                    else if (board[c][r].getType().equals(Tile.TileType.FIVE))  System.out.print(" 5 ");
                    else if (board[c][r].getType().equals(Tile.TileType.SIX))   System.out.print(" 6 ");
                    else if (board[c][r].getType().equals(Tile.TileType.SEVEN)) System.out.print(" 7 ");
                    else if (board[c][r].getType().equals(Tile.TileType.EIGHT)) System.out.print(" 8 ");
                    else                                                        System.out.print(" ? ");
                }
                System.out.println();
            }
        } else {
            System.out.print("  ");
            for (int c = 0; c < board.length; c++) {
                System.out.print(" " + (c < 9 ? "0" + (c + 1) : c + 1));
            }
            System.out.println();
            for (int r = 0; r < board.length; r++) {
                System.out.print((r < 9 ? "0" + (r + 1) : r + 1) + " ");
                for (int c = 0; c < board[0].length; c++) {
                    System.out.print(" - ");
                }
                System.out.println();
            }
        }
    }
}