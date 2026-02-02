package gitlet;

import java.util.Date;
import java.io.Serializable;
import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  This class encapsulates the core data structure of a commit in the Gitlet
 *  version control system, including metadata such as commit message, timestamp,
 *  parent references, and a snapshot of tracked files through blob references.
 *  @author Ronald LUO
 */
public class Commit {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** Commits folder. */
    public static final File COMMITS_FOLDER = join(Repository.OBJ_FOLDER, "commits");

    /** Commit Data */
    private final CommitData data;

    private static class CommitData implements Serializable {
        /** The message of this Commit. */
        private final String message;
        /** The commit date and the author of this Commit. */
        private final Date date;
        /** The parent commits' ids. */
        private final String parent;
        private final String secondParent;
        /** A mapping of file names to blob references. */
        private final TreeMap<String, String> tree;

        CommitData(String msg, Date d, String p, String p2, TreeMap<String, String> t) {
            message = msg;
            date = d;
            parent = p;
            secondParent = p2;
            tree = t;
        }
    }


    public Commit(String msg, String p, String p2) {
        Date date = new Date();
        date.getTime();
        data = new CommitData(msg, date, p, p2, new TreeMap<>());
    }

    public Commit(String msg, String p) {
        this(msg, p, null);
    }

    private Commit(CommitData d) {
        data = d;
    }

    public static Commit makeInitialCommit() {
        Commit c = new Commit("initial commit", null);
        c.data.date.setTime(0);
        return c;
    }

    /** Save commit at COMMITS_FOLDER. */
    public void saveCommit() {
        String id = sha1(serialize(data));
        File commitFile = join(COMMITS_FOLDER, id);
        writeObject(commitFile, data);
    }

    /** Get commit in COMMITS_FOLDER through ID. */
    public static Commit getCommit(String id) {
        File commitFile = join(COMMITS_FOLDER, id);
        return new Commit(readObject(commitFile, CommitData.class));
    }

    public String getId() {
        return sha1(serialize(data));
    }

    public Commit getParentCommit() {
        return data.parent == null ? null : getCommit(data.parent);
    }

    public Commit getSecondParentCommit() {
        return data.secondParent == null ? null : getCommit(data.secondParent);
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
        return c.getId().equals(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===\n");
        sb.append("commit ");
        sb.append(getId());
        if (data.secondParent != null) {
            sb.append("\nMerge: ");
            sb.append(data.parent.substring(0, 7));
            sb.append(" ");
            sb.append(data.secondParent.substring(0, 7));
        }
        sb.append("\nDate: ");
        sb.append(formatDate());
        sb.append("\n");
        sb.append(data.message);
        sb.append("\n");
        return sb.toString();
    }

    private String formatDate() {
        Date date = data.date;
        return String.format(Locale.ENGLISH, "%ta %tb %te %tT %tY %tz",
                date, date, date, date, date, date);
    }

    public String getMessage() {
        return data.message;
    }

    public TreeMap<String, String> getTree() {
        return data.tree;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
