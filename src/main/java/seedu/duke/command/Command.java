package seedu.duke.command;

import seedu.duke.data.Profile;
import seedu.duke.ui.TextUI;

/**
 * Represents a generic command that the user has entered into the application
 * that can be run to produce a change or outcome.
 * @author Warren
 */
public abstract class Command {
    /**
     * Runs the command with the information parsed from the user input, using the specified
     * Profile and TextUI objects.
     * @param ui      A user interface to which the command will read its input from and print its output to.
     * @param profile A Profile object from which Session, Activity and other objects are used to run
     *                the command.
     */
    public abstract void run(TextUI ui, Profile profile);
    public abstract void run(Manager manager);

    /**
     * Checks if command object is an instance of an ExitCommand.
     *
     * @param command A command object to be checked.
     * @return True if it is an ExitCommand, else false.
     */
    public static boolean isExitCommand(Command command) {
        return command instanceof ExitCommand;
    }
}
