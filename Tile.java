/**
 * Java Minesweeper - Tile
 * 
 * A class representing a minesweeper game tile
 * 
 * @author Yohan Berg
 * @version December 4, 2020
 */
public class Tile {

    /**
     * An enumeration of the types of a tile. 
     */ 
    enum TileType {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        BOMB
    }

    private final TileType type; // The type of the tile
    private boolean hidden; // Whether or not the tile is hidden
    private boolean flagged; // Whether or not the tile is flagged

    /**
     * The conventional constructor for Tile. Sets hidden to true and flagged to false.
     * 
     * @param type - The type of the tile.
     */
    public Tile(TileType type) {
        this.type = type;
        this.hidden = true;
        this.flagged = false;
    }

    /**
     * Returns the type of the tile
     * 
     * @return the type of the tile
     */
    public TileType getType() {
        return type;
    }

    /**
     * Returns whether or not the tile is hidden
     * 
     * @return whether or not the tile is hidden
     */
    public boolean getHidden() {
        return hidden;
    }

    /**
     * Returns whether or not the tile is flagged
     * 
     * @return whether or not the tile is flagged
     */
    public boolean getFlagged() {
        return flagged;
    }

    /**
     * Sets hidden to false and flagged to false.
     */
    public void reveal() {
        hidden = false;
        flagged = false;
    }

    /**
     * Sets flagged to true if the tile is hidden.
     * 
     * @return whether or not the tile could be flagged and was flagged.
     */
    public boolean flag() {
        if (hidden) {
            this.flagged = true;
            return true;
        } else return false;
    }
    
    /**
     * Sets flagged to false
     */
    public void unFlag() {
        this.flagged = false;
    }

    /**
     * Returns if the tile has been revealed
     * 
     * @return true if the tile is revealed.
     */
    public boolean isRevealed() {
        return !hidden;
    }
}