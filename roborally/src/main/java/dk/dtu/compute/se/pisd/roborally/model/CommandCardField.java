package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

/**
 * Represents a field on a player's programming board in the RoboRally game. Each field
 * can contain a CommandCard, representing a part of the robot's programmed actions for
 * a turn. This class is responsible for managing the command card contained within the field,
 * including its visibility to the player and other game mechanics.
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class CommandCardField extends Subject {

    final transient public Player player;
    @Expose
    private CommandCard card;
    @Expose
    private boolean visible;

    /**
     * Constructs a CommandCardField for the specified player, initially without a command card.
     * @param player The player to whom this field belongs.
     */
    public CommandCardField(Player player) {
        this.player = player;
        this. card = null;
        this.visible = true;
    }

    /**
     * Gets the command card currently placed in this field.
     * @return The command card in this field, or null if the field is empty.
     */
    public CommandCard getCard() {
        return card;
    }

    /**
     * Sets or replaces the command card in this field. Notifies observers of the change.
     * @param card The new command card to place in the field, or null to remove the current card.
     */
    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    /**
     * Checks whether this field is visible to the player.
     * @return True if the field is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of this field (and its card) to the player. Notifies observers of the change.
     * @param visible True to make the field visible, false to hide it.
     */
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }


}
