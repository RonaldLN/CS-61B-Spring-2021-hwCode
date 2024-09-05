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
        // TODO: what if args is empty?
        if (args.length == 0) {
            exitWithMessage("Please enter a command.");
        }
        String firstArg = args[0];
        String text;
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs("init", args, 1);
                Repository.gitInit();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs("add", args, 2);
                text = args[1];
                Repository.gitAdd(text);
                break;
            // TODO: FILL THE REST IN
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
            case "test":
                Repository.test();
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
