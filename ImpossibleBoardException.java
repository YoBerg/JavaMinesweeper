/**
 * Java Minesweeper - ImpossibleBoardException
 * 
 * An exception indicating that the game is impossible to create.
 * 
 * @author Yohan Berg
 * @version December 3, 2020
 */
@SuppressWarnings("serial")
public class ImpossibleBoardException extends Exception {

    public ImpossibleBoardException(String message) {
        super(message);
    }
}