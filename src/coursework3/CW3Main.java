package coursework3;

import java.util.Random;
import java.util.Scanner;

import static coursework3.Constants.INDEX_SKILL_MAP;

public class CW3Main {
    private static final SkillSorter sSorter = new SkillSorter();
    
    public static void main(String[] args) {
        // TODO: Remove testing code
        Scanner scanner = new Scanner(System.in);
        feedRandomData(scanner.nextInt());
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
}
