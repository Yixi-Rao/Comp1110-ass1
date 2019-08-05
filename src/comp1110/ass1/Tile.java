package comp1110.ass1;

import static comp1110.ass1.Orientation.*;

public class Tile {

    private TileType tileType;         // Which tile type is it (a ... f)
    private Location location;         // The tile's current location on board
    private Orientation orientation;   // The tile's current orientation

    public Tile(String placement) {
        this.tileType = TileType.valueOf(Character.toString((placement.charAt(0) - 32)));
        this.orientation = placementToOrientation(placement);
        this.location = placementToLocation(placement);
    }

    public Location getLocation() {
        return location;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public TileType getTileType() { return tileType; }

    /**
     * Given a four-character tile placement string, decode the tile's orientation.
     *
     * You will need to read the description of the encoding in the class `Objective`.
     *
     * @param placement A string representing the placement of a tile on the game board
     *
     * @return A value of type `Orientation` corresponding to the tile's orientation on board
     */
    public static Orientation placementToOrientation(String placement) {
        if (placement.charAt(3) == 'N'){
            return NORTH;
        }
        else if (placement.charAt(3) == 'S')
            return SOUTH;
        else if (placement.charAt(3) == 'W')
            return WEST;
        else
            return EAST;
        // FIXME Task 3

    }

    /**
     * Given a four-character tile placement string, decode the tile's location.
     *
     * You will need to read the description of the encoding in the class `Objective'
     *
     * @param placement A string representing the placement of a tile on the game board
     *
     * @return A value of type `Location` corresponding to the tile's location on the board
     */
    public static Location placementToLocation(String placement) {
        int x = 0;
        switch (placement.charAt(1)){
            case '0':
                x = 0;break;
            case '1':
                x = 1;break;
            case '2':
                x = 2;break;
            case '3':
                x = 3;break;
            case '4':
                x = 4;break;
        }
        int y=0;
        switch (placement.charAt(2)){
            case '0':
                y = 0;break;
            case '1':
                y = 1;break;
            case '2':
                y = 2;break;
            case '3':
                y = 3;break;
        }
        return new Location(x,y);

        // FIXME Task 4

    }
}
