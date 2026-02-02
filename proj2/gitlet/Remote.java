package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static gitlet.Utils.*;

public class Remote {
    private final File gitletDir;
    private final File objFolder;
    private final File branchesFolder;
    private final File commitsFolder;
    private final File blobsFolder;

    public Remote(String path) {
        gitletDir = join(Repository.CWD, path);
        if (!gitletDir.exists() || !gitletDir.isDirectory()) {
            exitWithMessage("Remote directory not found.");
        }
        objFolder = join(gitletDir, "objects");
        branchesFolder = join(gitletDir, "branches");
        commitsFolder = join(objFolder, "commits");
        blobsFolder = join(objFolder, "blobs");
    }

    public String fetch(String branchName) {
        List<String> allBranches = plainFilenamesIn(branchesFolder);
        if (!allBranches.contains(branchName)) {
            exitWithMessage("That remote does not have that branch.");
        }
        Branch branch = Branch.getBranch(branchName);

        copyFilesInFolder(commitsFolder, Commit.COMMITS_FOLDER);
        copyFilesInFolder(blobsFolder, Blob.BLOBS_FOLDER);
        return branch.getHeadId();
    }

    private static void copyFilesInFolder(File source, File destination) {
        for (String file : plainFilenamesIn(source)) {
            try {
                Files.copy(join(source, file).toPath(), join(destination, file).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy: " + file, e);
            }
        }
    }

    public File getBranchesFolder() {
        return branchesFolder;
    }

    public File getCommitsFolder() {
        return commitsFolder;
    }

    public File getBlobsFolder() {
        return blobsFolder;
    }
}
