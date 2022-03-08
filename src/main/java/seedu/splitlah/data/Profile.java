package seedu.splitlah.data;

import seedu.splitlah.exceptions.InvalidDataException;
import seedu.splitlah.ui.Message;

import java.util.ArrayList;

/**
 * Represents a Profile for the SplitLah application.
 */
public class Profile {
    private ArrayList<Session> sessionList;
    private int sessionIdTracker;
    private int activityIdTracker;

    /**
     * Constructor to create a profile object.
     */
    public Profile() {
        this.sessionList = new ArrayList<>();
        sessionIdTracker = 1;
        activityIdTracker = 1;
    }

    /**
     * Checks if there is a session exists with the same session name.
     *
     * @param sessionName The session name to be checked.
     * @return True if a session exists with the same name, false otherwise.
     */
    public boolean hasSessionName(String sessionName) {
        for (Session session : sessionList) {
            if (session.getSessionName().equalsIgnoreCase(sessionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a session to the list of sessions.
     *
     * @param session A session object to be added.
     */
    public void addSession(Session session) {
        this.sessionList.add(session);
    }

    /**
     * Returns the list of session stored in the profile.
     *
     * @return The ArrayList of sessions.
     */
    public ArrayList<Session> getSessionList() {
        return sessionList;
    }

    /**
     * Returns a new session id for session object to be created.
     * Assumption: Function is called when,
     * a new session object is created without errors.
     *
     * @return The new session id as int.
     */
    public int getNewSessionId() {
        int newSessionId = sessionIdTracker;
        sessionIdTracker += 1;
        return newSessionId;
    }

    /**
     * Returns a new activity id for activity object to be created.
     * Assumption: Function is called when,
     * a new activity object is created without errors.
     *
     * @return The new activity id as int.
     */
    public int getNewActivityId() {
        int newActivityId = activityIdTracker;
        activityIdTracker += 1;
        return newActivityId;
    }
}
