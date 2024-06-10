package dk.dtu.compute.se.pisd.roborally.model;

/**
 * Enumerates the different commands that can be issued to robots in the RoboRally game.
 * Each command represents an action that a robot can perform on the game board, such as
 * moving forward or turning. These commands are fundamental to the gameplay, dictating
 * how robots navigate the board and interact with various elements and each other.
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.

    FORWARD("Fwd"),
    FORWARD1("Fwd x2"),
    FORWARD2("Fwd x3"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    UTURN("U-Turn"),
    Back("Back up"),
    Again("Again"),
    Power("Power Up");

    final public String displayName;

    Command(String displayName) {
        this.displayName = displayName;
    }

}
