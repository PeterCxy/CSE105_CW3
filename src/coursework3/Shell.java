package coursework3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.out;

import static coursework3.Constants.INDEX_SKILL_MAP;

/*
 * The Command-Line Interface (CLI, or Shell) of this program
 * which is the main user interface.
 */
class Shell {
    /*
     * Base class of an acceptable Command available through this interface.
     * A Command has a name, a shortcut and a description.
     */
    private static abstract class Command {
        private String mName, mShortName, mDescription;
        protected Command(String name, String shortName, String description) {
            mName = name;
            mShortName = shortName;
            mDescription = description;
        }

        /*
         * This method will be called
         * when the specified command is invoked.
         */
        abstract void execute(Scanner scanner);

        String getName() {
            return mName;
        }

        String getShortName() {
            return mShortName;
        }

        String getDescription() {
            return mDescription;
        }
    }

    /*
     * Central registry of all available Commands available through the interface.
     */
    private static final List<Command> sCommandList = new ArrayList<>();

    /*
     * The main @{SkillSorter} instance. This is shared across all commands.
     */
    private static final SkillSorter sSorter = new SkillSorter();

    /*
     * Since the command list won't change during execution
     * just initialize the list here
     * Register all the available commands.
     */
    static {
        sCommandList.add(new HelpCommand());
        sCommandList.add(new ExitCommand());
    }

    /*
     * Start the user interface.
     * This registers all the implemented commands
     * and starts the input-execute-print loop.
     */
    public static final void startShell() {
        Scanner scanner = new Scanner(System.in); // Initialize the scanner for input

        // Loop forever waiting for command invocation
        // Exit only when `exit` is given or being killed by OS
        while (true) {
            out.print("> "); // The command prompt
            String userCmd = scanner.next().trim(); // Wait for invoking a command
            
            if (!userCmd.equals("")) { // Skip if the command is empty.
                // Find and invoke the corresponding command
                boolean executed = false;
                for (Command cmd : sCommandList) {
                    if (cmd.getName().equals(userCmd) || cmd.getShortName().equals(userCmd)) {
                        cmd.execute(scanner);
                        executed = true;
                    }
                }

                // If executed = false, that means the given command is invalid
                if (!executed) {
                    out.println("> ERROR: Your command `" + userCmd + "` is invalid. Use `help` to retrieve a list of all available commands.");
                }
            }
        }
    }

    /*
     * FOR TESTING PURPOSE
     * Feed random data to the sorter and print out the final distribution
     */
    private static void feedRandomData(int num) {
        for (int i = 0; i < num; i++) {
            sSorter.addVolunteer(new Volunteer(randomSkills()));
        }
        sSorter.printAll();
    }

    /*
     * FOR TESTING PURPOSE
     * Generate random skill set
     */
    private static String randomSkills() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(INDEX_SKILL_MAP[new Random().nextInt(INDEX_SKILL_MAP.length)]);
        }
        return sb.toString();
    }

    /*
     * The Help command
     * `help` or `h` to invoke.
     */
    private static class HelpCommand extends Command {
        HelpCommand() {
            super("help", "h", "Print this help message.");
        }

        @Override
        void execute(Scanner scanner) {
            // Print the information of all the available commands with a table
            // First print the header
            out.println("Command\t\tDescription");

            // Loop over every command to print the lines of this table
            for (Command cmd : sCommandList) {
                out.println(cmd.getName() + " (" + cmd.getShortName() + ")\t" + cmd.getDescription());
            }
        }
    }

    /*
     * The Exit command.
     * `exit` or `ex` to invoke.
     */
    private static class ExitCommand extends Command {
        ExitCommand() {
            super("exit", "ex", "Exit the program.");
        }

        @Override
        void execute(Scanner scanner) {
            // TODO: Save data.
            System.exit(0);
        }
    }
}