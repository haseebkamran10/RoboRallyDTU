package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.TabPane;

/**
 * Represents the graphical user interface for a player in the RoboRally game,
 * including the programming board and command cards. It allows the player to
 * view and interact with their program and command cards during the game.
 * It observes changes to the player's state and updates the display accordingly.
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayersView extends TabPane implements ViewObserver {

    private Board board;

    private PlayerView[] playerViews;

    /**
     * Initializes a new PlayersView with tabs for each player, using the given game controller.
     * This constructor creates a tab for each player in the game, allowing for individual
     * interaction and display of player-specific information, such as their program and
     * command cards.
     * @param gameController The GameController that manages the game, providing necessary
     * information about the game state and players to display their views correctly.
     */
    public PlayersView(GameController gameController) {
        board = gameController.board;

        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        playerViews = new PlayerView[board.getPlayersNumber()];
        for (int i = 0; i < board.getPlayersNumber();  i++) {
            for(int j = 0; j < board.getPlayersNumber(); j++) {
                int currentNumber = i + 1;
                Player playerCompare = board.getPlayer(j);
                String playernumberString = "" + playerCompare.getName().charAt(7);
                int playernumberInt = Integer.parseInt(playernumberString);

                if(playernumberInt   == currentNumber ) {
                    playerViews[i] = new PlayerView(gameController, playerCompare);
                    this.getTabs().add(playerViews[i]);
                }
            }
        }
        board.attach(this);
        update(board);
    }

    /**
     * Updates the display based on changes to the observed game board. This method is called
     * whenever the observed subject (the game board) notifies its observers of a change. It
     * ensures that the current player's tab is selected and visible to the user, reflecting
     * the current state of the game.
     * @param subject The game board being observed, which triggers the update when it changes.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            Player current = board.getCurrentPlayer();
            this.getSelectionModel().select(board.getPlayerNumber(current));
        }
    }

}
