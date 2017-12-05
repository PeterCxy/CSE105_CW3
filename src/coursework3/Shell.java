package coursework3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.out;

import static coursework3.Constants.INDEX_SKILL_MAP;
import static coursework3.Constants.DATA_FILE;

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

        // Shorthand to print a message with format `command_name > ....`
        void print(String msg) {
            out.print(mName + " > " + msg);
        }

        // Shorthand.
        void println(String msg) {
            print(msg + "\n");
        }

        // Shorthand to print the prompt to wait for input
        void printPrompt() {
            out.print(mName + " >> ");
        }

        /*
         * Prompt for next available token
         */
        String prompt(Scanner scanner) {
            printPrompt();
            return scanner.next().trim();
        }

        /*
         * Prompt for an Integer input
         * throw @{java.lang.IllegalArgumentException} if no integer given
         */
        int promptInt(Scanner scanner) {
            printPrompt();
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                scanner.next(); // Discard the invalid input
                throw new IllegalArgumentException("Need an integer.");
            }
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
        sCommandList.add(new ShowCommand());
        sCommandList.add(new AddCommand());
        sCommandList.add(new DeleteCommand());
        sCommandList.add(new MoveCommand());
        sCommandList.add(new ClearCommand());
        sCommandList.add(new RandomCommand());
    }

    /*
     * Load from saved data
     * do nothing if no data found or could not load
     */
    public static final void loadData() {
        out.println("Attempting to load saved data...");

        try {
            Utility.deserializeFromFile(sSorter, DATA_FILE);
            out.println("Data loaded.");
        } catch (Exception e) {
            out.println("Could not load saved data. Starting fresh.");
        }
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
    private static void feedRandomData(int num, boolean extreme) {
        for (int i = 0; i < num; i++) {
            sSorter.addVolunteer(new Volunteer(extreme ? randomExtremeSkills() : randomSkills()));
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
     * FOR TESTING PURPOSE
     * Generate random skill sets that are all "AAA" "BBB" "CCC" ....
     */
    private static String randomExtremeSkills() {
        char skill = INDEX_SKILL_MAP[new Random().nextInt(INDEX_SKILL_MAP.length)];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(skill);
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
            super("exit", "ex", "Save and exit the program.");
        }

        @Override
        void execute(Scanner scanner) {
            // Save data to disk before exit.
            out.println("Saving data...");

            try {
                Utility.serializeToFile(sSorter, DATA_FILE);
                out.println("Data saved.");
            } catch (Exception e) {
                out.println("Failed to save data.");
            }

            out.println("Program terminated.");
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
     * The Show command
     * Print the information about a group:
     *   how many volunteers does it have for each possible skill combination
     * 
     * `show` or `s` to invoke
     */
    private static class ShowCommand extends Command {
        ShowCommand() {
            super("show", "s", "Print a group's member information.");
        }

        @Override
        void execute(Scanner scanner) {
            println("Please specify which group you would like to view. [0-4]");
            int index = promptInt(scanner);
            printStats(index);
        }

        /*
         * Static reusable utility method to print the information of a group
         * also used in @{ManipulationCommand}
         */
        static void printStats(int index) {
            out.println("Group " + index);
            HashMap<String, Integer> stats = sSorter.getStats(index);
            // Again, print it as a table.
            // First make the header.
            // Don't need the command prompt here, so just use @{java.lang.System.out.println()}
            out.println("SkillSet\tTotal");

            // Print all entries available in the stats
            for (String skills : stats.keySet()) {
                out.println(skills + "\t\t" + stats.get(skills));
            }
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
            println("Please input the skill set of the new volunteer. e.g. ABC, BBA, CDE");
            String skillSet = prompt(scanner).toUpperCase();
            
            if (!skillSet.equals("")) {
                // Make sure that the skill set is not empty
                int index = sSorter.myAddVolunteer(new Volunteer(skillSet));
                println("The volunteer with skills `" + skillSet + "` has been added to group " + index);
            } else {
                // Error message.
                throw new IllegalArgumentException("Illegal skill set.");
            }
        }
    }

    /*
     * Base class for all manipulations on groups
     * Prints group information and waits for input
     */
    private static abstract class ManipulationCommand extends Command {
        ManipulationCommand(String name, String shortName, String description) {
            super(name, shortName, description);
        }

        @Override
        void execute(Scanner scanner) {
            println("Choose a group to manipulate [0-4]");
            int groupIndex = promptInt(scanner);
            ShowCommand.printStats(groupIndex); // Re-use code from @{ShowCommand}

            // Now that the group information is printed
            // let the subclass decide what to do.
            manipulate(groupIndex, scanner);
        }

        abstract void manipulate(int groupIndex, Scanner scanner);
    }

    /*
     * The Delete command
     * delete a volunteer from a group
     * also acts as the base class of @{MoveCommand}
     * 
     * `delete` or `d` to invoke
     */
    private static class DeleteCommand extends ManipulationCommand {
        DeleteCommand() {
            super("delete", "d", "Delete a volunteer from a group.");
        }

        DeleteCommand(String name, String shortName, String description) {
            super(name, shortName, description);
        }

        @Override
        void manipulate(int groupIndex, Scanner scanner) {
            println("Choose a volunteer from the table above.");
            println("Note that we don't distinguish between volunteers with the same skills.");
            println("Please input the skill set of the volunteer that you need to " + getName() + ".");
            String skillSet = prompt(scanner).toUpperCase();
            afterDelete(groupIndex, skillSet, scanner);
            println("Operation completed.");
        }

        /*
         * This method is to be overridden from the `move` command
         * because the two command shares everything before deleting
         */
        void afterDelete(int groupIndex, String skillSet, Scanner scanner) {
            println("A volunteer of skills `" + skillSet + "` will be deleted from group " + groupIndex);
            sSorter.deleteVolunteer(skillSet, groupIndex);
        }
    }

    /*
     * The Move command
     * Move a volunteer from one group to another.
     * This command shares some logic with @{DeleteCommand}
     * because it also needs to delete.
     * 
     * `move` or `m` to invoke
     */
    private static class MoveCommand extends DeleteCommand {
        MoveCommand() {
            super("move", "m", "Move a volunteer from one group to another.");
        }

        /*
         * The volunteer is to be deleted from the original group.
         * We need to add it to a new group
         * This differs from @{AddCommand} since it does not
         * find the best balance
         */
        void afterDelete(int groupIndex, String skillSet, Scanner scanner) {
            println("Please choose the target group to move to [0-4]");
            int targetGroup = promptInt(scanner);
            println("A volunteer of skills `" + skillSet + "` will be moved from group " + groupIndex + " to " + targetGroup);
            sSorter.moveVolunteer(skillSet, groupIndex, targetGroup);
        }
    }

    /*
     * The Clear command
     * delete everything and start fresh
     * 
     * `clear` or `clr` to invoke
     */
    private static class ClearCommand extends Command {
        ClearCommand() {
            super("clear", "clr", "Delete everything.");
        }

        @Override
        void execute(Scanner scanner) {
            sSorter.deleteAllVolunteers();
            println("Everything deleted.");
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
            println("How many volunteers with random skill set would you like?");
            int total = promptInt(scanner);
            println("Would you like an extreme test set? [y/n] (default: n)");
            String extremeStr = prompt(scanner);
            boolean extreme = extremeStr.equals("y");

            if (total > 0) {
                feedRandomData(total, extreme);
                println("Added " + total + " random volunteers.");
                println("Use `overview` to see how the groups are balanced.");
            } else {
                throw new IllegalArgumentException("Volunteer count can't be 0 or minus.");
            }
        }
    }
}