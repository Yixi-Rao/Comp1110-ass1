package comp1110.ass1;

public enum Orientation {
    NORTH, EAST, SOUTH, WEST;
    /**
     * Return the single character associated with a `Orientation`, which is the first character of
     * the direction name, as an upper case character ('N', 'E', 'S', 'W')
     *
     * @return A char value equivalent to the `Orientation` enum
     */
    public char toChar() {
        if (this == NORTH){
            return 'N';

        }
        else if (this == EAST){
            return 'E';
        }
        else if (this == SOUTH){
            return 'S';
        }
        else{
            return 'W';
        }
        // FIXME Task 2

    }
}
