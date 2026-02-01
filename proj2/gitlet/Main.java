package gitlet;

import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Ronald Luo
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            exitWithMessage("Please enter a command.");
        }
        String firstArg = args[0];
        String text;
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.gitInit();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                text = args[1];
                Repository.gitAdd(text);
                break;
            case "commit":
                validateNumArgs("commit", args, 2);
                text = args[1];
                Repository.gitCommit(text);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                text = args[1];
                Repository.gitRm(text);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.gitLog();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.gitGlobalLog();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                text = args[1];
                Repository.gitFind(text);
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.gitStatus();
                break;
            case "checkout":
                Repository.gitCheckout(args);
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                text = args[1];
                Repository.gitBranch(text);
                break;
            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                text = args[1];
                Repository.gitRmBranch(text);
                break;
            case "reset":
                validateNumArgs("reset", args, 2);
                text = args[1];
                Repository.gitReset(text);
                break;
            case "merge":
                validateNumArgs("merge", args, 2);
                text = args[1];
                Repository.gitMerge(text);
                break;
            default:
                exitWithMessage("No command with that name exists.");
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * print error message and exits if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            exitWithMessage("Incorrect operands.");
        }
    }
}
