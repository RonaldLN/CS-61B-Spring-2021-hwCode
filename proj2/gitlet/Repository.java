package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here
import static gitlet.Commit.*;
import static gitlet.Blob.*;
import static gitlet.Branch.*;
import java.util.TreeMap;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */

    /** Folder that blobs and commits are saved in. */
    public static final File OBJ_FOLDER = join(GITLET_DIR, "objects");
    /** Staging area. */
    private static TreeMap<String, String> stagingArea;
    /** Current branch. */
    private static String currentBranchName;
    private static Branch currentBranch;
    private static final File currentBranchNameFile = join(GITLET_DIR, "currentBranchName");
    /** Staging Area. */
    private static final File stagingAreaFile = join(GITLET_DIR, "stagingArea");

    /** Creates a new Gitlet version-control system in the current directory. */
    public static void gitInit() {
        if (GITLET_DIR.exists()) {
            exitWithMessage("A Gitlet version-control system already exists in the current directory.");
        }

        GITLET_DIR.mkdir();
        OBJ_FOLDER.mkdir();
        COMMITS_FOLDER.mkdir();
        BLOBS_FOLDER.mkdir();
        BRANCHES_FOLDER.mkdir();

        Commit initCommit = makeInitialCommit();
        String commitId = sha1(serialize(initCommit));
        initCommit.saveCommit();

        currentBranchName = "master";
        currentBranch = new Branch("master", commitId);
        currentBranch.saveBranch();
        setCurrentBranch();
    }

    private static void setCurrentBranch() {
        writeObject(currentBranchNameFile, currentBranchName);
    }

    private static void getCurrentBranch() {
        currentBranchName = readObject(currentBranchNameFile, String.class);
        File currentBranchFile = join(BRANCHES_FOLDER, currentBranchName);
        currentBranch = readObject(currentBranchFile, Branch.class);
    }

    public static void gitAdd(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            exitWithMessage("File does not exist.");
        }

        Blob blob = new Blob(fileName);
        String blobId = sha1(serialize(blob));

        getCurrentBranch();
        Commit headCommit = getCommit(currentBranch.head);
        if (headCommit.tree.containsKey(fileName)) {
            String lastBlobId = headCommit.tree.get(fileName);
            Blob last = getBlob(lastBlobId);
            if (last.equals(blob)) {
                return;
            }
        }

        getStagingArea();
        if (stagingArea.containsKey(fileName)) {
            File oldBlob = join(BLOBS_FOLDER, stagingArea.get(fileName));
            oldBlob.delete();
        }
        stagingArea.put(fileName, blobId);
        saveStagingArea();
        blob.saveBlob();
    }

    private static void getStagingArea() {
        if (stagingAreaFile.exists()) {
            stagingArea = readObject(stagingAreaFile, TreeMap.class);
        } else {
            stagingArea = new TreeMap<>();
        }
    }

    private static void saveStagingArea() {
        writeObject(stagingAreaFile, stagingArea);
    }

    public static void gitCommit(String message) {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }


    }

    /* Test. */
    public static void test() {
        Blob a = new Blob("gitlet-design.md");
        Blob b = new Blob("gitlet-design.md");
        System.out.println(a == b);
        System.out.println(sha1(serialize(a)));
        System.out.println(sha1(serialize(b)));
        System.out.println(a.equals(b));
        // System.out.println(sha1(a.content));
    }
}
