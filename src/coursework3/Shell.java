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
        sCommandList.add(new OverviewCommand());
        sCommandList.add(new AddCommand());
        sCommandList.add(new RandomCommand());
    }

    /*
     * Start the user interface.
     * This registers all the implemented commands
     * and starts the input-execute-print loop.
     */
    public static final void startShell() {
        Scanner scanner = new Scanner(System.in); // Initialize the scanner for input

        // Print the help information on start
        out.println("> help"); // Mimick user input
        new HelpCommand().execute(scanner); // Just using a newly-created @{HelpCommand} instance is fine here.

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
                        try {
                            cmd.execute(scanner);
                        } catch (Exception e) {
                            // Error happend. Don't crash and just print the error
                            out.println(cmd.getName() + " > ERROR: " + e.getMessage());
                        }
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
            out.println("Command\t\t\tDescription");

            // Loop over every command to print the lines of this table
            for (Command cmd : sCommandList) {
                // To deal with alignment of the table in help message
                // Longer command names might have exceeded the table cells.
                if (cmd.getName().length() < 8) {
                    // If the name is within 8 characters
                    // We need two TABs for correct alignment
                    out.println(cmd.getName() + "\t\t(" + cmd.getShortName() + ")\t" + cmd.getDescription());
                } else {
                    // Otherwise we only need one TAB
                    out.println(cmd.getName() + "\t(" + cmd.getShortName() + ")\t" + cmd.getDescription());
                }
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

    /*
     * The Overview command
     * Print overview information on how each group is balanced.
     * 
     * `overview` or `o` to invoke.
     */
    private static class OverviewCommand extends Command {
        OverviewCommand() {
            super("overview", "o", "Print overview information on how each group is balanced.");
        }

        @Override
        void execute(Scanner scanner) {
            sSorter.printAll();
        }
    }

    /*
     * The Add command.
     * Adds a volunteer to one of the groups
     * such that the groups are best balanced.
     * 
     * `add` or `a` to invoke
     */
    private static class AddCommand extends Command {
        AddCommand() {
            super("add", "a", "Add a volunteer to one of the groups such that they are best balanced.");
        }

        @Override
        void execute(Scanner scanner) {
            out.println("add > Please input the skill set of the new volunteer. e.g. ABC, BBA, CDE");
            out.print("add >> "); // The prompt
            String skillSet = scanner.next().trim().toUpperCase();
            
            if (!skillSet.equals("")) {
                // Make sure that the skill set is not empty
                int index = sSorter.myAddVolunteer(new Volunteer(skillSet));
                out.println("add > The volunteer with skills `" + skillSet + "` has been added to group " + index);
            } else {
                // Error message.
                throw new IllegalArgumentException("Illegal skill set.");
            }
        }
    }

    /*
     * The Random command
     * feeds random data to the program
     * for bulk testing
     * 
     * `random` or `ra` to invoke
     */
    private static class RandomCommand extends Command {
        RandomCommand() {
            super("random", "ra", "Feed random data to the program for bulk testing.");
        }

        @Override
        void execute(Scanner scanner) {
            out.println("random > How many volunteers with random skill set would you like?");
            out.print("random >> ");

            if (!scanner.hasNextInt()) { // Make sure we have en integer
                scanner.next(); // Discard this token
                throw new IllegalArgumentException("You must enter an integer.");
            }

            int total = scanner.nextInt();

            if (total > 0) {
                feedRandomData(total);
                out.println("random > Added " + total + " random volunteers.");
                out.println("random > Use `overview` to see how the groups are balanced.");
            } else {
                throw new IllegalArgumentException("Volunteer count can't be 0 or minus.");
            }
        }
    }
}