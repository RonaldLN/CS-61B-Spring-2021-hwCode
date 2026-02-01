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
    private static final File CURRENT_BRANCH_NAME_FILE = join(GITLET_DIR, "currentBranchName");
    /** Staging Area. */
    private static final File STAGING_AREA_FILE = join(GITLET_DIR, "stagingArea");

    /** Creates a new Gitlet version-control system in the current directory. */
    public static void gitInit() {
        if (GITLET_DIR.exists()) {
            exitWithMessage(
                    "A Gitlet version-control system already exists in the current directory.");
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
        writeObject(CURRENT_BRANCH_NAME_FILE, currentBranchName);
    }

    private static void getCurrentBranch() {
        currentBranchName = readObject(CURRENT_BRANCH_NAME_FILE, String.class);
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
        getStagingArea();
        Commit headCommit = currentBranch.getHeadCommit();
        if (headCommit.getTree().containsKey(fileName)) {
            String lastBlobId = headCommit.getTree().get(fileName);
            Blob last = getBlob(lastBlobId);
            if (last.equals(blob)) {
                // File identical to HEAD version -> clean staging operations
                // 1. Remove `removal` mark -> undo rm
                // 2. Delete staging blob -> avoid re-add
                if (stagingArea.containsKey(fileName)) {
                    String blobId = stagingArea.remove(fileName);
                    if (!blobId.equals("removal")) {
                        File blobFile = join(BLOBS_FOLDER, blobId);
                        blobFile.delete();
                    }
                    saveStagingArea();
                }

                return;
            }
        }

        if (stagingArea.containsKey(fileName)) {
            File oldBlob = join(BLOBS_FOLDER, stagingArea.get(fileName));
            oldBlob.delete();
        }
        stagingArea.put(fileName, blob.getId());
        saveStagingArea();
        blob.saveBlob();
    }

    private static void getStagingArea() {
        if (STAGING_AREA_FILE.exists()) {
            stagingArea = readObject(STAGING_AREA_FILE, TreeMap.class);
        } else {
            stagingArea = new TreeMap<>();
        }
    }

    private static void saveStagingArea() {
        writeObject(STAGING_AREA_FILE, stagingArea);
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
        Commit newCommit = new Commit(message, currentBranch.getHeadId());
        newCommit.getTree().putAll(head.getTree());
        stageToCommit(newCommit);
        STAGING_AREA_FILE.delete();

        currentBranch.setHead(newCommit.getId());
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
        if (!stagingArea.containsKey(fileName) && !head.getTree().containsKey(fileName)) {
            exitWithMessage("No reason to remove the file.");
        }
        String blobId = stagingArea.remove(fileName);
        if (blobId != null) {
            File blobFile = join(BLOBS_FOLDER, blobId);
            blobFile.delete();
        }
        if (head.getTree().containsKey(fileName)) {
            stagingArea.put(fileName, "removal");
            File file = join(CWD, fileName);
            file.delete();
        }
        saveStagingArea();
    }

    private static void stageToCommit(Commit commit) {
        for (String key : stagingArea.keySet()) {
            String blobId = stagingArea.get(key);
            if (blobId.equals("removal")) {
                commit.getTree().remove(key);
            } else {
                commit.getTree().put(key, blobId);
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
                if (commit.getMessage().equals(message)) {
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

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private static void branchesStatus() {
        message("=== Branches ===");
        List<String> allBranches = plainFilenamesIn(BRANCHES_FOLDER);
        allBranches.sort(new StringComparator());
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
        stagedFiles.sort(new StringComparator());
        removedFiles.sort(new StringComparator());

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
        TreeMap<String, String> trackedFiles = new TreeMap<>(head.getTree());
        trackedFiles.putAll(stagingArea);

        List<String> allFiles = new LinkedList<>(plainFilenamesIn(CWD));
        TreeMap<String, Integer> modifiedNotStaged = new TreeMap<>(new StringComparator());

        for (Map.Entry<String, String> entry : trackedFiles.entrySet()) {
            String f = entry.getKey();
            String value = entry.getValue();

            boolean res = allFiles.remove(f);
            if (!res) {
                if (trackedFiles.containsKey(f) && !value.equals("removal")) {
                    modifiedNotStaged.put(f, 1);
                }
                continue;
            }

            Blob b = Blob.getBlob(value);
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
                default:
                    break;
            }
        }
        message("");

        allFiles.sort(new StringComparator());
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
        int usage = parseCheckoutArgs(args);
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
            default:
                break;
        }
    }

    private static int parseCheckoutArgs(String[] args) {
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
            default:
                break;
        }
        return 0;
    }

    private static void checkoutUsage1(String fileName) {
        getCurrentBranch();
        checkoutCommitFile(currentBranch.getHeadId(), fileName);
    }

    private static void checkoutCommitFile(String commitId, String fileName) {
        Commit commit = Commit.getCommit(commitId);
        String blobId = commit.getTree().get(fileName);
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
        getCurrentBranch();
        if (branchName.equals(currentBranchName)) {
            exitWithMessage("No need to checkout the current branch.");
        }
        List<String> allBranches = plainFilenamesIn(BRANCHES_FOLDER);
        if (!allBranches.contains(branchName)) {
            exitWithMessage("No such branch exists.");
        }

        getStagingArea();
        File branchFile = join(BRANCHES_FOLDER, branchName);
        Branch branch = readObject(branchFile, Branch.class);
        checkoutCommitAllWithCheck(branch.getHeadId());
        cleanStagingArea();
        currentBranchName = branchName;
        setCurrentBranch();
    }

    private static void checkoutCommitAllWithCheck(Commit commit) {
        Commit current = currentBranch.getHeadCommit();

        TreeMap<String, String> trackedFiles = new TreeMap<>(current.getTree());
        trackedFiles.putAll(stagingArea);

        List<String> untrackedFiles = new LinkedList<>(plainFilenamesIn(CWD));
        untrackedFiles.removeAll(trackedFiles.keySet());

        TreeMap<String, String> targetTrackedFiles = new TreeMap<>(commit.getTree());

        untrackedFiles.retainAll(targetTrackedFiles.keySet());
        if (!untrackedFiles.isEmpty()) {
            exitWithMessage("There is an untracked file in the way;"
                + " delete it, or add and commit it first.");
        }

        TreeMap<String, String> restTrackedFiles = new TreeMap<>(trackedFiles);
        for (Map.Entry<String, String> entry : targetTrackedFiles.entrySet()) {
            Blob b = Blob.getBlob(entry.getValue());
            b.checkout();

            String fileName = entry.getKey();
            restTrackedFiles.remove(fileName);
        }
        for (String f : restTrackedFiles.keySet()) {
            join(CWD, f).delete();
        }
    }

    private static void checkoutCommitAllWithCheck(String commitId) {
        checkoutCommitAllWithCheck(Commit.getCommit(commitId));
    }

    private static void cleanStagingArea() {
        for (String blobId : stagingArea.values()) {
            if (!blobId.equals("removal")) {
                File blobFile = join(BLOBS_FOLDER, blobId);
                blobFile.delete();
            }
        }
        STAGING_AREA_FILE.delete();
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
        Branch newBranch = new Branch(branchName, currentBranch.getHeadId());
        newBranch.saveBranch();
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
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }

        getCurrentBranch();
        getStagingArea();
        String fullCommitId = checkCommitId(commitId);
        checkoutCommitAllWithCheck(fullCommitId);
        cleanStagingArea();
        currentBranch.setHead(fullCommitId);
        currentBranch.saveBranch();
    }

    public static void gitMerge(String branchName) {
        if (!GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }
        getStagingArea();
        if (!stagingArea.isEmpty()) {
            exitWithMessage("You have uncommitted changes.");
        }
        List<String> allBranches = plainFilenamesIn(BRANCHES_FOLDER);
        if (!allBranches.contains(branchName)) {
            exitWithMessage("A branch with that name does not exist.");
        }
        getCurrentBranch();
        if (branchName.equals(currentBranchName)) {
            exitWithMessage("Cannot merge a branch with itself.");
        }

        Branch targetBranch = Branch.getBranch(branchName);
        String currentHeadId = currentBranch.getHeadId();
        String targetHeadId = targetBranch.getHeadId();
        String splitPointId = findSplitPoint(currentHeadId, targetHeadId);
        if (splitPointId.equals(targetHeadId)) {
            exitWithMessage("Given branch is an ancestor of the current branch.");
        }
        if (splitPointId.equals(currentHeadId)) {
            checkoutCommitAllWithCheck(targetHeadId);
            currentBranch.setHead(targetHeadId);
            currentBranch.saveBranch();
            exitWithMessage("Current branch fast-forwarded.");
        }

        dealWithThreeCommit(branchName, currentHeadId, targetHeadId, splitPointId);
    }

    private static String findSplitPoint(String commitAId, String commitBId) {
        if (commitAId.equals(commitBId)) {
            return commitAId;
        }

        Set<String> visitedA = new HashSet<>();
        Set<String> visitedB = new HashSet<>();
        Queue<String> queueA = new LinkedList<>();
        Queue<String> queueB = new LinkedList<>();
        queueA.offer(commitAId);
        queueB.offer(commitBId);
        visitedA.add(commitAId);
        visitedB.add(commitBId);

        Commit A = Commit.getCommit(commitAId);
        Commit B = Commit.getCommit(commitBId);
        if (B.equals(A.getParentCommit()) || B.equals(A.getSecondParentCommit())) {
            return commitBId;
        }
        if (A.equals(B.getParentCommit()) || A.equals(B.getSecondParentCommit())) {
            return commitAId;
        }

        while (!queueA.isEmpty() || !queueB.isEmpty()) {
            String foundA = expandSearch(queueA, visitedA, visitedB);
            if (foundA != null) {
                return foundA;
            }

            String foundB = expandSearch(queueB, visitedB, visitedA);
            if (foundB != null) {
                return foundB;
            }
        }

        return null;
    }

    private static String expandSearch(Queue<String> queue,
                                       Set<String> visitedThis,
                                       Set<String> visitedOther) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String currentID = queue.poll();

            if (visitedOther.contains(currentID)) {
                return currentID;
            }

            Commit current = Commit.getCommit(currentID);
            Commit parent = current.getParentCommit();
            Commit secondParent = current.getSecondParentCommit();
            if (parent != null && !visitedThis.contains(parent.getId())) {
                queue.offer(parent.getId());
                visitedThis.add(parent.getId());
            }
            if (secondParent != null && !visitedThis.contains(secondParent.getId())) {
                queue.offer(secondParent.getId());
                visitedThis.add(secondParent.getId());
            }
        }
        return null;
    }

    private static void dealWithThreeCommit(String branchName,
                                            String currentHeadId,
                                            String targetHeadId,
                                            String splitPointId) {
        Commit currentHead = Commit.getCommit(currentHeadId);
        Commit targetHead = Commit.getCommit(targetHeadId);
        Commit splitPoint = Commit.getCommit(splitPointId);
        TreeMap<String, String> currentTree = currentHead.getTree();
        TreeMap<String, String> targetTree = targetHead.getTree();

        Map<String, Set<String>> currentDiff = compareCommitWithAncestor(currentHead, splitPoint);
        Map<String, Set<String>> targetDiff = compareCommitWithAncestor(targetHead, splitPoint);

        Set<String> conflictFiles = new HashSet<>();
        Set<String> filesToStage = new HashSet<>(targetDiff.get("added"));

        Set<String> commonAddedFiles = new HashSet<>(targetDiff.get("added"));
        commonAddedFiles.retainAll(currentDiff.get("added"));
        for (String f : commonAddedFiles) {
            String currentId = currentTree.get(f);
            String targetId = targetTree.get(f);
            if (!currentId.equals(targetId)) {
                conflictFiles.add(f);
            }
            filesToStage.remove(f);
        }

        filesToStage.addAll(targetDiff.get("modified"));
        filesToStage.addAll(targetDiff.get("removed"));
        Set<String> commonModifiedAndRemovedFiles = new HashSet<>(targetDiff.get("modified"));
        commonModifiedAndRemovedFiles.addAll(targetDiff.get("removed"));
        Set<String> currentModifiedAndRemovedFiles = new HashSet<>(currentDiff.get("modified"));
        currentModifiedAndRemovedFiles.addAll(currentDiff.get("removed"));
        commonModifiedAndRemovedFiles.retainAll(currentModifiedAndRemovedFiles);
        for (String f : commonModifiedAndRemovedFiles) {
            String currentId = currentTree.get(f);
            String targetId = targetTree.get(f);
            if (currentId == null && targetId == null) {
                filesToStage.remove(f);
            } else if (currentId != null && currentId.equals(targetId)) {
                filesToStage.remove(f);
            } else {
                conflictFiles.add(f);
                filesToStage.remove(f);
            }
        }

        for (String f : filesToStage) {
            String blobId = targetTree.get(f);
            stagingArea.put(f, blobId == null ? "removal" : blobId);
        }
        checkUntrackedFilesInMerge();
        for (String f : conflictFiles) {
            writeConflictFile(f, currentTree, targetTree);
            Blob b = new Blob(f);
            b.saveBlob();
            stagingArea.put(f, b.getId());
        }
        Commit mergeCommit = new Commit(String.format("Merged %s into %s.",
                branchName, currentBranchName), currentHeadId, targetHeadId);
        mergeCommit.getTree().putAll(currentTree);
        stageToCommit(mergeCommit);
        STAGING_AREA_FILE.delete();
        checkoutCommitAllWithCheck(mergeCommit);
        currentBranch.setHead(mergeCommit.getId());
        mergeCommit.saveCommit();
        currentBranch.saveBranch();

        if (!conflictFiles.isEmpty()) {
            exitWithMessage("Encountered a merge conflict.");
        }
    }

    private static void checkUntrackedFilesInMerge() {
        Set<String> untrackedFiles = new HashSet<>(plainFilenamesIn(CWD));
        untrackedFiles.removeAll(currentBranch.getHeadCommit().getTree().keySet());

        untrackedFiles.retainAll(stagingArea.keySet());
        if (!untrackedFiles.isEmpty()) {
            exitWithMessage("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
        }
    }

    private static void writeConflictFile(String fileName,
                                          TreeMap<String, String> currentTree,
                                          TreeMap<String, String> targetTree) {
        String currentContent = "";
        String targetContent = "";
        String currentBlobId = currentTree.get(fileName);
        String targetBlobId = targetTree.get(fileName);
        if (currentBlobId != null) {
            currentContent = new String(Blob.getBlob(currentBlobId).getContent());
        }
        if (targetBlobId != null) {
            targetContent = new String(Blob.getBlob(targetBlobId).getContent());
        }
        String conflictContent = String.format("<<<<<<< HEAD\n%s=======\n%s>>>>>>>\n",
                currentContent, targetContent);
        writeContents(join(Repository.CWD, fileName), conflictContent);
    }

    private static Map<String, Set<String>> compareCommitWithAncestor(Commit descendant,
                                                                      Commit ancestor) {
        TreeMap<String, String> descendantTree = new TreeMap<>(descendant.getTree());
        TreeMap<String, String> ancestorTree = new TreeMap<>(ancestor.getTree());
        Set<String> allKeys = new HashSet<>(descendantTree.keySet());
        allKeys.addAll(ancestorTree.keySet());

        Set<String> addedFiles = new HashSet<>();
        Set<String> removedFiles = new HashSet<>();
        Set<String> modifiedFiles = new HashSet<>();
        Set<String> unchangedFiles = new HashSet<>();
        for (String k : allKeys) {
            String valueInDescendant = descendantTree.get(k);
            String valueInAncestor = ancestorTree.get(k);
            if (valueInAncestor == null) {
                addedFiles.add(k);
            } else if (valueInDescendant == null) {
                removedFiles.add(k);
            } else if (valueInDescendant.equals(valueInAncestor)) {
                unchangedFiles.add(k);
            } else {
                modifiedFiles.add(k);
            }
        }
        return Map.of(
                "added", addedFiles,
                "modified", modifiedFiles,
                "removed", removedFiles,
                "unchanged", unchangedFiles
        );
    }
}
