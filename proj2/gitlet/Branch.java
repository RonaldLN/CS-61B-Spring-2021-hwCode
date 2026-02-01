package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

public class Branch implements Serializable {
    /** Branches folder. */
    public static final File BRANCHES_FOLDER = join(Repository.GITLET_DIR, "branches");
    /** Branch name. */
    private final String name;
    /** The head commit of this branch. */
    private String head;

    public Branch(String n, String h) {
        name = n;
        head = h;
    }

    /** Save branch at BRANCHES_FOLDER. */
    public void saveBranch() {
        File branchFile = join(BRANCHES_FOLDER, name);
        writeObject(branchFile, this);
    }

    /** Get branch in BRANCHES_FOLDER through NAME */
    public static Branch getBranch(String name) {
        File branchFile = join(BRANCHES_FOLDER, name);
        return readObject(branchFile, Branch.class);
    }

    public String getHeadId() {
        return head;
    }

    public void setHead(String id) {
        head = id;
    }

    public Commit getHeadCommit() {
        return Commit.getCommit(head);
    }
}
