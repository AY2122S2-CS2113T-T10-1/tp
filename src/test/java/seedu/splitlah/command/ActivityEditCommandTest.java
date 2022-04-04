package seedu.splitlah.command;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.splitlah.data.Activity;
import seedu.splitlah.data.Manager;
import seedu.splitlah.data.Person;
import seedu.splitlah.data.Session;
import seedu.splitlah.exceptions.InvalidDataException;
import seedu.splitlah.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityEditCommandTest {

    private static final int SESSION_ID = 1;
    private static final int ACTIVITY_ID = 1;

    Manager manager = new Manager();
    Session session;

    /**
     * Creates a session that is stored and managed by the Manager object.
     * Creates an activity in the new session that was created.
     */
    @BeforeEach
    void setUp() throws InvalidDataException {
        String sessionOneArgs = "session /create /n Class outing /d 15-02-2022 /pl Alice Bob Charlie";
        Command createSessionOne = Parser.getCommand(sessionOneArgs);
        createSessionOne.run(manager);
        String activityOneArgs = "activity /create /sid 1 /n Lunch /p Alice /i Alice Bob Charlie /co 15";
        Command createActivityOne = Parser.getCommand(activityOneArgs);
        createActivityOne.run(manager);
        session = manager.getProfile().getSession(SESSION_ID);
    }

    /**
     * Checks if an activity's name is edited when a valid activity name is supplied.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityName_nameIsEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /n editedLunch";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        assertEquals("editedLunch",
                session.getActivity(ACTIVITY_ID).getActivityName());
    }

    /**
     * Checks if an activity's payer is changed to one of the other participants when a valid payer name is supplied
     * that exists in the list of participants.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityPayerToOtherParticipant_payerIsEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /p Bob";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        assertEquals("Bob",
                session.getActivity(ACTIVITY_ID).getPersonPaid().getName());
    }

    /**
     * Checks if an activity's payer remains unchanged when trying to change the payer to a person not in the session.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityPayerToPersonNotInSession_payerIsNotEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /p Michael";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        assertEquals("Alice",
                session.getActivity(ACTIVITY_ID).getPersonPaid().getName());
    }

    /**
     * Checks if a participant is removed from the activity when the participant list supplied no longer includes
     * the removed participant.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityPersonList_PersonListIsEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /i Alice Bob";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        ArrayList<Person> involvedPersonList =
                session.getActivity(ACTIVITY_ID).getInvolvedPersonList();
        assertEquals(2, involvedPersonList.size());
        assertEquals("Alice", involvedPersonList.get(0).getName());
        assertEquals("Bob", involvedPersonList.get(1).getName());
    }

    /**
     * Checks if the participants remain unchanged when the participant list supplied includes a person who is not part
     * of the session.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityPersonListWithPersonNotInSession_PersonListIsNotEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /i Michael Bob";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        ArrayList<Person> involvedPersonList =
                session.getActivity(ACTIVITY_ID).getInvolvedPersonList();
        assertEquals(3, involvedPersonList.size());
        assertEquals("Alice", involvedPersonList.get(0).getName());
        assertEquals("Bob", involvedPersonList.get(1).getName());
        assertEquals("Charlie", involvedPersonList.get(2).getName());
    }

    /**
     * Checks if the overall cost of the activity updates correctly when a valid cost is supplied.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityOverallCost_overallCostIsEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /co 20";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        assertEquals(20, session.getActivity(ACTIVITY_ID).getTotalCost());
    }

    /**
     * Checks if the overall cost of the activity is unchanged when an invalid overall cost is supplied.
     *
     */
    @Test
    public void run_editActivityOverallCostToInvalidCost_overallCostIsNotEdited() {
        String userInput = "activity /edit /sid 1 /aid 1 /co -1";
        Command command = Parser.getCommand(userInput);
        assertEquals(InvalidCommand.class, command.getClass());
    }

    /**
     * Checks if the overall cost of an activity updates correctly when a valid gst value is supplied.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityGst_gstIsEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /gst 7";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        double newCost = session.getActivity(ACTIVITY_ID).getTotalCost();
        assertTrue(Math.abs(newCost - 16.05) < 0.1);
    }

    /**
     * Checks if the overall cost of an activity does not update when an invalid gst value is supplied.
     *
     */
    @Test
    public void run_editActivityGstWithInvalidGst_gstIsNotEdited() {
        String userInput = "activity /edit /sid 1 /gst -1";
        Command command = Parser.getCommand(userInput);
        assertEquals(InvalidCommand.class, command.getClass());
    }

    /**
     * Checks if the overall cost of an activity updates correctly when a valid service charge is supplied.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityServiceCharge_serviceChargeIsEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /sc 10";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        double newCost = session.getActivity(ACTIVITY_ID).getTotalCost();
        assertTrue(Math.abs(newCost - 16.5) < 0.1);
    }

    /**
     * Checks if the overall cost of an activity remains unchanged when an invalid service charge is supplied.
     *
     */
    @Test
    public void run_editActivityServiceChargeWithInvalidServiceCharge_serviceChargeIsNotEdited()
            throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /sc -1";
        Command command = Parser.getCommand(userInput);
        assertEquals(InvalidCommand.class, command.getClass());
    }

    /**
     * Checks if the cost list of an activity updates correctly when a new cost list is supplied.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editActivityCostList_costListIsEdited() throws InvalidDataException {
        Activity activity = manager.getProfile().getSession(1).getActivity(1);
        String userInput = "activity /edit /sid 1 /aid 1 /cl 5 10 15";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        double aliceCost = activity.getInvolvedPersonList().get(0).getActivityCostList().get(0).getCostOwed();
        double bobCost = activity.getInvolvedPersonList().get(1).getActivityCostList().get(0).getCostOwed();
        double charlieCost = activity.getInvolvedPersonList().get(2).getActivityCostList().get(0).getCostOwed();
        assertEquals(5, aliceCost);
        assertEquals(10, bobCost);
        assertEquals(15, charlieCost);
    }

    /**
     * Checks if the cost list remains unchanged when an invalid cost list is supplied.
     *
     */
    @Test
    public void run_editActivityCostListWithInvalidCostList_costListIsNotEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /cl -1 10 15";
        Command command = Parser.getCommand(userInput);
        command.run(manager);
        Activity editedActivity = manager.getProfile().getSession(1).getActivity(1);
        assertEquals(15, editedActivity.getTotalCost());
        assertEquals(3, editedActivity.getInvolvedPersonList().size());
    }

    /**
     * Checks if the cost list remains unchanged when an invalid cost list is supplied.
     *
     */
    @Test
    public void run_editActivityCostListWithIncompleteCostList_costListIsNotEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /cl 10 15";
        Command command = Parser.getCommand(userInput);
        command.run(manager);
        Activity editedActivity = manager.getProfile().getSession(1).getActivity(1);
        assertEquals(15, editedActivity.getTotalCost());
        assertEquals(3, editedActivity.getInvolvedPersonList().size());
    }

    /**
     * Checks if all fields of an activity are changed correctly when trying to change all fields.
     *
     * @throws InvalidDataException if the Activity object cannot be retrieved from the Session object.
     */
    @Test
    public void run_editWholeActivity_wholeActivityIsEdited() throws InvalidDataException {
        String userInput = "activity /edit /sid 1 /aid 1 /n editedLunch /p Bob /i Alice Bob /co 20 /gst 7 /sc 10";
        Command command = Parser.getCommand(userInput);
        assertEquals(ActivityEditCommand.class, command.getClass());
        command.run(manager);
        Activity editedActivity = manager.getProfile().getSession(1).getActivity(1);
        assertEquals(1, editedActivity.getActivityId());
        assertEquals(1, manager.getProfile().getSession(1).getSessionId());
        assertEquals("editedLunch", editedActivity.getActivityName());
        assertEquals("Bob", editedActivity.getPersonPaid().getName());
        assertEquals(2, editedActivity.getInvolvedPersonList().size());
        assertEquals("Alice", editedActivity.getInvolvedPersonList().get(0).getName());
        assertEquals("Bob", editedActivity.getInvolvedPersonList().get(1).getName());
        double newCost = editedActivity.getTotalCost();
        assertTrue(newCost - (20 * 1.07 * 1.1) < 0.001);
    }

    /**
     * Checks if an activity is not edited when an activity has duplicate names in the involved list.
     */
    @Test
    public void run_hasNameDuplicatesInInvolvedList_activityNotEdited() {
        String userInput = "activity /edit /sid 1 /n Dinner /p Alice /i Alice Alice Charlie /co 30";
        Command command = Parser.getCommand(userInput);
        assertEquals(InvalidCommand.class, command.getClass());
    }
}
