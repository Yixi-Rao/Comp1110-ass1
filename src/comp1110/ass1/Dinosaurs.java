package comp1110.ass1;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static comp1110.ass1.Orientation.NORTH;
import static comp1110.ass1.Orientation.SOUTH;
import static comp1110.ass1.State.*;

public class Dinosaurs {

    /* The objective represents the problem to be solved in this instance of the game. */
    private Objective objective;

    /*
     * States at each of the board's 20 locations (corners), initialized
     * to represent the empty board.  EMPTY states may be overwritten
     * by non-empty states (RED, GREEN), when a tile in that state is
     * placed at the same corner.
     *
     * Notice that this is initialized with all perimeter states, and
     * all inner locations that must be water are initialized as water
     * and all inner locations that must be land are initialized as
     * empty.
     */
    private State[][] boardstates = {
            {EMPTY, WATER, EMPTY, WATER, EMPTY},
            {WATER, EMPTY, WATER, EMPTY, WATER},
            {EMPTY, WATER, EMPTY, WATER, EMPTY},
            {WATER, EMPTY, WATER, EMPTY, WATER}
    };

    /*
     * Tiles occupying the board.   Indices refer to the square
     * to the lower-right of the given location (remember, locations
     * refer to corners, not to tiles).   So (0,0) refers to the
     * square in the top-left corner, (3,0) refers to the sqaure
     * in the top-right corner, (0,2) refers to the square in the
     * bottom-left corner, and (3,2) refers to the square in the
     * bottom-right corner.
     *
     * Each entry in the array points to the Tile instance occupying
     * the square, or null if the square is empty.   Since tiles
     * are two squares big, each placed tile should have two array
     * entries referring to it.
     *
     * Since this data structure only reflects placed tiles, it is
     * initially empty (all entries are null).
     */
    private Tile[][] tiles = new Tile[3][4];

    /**
     * Construct a game with a given objective
     *
     * @param objective The objective of this game.
     */
    public Dinosaurs(Objective objective) {
        this.objective = objective;
    }

    /**
     * Construct a game for a given level of difficulty.
     * This chooses a new objective and creates a new instance of
     * the game at the given level of difficulty.
     *
     * @param difficulty The difficulty of the game.
     */
    public Dinosaurs(int difficulty) {
        this(Objective.newObjective(difficulty));
    }

    public Objective getObjective() {
        return objective;
    }

    /**
     * @param boardState A string consisting of 4*N characters, representing
     * initial tile placements (initial game state).
     */
    public void initializeBoardState(String boardState) {
        for (int i = 0; i < boardState.length()/4; i++) {
            String placement = boardState.substring(i * 4, ((i + 1) * 4));
            addTileToBoard(placement);
        }
    }


    /**
     * Print out the board state.   May be useful for debugging.
     */
    private void printBoardState() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 5; c++) {
                System.out.print((boardstates[r][c] == null ? ' ' : boardstates[r][c].name().charAt(0)) + " ");
            }
            System.out.println();
        }
    }


    /**
     * Print out tile state.   May be useful for debugging.
     */
    private void printTileState() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                System.out.print(tiles[r][c] == null ? '.' : tiles[r][c].getTileType().name().charAt(0)+"");
            }
            System.out.println();
        }
    }

    /**
     * Check whether a tile placement fits inside the game board.
     *
     * @param placement A String representing a tile placement.
     * @return  True if the tile is completely within the board, and false otherwise.
     */
    public static boolean isPlacementOnBoard(String placement) {
        Location l = Tile.placementToLocation(placement);
        int x = l.getX();
        int y = l.getY();
        if (placement.charAt(3) == 'N' ||  placement.charAt(3) == 'S'){
            if (x + 1 > 4 || y + 2 > 3){
                return false;
            }
        }
        else {
            if (x + 2 > 4 || y + 1 > 3){
                return false;
            }
        }
        // FIXME Task 5
        return true;
    }


    /**
     * Add a new tile placement to the board state, updating
     * all relevant data structures accordingly.  If you add
     * additional data structures, you will need to update
     * this.
     *
     * @param placement The placement to add.
     */
    private void addTileToBoard(String placement) {
        /* create the tile, and figure out its location and orientation */
        Tile tile = new Tile(placement);

        /* update the tile data structure for the two squares that compose this tile */
        updateTiles(tile);

        /* update the states for each of the six points on the tile */
        updateBoardStates(tile);
    }


    /**
     * Update the tile data structure with a new tile placement.
     *
     * Each entry in the data structure corresponds to a square, and
     * each tile is composed of two squares.   So each time a tile
     * is added, two entries in the data structure need to be updated
     * to point to the new tile.
     *
     * Squares that are covered by a tile will have their data structure
     * entry point to the covering tile.
     *
     * Squares that are not covered by a tile will point to null.
     *
     * @param tile The tile being placed
     */
    private void updateTiles(Tile tile) {
        Location location = tile.getLocation();
        Orientation orientation = tile.getOrientation();
        tiles[location.getY()][location.getX()] = tile;
        if (orientation == NORTH || orientation == SOUTH)
            tiles[location.getY()+1][location.getX()] = tile;  // vertical orientation
        else
            tiles[location.getY()][location.getX()+1] = tile;  // horizontal orientation
    }


    /**
     * Check whether a proposed tile placement overlaps with any previous
     * placements.
     *
     * You will need to use both the placement about to be made, and
     * the existing board state (specifically, the tiles data structure),
     * which is kept up to date when addTileToBoard is called.
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return  False if the proposed tile placement does not overlap with
     * the already placed tiles, and True if there is any overlap.
     */
    public boolean doesPlacementOverlap(String placement) {
        String state = objective.getInitialState();//use object to get initialstate
        initializeBoardState(state);
        Tile t = new Tile(placement);
        Orientation ori_t = t.getOrientation();
        int x = t.getLocation().getX();
        int y = t.getLocation().getY();

        if (ori_t == NORTH || ori_t == SOUTH){
            if (tiles[y][x] != null || tiles[y+1][x] != null)
                return true;
        }
        else {
            if (tiles[y][x] != null || tiles[y][x+1] != null ){
                return true;
            }
        }
        return false;
        // FIXME Task 6

    }


    /**
     * Update the boardstates data structure due to a valid (correct) new
     * tile placement.
     *
     * Each point in the boardstates data structure corresponds to one of
     * the twenty board locations, each of which is a place where the corner
     * of a tile may be placed (see the game description for a diagram, and
     * more information).
     *
     * Each entry in the boardstates data structure is a State, which may
     * be null (unassigned), WATER, EMPTY, GREEN, or RED.
     *
     * When a valid tile placement is made, some locations may change.   For
     * example null locations may become non-null due to a tile placement,
     * and some EMPTY locations may become GREEN or RED.
     *
     * Notice that when a tile is placed, six locations will change, since
     * a tile covers two squares, it will affect two locations at each end
     * and two locations in its middle.  The affected locations will depend
     * on the location of the tile (its top-left corner), and the tile's
     * orientation.
     *
     * @param tile The tile being placed
     */
    private void updateBoardStates(Tile tile) {
        int x = tile.getLocation().getX();
        int y = tile.getLocation().getY();
        Orientation orientation = tile.getOrientation();
        TileType type = tile.getTileType();
        if (orientation == NORTH || orientation == SOUTH){
            State state0 = type.stateFromOffset(0,0,orientation);
            boardstates[y][x] = (boardstates[y][x]==GREEN || boardstates[y][x]==RED)?boardstates[y][x]:state0;
            State state1 = type.stateFromOffset(1,0,orientation);
            boardstates[y][x+1] = (boardstates[y][x+1]==GREEN || boardstates[y][x+1]==RED)?boardstates[y][x+1]:state1;
            State state2 = type.stateFromOffset(0,1,orientation);
            boardstates[y+1][x] = (boardstates[y+1][x]==GREEN || boardstates[y+1][x]==RED)?boardstates[y+1][x]:state2;
            State state3 = type.stateFromOffset(1,1,orientation);
            boardstates[y+1][x+1] = (boardstates[y+1][x+1]==GREEN || boardstates[y+1][x+1]==RED)?boardstates[y+1][x+1]:state3;
            State state4 = type.stateFromOffset(0,2,orientation);
            boardstates[y+2][x] = (boardstates[y+2][x]==GREEN || boardstates[y+2][x]==RED)?boardstates[y+2][x]:state4;
            State state5 = type.stateFromOffset(1,2,orientation);
            boardstates[y+2][x+1] = (boardstates[y+2][x+1]==GREEN || boardstates[y+2][x+1]==RED)?boardstates[y+2][x+1]:state5;
        }else{
            State state0 = type.stateFromOffset(0,0,orientation);
            boardstates[y][x] = (boardstates[y][x]==GREEN || boardstates[y][x]==RED)?boardstates[y][x]:state0;
            State state1 = type.stateFromOffset(1,0,orientation);
            boardstates[y][x+1] = (boardstates[y][x+1]==GREEN || boardstates[y][x+1]==RED)?boardstates[y][x+1]:state1;
            State state2 = type.stateFromOffset(2,0,orientation);
            boardstates[y][x+2] = (boardstates[y][x+2]==GREEN || boardstates[y][x+2]==RED)?boardstates[y][x+2]:state2;
            State state3 = type.stateFromOffset(0,1,orientation);
            boardstates[y+1][x] = (boardstates[y+1][x]==GREEN || boardstates[y+1][x]==RED)?boardstates[y+1][x]:state3;
            State state4 = type.stateFromOffset(1,1,orientation);
            boardstates[y+1][x+1] = (boardstates[y+1][x+1]==GREEN || boardstates[y+1][x+1]==RED)?boardstates[y+1][x+1]:state4;
            State state5 = type.stateFromOffset(2,1,orientation);
            boardstates[y+1][x+2] = (boardstates[y+1][x+2]==GREEN || boardstates[y+1][x+2]==RED)?boardstates[y+1][x+2]:state5;

        }

        //State state = type.stateFromOffset(location.getX(),location.getY(),orientation);
        //boardstates[location.getX()][location.getY()] = state;









        // FIXME Task 7 (part I)
    }

    /**
     * Given an island location, return its current state.
     *
     * The current state of an island is the state of the dinosaur(s)
     * which are directly or indirectly connected to the island.
     *
     * For example, after applying the placement string "c00N",
     * the islands at location(0,0) and location(1,1) become RED,
     * as they are connected to a RED dinosaur.
     *
     * When an island is not connected to any dinosaurs,
     * its state is EMPTY.
     *
     * @param location  A location on the game board.
     * @return          An object of type `enum State`, representing
     * the given location.
     */
    public State getLocationState(Location location) {
        int x = location.getX();
        int y = location.getY();
        return boardstates[y][x];




        // FIXME Task 7 (part II)

    }

    /**
     * Check whether the locations of land and water on a tile placement
     * are consistent with its surrounding tiles or board, given the current
     * state of the board due to previous placements.
     *
     * Important: The test for this method is not concerned with dinosaur color.
     * Thus it is simply a matter of ensuring that water meets water and land
     * meets land (whether or not the land is occupied by a dinosaur).
     *
     * For example, the placement string "a00N" is not consistent
     * since it puts the water at top-left of tile 'a', next to the island
     * at the top-left of the game board.
     *
     * You will need to use both the placement about to be made, and
     * the existing board state which is kept up to date when
     * addTileToBoard is called.
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return True if the placement is consistent with the board and placed
     * tiles, and False if it is inconsistent.
     */
    public boolean isPlacementConsistent(String placement) {
        Tile tile = new Tile(placement);
        Orientation orientation = tile.getOrientation();
        TileType type = tile.getTileType();
        int x = tile.getLocation().getX();
        int y = tile.getLocation().getY();

        if (orientation == NORTH || orientation == SOUTH){
            State[] states = {type.stateFromOffset(0,0,orientation),type.stateFromOffset(1,0,orientation),
                              type.stateFromOffset(0,1,orientation),type.stateFromOffset(1,1,orientation),
                              type.stateFromOffset(0,2,orientation),type.stateFromOffset(1,2,orientation)};
            State[] states1 = {boardstates[y][x],boardstates[y][x+1],
                               boardstates[y+1][x],boardstates[y+1][x+1],
                               boardstates[y+2][x],boardstates[y+2][x+1]};
            for (int i = 0;i < states.length;i++){
                if (judgeIsland(states[i],states1[i]) == false){
                    return false;
                }
            }
        }
        else {
                    State[] states = {type.stateFromOffset(0,0,orientation),type.stateFromOffset(1,0,orientation), type.stateFromOffset(2,0,orientation),
                            type.stateFromOffset(0,1,orientation), type.stateFromOffset(1,1,orientation),type.stateFromOffset(2,1,orientation)};
                    State[] states1 = {boardstates[y][x],boardstates[y][x+1], boardstates[y][x+2],
                            boardstates[y+1][x], boardstates[y+1][x+1],boardstates[y+1][x+2]};
                    for (int i = 0;i < states.length;i++){
                        if (judgeIsland(states[i],states1[i]) == false){
                            return false;
                        }
            }
        }

        // FIXME Task 9
        return true;
    }
    public static boolean judgeIsland(State s1,State s2){
        if (s1 == WATER){
            if (s2 == WATER){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (s2 == WATER){
                return false;
            }
            else {
                return true;
            }

        }
    }

    /**
     * Check whether a tile placement would cause a collision between
     * green and red dinosaurs.
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return True if the placement would cause a collision
     * between red and green dinosaurs.
     */
    public boolean isPlacementDangerous(String placement) {
        Tile tile = new Tile(placement);
        Orientation orientation = tile.getOrientation();
        TileType type = tile.getTileType();
        int x = tile.getLocation().getX();
        int y = tile.getLocation().getY();
        if (orientation == NORTH || orientation == SOUTH){
            State[] states = {type.stateFromOffset(0,0,orientation),type.stateFromOffset(1,0,orientation),
                              type.stateFromOffset(0,1,orientation),type.stateFromOffset(1,1,orientation),
                              type.stateFromOffset(0,2,orientation),type.stateFromOffset(1,2,orientation)};
            State[] states1 = {boardstates[y][x],boardstates[y][x+1],
                               boardstates[y+1][x],boardstates[y+1][x+1],
                               boardstates[y+2][x],boardstates[y+2][x+1]};
            for (int i = 0;i < states.length;i++){
                if (judgeDinosaurs(states[i],states1[i]) == true){
                    return true;
                }
            }
        }
        else {
            State[] states = {type.stateFromOffset(0,0,orientation),type.stateFromOffset(1,0,orientation), type.stateFromOffset(2,0,orientation),
                    type.stateFromOffset(0,1,orientation), type.stateFromOffset(1,1,orientation),type.stateFromOffset(2,1,orientation)};
            State[] states1 = {boardstates[y][x],boardstates[y][x+1], boardstates[y][x+2],
                    boardstates[y+1][x], boardstates[y+1][x+1],boardstates[y+1][x+2]};
            for (int i = 0;i < states.length;i++){
                if (judgeDinosaurs(states[i],states1[i]) == true){
                    return true;
                }
            }
        }
        // FIXME Task 10
        return false;
    }
    public static Boolean judgeDinosaurs(State s1,State s2){
        if ((s1 == GREEN && s2 == RED)||(s1 == RED && s2 == GREEN )){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Check whether the given tile placement violates the game objective,
     * specifically:
     *
     * 1 - All required island connections are either connected or not occupied,
     *   and
     * 2 - All other pairs of islands are either disconnected or not occupied
     *
     * @param placement A string consisting of 4 characters,
     *                  representing a tile placement
     * @return True if the placement violates the game objective,
     * and false otherwise.
     */
    public boolean violatesObjective(String placement) {
        if (isPlacementConsistent(placement) == false ){
            return true;
        }
        String cnnective = objective.getConnectedIslands();
        Tile tile = new Tile(placement);
        updateBoardStates(tile);

        for (int i = 0; i < cnnective.length()/4; i++){
            String twoRequire = cnnective.substring(i * 4, ((i + 1) * 4));
            State s1 = boardstates[connectiveLocation(twoRequire.charAt(1))][connectiveLocation(twoRequire.charAt(0))];
            State s2 = boardstates[connectiveLocation(twoRequire.charAt(3))][connectiveLocation(twoRequire.charAt(2))];
            if (crossOri(connectiveLocation(twoRequire.charAt(0)),connectiveLocation(twoRequire.charAt(1)),connectiveLocation(twoRequire.charAt(2)),connectiveLocation(twoRequire.charAt(3)))== null)
                continue;
            if (isPlacementDangerous(placement))
               return true;
            if ((s1 == GREEN && s2 == EMPTY)||(s1 == RED && s2 == EMPTY)||(s2 == GREEN && s1 == EMPTY)||(s2 == RED && s1 == EMPTY)){

                return  true;
            }
        }
        // FIXME Task 11
        return false;
    }
    public Tile crossOri(int x1,int y1,int x2,int y2) {
        if (x1 < x2 && y1 < y2) {
            return tiles[y1][x1];
        }
        else if (x1 > x2 && y1 > y2) {
            return tiles[y2][x2];
        }
        else if (x1 > x2 && y1 < y2){
            return tiles[y1][x1-1];
        }
        else {
            return tiles[y1-1][x1];
        }

    }
    public static int connectiveLocation(char c){
        int x = 0;
        switch (c){
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
        return x;
    }

    /**
     * Given a target location, find the set of actions which:
     * 1 - occupy the target location
     * 2 - satisfy all of the game requirements(e.g. objectives)
     *
     * Notice that this question is an advanced question and is entirely
     * optional.   You will need to use the HashSet data type, which is
     * not covered until lecture unit J14.
     *
     * @param targetLoc A location (x,y) on the game board.
     * @return A set of strings, each representing a tile placement
     */
    public Set<String> findCandidatePlacements(Location targetLoc) {
        // FIXME Task 12
        return new HashSet<>();
    }

    /**
     * Find the solutions to the game (the current Dinosaurs object).
     *
     * Notice that this question is an advanced question and is entirely
     * optional.   You will need to use advanced data types and will
     * need to understand how to perform a search, most likely using
     * recursion, which is not covered until lecture unit C1.
     *
     * @return A set of strings, each representing a placement of all tiles,
     * which satisfies all of the game objectives.
     */
    public Set<String> getSolutions() {
        // FIXME Task 13
        return new LinkedHashSet<>();
    }
}
