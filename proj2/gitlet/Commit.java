package gitlet;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class
import java.io.Serializable;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /* TODO: fill in the rest of this class. */

    /** Commits folder. */
    public static final File COMMITS_FOLDER = join(Repository.OBJ_FOLDER, "commits");

    /** The commit date and the auther of this Commit. */
    private Date date;
    /** The parent commit. */
    private Commit parent;
    private Commit secondParent;
    /** A mapping of file names to blob references. */
    public Map<String, String> tree;

    public Commit(String msg, Commit p) {
        message = msg;
        date = new Date();
        date.getTime();
        parent = p;
        secondParent = null;
        tree = new TreeMap<>();
    }

    public static Commit makeInitialCommit() {
        Commit c = new Commit("initial commit", null);
        c.date.setTime(0);
        return c;
    }

    /** Save commit at COMMITS_FOLDER. */
    public void saveCommit() {
        String id = sha1(serialize(this));
        File commitFile = join(COMMITS_FOLDER, id);
        writeObject(commitFile, this);
    }

    /** Get commit in COMMITS_FOLDER through ID. */
    public static Commit getCommit(String id) {
        File commitFile = join(COMMITS_FOLDER, id);
        return readObject(commitFile, Commit.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Commit)) {
            return false;
        }
        Commit c = (Commit) obj;
        return sha1(serialize(c)).equals(sha1(serialize(this)));
    }
}
