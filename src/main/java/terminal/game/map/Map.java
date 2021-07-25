package terminal.game.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Reads and contains in memory the map of the game.
 */
public class Map {

    private char[][] map;

    private String mapName;

    private int goldRequired;

    /**
     * Default constructor, creates the default map "Small Dungeon - Easy Map".
     */
    public Map() {
        mapName = "Small Dungeon - Easy Map";
        goldRequired = 2;
        map = new char[][] {
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'E', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', 'E', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#','#' }};
    }

    /**
     * Constructor that accepts a map to read in from.
     * @param filename the name of the file containing the map.
     * @throws Exception
     */
    public Map(String fileName) throws Exception {
        readMap(fileName);
    }

    /**
     * @return Gold required to exit the current map.
     */
    public int getGoldRequired() {
        return goldRequired;
    }

    /**
     * @return The map as stored in memory.
     */
    public char[][] getMap() {
        return map;
    }

    /**
     * @return The name of the current map.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * @return The number of rows in the map.
     */
    public int getNumberOfRows() {
        return getMap().length;
    }

    /**
     * @return The number of columns in the map.
     */
    public int getNumberOfColumns() {
        return getMap()[0].length;
    }

    /**
     * Changes the character at given position to the new character provided.
     * @param row the row of the character to be changed.
     * @param column the column of the character to be changed.
     * @param newCharacter the new character the old character is to be replaced with.
     */
    public void changeCharacter(int row, int column, char newCharacter) {
        map[row][column] = newCharacter;
    }

    /**
     * Reads the map from file.
     * @param filename name of file containing the map to be read.
     * @throws Exception
     */
    protected void readMap(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        // gets the name and gold required from the file and assigns it the the respective fields
        mapName = br.readLine().split("name ")[1];
        goldRequired = Integer.parseInt(br.readLine().split("win ")[1]);

        ArrayList<String> rowArrayList = new ArrayList<String>();

        String line;

        while ((line = br.readLine()) != null) {
            rowArrayList.add(line);
        }

        map = new char[rowArrayList.size()][];

        // loop through each row and create an array to store the characters in the column
        for (int i = 0; i < map.length; i++) {
            map[i] = rowArrayList.get(i).toCharArray();
        }

        br.close();

        // throw an exception if the map cannot be played fairly, so that the default map is generated in
        // the GameLogic class if the user attempts to play a map that cannot be won
        if (!isMapValid()) {
            throw new Exception();
        }
    }

    /**
     * Loop through the map and ensure that the game has at least one exit tile, enough
     * gold tiles to be able to win and is rectangular. Also if the map contains a character
     * that is not a valid tile, then return false.
     * @return True if the map is a valid map that can be played.
     */
    protected boolean isMapValid() {
        boolean mapValid = false;

        boolean mapRectangular = true;
        int exitTiles = 0;
        int goldTiles = 0;

        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j < getNumberOfColumns(); j++) {
                if (map[i][j] == 'E') {
                    exitTiles++;
                } else if (map[i][j] == 'G') {
                    goldTiles++;
                } else if (map[i][j] != '.' && map[i][j] != '#') {
                    mapValid = false;
                    return mapValid;
                }
            }

            if (map[i].length != map[0].length) {
                mapRectangular = false;
            }
        }

        if (exitTiles > 0 && goldTiles >= goldRequired && mapRectangular) {
            mapValid = true;
        }

        return mapValid;
    }
}
