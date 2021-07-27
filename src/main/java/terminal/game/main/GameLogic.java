package terminal.game.main;

import java.util.Scanner;

import terminal.game.entity.BotPlayer;
import terminal.game.entity.HumanPlayer;
import terminal.game.entity.Player;
import terminal.game.map.Map;

/**
 * Contains the main game logic.
 */
public class GameLogic {

    private Map map;

    private HumanPlayer humanPlayer;
    private BotPlayer bot;

    private boolean running;

    private Scanner reader;

    /**
     * Default constructor.
     */
    public GameLogic() {
        running = false;

        map = generateMap();

        humanPlayer = new HumanPlayer(map);

        bot = new BotPlayer(map, humanPlayer);
    }

    /**
     * Prompts user for their map selection and calls the appropriate map
     * constructor. Validates the users input in case they omit the .txt extension.
     * @return The default map if no file is entered, or a map object of the desired map from the given file.
     */
    public Map generateMap() {
        Map generatedMap;

        try {
            reader = new Scanner(System.in);
            System.out.print("Enter map name (enter nothing for the default map): ");

            String fileName = reader.nextLine(); // get the name of the file containing the map

            if (!fileName.equals("")) {
                if (!fileName.endsWith(".txt")) {
                    fileName += ".txt";
                }

                generatedMap = new Map("./src/main/resources/maps/" + fileName); // generate the map from the file

                System.out.println("\nSuccessfully generated map: " + generatedMap.getMapName());
            } else {
                generatedMap = new Map(); // generate default map if no file entered

                System.out.println("\nNo file given, default map generated.");
            }
        } catch (Exception e)  {
            generatedMap = new Map(); // if the file entered cannot be read properly, generate the default map

            System.err.println("\nThere was a problem with the chosen file, default map generated.");
        }

        System.out.println("Gold required to win: " + generatedMap.getGoldRequired() + "\n");

        return generatedMap;
    }

    /**
     * @return Map object of the game.
     */
    public Map getGameMap() {
        return map;
    }

    /**
     * @return If the game is running.
     */
    protected boolean gameRunning() {
        return running;
    }

    /**
     * @return Gold required to win.
     */
    protected String hello() {
        return String.valueOf(map.getGoldRequired());
    }

    /**
     * @return Gold currently owned.
     */
    protected String gold() {
        return String.valueOf(humanPlayer.getGold());
    }

    /**
     * Checks if movement is legal and updates player's location on the map.
     * @param player the player object that will be moved, i.e. the human player or the bot
     * @param direction the direction of the movement.
     * @return a string that says the the move was either a success or it failed.
     */
    protected String move(Player player, char direction) {
        int previousRow = player.getRow();
        int previousColumn = player.getColumn();

        try{
            if (player instanceof HumanPlayer) {
                humanPlayer.move(direction);
            } else if (player instanceof BotPlayer) {
                bot.moveBot();
            }

            // if the move is unsuccessful, move the player back
            if (map.getMap()[player.getRow()][player.getColumn()] == '#') {
                player.setRow(previousRow);
                player.setColumn(previousColumn);

                return "Fail\n";
            } else {
                return "Success\n";
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // prevent the player from moving outside the boundary of the map,
            // even if the the map does not use a # character for the walls
            player.setRow(previousRow);
            player.setColumn(previousColumn);

            return "Fail\n";
        }
    }

    /**
     * Perform the PICKUP command, updating the map and the player's gold amount.
     * @return If the player successfully picked-up gold.
     */
    protected String pickup() {
        // the character at the players position in the map
        char mapCharacter = map.getMap()[humanPlayer.getRow()][humanPlayer.getColumn()];
        if (mapCharacter== 'G') {
            // remove the gold from the map if the pickup is successful
            map.changeCharacter(humanPlayer.getRow(), humanPlayer.getColumn(), '.');
            humanPlayer.incrementGold();
            return "Success. Gold owned:" + gold();
        } else {
            return "Fail. Gold owned: " + gold();
        }
    }

    /**
     * Converts the map from a 2D character array to a single string.
     * @param player the player that is using the method.
     * @return A String representation of the game map.
     */
    protected String look(Player player) {
        int outputSize = 5;
        String outputMap = "";

        int centre = (outputSize - 1) / 2;

        // loop through a 5x5 grid within the game map with the given player at the centre
        for (int i = player.getRow() - centre; i < player.getRow() - centre + outputSize; i++) {
            for (int j = player.getColumn() - centre; j < player.getColumn() - centre + outputSize; j++) {
                try {
                    if (i == humanPlayer.getRow() && j == humanPlayer.getColumn()) {
                        // put a P at the human player position of the player if the player is in the 5x5 grid
                        outputMap += 'P';
                    } else if (i == bot.getRow() && j == bot.getColumn()) {
                        // put a B at the bot player position of the bot if the bot is in the 5x5 grid
                        outputMap += 'B';
                    } else {
                        // otherwise, put the corresponding character from the map into the 5x5 grid
                        outputMap += map.getMap()[i][j];
                    }
                } catch(ArrayIndexOutOfBoundsException e) {
                    // display any visible areas outside the map as a #
                    outputMap += '#';
                }
            }

            outputMap += "\n";
        }

        return outputMap;
    }

    /**
     * Checks the winning condition of the player.
     * @return String which says whether or not the player has won or lost.
     */
    protected String exit() {
        char characterUnderPlayer = map.getMap()[humanPlayer.getRow()][humanPlayer.getColumn()];
        if (humanPlayer.getGold() >= map.getGoldRequired() && characterUnderPlayer == 'E') {
            return "WIN\nCongratulations you collected enough gold to escape the dungeon.";
        } else {
            return "LOSE";
        }
    }

    /**
     * Quits the game, shutting down the program.
     * @param message the message displayed when the game quits.
     */
    protected void quitGame(String message) {
        System.out.println("\n" + message + "\n");
        System.exit(0);
    }

    /**
     * Provides the feedback to the user about their chosen action.
     * @param action the command the user inputs.
     */
    protected void processAction(String action) {
        if (action.equals("HELLO")) {
            System.out.println("\nGold to win: " + hello() + "\n");
        } else if (action.equals("GOLD")) {
            System.out.println("\nGold owned: " + gold() + "\n");
        } else if (action.contains("MOVE ")) {
            System.out.println("\n" + move(humanPlayer, action.charAt(action.length() - 1)));
        } else if (action.equals("PICKUP")) {
            System.out.println("\n" + pickup() + "\n");
        } else if (action.equals("LOOK")) {
            System.out.println("\n" + look(humanPlayer));
        } else if (action.equals("EXIT")) {
            quitGame(exit());
        } else if (action.equals("Invalid")) {
            System.out.println("\n" + action + "\n");
        }
    }

    /**
     * Game loop that, while the game is running, will ask the user for their command and
     * perform the appropriate action based on the users input.
     */
    public void runGame() {
        running = true;

        String input;
        while (gameRunning()) {
            // get the player's command and process it
            input = humanPlayer.getNextAction();
            processAction(input);

            if (bot.getMapRequired()) {
                // if the bot neds to update its map, call the look command for the bot, otherwise, move the bot
                bot.updateVisibleMap(look(bot));
            } else {
                move(bot, '0'); // '0' passed as direction as it is not required by the bot
            }

            bot.toggleMapRequired();

            // end the game if the bot catches the player
            if (bot.getRow() == humanPlayer.getRow() && bot.getColumn() == humanPlayer.getColumn()) {
                quitGame("You were caught by the bot, you lose.");
            }
        }

        // uncomment to print the map with both players after each turn
        //map.printMap(humanPlayer.getRow(), humanPlayer.getColumn(), bot.getRow(), bot.getColumn());

        reader.close();
    }

    /**
     * Method used for debugging, prints the entire game map with both the bot and player characters in it.
     * @param playerRow value of the row of the player
     * @param playerColumn value of the column of the player
     * @param botRow value of the row of the bot
     * @param botColumn value of the column of the bot
     */
    private void printMap(int playerRow, int playerColumn, int botRow, int botColumn) {
        for (int i = 0; i < map.getNumberOfRows(); i++) {
            for (int j = 0; j < map.getNumberOfColumns(); j++) {
                if (i == playerRow && j == playerColumn) {
                    System.out.print('P');
                } else if (i == botRow && j == botColumn) {
                    System.out.print('B');
                } else {
                    System.out.print(map.getMap()[i][j]);
                }
            }

            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        GameLogic logic = new GameLogic();

        logic.runGame();
    }

}
