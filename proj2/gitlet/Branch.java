package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

public class Branch implements Serializable {
    /** Branches folder. */
    public static final File BRANCHES_FOLDER = join(Repository.GITLET_DIR, "branches");
    /** Branch name. */
    public String name;
    /** The head commit of this branch. */
    public String head;

    public Branch(String n, String h) {
        name = n;
        head = h;
    }

    /** Save branch at BRANCHES_FOLDER. */
    public void saveBranch() {
        File branchFile = join(BRANCHES_FOLDER, name);
        writeObject(branchFile, this);
    }
}
