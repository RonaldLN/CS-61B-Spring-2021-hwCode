package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here
import static gitlet.Commit.*;
import static gitlet.Blob.*;
import static gitlet.Branch.*;
import java.util.*;

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
        initCommit.saveCommit();

        currentBranchName = "master";
        currentBranch = new Branch("master", initCommit.getId());
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
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        File file = join(CWD, fileName);
        if (!file.exists()) {
            exitWithMessage("File does not exist.");
        }

        Blob blob = new Blob(fileName);

        getCurrentBranch();
        Commit headCommit = currentBranch.getHeadCommit();
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
        stagingArea.put(fileName, blob.getId());
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

        if (message.isEmpty()) {
            exitWithMessage("Please enter a commit message.");
        }

        getStagingArea();
        if (stagingArea.isEmpty()) {
            exitWithMessage("No changes added to the commit.");
        }

        getCurrentBranch();
        Commit head = currentBranch.getHeadCommit();
        Commit newCommit = new Commit(message, currentBranch.head);
        newCommit.tree.putAll(head.tree);
        stageToCommit(newCommit);
        stagingAreaFile.delete();

        currentBranch.head = newCommit.getId();
        newCommit.saveCommit();
        currentBranch.saveBranch();
    }

    public static void gitRm(String fileName) {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        getStagingArea();
        getCurrentBranch();
        Commit head = currentBranch.getHeadCommit();
        if (!stagingArea.containsKey(fileName) && !head.tree.containsKey(fileName)) {
            exitWithMessage("No reason to remove the file.");
        }
        String blobId = stagingArea.remove(fileName);
        if (blobId != null) {
            File blobFile = join(BLOBS_FOLDER, blobId);
            blobFile.delete();
        }
        if (head.tree.containsKey(fileName)) {
            stagingArea.put(fileName, "removal");
        }
        saveStagingArea();
        File file = join(CWD, fileName);
        file.delete();
    }

    private static void stageToCommit(Commit commit) {
        for (String key : stagingArea.keySet()) {
            String blobId = stagingArea.get(key);
            if (blobId.equals("removal")) {
                commit.tree.remove(key);
            } else {
                commit.tree.put(key, blobId);
            }
        }
    }

    public static void gitLog() {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        getCurrentBranch();
        Commit commit = currentBranch.getHeadCommit();
        while (commit != null) {
            message(commit.toString());
            commit = commit.getParentCommit();
        }
    }

    public static void gitGlobalLog() {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        List<String> allCommits = plainFilenamesIn(COMMITS_FOLDER);
        if (allCommits != null) {
            for (String id : allCommits) {
                message(getCommit(id).toString());
            }
        }
    }

    public static void gitFind(String message) {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        List<String> allCommits = plainFilenamesIn(COMMITS_FOLDER);
        boolean notFound = true;
        if (allCommits != null) {
            for (String id : allCommits) {
                Commit commit = getCommit(id);
                if (commit.message.equals(message)) {
                    message(commit.getId());
                    notFound = false;
                }
            }
        }
        if (notFound) {
            exitWithMessage("Found no commit with that message.");
        }
    }

    public static void gitStatus() {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        getCurrentBranch();
        getStagingArea();

        branchesStatus();
        stageAndRemoveStatus();
        modifiedAndUntrackedStatus();
    }

    private static class stringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private static void branchesStatus() {
        message("=== Branches ===");
        List<String> allBranches = plainFilenamesIn(BRANCHES_FOLDER);
        allBranches.sort(new stringComparator());
        for (String b : allBranches) {
            if (b.equals(currentBranchName)) {
                message("*%s", b);
            } else {
                message(b);
            }
        }
        message("");
    }

    private static void stageAndRemoveStatus() {
        List<String> stagedFiles = new LinkedList<>();
        List<String> removedFiles = new LinkedList<>();
        for (String key : stagingArea.keySet()) {
            if (stagingArea.get(key).equals("removal")) {
                removedFiles.add(key);
            } else {
                stagedFiles.add(key);
            }
        }
        stagedFiles.sort(new stringComparator());
        removedFiles.sort(new stringComparator());

        message("=== Staged Files ===");
        for (String f : stagedFiles) {
            message(f);
        }
        message("");

        message("=== Removed Files ===");
        for (String f : removedFiles) {
            message(f);
        }
        message("");
    }

    private static void modifiedAndUntrackedStatus() {
        Commit head = currentBranch.getHeadCommit();
        TreeMap<String, String> stagedFiles = new TreeMap<>(head.tree);
        stagedFiles.putAll(stagingArea);

        List<String> allFiles = new LinkedList<>(plainFilenamesIn(CWD));
        TreeMap<String, Integer> modifiedNotStaged = new TreeMap<>(new stringComparator());

        for (String f : stagedFiles.keySet()) {
            boolean res = allFiles.remove(f);
            if (!res) {
                modifiedNotStaged.put(f, 1);
                continue;
            }
            Blob b = Blob.getBlob(stagedFiles.get(f));
            if (!b.equalsWithContent(f)) {
                modifiedNotStaged.put(f, 0);
            }
        }

        message("=== Modifications Not Staged For Commit ===");
        for (String f : modifiedNotStaged.keySet()) {
            switch (modifiedNotStaged.get(f)) {
                case 0:
                    message("%s (modified)", f);
                    break;
                case 1:
                    message("%s (deleted)", f);
                    break;
            }
        }
        message("");

        allFiles.sort(new stringComparator());
        message("=== Untracked Files ===");
        for (String f : allFiles) {
            message(f);
        }
        message("");
    }

    public static void gitCheckout(String[] args) {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }
        int usage = parseChechoutArgs(args);
        switch (usage) {
            case 1:
                checkoutUsage1(args[2]);
                break;
            case 2:
                checkoutUsage2(args[1], args[3]);
                break;
            case 3:
                checkoutUsage3(args[1]);
                break;
        }
    }

    private static int parseChechoutArgs(String[] args) {
        if (args.length < 2 || args.length > 4) {
            exitWithMessage("Incorrect operands.");
        }
        switch (args.length) {
            case 3:
                if (!args[1].equals("--")) {
                    exitWithMessage("Incorrect operands.");
                }
                return 1;
            case 4:
                if (!args[2].equals("--")) {
                    exitWithMessage("Incorrect operands.");
                }
                return 2;
            case 2:
                return 3;
        }
        return 0;
    }

    private static void checkoutUsage1(String fileName) {
        getCurrentBranch();
        checkoutCommitFile(currentBranch.head, fileName);
    }

    private static void checkoutCommitFile(String commitId, String fileName) {
        Commit commit = Commit.getCommit(commitId);
        String blobId = commit.tree.get(fileName);
        if (blobId == null) {
            exitWithMessage("File does not exist in that commit.");
        }
        Blob blob = Blob.getBlob(blobId);
        blob.checkout();
    }

    private static void checkoutUsage2(String commitId, String fileName) {
        String fullCommitId = checkCommitId(commitId);
        checkoutCommitFile(fullCommitId, fileName);
    }

    private static String checkCommitId(String commitId) {
        List<String> allCommits = plainFilenamesIn(COMMITS_FOLDER);
        for (String id : allCommits) {
            if (id.equals(commitId) || id.substring(0, 6).equals(commitId)) {
                return id;
            }
        }
        exitWithMessage("No commit with that id exists.");
        return "";
    }

    private static void checkoutUsage3(String branchName) {
        if (branchName.equals(currentBranchName)) {
            exitWithMessage("No need to checkout the current branch.");
        }
        List<String> allBranches = plainFilenamesIn(BRANCHES_FOLDER);
        if (!allBranches.contains(branchName)) {
            exitWithMessage("No such branch exists.");
        }

        File branchFile = join(BRANCHES_FOLDER, branchName);
        Branch branch = readObject(branchFile, Branch.class);
        checkoutCommitAll(branch.head);
        stagingAreaFile.delete();
        currentBranchName = branchName;
        setCurrentBranch();
    }

    private static void checkoutCommitAll(Commit commit) {
        Commit current = currentBranch.getHeadCommit();
        Set<String> allFilesTracked = current.tree.keySet();

        for (String f : commit.tree.keySet()) {
            String blobId = commit.tree.get(f);
            Blob b = Blob.getBlob(blobId);
            b.checkout();
            allFilesTracked.remove(f);
        }

        for (String f : allFilesTracked) {
            join(CWD, f).delete();
        }
    }

    private static void checkoutCommitAll(String commitId) {
        checkoutCommitAll(Commit.getCommit(commitId));
    }

    public static void gitBranch(String branchName) {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        List<String> allBranches = plainFilenamesIn(BRANCHES_FOLDER);
        if (allBranches.contains(branchName)) {
            exitWithMessage("A branch with that name already exists.");
        }
        getCurrentBranch();
        Branch newBranch = new Branch(branchName, currentBranch.head);
        newBranch.saveBranch();
        currentBranchName = branchName;
        setCurrentBranch();
    }

    public static void gitRmBranch(String branchName) {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }
        getCurrentBranch();
        if (branchName.equals(currentBranchName)) {
            exitWithMessage("Cannot remove the current branch.");
        }
        List<String> allBranches = plainFilenamesIn(BRANCHES_FOLDER);
        if (!allBranches.contains(branchName)) {
            exitWithMessage("A branch with that name does not exist.");
        }
        File branchFile = join(BRANCHES_FOLDER, branchName);
        branchFile.delete();
    }

    public static void gitReset(String commitId) {
        String fullCommitId = checkCommitId(commitId);
        Commit commit = Commit.getCommit(fullCommitId);
        List<String> untrackedFiles = new LinkedList<>(plainFilenamesIn(CWD));
        untrackedFiles.removeAll(commit.tree.keySet());
        if (!untrackedFiles.isEmpty()) {
            exitWithMessage("There is an untracked file in the way; delete it, or add and commit it first.");
        }
        checkoutCommitAll(fullCommitId);
        getCurrentBranch();
        currentBranch.head = fullCommitId;
        currentBranch.saveBranch();
    }
}
